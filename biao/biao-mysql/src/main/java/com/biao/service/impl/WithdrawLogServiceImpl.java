package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.Coin;
import com.biao.entity.PlatUser;
import com.biao.entity.UserCoinVolume;
import com.biao.entity.WithdrawLog;
import com.biao.enums.UserCardStatusEnum;
import com.biao.enums.WithdrawFeeTypeEnum;
import com.biao.enums.WithdrawStatusEnum;
import com.biao.exception.PlatException;
import com.biao.mapper.CoinDao;
import com.biao.mapper.PlatUserDao;
import com.biao.mapper.UserCoinVolumeDao;
import com.biao.mapper.WithdrawLogDao;
import com.biao.pojo.ResponsePage;
import com.biao.redis.RedisCacheManager;
import com.biao.service.UserCoinVolumeExService;
import com.biao.service.WithdrawLogService;
import com.biao.util.SnowFlake;
import com.biao.vo.WithdrawListVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WithdrawLogServiceImpl implements WithdrawLogService {

    @Autowired
    private WithdrawLogDao withdrawLogDao;
    @Autowired
    private PlatUserDao platUserDao;
    @Autowired
    private UserCoinVolumeDao userCoinVolumeDao;
    @Autowired
    private CoinDao coinDao;

    @Autowired
    private UserCoinVolumeExService userCoinVolumeExService;
    private static final Logger LOGGER = LoggerFactory.getLogger(WithdrawLogServiceImpl.class);
    @Override
    @Transactional
    public String save(WithdrawLog withdrawLog) {
        String userId = withdrawLog.getUserId();
        String coinId = withdrawLog.getCoinId();
        PlatUser user = platUserDao.findById(userId);
        if (user.getCoinOut().equals("1")) {
            throw new PlatException(Constants.COIN_OUT_ERROR, "禁止提现");
        }

//        if (StringUtils.isEmpty(user.getGoogleAuth())) {
//            throw new PlatException(Constants.NOT_GOOGLE_ERROR, "请先设置GoogleAuth!");
//        }
//        if (!UserCardStatusEnum.authRealName(user.getCardStatus(),user.getCountryCode())) {
//            throw new PlatException(Constants.IDENTIRY_ERROR, "请进行身份认证!");
//        }
        //判断最小提现金额
        //判断用户该种币种资产
        UserCoinVolume userCoinVolume = userCoinVolumeDao.findByUserIdAndCoinId(userId, withdrawLog.getCoinId());
        if (null == userCoinVolume) {
            throw new PlatException(Constants.USER_VOLUME_NOT_ENOUGH_ERROR, "可提现资产不足");
        }

        //实际到账的钱 + 手续费 不能高于用户可用资产
        Coin coin = coinDao.findById(withdrawLog.getCoinId());
        if (null == coin) return null;
        //一次提现最小额度 比最小的还要小
        if (withdrawLog.getVolume().compareTo(coin.getWithdrawMinVolume()) == -1) {
            throw new PlatException(Constants.WITHDRAW_MIN_ERROR, "小于最小提现额度");
        }
        //一次提现最大额度
        if (withdrawLog.getVolume().compareTo(coin.getWithdrawMaxVolume()) == 1) {
            throw new PlatException(Constants.WITHDRAW_MAX_ERROR, "超过一次提现最大额度");
        }
        //当天提现总额 排除取消的 和审核未通过的
        long volumeDay = withdrawLogDao.countDayVolumeByUserIdAndCoinIdAndStatus(userId, coinId);
        BigDecimal dayVolume = BigDecimal.valueOf(volumeDay).add(withdrawLog.getVolume());
        if (dayVolume.compareTo(coin.getWithdrawDayMaxVolume()) == 1) {
            throw new PlatException(Constants.WITHDRAW_DAY_MAX_ERROR, "超过当天提现最大额度");
        }
        BigDecimal fee = BigDecimal.ZERO;
        BigDecimal realVolume = BigDecimal.ZERO;
        if (coin.getWithdrawFeeType().equals(WithdrawFeeTypeEnum.RATE.getCode())) {
            if (coin.getWithdrawFee().compareTo(BigDecimal.ONE) >= 1) {
                throw new PlatException(Constants.WITHDRAW_FEE_ERROR, "手续费设置出现问题");
            }
            fee = withdrawLog.getVolume().multiply(coin.getWithdrawFee());
            realVolume = withdrawLog.getVolume().subtract(fee);
        } else if (coin.getWithdrawFeeType().equals(WithdrawFeeTypeEnum.NUMBER.getCode())) {
            fee = coin.getWithdrawFee();
            realVolume = withdrawLog.getVolume().subtract(fee);
            if (realVolume.compareTo(BigDecimal.ZERO) <= 0) {
                throw new PlatException(Constants.WITHDRAW_VOLUME_TOOLOW_ERROR, "提现额度太少,不足以支付手续费");
            }
        }
        if (userCoinVolume.getVolume().compareTo(withdrawLog.getVolume()) == -1) {
            throw new PlatException(Constants.USER_VOLUME_NOT_ENOUGH_ERROR, "可提现资产不足");
        }
        String id = SnowFlake.createSnowFlake().nextIdString();
        withdrawLog.setStatus(Integer.parseInt(WithdrawStatusEnum.PRE_INIT.getCode()));
        withdrawLog.setRealVolume(realVolume);
        withdrawLog.setFee(fee);
        withdrawLog.setCreateDate(LocalDateTime.now());
        withdrawLog.setUpdateDate(LocalDateTime.now());
        withdrawLog.setId(id);
        withdrawLog.setCoinType(coin.getCoinType());
        withdrawLog.setAddress(withdrawLog.getAddress().trim());
        long addressCount = withdrawLogDao.countAddressByStatusAndUserId(userId, withdrawLog.getAddress());
        if (addressCount > 2) withdrawLog.setRemark("常用地址提现");
        withdrawLogDao.insert(withdrawLog);

        return id;
    }

    @Override
    public void updateById(WithdrawLog withdrawLog) {

    }

    @Override
    public WithdrawLog findById(String id) {
        return withdrawLogDao.findById(id);
    }

    @Override
    @Transactional
    public void withDrawCancel(String userId, String id) {
        WithdrawLog withdrawLog = withdrawLogDao.findById(id);
        if (null == withdrawLog) {
            throw new PlatException(Constants.PARAM_ERROR, "参数错误");
        }
        //如果提现已经被处理那么取消是不成立
        if (!withdrawLog.getStatus().toString().equals(WithdrawStatusEnum.INIT.getCode()))
            throw new PlatException(Constants.WITHDRAW_CANCEL_ERROR, "取消提现失败");
        Timestamp updateDate = Timestamp.valueOf(withdrawLog.getUpdateDate());
        long result = withdrawLogDao.updateStatusByIdAndStatus(id, WithdrawStatusEnum.CANCEL.getCode(), WithdrawStatusEnum.INIT.getCode(), updateDate);
        if (result <= 0) throw new PlatException(Constants.WITHDRAW_CANCEL_ERROR, "取消提现失败");
        //将提现冻结资产和手续费等归还给用户
        UserCoinVolume userCoinVolume = userCoinVolumeDao.findByUserIdAndCoinId(userId, withdrawLog.getCoinId());
        if (null == userCoinVolume) {
            throw new PlatException(Constants.USER_VOLUME_NOT_ENOUGH_ERROR, "可提现资产不足");
        }
        //更新用户可用资产 和冻结资产
        BigDecimal outLockVolume = userCoinVolume.getOutLockVolume().subtract(withdrawLog.getVolume());
        if (outLockVolume.compareTo(BigDecimal.ZERO) < 0) {
            throw new PlatException(Constants.LOCK_VOLUME_ERROR, "冻结资产异常");
        }
        result = userCoinVolumeDao.updateOutLockVolumeByUserIdAndCoinId(userId, withdrawLog.getCoinId(), outLockVolume, userCoinVolume.getVersion());
        if (result <= 0){
            throw new PlatException(Constants.WITHDRAW_CANCEL_ERROR, "取消提现失败");
        }

        long count = userCoinVolumeExService.updateIncome(null,withdrawLog.getVolume(),userId,withdrawLog.getCoinSymbol(),false);
        if(count <= 0){
            LOGGER.error("用户取消提现失败{},{},{}",userId,withdrawLog.getCoinSymbol(),withdrawLog.getVolume());
        }

    }

    @Override
    public ResponsePage<WithdrawLog> findPage(WithdrawListVO withdrawListVO) {
        ResponsePage<WithdrawLog> responsePage = new ResponsePage<>();
        Page<WithdrawLog> page = PageHelper.startPage(withdrawListVO.getCurrentPage(), withdrawListVO.getShowCount());
        List<WithdrawLog> data = null;
        if (StringUtils.isEmpty(withdrawListVO.getCoinId())) {
            data = withdrawLogDao.findWithdrawListByUserId(withdrawListVO.getUserId());

        } else {
            data = withdrawLogDao.findWithdrawListByUserIdAndCoinId(withdrawListVO.getUserId(), withdrawListVO.getCoinId());
        }
        responsePage.setCount(page.getTotal());
        responsePage.setList(data);
        return responsePage;
    }

    @Override
    @Transactional
    public void updateStatusById(String userId, String status, String id) {
        WithdrawLog withdrawLog = withdrawLogDao.findById(id);
        UserCoinVolume userCoinVolume = userCoinVolumeDao.findByUserIdAndCoinId(userId, withdrawLog.getCoinId());
        //更新用户可用资产
        BigDecimal volume = userCoinVolume.getVolume().subtract(withdrawLog.getVolume());
        if (volume.compareTo(BigDecimal.ZERO) == -1) {
            throw new PlatException(Constants.WITHDRAW_ERROR, "提现失败");
        }

        BigDecimal outLockVolume = userCoinVolume.getOutLockVolume().add(withdrawLog.getVolume());
        long result = userCoinVolumeDao.updateOutLockVolumeByUserIdAndCoinId(userId, withdrawLog.getCoinId(), outLockVolume, userCoinVolume.getVersion());
        if (result <= 0){
            LOGGER.error("用户{}提现{}失败volume{}",userId,withdrawLog.getCoinSymbol(),withdrawLog.getVolume());
            throw new PlatException(Constants.WITHDRAW_ERROR, "提现失败");
        }
        long count = withdrawLogDao.updateStatusById(status, id);
        if (count <= 0) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }

        count = userCoinVolumeExService.updateOutcome(null,withdrawLog.getVolume(),userId,withdrawLog.getCoinSymbol(),false);
        if (count <= 0){
            throw new PlatException(Constants.WITHDRAW_ERROR, "提现失败");
        }



    }


}
