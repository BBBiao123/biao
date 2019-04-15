package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.*;
import com.biao.enums.OfflineTransferLogEnum;
import com.biao.enums.UserCoinVolumeEventEnum;
import com.biao.exception.PlatException;
import com.biao.mapper.*;
import com.biao.pojo.UserCoinVolumeBillDTO;
import com.biao.service.SuperCoinVolumeService;
import com.biao.service.UserCoinVolumeBillService;
import com.biao.service.UserCoinVolumeExService;
import com.biao.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class SuperCoinVolumeServiceImpl implements SuperCoinVolumeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuperCoinVolumeServiceImpl.class);

    @Autowired
    private SuperCoinVolumeDao superCoinVolumeDao;

    @Autowired
    private UserCoinVolumeDao userCoinVolumeDao;

    @Autowired
    private UserCoinVolumeExService userCoinVolumeService;

    @Autowired
    private UserCoinVolumeBillService userCoinVolumeBillService;

    @Autowired
    private SuperCoinVolumeConfDao superCoinVolumeConfDao;

    @Autowired
    private OfflineTransferLogDao offlineTransferLogDao;

    @Autowired
    private DestroyAccountLogDao destroyAccountLogDao;

    @Autowired
    private PlatUserDao platUserDao;

    @Autowired
    private SuperCoinVolumeServiceImpl superCoinVolumeService;

    @Override
    public SuperCoinVolume findById(String id) {
        return superCoinVolumeDao.findById(id);
    }

    @Override
    public void updateById(SuperCoinVolume superCoinVolume) {

    }

    @Override
    public List<SuperCoinVolume> findAll(String userId) {
        return superCoinVolumeDao.findByUserId(userId);
    }

    /**
     * 常规转超级
     * @param userId
     * @param coinId
     * @param volume
     * @param symbol
     */
    @Override
    @Transactional
    public void in(String userId, String coinId, BigDecimal volume, String symbol) {
        if (volume.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,转入资金非法");
        }

        // 取划转之前账户资产，记录到transfer表source_volume字段，用于达子对账
        BigDecimal bbVolume = BigDecimal.ZERO; //
        UserCoinVolume userCoinVolume =  userCoinVolumeService.findByUserIdAndCoinSymbol(userId, symbol);
        if (Objects.nonNull(userCoinVolume)) {
            bbVolume = userCoinVolume.getVolume();
        }

        //判断最小值
        SuperCoinVolumeConf superCoinVolumeConf = this.checkCoinInAndGetConf(coinId, volume);

        LOGGER.info("bb-c2c bb update userId: " + userId + " bbVolume:" + volume);
        userCoinVolumeService.updateOutcomeException(null,volume,userId,symbol);

        //判断币币账户该资产
//        UserCoinVolume userCoinVolume = userCoinVolumeDao.findByUserIdAndCoinId(userId, coinId);
        //判断super 是否有值
        SuperCoinVolume superCoinVolume = superCoinVolumeDao.findByUserIdAndCoinId(userId, coinId);
//        if (null != userCoinVolume) {
//            if (userCoinVolume.getVolume().compareTo(volume) >= 0) {
                if (superCoinVolume != null) {
                    //判断合约是否到期
                    if((Objects.isNull(superCoinVolume.getDepositBegin()) && Objects.isNull(superCoinVolume.getDepositEnd()))){
                        superCoinVolume.setDepositBegin(LocalDateTime.now());
                        superCoinVolume.setDepositEnd(LocalDateTime.now().plusDays(superCoinVolumeConf.getLockCycle()));
                        superCoinVolumeDao.updateById(superCoinVolume);
                    }
                    //super add
//                    BigDecimal superVolume = superCoinVolume.getVolume().add(volume);
//                    if (superVolume.compareTo(BigDecimal.ZERO) < 0) {
//                        throw new PlatException(Constants.OPERRATION_ERROR, "操作非法");
//                    }
//                    Timestamp timestamp = Timestamp.valueOf(superCoinVolume.getUpdateDate());
//                    LOGGER.info("bb-super super update userId: " + userId + " superVolume:" + superVolume);
//                    long count = superCoinVolumeDao.updateVolume(userId, coinId, superVolume, timestamp, superCoinVolume.getVersion());
                    LOGGER.info("bb-super super update userId: " + userId + " addVolume:" + volume);
                    long count = superCoinVolumeDao.updateAddVolume(userId, coinId, volume);
                    if (count == 0) {
                        throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
                    }
                } else {
                    superCoinVolume = new SuperCoinVolume();
                    String id = SnowFlake.createSnowFlake().nextIdString();
                    superCoinVolume.setId(id);
                    superCoinVolume.setVolume(volume);
                    superCoinVolume.setDepositBegin(LocalDateTime.now());
                    superCoinVolume.setDepositEnd(LocalDateTime.now().plusDays(superCoinVolumeConf.getLockCycle()));
                    superCoinVolume.setCreateDate(LocalDateTime.now());
                    superCoinVolume.setUpdateDate(LocalDateTime.now());
                    superCoinVolume.setUserId(userId);
                    superCoinVolume.setCoinId(coinId);
                    superCoinVolume.setCoinSymbol(symbol);
                    superCoinVolume.setVersion(0L);
                    superCoinVolumeDao.insert(superCoinVolume);
                }
//                //bb sub
//                BigDecimal bbVolume = userCoinVolume.getVolume().subtract(volume);
//                if (bbVolume.compareTo(BigDecimal.ZERO) < 0) {
//                    throw new PlatException(Constants.OPERRATION_ERROR, "操作非法");
//                }

                //写入 转入日志日志
                Integer type = Integer.parseInt(OfflineTransferLogEnum.COIN_TO_SUPER.getCode());
                this.saveLog(userId, coinId, symbol, volume, type, BigDecimal.ZERO, bbVolume);
//            } else {
//                throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,可用资金不足");
//            }
//        } else {
//            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,可用资金不足");
//        }
    }

    /**
     *
     * @param userId
     * @param coinId
     * @param coinSymbol
     * @param volume
     * @param type
     * @param penaltyVolume
     * @param sourceVolume  取划转之前from账户资产，记录到transfer表source_volume字段，用于达子对账
     * @return
     */
    private OfflineTransferLog saveLog(String userId, String coinId, String coinSymbol, BigDecimal volume, Integer type, BigDecimal penaltyVolume, BigDecimal sourceVolume) {
        OfflineTransferLog offlineTransferLog = new OfflineTransferLog();
        String id = SnowFlake.createSnowFlake().nextIdString();
        offlineTransferLog.setId(id);
        offlineTransferLog.setCreateDate(LocalDateTime.now());
        offlineTransferLog.setUpdateDate(LocalDateTime.now());
        offlineTransferLog.setUserId(userId);
        offlineTransferLog.setCoinSymbol(coinSymbol);
        offlineTransferLog.setVolume(volume);
        offlineTransferLog.setFeeVolume(penaltyVolume);
        offlineTransferLog.setType(type);
        offlineTransferLog.setCoinId(coinId);
        offlineTransferLog.setSourceVolume(sourceVolume);
        offlineTransferLogDao.insert(offlineTransferLog);
        return offlineTransferLog;
    }

    private SuperCoinVolumeConf checkCoinInAndGetConf(String coinId, BigDecimal volume){
        SuperCoinVolumeConf superCoinVolumeConf = this.checkAndGetConf(coinId);
        if(volume.compareTo(superCoinVolumeConf.getInMinVolume()) < 0){
            throw new PlatException(Constants.OPERRATION_ERROR, "转入最小数量为：" + String.valueOf(superCoinVolumeConf.getInMinVolume().setScale(2, BigDecimal.ROUND_HALF_DOWN)));
        }
        return superCoinVolumeConf;
    }


    private SuperCoinVolumeConf checkAndGetConf(String coinId){
        SuperCoinVolumeConf superCoinVolumeConf = superCoinVolumeConfDao.findOneByCoin(coinId);
        if(Objects.isNull(superCoinVolumeConf)){
            throw new PlatException(Constants.OPERRATION_ERROR, "操作非法");
        }

        if(superCoinVolumeConf.getTransferStatus().equals("0")){
            throw new PlatException(Constants.OPERRATION_ERROR, "钱包升级中，无法操作划转（算力不受影响）");
        }

        return superCoinVolumeConf;
    }

    @Override
    @Transactional
    public void out(String userId, String coinId, BigDecimal volume, String symbol) {
        if (volume.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,转出资金非法");
        }

        SuperCoinVolumeConf superCoinVolumeConf = this.checkCoinOutAndGetConf(coinId, volume);
        SuperCoinVolume superCoinVolume = superCoinVolumeDao.findByUserIdAndCoinId(userId, coinId);
        if (superCoinVolume != null) {
            if (superCoinVolume.getVolume().compareTo(volume) >= 0) {

                if(Objects.nonNull(superCoinVolume.getDepositBegin()) && superCoinVolume.getDepositBegin().plusDays(superCoinVolumeConf.getFrozenDay()).isAfter(LocalDateTime.now())){
                    throw new PlatException(Constants.OPERRATION_ERROR, String.format("资产转入%s天后才能转出", String.valueOf(superCoinVolumeConf.getFrozenDay())));
                }

                //super sub
                BigDecimal subVolume = superCoinVolume.getVolume().subtract(volume);
                if (subVolume.compareTo(BigDecimal.ZERO) < 0) {
                    throw new PlatException(Constants.OPERRATION_ERROR, "操作非法");
                }

                BigDecimal penaltyVolume = BigDecimal.ZERO;
                if(Objects.nonNull(superCoinVolume.getDepositEnd()) && superCoinVolume.getDepositEnd().isAfter(LocalDateTime.now())){
                    penaltyVolume = this.getPenaltyVolume(superCoinVolumeConf, volume);
                    volume = volume.subtract(penaltyVolume);
                }

                if (volume.compareTo(BigDecimal.ZERO) < 0) {
                    throw new PlatException(Constants.OPERRATION_ERROR, "操作非法");
                }

                Timestamp timestamp = Timestamp.valueOf(superCoinVolume.getUpdateDate());
                LOGGER.info("super-bb super update userId: " + userId + " subVolume:" + subVolume);
                long count = superCoinVolumeDao.updateVolume(userId, coinId, subVolume, timestamp, superCoinVolume.getVersion());
                if (count == 0) {
                    throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
                }

                //写入 转入日志日志
                Integer type = Integer.parseInt(OfflineTransferLogEnum.SUPER_TO_COIN.getCode());
                OfflineTransferLog offlineTransferLog = this.saveLog(userId, coinId, symbol, volume, type, penaltyVolume, superCoinVolume.getVolume());
                //生成币币资产变更记录
                this.saveCoinVolumeBillDTO(symbol, userId, volume, "超级钱包转币币", offlineTransferLog.getId(),"TRANSFER");
//                //用户bb add
//                userCoinVolumeService.updateIncomeException(null, volume, userId, symbol);
//                销毁账户bb add
//                userCoinVolumeService.updateIncomeException(null, penaltyVolume, superCoinVolumeConf.getDestroyUserId(), symbol);

                //销毁日志, 销毁账户
                if(penaltyVolume.compareTo(BigDecimal.ZERO) > 0){
                    DestroyAccountLog destroyAccountLog = this.saveDestroyLog(userId, coinId, symbol, penaltyVolume, "2");
                    this.saveCoinVolumeBillDTO(symbol, superCoinVolumeConf.getDestroyUserId(), penaltyVolume, "超级钱包转币币-销毁", destroyAccountLog.getId(),"DESTROY");
                }
            } else {
                throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,可用资金不足");
            }
        } else {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,可用资金不足");
        }
    }

    private void saveCoinVolumeBillDTO(String coinSymbol, String userId, BigDecimal volume, String mark, String refKey, String source){
        UserCoinVolumeBillDTO billDTO = new UserCoinVolumeBillDTO();
        billDTO.setCoinSymbol(coinSymbol);
        billDTO.setForceLock(true);
        billDTO.setMark(mark);
        billDTO.setOpLockVolume(new BigDecimal(0));
        billDTO.setOpVolume(volume);
        billDTO.setUserId(userId);
        billDTO.setOpSign(new UserCoinVolumeEventEnum[]{UserCoinVolumeEventEnum.ADD_VOLUME});
        billDTO.setPriority(5);
        billDTO.setRefKey(refKey);
        billDTO.setSource(source);

        userCoinVolumeBillService.insert(billDTO);
    }

    private DestroyAccountLog saveDestroyLog(String userId, String coinId, String coinSymbol, BigDecimal volume, String type){
        DestroyAccountLog destroyAccountLog = new DestroyAccountLog();
        destroyAccountLog.setId(SnowFlake.createSnowFlake().nextIdString());
        destroyAccountLog.setCoinId(coinId);
        destroyAccountLog.setCoinSymbol(coinSymbol);
        destroyAccountLog.setType(type);
        destroyAccountLog.setVolume(volume);
        PlatUser platUser = platUserDao.findById(userId);
        if(Objects.isNull(platUser)){
            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败");
        }
        destroyAccountLog.setUserId(userId);
        destroyAccountLog.setMail(platUser.getMail());
        destroyAccountLog.setMobile(platUser.getMobile());
        destroyAccountLog.setCreateDate(LocalDateTime.now());
        destroyAccountLog.setUpdateDate(LocalDateTime.now());
        destroyAccountLogDao.insert(destroyAccountLog);
        return destroyAccountLog;
    }


    private SuperCoinVolumeConf checkCoinOutAndGetConf(String coinId, BigDecimal volume){
        SuperCoinVolumeConf superCoinVolumeConf = this.checkAndGetConf(coinId);
        if(volume.compareTo(superCoinVolumeConf.getOutMinVolume()) < 0){
            throw new PlatException(Constants.OPERRATION_ERROR, "转出最小数量为：" + String.valueOf(superCoinVolumeConf.getOutMinVolume().setScale(2, BigDecimal.ROUND_HALF_DOWN)));
        }

        return superCoinVolumeConf;
    }

    private BigDecimal getPenaltyVolume(SuperCoinVolumeConf superCoinVolumeConf, BigDecimal volume){
        return superCoinVolumeConf.getBreakRatio().multiply(volume);
    }

    @Override
    public void handleExpireAccount(LocalDateTime expireDate) {
        SuperCoinVolumeConf superCoinVolumeConf = superCoinVolumeConfDao.findOne();
        if(Objects.isNull(superCoinVolumeConf)){
            throw new PlatException(Constants.OPERRATION_ERROR, "操作非法");
        }

        List<SuperCoinVolume> superCoinVolumeList = superCoinVolumeDao.findByExpireDate(expireDate);
        superCoinVolumeList.forEach(superCoinVolume -> {
            superCoinVolumeService.handleOne(superCoinVolume);
        });
    }

    @Transactional
    public void handleOne(SuperCoinVolume superCoinVolume){

        BigDecimal volume = superCoinVolume.getVolume();
        String userId = superCoinVolume.getUserId();
        String coinId = superCoinVolume.getCoinId();
        String symbol = superCoinVolume.getCoinSymbol();
        //super sub
        BigDecimal subVolume = superCoinVolume.getVolume().subtract(volume);
        if (subVolume.compareTo(BigDecimal.ZERO) < 0) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作非法");
        }

        BigDecimal penaltyVolume = BigDecimal.ZERO; //违约金为零
        Timestamp timestamp = Timestamp.valueOf(superCoinVolume.getUpdateDate());
        LOGGER.info("super-bb super update userId: " + userId + " subVolume:" + subVolume);
        long count = superCoinVolumeDao.updateExpiredVolume(userId, coinId, subVolume, timestamp, superCoinVolume.getVersion());
        if (count == 0) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        //用户bb add
        if(volume.compareTo(BigDecimal.ZERO) > 0){
            //写入 转入日志日志
            Integer type = Integer.parseInt(OfflineTransferLogEnum.SUPER_TO_COIN.getCode());
            OfflineTransferLog offlineTransferLog = this.saveLog(userId, coinId, symbol, volume, type, penaltyVolume, superCoinVolume.getVolume());
            //超级钱包转币币
            this.saveCoinVolumeBillDTO(symbol, userId, volume, "超级钱包转币币-到期处理", offlineTransferLog.getId(),"TRANSFER");
        }



    }
}
