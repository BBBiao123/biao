package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.OfflineChangeLog;
import com.biao.entity.OfflineCoin;
import com.biao.entity.OfflineCoinVolume;
import com.biao.entity.PlatUser;
import com.biao.enums.OfflineFeeTypeEnum;
import com.biao.enums.UserCardStatusEnum;
import com.biao.enums.UserStatusEnum;
import com.biao.exception.PlatException;
import com.biao.mapper.OfflineChangeLogDao;
import com.biao.pojo.OfflineChangeVO;
import com.biao.pojo.ResponsePage;
import com.biao.service.OfflineChangeLogService;
import com.biao.service.OfflineCoinService;
import com.biao.service.OfflineCoinVolumeService;
import com.biao.service.PlatUserService;
import com.biao.util.FeeUtils;
import com.biao.util.SnowFlake;
import com.biao.vo.OfflineChangeListVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OfflineChangeLogServiceImpl implements OfflineChangeLogService {

    private static Logger logger = LoggerFactory.getLogger(OfflineChangeLogServiceImpl.class);

    private static String OFFLINE_CHANGE = "offline:change:";

    @Autowired
    private OfflineChangeLogDao offlineChangeLogDao;

    @Autowired
    private PlatUserService platUserService;

    @Autowired
    private OfflineCoinService offlineCoinService;

    @Autowired
    private OfflineCoinVolumeService offlineCoinVolumeService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public String save(OfflineChangeLog offlineChangeLog) {
        return null;
    }

    @Override
    public long updateById(OfflineChangeLog offlineChangeLog) {
        return 0;
    }

    @Override
    public OfflineChangeLog findById(String id) {
        OfflineChangeLog offlineChangeLog = offlineChangeLogDao.findById(id);
        if (!ObjectUtils.isEmpty(offlineChangeLog)) {
            offlineChangeLog.setUserId("");
            offlineChangeLog.setOtherUserId("");
            offlineChangeLog.setOtherRealName(this.maskOffRealName(offlineChangeLog.getOtherRealName()));
            offlineChangeLog.setRealName("");
        }
        return offlineChangeLog;
    }

    @Override
    public ResponsePage<OfflineChangeLog> findPage(OfflineChangeListVO offlineChangeListVO) {
        ResponsePage<OfflineChangeLog> responsePage = new ResponsePage<>();
        Page<OfflineChangeLog> page = PageHelper.startPage(offlineChangeListVO.getCurrentPage(), offlineChangeListVO.getShowCount());
        List<OfflineChangeLog> data = null;
        if (StringUtils.isNotEmpty(offlineChangeListVO.getCoinId())) {
            data = offlineChangeLogDao.findOfflineChangeLogByUserAndCoin(offlineChangeListVO.getCoinId(), offlineChangeListVO.getUserId());
        } else {
            data = offlineChangeLogDao.findOfflineChangeLogByUserId(offlineChangeListVO.getUserId());
        }
        data.forEach(offlineChangeLog -> {
            offlineChangeLog.setUserId("");
            offlineChangeLog.setOtherUserId("");
            offlineChangeLog.setOtherRealName(this.maskOffRealName(offlineChangeLog.getOtherRealName()));
            offlineChangeLog.setRealName("");
        });
        responsePage.setCount(page.getTotal());
        responsePage.setList(data);
        return responsePage;
    }

    @Override
    public OfflineChangeVO preCheck(OfflineChangeVO offlineChangeVO, String userId) {
        //检查用户转账状态
        PlatUser platUser = checkAndGetUserInfo(userId);
        //检查币种转账状态
        OfflineCoin offlineCoin = checkAngGetOfflineCoin(offlineChangeVO);
        //获取用户可转账余额
        BigDecimal myVolumeLimit = getMyVolumeLimit(platUser, offlineCoin, offlineChangeVO);
        //返回数据
        return offlineChangeVO;
    }

    private PlatUser checkAndGetUserInfo(String userId) {
        PlatUser platUser = platUserService.findById(userId);
        if (ObjectUtils.isEmpty(platUser)) {
            logger.error("数据异常");
            throw new PlatException(Constants.PARAM_ERROR, "数据异常");
        }

        if (ObjectUtils.isEmpty(platUser.getStatus()) || UserStatusEnum.USER_LOCK.getCode().equals(platUser.getStatus().toString())) {
            logger.error("用户账号被锁定");
            throw new PlatException(Constants.PARAM_ERROR, "用户账号被锁定");
        }

        if (ObjectUtils.isEmpty(platUser.getStatus()) || UserStatusEnum.USER_DISABLE.getCode().equals(platUser.getStatus().toString())) {
            logger.error("用户账号禁用");
            throw new PlatException(Constants.PARAM_ERROR, "用户账号禁用");
        }

        if (StringUtils.isEmpty(platUser.getC2cChange()) || !platUser.getC2cChange().equals("0")) {
            logger.error("账号异常，无法转账");
            throw new PlatException(Constants.PLAT_USER_CHANGE_FORBIDDEN, "账号异常，无法转账");
        }

        if (isFMOrYSPlatUser(platUser)) {
            logger.error("财务账号或银商账号不能转账");
            throw new PlatException(Constants.PARAM_ERROR, "财务账号或银商账号不能转账！");
        }
        return platUser;
    }

    private OfflineCoin checkAngGetOfflineCoin(OfflineChangeVO offlineChangeVO) {
        OfflineCoin offlineCoin = offlineCoinService.findByCoinIdForChange(offlineChangeVO.getCoinId());
        if (ObjectUtils.isEmpty(offlineCoin)) {
            throw new PlatException(Constants.PARAM_ERROR, "数据异常");
        }

        if (StringUtils.isEmpty(offlineCoin.getIsChangeAccount()) || !"0".equals(offlineCoin.getIsChangeAccount())) {
            logger.error("该币种未开放转账功能！");
            throw new PlatException(Constants.PARAM_ERROR, "该币种未开放转账功能！");
        }

        if (ObjectUtils.isEmpty(offlineCoin.getChangeMinVolume()) || offlineCoin.getChangeMinVolume().compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("该币种转账功能出现异常！");
            throw new PlatException(Constants.PARAM_ERROR, "该币种转账功能出现异常！");
        }

        offlineChangeVO.setPointVolume(offlineCoin.getPointVolume());
        offlineChangeVO.setSymbol(offlineCoin.getSymbol());
        offlineChangeVO.setChangeMinVolume(offlineCoin.getChangeMinVolume());
        return offlineCoin;
    }

    private BigDecimal getMyVolumeLimit(PlatUser platUser, OfflineCoin offlineCoin, OfflineChangeVO offlineChangeVO) {

        BigDecimal changeVolumeSum = BigDecimal.ZERO;
        List<OfflineChangeLog> offlineChangeLogList = offlineChangeLogDao.findOfflineChangeLogByUserIdAndCoinId(offlineCoin.getCoinId(), platUser.getId(), LocalDateTime.now());
        if (!CollectionUtils.isEmpty(offlineChangeLogList)) {
            changeVolumeSum = BigDecimal.valueOf(offlineChangeLogList.stream().mapToDouble(e -> e.getVolume().doubleValue()).sum());
        }

        BigDecimal myVolumeLimit = BigDecimal.ZERO;
        if (UserCardStatusEnum.authRealName(platUser.getCardStatus(),platUser.getCountryCode())) {
            myVolumeLimit = offlineCoin.getRealDayLimit().subtract(changeVolumeSum);
            if (myVolumeLimit.compareTo(BigDecimal.ZERO) <= 0) {
                logger.error("用户可可转账余额不足，无法转账！");
                throw new PlatException(Constants.MY_VOLUME_LIMIT_OVER, "用户可可转账余额不足，无法转账！");
            }
        } else {
            myVolumeLimit = offlineCoin.getNonRealDayLimit().subtract(changeVolumeSum);
            if (myVolumeLimit.compareTo(BigDecimal.ZERO) <= 0) {
                logger.error("用户可可转账余额不足，可开通实名认证提高转账额度！");
                throw new PlatException(Constants.MY_VOLUME_LIMIT_OVER, "用户可可转账余额不足，可开通实名认证提高转账额度！");
            }
        }

        offlineChangeVO.setMyVolumeLimit(myVolumeLimit);
        return myVolumeLimit;
    }

    @Override
    public OfflineChangeVO preConfirm(OfflineChangeVO offlineChangeVO, String userId) {
        //转账账户
        PlatUser otherUser = getAndCheckOtherUser(offlineChangeVO.getToAccount(), userId);
        //检查用户转账状态
        PlatUser platUser = checkAndGetUserInfo(userId);
        //检查币种转账状态
        OfflineCoin offlineCoin = checkAngGetOfflineCoin(offlineChangeVO);
        //获取用户可转账余额
        this.getMyVolumeLimit(platUser, offlineCoin, offlineChangeVO);
        //检查小数的格式
        this.checkChangeVolumeFormat(offlineChangeVO);
        //
        OfflineCoinVolume offlineCoinVolume = checkAngGetUserCoinVolume(platUser, offlineCoin);
        //检测转账金额
        this.checkChangeVolume(platUser, offlineCoin, offlineChangeVO, offlineCoinVolume);
        //检查转账费用
        BigDecimal fee = checkAndGetFee(offlineCoin, offlineChangeVO, offlineCoinVolume);
        offlineChangeVO.setChangeNo(SnowFlake.createSnowFlake().nextIdString());
        offlineChangeVO.setRealName(StringUtils.isNotEmpty(otherUser.getRealName()) ? this.maskOffRealName(otherUser.getRealName()) : "暂未实名");

        redisTemplate.opsForValue().set(this.getOfflineChangeKey(offlineChangeVO), offlineChangeVO, 30L, TimeUnit.MINUTES);
        return offlineChangeVO;
    }

    private PlatUser getAndCheckOtherUser(String toAccount, String userId) {
        PlatUser otherUser = platUserService.findByLoginName(toAccount);
        if (ObjectUtils.isEmpty(otherUser)) {
            logger.error("转账对象不存在!");
            throw new PlatException(Constants.PARAM_ERROR, "转账对象不存在!");
        }

        if (otherUser.getId().equals(userId)) {
            logger.error("不能给自己转账！");
            throw new PlatException(Constants.PARAM_ERROR, "不能给自己转账！");
        }

        if (isFMOrYSPlatUser(otherUser)) {
            logger.error("转账对象不能为财务账号或银商账号");
            throw new PlatException(Constants.PARAM_ERROR, "转账对象不能为财务账号或银商账号");
        }
        return otherUser;
    }

    private boolean isFMOrYSPlatUser(PlatUser platUser) {
        return StringUtils.isNotEmpty(platUser.getTag()) && ("FM".equals(platUser.getTag()) || platUser.getTag().startsWith("YS"));
    }

    private OfflineCoinVolume checkAngGetUserCoinVolume(PlatUser platUser, OfflineCoin offlineCoin) {
        OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeService.findByUserIdAndCoinId(platUser.getId(), offlineCoin.getCoinId());
        if (ObjectUtils.isEmpty(offlineCoinVolume)) {
            logger.error("转账失败，资产账户不存在！");
            throw new PlatException(Constants.PARAM_ERROR, "转账失败，资产账户不存在！");
        }

        if (StringUtils.isEmpty(offlineCoinVolume.getUserId()) || offlineCoinVolume.getVolume().compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("转账失败，资产不能为空！");
            throw new PlatException(Constants.PARAM_ERROR, "转账失败，资产不能为空！");
        }
        return offlineCoinVolume;
    }

    private void checkChangeVolumeFormat(OfflineChangeVO offlineChangeVO) {

        String regExp = "^[0-9][0-9]*(\\.[0-9]{1," + offlineChangeVO.getPointVolume() + "})?$"; //n为小数位数
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(String.valueOf(offlineChangeVO.getVolume()));
        if (!m.matches()) {
            logger.error("转账失败，转账数量小数位非法！");
            throw new PlatException(Constants.PARAM_ERROR, String.format("转账失败，最多输入%s位小数", String.valueOf(offlineChangeVO.getPointVolume())));
        }

    }

    private void checkChangeVolume(PlatUser platUser, OfflineCoin offlineCoin, OfflineChangeVO offlineChangeVO, OfflineCoinVolume offlineCoinVolume) {

        if (offlineCoinVolume.getVolume().compareTo(offlineChangeVO.getVolume()) < 0) {
            logger.error("转账失败，C2C可用余额不足！");
            throw new PlatException(Constants.PARAM_ERROR, "转账失败，C2C可用余额不足！");
        }

        if (offlineCoin.getChangeMinVolume().compareTo(offlineChangeVO.getVolume()) > 0) {
            logger.error("不能小于转账最低数量");
            throw new PlatException(Constants.PARAM_ERROR, "不能小于转账最低数量：" + String.valueOf(offlineCoin.getChangeMinVolume()));
        }

        //获取用户可转账余额
        BigDecimal myVolumeLimit = getMyVolumeLimit(platUser, offlineCoin, offlineChangeVO);
        if (myVolumeLimit.compareTo(offlineCoinVolume.getVolume()) > 0) {
            myVolumeLimit = offlineCoinVolume.getVolume();
        }

        if (myVolumeLimit.compareTo(offlineChangeVO.getVolume()) < 0) {
            logger.info("转账失败，今日剩余转账数额为" + myVolumeLimit.toString());
            throw new PlatException(Constants.PARAM_ERROR, "转账失败，今日剩余转账数额为" + myVolumeLimit.toString());
        }
    }

    private BigDecimal checkAndGetFee(OfflineCoin offlineCoin, OfflineChangeVO offlineChangeVO, OfflineCoinVolume offlineCoinVolume) {
        String feeType = offlineCoin.getChangeFeeType();
        BigDecimal feeVolume = BigDecimal.ZERO;
        if (feeType.equals(OfflineFeeTypeEnum.NONE.getCode())) {//不收取
            logger.info("转账不收费！");
        } else {
            //判断用户是否有保证金
            BigDecimal sellerBailVolume = offlineCoinVolume.getBailVolume();
            if (sellerBailVolume.compareTo(BigDecimal.ZERO) <= 0) {//无保证金
                logger.error("C2C手续费预备金不足");
                throw new PlatException(Constants.OFFLINE_BAIL_VOLUME_NOT_ENOUGH_ERROR, "C2C手续费预备金不足");
            } else {
                if (feeType.equals(OfflineFeeTypeEnum.STATIC.getCode())) {
                    String changeFeeStep = offlineCoin.getChangeFeeStep();
                    feeVolume = FeeUtils.getFeeByStep(offlineChangeVO.getVolume(), changeFeeStep);
                } else if (feeType.equals(OfflineFeeTypeEnum.SCALE.getCode())) {
                    feeVolume = offlineChangeVO.getVolume().multiply(offlineCoin.getChangeFee());
                }
                //向上取整
                feeVolume = feeVolume.setScale(offlineChangeVO.getPointVolume(), BigDecimal.ROUND_UP);
                if (sellerBailVolume.compareTo(feeVolume) < 0) {
                    logger.error("C2C手续费预备金不足");
                    throw new PlatException(Constants.OFFLINE_BAIL_VOLUME_NOT_ENOUGH_ERROR, "C2C手续费预备金不足");
                }
            }
        }
        offlineChangeVO.setFee(feeVolume);
        return feeVolume;
    }

    @Override
    @Transactional
    public long confirm(OfflineChangeVO offlineChangeVO, String userId) {
        //检测转账确认参数
        offlineChangeVO = this.checkAndGetConfirmInfo(offlineChangeVO);
        //转账账户
        PlatUser otherUser = this.getAndCheckOtherUser(offlineChangeVO.getToAccount(), userId);
        //检查用户转账状态
        PlatUser platUser = this.checkAndGetUserInfo(userId);
        //检查币种转账状态
        OfflineCoin offlineCoin = this.checkAngGetOfflineCoin(offlineChangeVO);
        //检查资产账户
        OfflineCoinVolume offlineCoinVolume = this.checkAngGetUserCoinVolume(platUser, offlineCoin);
        //检测转账金额
        this.checkChangeVolume(platUser, offlineCoin, offlineChangeVO, offlineCoinVolume);
        //检查转账费用
        BigDecimal fee = this.checkAndGetFee(offlineCoin, offlineChangeVO, offlineCoinVolume);
        //转出与手续费
        offlineCoinVolumeService.coinVolumeSubBailSub(platUser.getId(), offlineCoin.getCoinId(), offlineChangeVO.getVolume(), offlineChangeVO.getFee(), offlineChangeVO.getChangeNo());
        //转入
        offlineCoinVolumeService.coinVolumeAdd(otherUser.getId(), offlineCoin.getCoinId(), offlineCoin.getSymbol(), offlineChangeVO.getVolume(), offlineChangeVO.getChangeNo());
        //转入与转出流水
        this.createChangeLog(platUser, otherUser, offlineCoin, offlineChangeVO, offlineCoinVolume);
        //删除redis缓存
        redisTemplate.delete(this.getOfflineChangeKey(offlineChangeVO));
        return 1L;
    }

    private OfflineChangeVO checkAndGetConfirmInfo(OfflineChangeVO offlineChangeVO) {
        OfflineChangeVO saveOfflineChangeVO = (OfflineChangeVO) redisTemplate.opsForValue().get(this.getOfflineChangeKey(offlineChangeVO));
        if (ObjectUtils.isEmpty(saveOfflineChangeVO)) {
            logger.info("转账提交数据非法！");
            throw new PlatException(Constants.OFFLINE_BAIL_VOLUME_NOT_ENOUGH_ERROR, "转账提交数据非法！");
        }
        return saveOfflineChangeVO;
    }

    private String getOfflineChangeKey(OfflineChangeVO offlineChangeVO) {
        return OFFLINE_CHANGE.concat(offlineChangeVO.getChangeNo());
    }

    private void createChangeLog(PlatUser platUser, PlatUser otherUser, OfflineCoin offlineCoin, OfflineChangeVO offlineChangeVO, OfflineCoinVolume offlineCoinVolume) {
        //转出方转账流水
        List<OfflineChangeLog> offlineChangeLogList = new ArrayList<>();
        OfflineChangeLog changeLog = new OfflineChangeLog();
        changeLog.setId(SnowFlake.createSnowFlake().nextIdString());
        changeLog.setUserId(platUser.getId());
        changeLog.setType("1");
        changeLog.setStatus("1");
        changeLog.setCoinId(offlineCoin.getCoinId());
        changeLog.setCoinSymbol(offlineCoin.getSymbol());
        changeLog.setVolume(offlineChangeVO.getVolume());
        changeLog.setFee(offlineChangeVO.getFee());
        changeLog.setAccount(StringUtils.isNotEmpty(platUser.getMobile()) ? platUser.getMobile() : platUser.getMail());
        changeLog.setRealName(UserCardStatusEnum.authRealName(platUser.getCardStatus(),platUser.getCountryCode()) ? platUser.getRealName() : "暂未实名");
        changeLog.setOtherUserId(otherUser.getId());
        changeLog.setOtherAccount(offlineChangeVO.getToAccount());
        changeLog.setOtherRealName(UserCardStatusEnum.authRealName(otherUser.getCardStatus(),otherUser.getCountryCode())? otherUser.getRealName() : "暂未实名");
        changeLog.setChangeNo(offlineChangeVO.getChangeNo());
        offlineChangeLogList.add(changeLog);
        //转入方转账流水
        OfflineChangeLog otherChangeLog = new OfflineChangeLog();
        otherChangeLog.setId(SnowFlake.createSnowFlake().nextIdString());
        otherChangeLog.setUserId(otherUser.getId());
        otherChangeLog.setType("0");
        otherChangeLog.setStatus("1");
        otherChangeLog.setCoinId(offlineCoin.getCoinId());
        otherChangeLog.setCoinSymbol(offlineCoin.getSymbol());
        otherChangeLog.setVolume(offlineChangeVO.getVolume());
        otherChangeLog.setFee(BigDecimal.ZERO);
        otherChangeLog.setAccount(offlineChangeVO.getToAccount());
        otherChangeLog.setRealName(UserCardStatusEnum.authRealName(otherUser.getCardStatus(),otherUser.getCountryCode()) ? otherUser.getRealName() : "暂未实名");
        otherChangeLog.setOtherUserId(platUser.getId());
        otherChangeLog.setOtherAccount(StringUtils.isNotEmpty(platUser.getMobile()) ? platUser.getMobile() : platUser.getMail());
        otherChangeLog.setOtherRealName(UserCardStatusEnum.authRealName(platUser.getCardStatus(),platUser.getCountryCode()) ? platUser.getRealName() : "暂未实名");
        otherChangeLog.setChangeNo(offlineChangeVO.getChangeNo());
        offlineChangeLogList.add(otherChangeLog);
        //批量插入
        long count = offlineChangeLogDao.insertBatch(offlineChangeLogList);
        if (count != 2) {
            throw new PlatException(Constants.PARAM_ERROR, "插入C2C转账记录失败！");
        }
    }

    private String maskOffRealName(String realName) {
        if (StringUtils.isEmpty(realName) || "暂未实名".equals(realName)) return realName;
        String firstName = realName.substring(0, 1);
        String endName = realName.substring(realName.length() - 1, realName.length());
        if (realName.length() <= 4) {
            return firstName.concat("*");
        } else {
            return firstName.concat("*").concat(endName);
        }
    }
}
