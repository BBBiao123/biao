package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.*;
import com.biao.enums.UserCardStatusEnum;
import com.biao.enums.UserStatusEnum;
import com.biao.exception.PlatException;
import com.biao.mapper.MkRedEnvelopeConfDao;
import com.biao.mapper.MkRedEnvelopeDao;
import com.biao.mapper.MkRedEnvelopeSubDao;
import com.biao.mapper.OfflineChangeLogDao;
import com.biao.pojo.ResponsePage;
import com.biao.service.OfflineCoinVolumeService;
import com.biao.service.OfflineRedEnvelopeService;
import com.biao.service.PlatUserService;
import com.biao.service.otc.OtcAdminService;
import com.biao.util.SnowFlake;
import com.biao.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class OfflineRedEnvelopeServiceImpl implements OfflineRedEnvelopeService{

    private static Logger logger = LoggerFactory.getLogger(OfflineRedEnvelopeServiceImpl.class);

    private final static String coinSymbol = "UES";

    private final static String coinMain = "EUC";

    private final String EUC_RMB_RATE = "7.88";

    @Autowired
    private MkRedEnvelopeConfDao mkRedEnvelopeConfDao;

    @Autowired
    private MkRedEnvelopeDao mkRedEnvelopeDao;

    @Autowired
    private MkRedEnvelopeSubDao mkRedEnvelopeSubDao;

    @Autowired
    private OfflineCoinVolumeService offlineCoinVolumeService;

    @Autowired
    private OfflineChangeLogDao offlineChangeLogDao;

    @Autowired
    private PlatUserService platUserService;

    @Autowired
    private OtcAdminService otcAdminService;

    @Autowired
    private OfflineRedEnvelopeServiceImpl offlineRedEnvelopeService;

    @Override
    @Transactional
    public RedEnvelopeViewVO send(RedEnvelopeSendVO redEnvelopeSendVO) {
        //检查参数
        this.checkParam(redEnvelopeSendVO);
        //检查后台配置文件
        MkRedEnvelopeConf mkRedEnvelopeConf = this.checkAndGetConf(redEnvelopeSendVO);
        //检查用户
        PlatUser platUser = this.checkAndGetUserInfo(redEnvelopeSendVO.getUserId());
        //创建红包
        MkRedEnvelope mkRedEnvelope = this.createRedEnvelope(redEnvelopeSendVO, mkRedEnvelopeConf, platUser);
        //扣款
        offlineCoinVolumeService.coinVolumeSubtract(redEnvelopeSendVO.getUserId(), mkRedEnvelopeConf.getCoinId(), redEnvelopeSendVO.getVolume(), mkRedEnvelope.getId());
        //发红包转账记录
        this.createSendChangeLog(platUser, mkRedEnvelopeConf, redEnvelopeSendVO, mkRedEnvelope);
        //生成随机红包
        this.createRedEnvelopeSub(mkRedEnvelope, mkRedEnvelopeConf);

        RedEnvelopeViewVO redEnvelopeViewVO = new RedEnvelopeViewVO();
        redEnvelopeViewVO.setRedEnvelopeId(mkRedEnvelope.getId());
        return redEnvelopeViewVO;
    }

    private void checkParam(RedEnvelopeSendVO redEnvelopeSendVO){
        //
        if(redEnvelopeSendVO.getVolume().compareTo(BigDecimal.ZERO) <= 0){
            throw new PlatException(Constants.PARAM_ERROR, "红包总额有误");
        }
        //
        if(redEnvelopeSendVO.getTotalNumber() <= 0){
            throw new PlatException(Constants.PARAM_ERROR, "红包个数参数有误");
        }
        //
        if(StringUtils.isEmpty(redEnvelopeSendVO.getType())){
            throw new PlatException(Constants.PARAM_ERROR, "红包类型不能空");
        }
        //
        if(!redEnvelopeSendVO.getType().equals("1") && !redEnvelopeSendVO.getType().equals("2")){
            throw new PlatException(Constants.PARAM_ERROR, "红包类型参数有误");
        }
        //默认
        if(StringUtils.isEmpty(redEnvelopeSendVO.getBestWith())){
            redEnvelopeSendVO.setBestWith("恭喜发财");
        }


    }

    private MkRedEnvelopeConf checkAndGetConf(RedEnvelopeSendVO redEnvelopeSendVO){

        MkRedEnvelopeConf mkRedEnvelopeConf = mkRedEnvelopeConfDao.findOneByCoinSymbol(coinSymbol);
        if(Objects.isNull(mkRedEnvelopeConf)){
            throw new PlatException(Constants.PARAM_ERROR, "红包功能已关闭");
        }

        if(redEnvelopeSendVO.getTotalNumber() < mkRedEnvelopeConf.getLowerNumber()){
            throw new PlatException(Constants.PARAM_ERROR, "红包个数不能小于：" + mkRedEnvelopeConf.getLowerNumber());
        }

        if(redEnvelopeSendVO.getTotalNumber() > mkRedEnvelopeConf.getHigherNumber()){
            throw new PlatException(Constants.PARAM_ERROR, "红包个数不能大于：" + mkRedEnvelopeConf.getHigherNumber());
        }

        if(redEnvelopeSendVO.getVolume().compareTo(mkRedEnvelopeConf.getSingleHigherVolume().multiply(new BigDecimal(redEnvelopeSendVO.getTotalNumber().toString()))) > 0){
            throw new PlatException(Constants.PARAM_ERROR, "单个红包不能大于：" + mkRedEnvelopeConf.getSingleHigherVolume());
        }

        if(redEnvelopeSendVO.getVolume().compareTo(mkRedEnvelopeConf.getSingleLowerVolume().multiply(new BigDecimal(redEnvelopeSendVO.getTotalNumber().toString()))) < 0){
            throw new PlatException(Constants.PARAM_ERROR, "单个红包不能低于：" + mkRedEnvelopeConf.getSingleLowerVolume());
        }

        return mkRedEnvelopeConf;
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
            logger.error("账号异常，无法发红包");
            throw new PlatException(Constants.PLAT_USER_CHANGE_FORBIDDEN, "账号异常，无法发红包");
        }
        return platUser;
    }

    private MkRedEnvelope createRedEnvelope(RedEnvelopeSendVO redEnvelopeSendVO, MkRedEnvelopeConf mkRedEnvelopeConf, PlatUser platUser){
        MkRedEnvelope mkRedEnvelope = new MkRedEnvelope();
        mkRedEnvelope.setId(SnowFlake.createSnowFlake().nextIdString());
        mkRedEnvelope.setCoinId(mkRedEnvelopeConf.getCoinId());
        mkRedEnvelope.setCoinSymbol(mkRedEnvelopeConf.getCoinSymbol());
        mkRedEnvelope.setUserId(platUser.getId());
        mkRedEnvelope.setMail(platUser.getMail());
        mkRedEnvelope.setMobile(platUser.getMobile());
        mkRedEnvelope.setRealName(platUser.getRealName());
        mkRedEnvelope.setStatus("0");
        mkRedEnvelope.setType(redEnvelopeSendVO.getType());
        mkRedEnvelope.setVolume(redEnvelopeSendVO.getVolume());
        mkRedEnvelope.setReceiveVolume(BigDecimal.ZERO);
        mkRedEnvelope.setTotalNumber(redEnvelopeSendVO.getTotalNumber());
        mkRedEnvelope.setReceiveNumber(0);
        mkRedEnvelope.setBestWith(redEnvelopeSendVO.getBestWith());
        mkRedEnvelope.setVersion(0L);
        mkRedEnvelopeDao.insert(mkRedEnvelope);
        return mkRedEnvelope;
    }

    private long createRedEnvelopeSub(MkRedEnvelope mkRedEnvelope, MkRedEnvelopeConf mkRedEnvelopeConf){
        List<MkRedEnvelopeSub> mkRedEnvelopeSubs = new ArrayList<>();
        for(int i = 0; i < mkRedEnvelope.getTotalNumber(); i++){
            MkRedEnvelopeSub mkRedEnvelopeSub = new MkRedEnvelopeSub();
            mkRedEnvelopeSub.setId(SnowFlake.createSnowFlake().nextIdString());
            mkRedEnvelopeSub.setCoinId(mkRedEnvelope.getCoinId());
            mkRedEnvelopeSub.setCoinSymbol(mkRedEnvelope.getCoinSymbol());
            mkRedEnvelopeSub.setEnvelopeId(mkRedEnvelope.getId());
            mkRedEnvelopeSub.setSendUserId(mkRedEnvelope.getUserId());
            mkRedEnvelopeSub.setSendMail(mkRedEnvelope.getMail());
            mkRedEnvelopeSub.setSendMobile(mkRedEnvelope.getMobile());
            mkRedEnvelopeSub.setSendRealName(mkRedEnvelope.getRealName());
            mkRedEnvelopeSub.setReceiveUserId(mkRedEnvelopeSub.getId().concat("-").concat(String.valueOf(i)));
            mkRedEnvelopeSub.setType(mkRedEnvelope.getType());
            mkRedEnvelopeSub.setStatus("0");
            mkRedEnvelopeSub.setVersion(0L);
            mkRedEnvelopeSub.setVolume(this.getRedEnvelopeSubVolume(mkRedEnvelopeSubs, i, mkRedEnvelope, mkRedEnvelopeConf));
            mkRedEnvelopeSubs.add(mkRedEnvelopeSub);
        }
        long count = mkRedEnvelopeSubDao.insertBatch(mkRedEnvelopeSubs);
        return count;
    }

    private BigDecimal getRedEnvelopeSubVolume(List<MkRedEnvelopeSub> mkRedEnvelopeSubs, Integer index, MkRedEnvelope mkRedEnvelope, MkRedEnvelopeConf mkRedEnvelopeConf){
        //普通红包
        if(mkRedEnvelope.getType().equals("1")){
            if(index == (mkRedEnvelope.getTotalNumber() - 1)){
                BigDecimal volumeSum = BigDecimal.valueOf(mkRedEnvelopeSubs.stream().mapToDouble(e -> Double.valueOf(String.valueOf(e.getVolume()))).sum());
                return mkRedEnvelope.getVolume().subtract(volumeSum).setScale(mkRedEnvelopeConf.getPointVolume(),BigDecimal.ROUND_DOWN);
            }else{
                return mkRedEnvelope.getVolume().divide(new BigDecimal(String.valueOf(mkRedEnvelope.getTotalNumber())),mkRedEnvelopeConf.getPointVolume(),BigDecimal.ROUND_DOWN);
            }
        }else{
        //手气红包
            if(index == (mkRedEnvelope.getTotalNumber() - 1)){
                BigDecimal volumeSum = BigDecimal.valueOf(mkRedEnvelopeSubs.stream().mapToDouble(e -> Double.valueOf(String.valueOf(e.getVolume()))).sum());
                return mkRedEnvelope.getVolume().subtract(volumeSum).setScale(mkRedEnvelopeConf.getPointVolume(),BigDecimal.ROUND_DOWN);
            }else{
                BigDecimal leftVolume = mkRedEnvelope.getVolume();
                if(!CollectionUtils.isEmpty(mkRedEnvelopeSubs)){
                    BigDecimal volumeSum = BigDecimal.valueOf(mkRedEnvelopeSubs.stream().mapToDouble(e -> Double.valueOf(String.valueOf(e.getVolume()))).sum());
                    leftVolume = mkRedEnvelope.getVolume().subtract(volumeSum);
                }

                Double minVolume = mkRedEnvelopeConf.getLuckyLowerVolume().doubleValue();
//                Double maxVolume = mkRedEnvelope.getVolume().subtract(volumeSum).divide(new BigDecimal("2")).doubleValue();
                Double maxVolume = mkRedEnvelopeConf.getLuckyHigherVolume().doubleValue();
                BigDecimal halfLeftVolume = leftVolume.divide(new BigDecimal("2"));
                if(mkRedEnvelopeConf.getLuckyHigherVolume().compareTo(halfLeftVolume) > 0){
                    maxVolume = halfLeftVolume.doubleValue();
                }

                BigDecimal curVolume = BigDecimal.valueOf(Math.random() * (maxVolume - minVolume) + minVolume);
//                if(curVolume.compareTo(mkRedEnvelopeConf.getLuckyHigherVolume()) > 0){
//                    curVolume = mkRedEnvelopeConf.getLuckyHigherVolume();
//                }

                Integer leftNumber = mkRedEnvelope.getTotalNumber() - mkRedEnvelopeSubs.size() - 1;
                BigDecimal curMinVolume = leftVolume.subtract(mkRedEnvelopeConf.getLuckyHigherVolume().multiply(BigDecimal.valueOf(leftNumber.doubleValue())));
                BigDecimal curMaxVolume = leftVolume.subtract(mkRedEnvelopeConf.getLuckyLowerVolume().multiply(BigDecimal.valueOf(leftNumber.doubleValue())));
                if(curVolume.compareTo(curMinVolume) < 0){
                    curVolume = curMinVolume;
                }else if(curVolume.compareTo(curMaxVolume) > 0){
                    curVolume = curMaxVolume;
                }
                return curVolume.setScale(mkRedEnvelopeConf.getPointVolume(), BigDecimal.ROUND_DOWN);
            }

        }

    }

    private void createSendChangeLog(PlatUser platUser,  MkRedEnvelopeConf mkRedEnvelopeConf, RedEnvelopeSendVO redEnvelopeSendVO, MkRedEnvelope mkRedEnvelope) {
        //转出方转账流水
        List<OfflineChangeLog> offlineChangeLogList = new ArrayList<>();
        OfflineChangeLog changeLog = new OfflineChangeLog();
        changeLog.setId(SnowFlake.createSnowFlake().nextIdString());
        changeLog.setUserId(platUser.getId());
        changeLog.setType("2"); //红包支出
        changeLog.setStatus("1");
        changeLog.setCoinId(mkRedEnvelopeConf.getCoinId());
        changeLog.setCoinSymbol(mkRedEnvelopeConf.getCoinSymbol());
        changeLog.setVolume(redEnvelopeSendVO.getVolume());
        changeLog.setFee(BigDecimal.ZERO);
        changeLog.setAccount(StringUtils.isNotEmpty(platUser.getMobile()) ? platUser.getMobile() : platUser.getMail());
        changeLog.setRealName(UserCardStatusEnum.authRealName(platUser.getCardStatus(),platUser.getCountryCode()) ? platUser.getRealName() : "暂未实名");
        changeLog.setOtherUserId("");
        changeLog.setOtherAccount("多个");
        changeLog.setOtherRealName("多个");
        changeLog.setChangeNo(mkRedEnvelope.getId());
        offlineChangeLogList.add(changeLog);
        long count = offlineChangeLogDao.insertBatch(offlineChangeLogList);
        if (count != 1) {
            throw new PlatException(Constants.PARAM_ERROR, "发红包转账记录失败！");
        }
    }

    @Override
    @Transactional
    public RedEnvelopeViewVO open(RedEnvelopeViewVO redEnvelopeViewVO) {

        MkRedEnvelopeConf mkRedEnvelopeConf = mkRedEnvelopeConfDao.findOneByCoinSymbol(coinSymbol);
        if(Objects.isNull(mkRedEnvelopeConf)){
            logger.info("未在红包活动时间范围内");
            throw new PlatException(Constants.MK_RED_ENVELOPE_FORBIDDEN, "未在红包活动时间范围内");
        }

        //检查红包主体信息
        MkRedEnvelope mkRedEnvelope = this.checkAndGetRedEnvelope(redEnvelopeViewVO);
        //检查用户信息
        PlatUser platUser = this.checkAngGetPlatUser(redEnvelopeViewVO, mkRedEnvelope);
        //检查用户是否已领取
        MkRedEnvelopeSub mkRedEnvelopeSub = mkRedEnvelopeSubDao.findByUserIdAndRedEnvelopeId(platUser.getId(), mkRedEnvelope.getId());
        if(Objects.nonNull(mkRedEnvelopeSub)){
            redEnvelopeViewVO.setVolume(mkRedEnvelopeSub.getVolume());
            redEnvelopeViewVO.setCoinSymbol(mkRedEnvelope.getCoinSymbol());
            redEnvelopeViewVO.setRealName(mkRedEnvelope.getRealName());
            redEnvelopeViewVO.setIsReceived("1");
            redEnvelopeViewVO.setUserId("");
            return redEnvelopeViewVO;
        }
        //打开一个红包
        mkRedEnvelopeSub = this.openOne(mkRedEnvelope, redEnvelopeViewVO, platUser);
        if(Objects.isNull(mkRedEnvelope)){
            throw new PlatException(Constants.MK_RED_ENVELOPE_FINISH, "手慢了红包被抢光");
        }
        //打款
        offlineCoinVolumeService.coinVolumeAdd(platUser.getId(), mkRedEnvelope.getCoinId(), mkRedEnvelope.getCoinSymbol(), mkRedEnvelopeSub.getVolume(), mkRedEnvelopeSub.getId());
        //打款日志
        this.createOpenChangeLog(platUser, mkRedEnvelope, mkRedEnvelopeSub);
        //更新主红包信息
        long count = mkRedEnvelopeDao.updateOpenOne(mkRedEnvelope.getId(), mkRedEnvelopeSub.getVolume());
        if(count != 1){
            throw new PlatException(Constants.MK_RED_ENVELOPE_FINISH, "手慢了红包被抢光");
        }
        //更新主红包是否领取完
        mkRedEnvelopeDao.updateForEnd(mkRedEnvelope.getId());
        //返回开奖金额
        redEnvelopeViewVO.setVolume(mkRedEnvelopeSub.getVolume().setScale(2, BigDecimal.ROUND_DOWN));
        redEnvelopeViewVO.setCoinSymbol(mkRedEnvelope.getCoinSymbol());
        redEnvelopeViewVO.setRealName(mkRedEnvelope.getRealName());
        redEnvelopeViewVO.setIsReceived("1");
        redEnvelopeViewVO.setUserId("");
        return redEnvelopeViewVO;
    }

    public MkRedEnvelopeSub openOne(MkRedEnvelope mkRedEnvelope,RedEnvelopeViewVO redEnvelopeViewVO, PlatUser platUser){
        long count = 0L;
        MkRedEnvelopeSub mkRedEnvelopeSub = null;
        for(int i = 0; i < 3; i++){
            try{
                List<MkRedEnvelopeSub> mkRedEnvelopeSubs = mkRedEnvelopeSubDao.findActiveList(redEnvelopeViewVO.getRedEnvelopeId());
                if(CollectionUtils.isEmpty(mkRedEnvelopeSubs)){
                    break;
                }
                Random random = new Random();
                mkRedEnvelopeSub = mkRedEnvelopeSubs.get(random.nextInt(mkRedEnvelopeSubs.size())); //随机选一个红包
                count = mkRedEnvelopeSubDao.updateOpenOne(platUser.getId(), platUser.getMobile(), platUser.getMail(), platUser.getRealName(), mkRedEnvelopeSub.getId(), mkRedEnvelopeSub.getVersion());
                if(count != 1){
                    throw new PlatException(Constants.PARAM_ERROR, "抢红包失败，发起重抢");
                }else{
                    break;
                }
            }catch(Exception e){
                logger.error("拆红包出现异常：", e);
            }
        }
        if(count != 1){
            throw new PlatException(Constants.PARAM_ERROR, "手慢了红包被抢光");
        }
        return mkRedEnvelopeSub;
    }

    private PlatUser checkAngGetPlatUser(RedEnvelopeViewVO redEnvelopeViewVO, MkRedEnvelope mkRedEnvelope){
        //用户信息
        PlatUser platUser = platUserService.findById(redEnvelopeViewVO.getUserId());
        return platUser;
    }
    private void createOpenChangeLog(PlatUser platUser, MkRedEnvelope mkRedEnvelope, MkRedEnvelopeSub mkRedEnvelopeSub) {
        //转出方转账流水
        List<OfflineChangeLog> offlineChangeLogList = new ArrayList<>();
        OfflineChangeLog changeLog = new OfflineChangeLog();
        changeLog.setId(SnowFlake.createSnowFlake().nextIdString());
        changeLog.setUserId(platUser.getId());
        changeLog.setType("3"); //红包收入
        changeLog.setStatus("1");
        changeLog.setCoinId(mkRedEnvelope.getCoinId());
        changeLog.setCoinSymbol(mkRedEnvelope.getCoinSymbol());
        changeLog.setVolume(mkRedEnvelopeSub.getVolume());
        changeLog.setFee(BigDecimal.ZERO);
        changeLog.setAccount(StringUtils.isNotEmpty(platUser.getMobile()) ? platUser.getMobile() : platUser.getMail());
        changeLog.setRealName(UserCardStatusEnum.authRealName(platUser.getCardStatus(),platUser.getCountryCode()) ? platUser.getRealName() : "暂未实名");
        changeLog.setOtherUserId(mkRedEnvelope.getUserId());
        changeLog.setOtherAccount(StringUtils.isNotEmpty(mkRedEnvelope.getMobile()) ? mkRedEnvelope.getMobile() : mkRedEnvelope.getMail());
        changeLog.setOtherRealName(mkRedEnvelope.getRealName());
        changeLog.setChangeNo(mkRedEnvelopeSub.getId());
        offlineChangeLogList.add(changeLog);
        long count = offlineChangeLogDao.insertBatch(offlineChangeLogList);
        if (count != 1) {
            throw new PlatException(Constants.PARAM_ERROR, "拆红包转账记录失败！");
        }
    }

    @Override
    public RedEnvelopeViewVO view(RedEnvelopeViewVO redEnvelopeViewVO) {

        MkRedEnvelopeConf mkRedEnvelopeConf = mkRedEnvelopeConfDao.findOneByCoinSymbol(coinSymbol);
        if(Objects.isNull(mkRedEnvelopeConf)){
            redEnvelopeViewVO.setUserId("");
            redEnvelopeViewVO.setStatus("3");
            return redEnvelopeViewVO;
        }


        MkRedEnvelope mkRedEnvelope = mkRedEnvelopeDao.findById(redEnvelopeViewVO.getRedEnvelopeId());
        if(Objects.isNull(mkRedEnvelope)){
            throw new PlatException(Constants.PARAM_ERROR, "参数异常");
        }

        redEnvelopeViewVO.setRealName(mkRedEnvelope.getRealName());
        redEnvelopeViewVO.setCoinSymbol(mkRedEnvelope.getCoinSymbol());
        redEnvelopeViewVO.setBestWith(mkRedEnvelope.getBestWith());
        redEnvelopeViewVO.setStatus(mkRedEnvelope.getStatus());
        redEnvelopeViewVO.setUpdateDate(mkRedEnvelope.getCreateDate());

        MkRedEnvelopeSub mkRedEnvelopeSub = mkRedEnvelopeSubDao.findByUserIdAndRedEnvelopeId(redEnvelopeViewVO.getUserId(), mkRedEnvelope.getId());
        if(Objects.nonNull(mkRedEnvelopeSub)){
            redEnvelopeViewVO.setIsReceived("1");
            redEnvelopeViewVO.setVolume(mkRedEnvelopeSub.getVolume().setScale(2, BigDecimal.ROUND_DOWN));
        }

        if((redEnvelopeViewVO.getStatus().equals("1")) && redEnvelopeViewVO.getIsReceived().equals("0")){
            redEnvelopeViewVO.setRemark("手慢了红包已被抢光");
        }else if((redEnvelopeViewVO.getStatus().equals("2") || this.isExpired(redEnvelopeViewVO.getUpdateDate())) && redEnvelopeViewVO.getIsReceived().equals("0")){ //红包已过期
            redEnvelopeViewVO.setStatus("2");
            redEnvelopeViewVO.setRemark("红包已结束");
        }else if(redEnvelopeViewVO.getStatus().equals("0") && redEnvelopeViewVO.getIsReceived().equals("0")){
            redEnvelopeViewVO.setRemark("给您发了一个UES红包");
        }

        redEnvelopeViewVO.setUserId("");
        return redEnvelopeViewVO;
    }

    private MkRedEnvelope checkAndGetRedEnvelope(RedEnvelopeViewVO redEnvelopeViewVO){

        MkRedEnvelope mkRedEnvelope = mkRedEnvelopeDao.findById(redEnvelopeViewVO.getRedEnvelopeId());
        if(Objects.isNull(mkRedEnvelope)){
            throw new PlatException(Constants.PARAM_ERROR, "参数异常");
        }

        if(mkRedEnvelope.getStatus().equals("1")){
            throw new PlatException(Constants.MK_RED_ENVELOPE_FINISH, "红包已领完");
        }

        if(mkRedEnvelope.getStatus().equals("2")){
            throw new PlatException(Constants.MK_RED_ENVELOPE_END, "红包已过期");
        }

        if(!mkRedEnvelope.getStatus().equals("0")){
            throw new PlatException(Constants.PARAM_ERROR, "红包状态异常");
        }

        if(this.isExpired(mkRedEnvelope.getCreateDate())){
            throw new PlatException(Constants.MK_RED_ENVELOPE_END, "红包已过期");
        }

        return mkRedEnvelope;
    }

    @Override
    public RedEnvelopeDetailVO detail(String id, String userId) {

        MkRedEnvelope mkRedEnvelope = mkRedEnvelopeDao.findById(id);
        if(Objects.isNull(mkRedEnvelope)){
            throw new PlatException(Constants.PARAM_ERROR, "非法参数");
        }

        RedEnvelopeDetailVO redEnvelopeDetailVO = new RedEnvelopeDetailVO();
        RedEnvelopeViewVO redEnvelopeViewVO = new RedEnvelopeViewVO();
        redEnvelopeViewVO.setCoinSymbol(mkRedEnvelope.getCoinSymbol());
        redEnvelopeViewVO.setRealName(mkRedEnvelope.getRealName());
        redEnvelopeViewVO.setRedEnvelopeId(mkRedEnvelope.getId());
        redEnvelopeViewVO.setStatus(mkRedEnvelope.getStatus());
        redEnvelopeViewVO.setVolume(mkRedEnvelope.getVolume().setScale(2, BigDecimal.ROUND_DOWN));
        redEnvelopeViewVO.setReceiveVolume(mkRedEnvelope.getReceiveVolume().setScale(2, BigDecimal.ROUND_DOWN));
        redEnvelopeViewVO.setTotalNumber(mkRedEnvelope.getTotalNumber());
        redEnvelopeViewVO.setReceiveNumber(mkRedEnvelope.getReceiveNumber());
        redEnvelopeViewVO.setBestWith(mkRedEnvelope.getBestWith());
        redEnvelopeViewVO.setUpdateDate(mkRedEnvelope.getCreateDate());
        redEnvelopeDetailVO.setRedEnvelopeViewVO(redEnvelopeViewVO);

        if(redEnvelopeViewVO.getStatus().equals("0") && this.isExpired(redEnvelopeViewVO.getUpdateDate())){
            redEnvelopeViewVO.setStatus("2");
        }

        String uesRate = otcAdminService.getLastRate(coinMain, coinSymbol);
        List<MkRedEnvelopeSub> mkRedEnvelopeSubs = mkRedEnvelopeSubDao.findReceiveList(mkRedEnvelope.getId());
        if(!CollectionUtils.isEmpty(mkRedEnvelopeSubs)){
            final List<RedEnvelopeSubViewVO> redEnvelopeSubViewVOS = new ArrayList<>();
            BigDecimal bestVolume = mkRedEnvelopeSubDao.findBestOne(mkRedEnvelope.getId());
            mkRedEnvelopeSubs.forEach(sub ->{
                RedEnvelopeSubViewVO redEnvelopeSubViewVO = new RedEnvelopeSubViewVO();
                redEnvelopeSubViewVO.setCoinSymbol(sub.getCoinSymbol());
                redEnvelopeSubViewVO.setRealName(this.maskOffCode(sub.getReceiveRealName(), sub.getReceiveMobile(), sub.getReceiveMail()));
                redEnvelopeSubViewVO.setVolume(sub.getVolume().setScale(2, BigDecimal.ROUND_DOWN));
                redEnvelopeSubViewVO.setUpdateDate(sub.getUpdateDate());
                redEnvelopeSubViewVO.setPrice(this.getPrice(sub.getVolume(), uesRate));

                if(mkRedEnvelope.getStatus().equals("1") && mkRedEnvelope.getType().equals("2")){
                    if(Objects.isNull(bestVolume) || bestVolume.compareTo(BigDecimal.ZERO) == 0 || bestVolume.compareTo(sub.getVolume()) == 0){
                        redEnvelopeSubViewVO.setIsBest("1");
                    }
                }

                if(sub.getReceiveUserId().equals(userId)){
                    redEnvelopeSubViewVO.setRealName(this.getMyMaskOffCode(sub.getReceiveRealName(), sub.getReceiveMobile(), sub.getReceiveMail()));
                    redEnvelopeViewVO.setIsReceived("1");
                    redEnvelopeViewVO.setMyReceiveVolume(sub.getVolume().setScale(2, BigDecimal.ROUND_DOWN));
                }
                redEnvelopeSubViewVOS.add(redEnvelopeSubViewVO);
            });
            redEnvelopeDetailVO.setRedEnvelopeSubViewVOList(redEnvelopeSubViewVOS);
        }

        return redEnvelopeDetailVO;
    }

    @Override
    public RedEnvelopeMySendVO findMySendInfo(RedEnvelopeListVO redEnvelopeListVO) {
        RedEnvelopeMySendVO redEnvelopeMySendVO = new RedEnvelopeMySendVO();
        List<MkRedEnvelope> mkRedEnvelopes = mkRedEnvelopeDao.findByUserId(redEnvelopeListVO.getUserId());
        if(!CollectionUtils.isEmpty(mkRedEnvelopes)){
            redEnvelopeMySendVO.setCoinSymbol(coinSymbol);
            redEnvelopeMySendVO.setRealName(mkRedEnvelopes.get(0).getRealName());
            redEnvelopeMySendVO.setTotalNumber(mkRedEnvelopes.size());
            redEnvelopeMySendVO.setVolume(BigDecimal.valueOf(mkRedEnvelopes.stream().mapToDouble(e -> Double.valueOf(String.valueOf(e.getVolume()))).sum()).setScale(2, BigDecimal.ROUND_DOWN));
        }
        return redEnvelopeMySendVO;
    }

    @Override
    public ResponsePage<RedEnvelopeViewVO> findMySendList(RedEnvelopeListVO redEnvelopeListVO) {
        ResponsePage<RedEnvelopeViewVO> responsePage = new ResponsePage<>();
        Page<RedEnvelopeViewVO> page = PageHelper.startPage(redEnvelopeListVO.getCurrentPage(), redEnvelopeListVO.getShowCount());
        List<MkRedEnvelope> mkRedEnvelopes = mkRedEnvelopeDao.findByUserId(redEnvelopeListVO.getUserId());

        List<RedEnvelopeViewVO> data = new ArrayList<>();
        mkRedEnvelopes.forEach(mkRedEnvelope -> {
            RedEnvelopeViewVO redEnvelopeViewVO = new RedEnvelopeViewVO();
            redEnvelopeViewVO.setCoinSymbol(mkRedEnvelope.getCoinSymbol());
            redEnvelopeViewVO.setType(mkRedEnvelope.getType());
            redEnvelopeViewVO.setVolume(mkRedEnvelope.getVolume().setScale(2, BigDecimal.ROUND_DOWN));
            redEnvelopeViewVO.setTotalNumber(mkRedEnvelope.getTotalNumber());
            redEnvelopeViewVO.setReceiveNumber(mkRedEnvelope.getReceiveNumber());
            redEnvelopeViewVO.setReceiveVolume(mkRedEnvelope.getReceiveVolume().setScale(2, BigDecimal.ROUND_DOWN));
            redEnvelopeViewVO.setStatus(mkRedEnvelope.getStatus());
            redEnvelopeViewVO.setRedEnvelopeId(mkRedEnvelope.getId());
            redEnvelopeViewVO.setUpdateDate(mkRedEnvelope.getCreateDate());
            data.add(redEnvelopeViewVO);
        });
        responsePage.setCount(page.getTotal());
        responsePage.setList(data);
        return responsePage;
    }

    @Override
    public RedEnvelopeMyReceiveVO findMyReceiveInfo(RedEnvelopeListVO redEnvelopeListVO) {
        RedEnvelopeMyReceiveVO redEnvelopeMyReceiveVO = new RedEnvelopeMyReceiveVO();
        List<MkRedEnvelopeSub> mkRedEnvelopes = mkRedEnvelopeSubDao.findByUserId(redEnvelopeListVO.getUserId());
        if(!CollectionUtils.isEmpty(mkRedEnvelopes)){
            redEnvelopeMyReceiveVO.setCoinSymbol(coinSymbol);
            redEnvelopeMyReceiveVO.setRealName(mkRedEnvelopes.get(0).getReceiveRealName());
            redEnvelopeMyReceiveVO.setTotalNumber(mkRedEnvelopes.size());
            redEnvelopeMyReceiveVO.setVolume(BigDecimal.valueOf(mkRedEnvelopes.stream().mapToDouble(e -> Double.valueOf(String.valueOf(e.getVolume()))).sum()).setScale(2, BigDecimal.ROUND_DOWN));
        }
        return redEnvelopeMyReceiveVO;
    }

    @Override
    public ResponsePage<RedEnvelopeSubViewVO> findMyReceiveList(RedEnvelopeListVO redEnvelopeListVO) {
        ResponsePage<RedEnvelopeSubViewVO> responsePage = new ResponsePage<>();
        Page<RedEnvelopeSubViewVO> page = PageHelper.startPage(redEnvelopeListVO.getCurrentPage(), redEnvelopeListVO.getShowCount());
        List<MkRedEnvelopeSub> mkRedEnvelopes = mkRedEnvelopeSubDao.findByUserId(redEnvelopeListVO.getUserId());
        String uesRate = otcAdminService.getLastRate(coinMain, coinSymbol); //ues/euc汇率
        List<RedEnvelopeSubViewVO> data = new ArrayList<>();
        mkRedEnvelopes.forEach(mkRedEnvelope -> {
            RedEnvelopeSubViewVO redEnvelopeSubViewVO = new RedEnvelopeSubViewVO();
            redEnvelopeSubViewVO.setCoinSymbol(mkRedEnvelope.getCoinSymbol());
            redEnvelopeSubViewVO.setRealName(mkRedEnvelope.getSendRealName());
            redEnvelopeSubViewVO.setUpdateDate(mkRedEnvelope.getUpdateDate());
            redEnvelopeSubViewVO.setPrice(this.getPrice(mkRedEnvelope.getVolume(),uesRate));
            redEnvelopeSubViewVO.setVolume(mkRedEnvelope.getVolume().setScale(2, BigDecimal.ROUND_DOWN));
            redEnvelopeSubViewVO.setRedEnvelopeId(mkRedEnvelope.getEnvelopeId());
            data.add(redEnvelopeSubViewVO);
        });
        responsePage.setCount(page.getTotal());
        responsePage.setList(data);
        return responsePage;
    }

    @Override
    public MkRedEnvelopeConf getRedEnvelopeConf() {
        MkRedEnvelopeConf mkRedEnvelopeConf = mkRedEnvelopeConfDao.findOneByCoinSymbol(coinSymbol);
        if(ObjectUtils.isEmpty(mkRedEnvelopeConf)){
            mkRedEnvelopeConf = new MkRedEnvelopeConf();
            mkRedEnvelopeConf.setStatus("0");
        }else{
            mkRedEnvelopeConf.setDestroyUserId(null);
            mkRedEnvelopeConf.setId(null);
            mkRedEnvelopeConf.setCreateBy(null);
            mkRedEnvelopeConf.setCreateDate(null);
            mkRedEnvelopeConf.setUpdateBy(null);
            mkRedEnvelopeConf.setUpdateBy(null);
            mkRedEnvelopeConf.setCoinId(null);
            mkRedEnvelopeConf.setVersion(null);
            mkRedEnvelopeConf.setRemark(null);
            mkRedEnvelopeConf.setUpdateDate(null);

            mkRedEnvelopeConf.setLuckyLowerVolume(null);
        }
        return mkRedEnvelopeConf;
    }

    private BigDecimal getPrice(BigDecimal volume, String uesRate){
        if(StringUtils.isEmpty(uesRate)){
            return BigDecimal.ZERO;
        }
        return volume.multiply(new BigDecimal(uesRate)).multiply(new BigDecimal(EUC_RMB_RATE)).setScale(2, BigDecimal.ROUND_DOWN);
    }

    private boolean isExpired(LocalDateTime createDate){
        return createDate.plusHours(24L).isBefore(LocalDateTime.now());
    }

    @Override
    public void handleExpired() {
        List<MkRedEnvelope> mkRedEnvelopes = mkRedEnvelopeDao.findExpiredList();
        mkRedEnvelopes.forEach(mkRedEnvelope -> {
            try{
                offlineRedEnvelopeService.updateExpired(mkRedEnvelope);
            }catch (Exception e){
                logger.error(String.format("退回红包[%s]出现异常", mkRedEnvelope.getId()), e);
            }
        });
    }

    @Transactional
    public void updateExpired(MkRedEnvelope mkRedEnvelope){

        //更新红包信息
        long count = mkRedEnvelopeSubDao.updateExpired(mkRedEnvelope.getId());
        if(count <= 0L){
            throw new PlatException(Constants.PARAM_ERROR, "更新红包出现异常");
        }
        //更新红包主体信息
        count = mkRedEnvelopeDao.updateExpiredOne(mkRedEnvelope.getId(), mkRedEnvelope.getVersion());
        if(count != 1){
            throw new PlatException(Constants.PARAM_ERROR, "更新红包出现异常");
        }
        //退回金额
        BigDecimal addVolume = mkRedEnvelope.getVolume().subtract(mkRedEnvelope.getReceiveVolume());
        String bathNo = SnowFlake.createSnowFlake().nextIdString();
        offlineCoinVolumeService.coinVolumeAdd(mkRedEnvelope.getUserId(), mkRedEnvelope.getCoinId(), mkRedEnvelope.getCoinSymbol(), addVolume, bathNo);
        //创建转账记录
        this.createExpiredChangeLog(mkRedEnvelope, bathNo);
    }

    private void createExpiredChangeLog(MkRedEnvelope mkRedEnvelope, String bathNo) {
        //转出方转账流水
        List<OfflineChangeLog> offlineChangeLogList = new ArrayList<>();
        OfflineChangeLog changeLog = new OfflineChangeLog();
        changeLog.setId(SnowFlake.createSnowFlake().nextIdString());
        changeLog.setUserId(mkRedEnvelope.getUserId());
        changeLog.setType("4"); //红包退回
        changeLog.setStatus("1");
        changeLog.setCoinId(mkRedEnvelope.getCoinId());
        changeLog.setCoinSymbol(mkRedEnvelope.getCoinSymbol());
        changeLog.setVolume(mkRedEnvelope.getVolume().subtract(mkRedEnvelope.getReceiveVolume()));
        changeLog.setFee(BigDecimal.ZERO);
        changeLog.setAccount(StringUtils.isNotEmpty(mkRedEnvelope.getMobile()) ? mkRedEnvelope.getMobile() : mkRedEnvelope.getMail());
        changeLog.setRealName(mkRedEnvelope.getRealName());
        changeLog.setOtherUserId(mkRedEnvelope.getUserId());
        changeLog.setOtherAccount(StringUtils.isNotEmpty(mkRedEnvelope.getMobile()) ? mkRedEnvelope.getMobile() : mkRedEnvelope.getMail());
        changeLog.setOtherRealName(mkRedEnvelope.getRealName());
        changeLog.setChangeNo(bathNo);
        offlineChangeLogList.add(changeLog);
        long count = offlineChangeLogDao.insertBatch(offlineChangeLogList);
        if (count != 1) {
            throw new PlatException(Constants.PARAM_ERROR, "发红包转账记录失败！");
        }
    }

    private String getMyMaskOffCode(String realName,String mobile, String mail){
        if(StringUtils.isNotEmpty(realName)){
            return realName;
        }else if(StringUtils.isNotEmpty(mobile)){
            return mobile;
        }else if(StringUtils.isNotEmpty(mail)){
            return mail;
        }else{
            return "";
        }
    }

    private String maskOffCode(String realName,String mobile, String mail){
        if(StringUtils.isNotEmpty(realName)){
            return maskOffRealName(realName);
        }else if(StringUtils.isNotEmpty(mobile)){
            return maskOff(mobile);
        }else if(StringUtils.isNotEmpty(mail)){
            return maskOff(mail);
        }else{
            return "";
        }
    }
    private String maskOff(String username) {
        if (org.springframework.util.StringUtils.isEmpty(username)) return "";

        String usernameTmp = username;
        String tail = "";
        if (username.indexOf("@") > 0) {
            usernameTmp = username.substring(0, username.indexOf("@"));
            tail = username.substring(username.indexOf("@"), username.length());
        }

        String startStr = "";
        String endStr = "";
        if (usernameTmp.length() > 7) {
            startStr = usernameTmp.substring(0, 3);
            endStr = usernameTmp.substring(usernameTmp.length() - 4, usernameTmp.length());
        } else {
            startStr = usernameTmp.substring(0, 1);
            endStr = usernameTmp.substring(usernameTmp.length() - 2, usernameTmp.length());
        }

        return startStr.concat("****").concat(endStr).concat(tail);
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
