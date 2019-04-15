package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.*;
import com.biao.entity.otc.OtcOfflineOrder;
import com.biao.entity.otc.OtcOfflineOrderDetail;
import com.biao.enums.*;
import com.biao.exception.PlatException;
import com.biao.mapper.*;
import com.biao.pojo.UserCoinVolumeBillDTO;
import com.biao.redis.RedisCacheManager;
import com.biao.service.OfflineCoinVolumeLogService;
import com.biao.service.OfflineCoinVolumeService;
import com.biao.service.UserCoinVolumeBillService;
import com.biao.service.UserCoinVolumeExService;
import com.biao.util.SnowFlake;
import org.apache.commons.lang.StringUtils;
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
import java.util.Optional;

@Service
public class OfflineCoinVolumeServiceImpl implements OfflineCoinVolumeService {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OfflineCoinVolumeServiceImpl.class);

    private final OfflineCoinVolumeDao offlineCoinVolumeDao;

    private final OfflineCoinVolumeLogDao offlineCoinVolumeLogDao;

    private final UserCoinVolumeDao userCoinVolumeDao;

    private final OfflineTransferLogDao offlineTransferLogDao;

    private final RedisCacheManager redisCacheManager;

    private final PlatUserDao platUserDao;

    private final OfflineCoinDao offlineCoinDao;

    private final OfflineOrderDetailDao offlineOrderDetailDao;

    private final OfflineCoinVolumeLogService offlineCoinVolumeLogService;

    private final UserCoinVolumeExService userCoinVolumeService;

    private UserCoinVolumeBillService userCoinVolumeBillService;

    @Autowired(required = false)
    public OfflineCoinVolumeServiceImpl(OfflineCoinVolumeDao offlineCoinVolumeDao,
                                        OfflineCoinVolumeLogDao offlineCoinVolumeLogDao,
                                        UserCoinVolumeDao userCoinVolumeDao,
                                        OfflineTransferLogDao offlineTransferLogDao,
                                        RedisCacheManager redisCacheManager,
                                        OfflineCoinVolumeLogService offlineCoinVolumeLogService,
                                        PlatUserDao platUserDao, OfflineCoinDao offlineCoinDao,
                                        OfflineOrderDetailDao offlineOrderDetailDao,
                                        UserCoinVolumeExService userCoinVolumeService,
                                        UserCoinVolumeBillService userCoinVolumeBillService
    ) {
        this.offlineCoinVolumeDao = offlineCoinVolumeDao;
        this.offlineCoinVolumeLogDao = offlineCoinVolumeLogDao;
        this.userCoinVolumeDao = userCoinVolumeDao;
        this.offlineTransferLogDao = offlineTransferLogDao;
        this.redisCacheManager = redisCacheManager;
        this.platUserDao = platUserDao;
        this.offlineCoinDao = offlineCoinDao;
        this.offlineOrderDetailDao = offlineOrderDetailDao;
        this.offlineCoinVolumeLogService = offlineCoinVolumeLogService;
        this.userCoinVolumeService = userCoinVolumeService;
        this.userCoinVolumeBillService = userCoinVolumeBillService;
    }

    @Override
    public void updateById(OfflineCoinVolume offlineCoinVolume) {
    }


    @Override
    public List<OfflineCoinVolume> findAll(String userId) {
        return offlineCoinVolumeDao.findAll(userId);
    }

    @Override
    @Transactional
    public void inOut(String userId, String coinId, BigDecimal volume, String symbol, String from, String to, String loginSource) {
        if (from.equals(VolumeTypeEnum.COIN_VOLUME.getCode())) {
            notAllowIn(userId);
            if (to.equals(VolumeTypeEnum.OFFLINE_VOLUME.getCode())) {
                LOGGER.info("bb 到 c2c");
                in(userId, coinId, volume, symbol);
            } else if (to.equals(VolumeTypeEnum.OFFLINE_BAIL_VOLUME.getCode())) {
                LOGGER.info("bb 到 bail");
                coinToBail(userId, coinId, volume, symbol);
            }

        } else if (from.equals(VolumeTypeEnum.OFFLINE_VOLUME.getCode())) {
            if (to.equals(VolumeTypeEnum.COIN_VOLUME.getCode())) {
                notAllowOut(userId);
                LOGGER.info("c2c 到 bb");
                out(userId, coinId, volume, symbol);
            } else if (to.equals(VolumeTypeEnum.OFFLINE_BAIL_VOLUME.getCode())) {
                LOGGER.info("c2c 到 bail");
                bailIn(userId, coinId, volume, symbol, loginSource);
            }
        } else if (from.equals(VolumeTypeEnum.OFFLINE_BAIL_VOLUME.getCode())) {
            if (to.equals(VolumeTypeEnum.COIN_VOLUME.getCode())) {
                notAllowOut(userId);
                LOGGER.info("bail 到 bb");
                bailToCoin(userId, coinId, volume, symbol);
            } else if (to.equals(VolumeTypeEnum.OFFLINE_VOLUME.getCode())) {
                LOGGER.info("bail 到 c2c");
                bailOut(userId, coinId, volume, symbol);
            }
        }

    }

    public void notAllowOut(String userId) {
        final PlatUser user = platUserDao.findById(userId);
        if (Objects.nonNull(user)) {
            final String c2cOut = user.getC2cOut();
            if (StringUtils.isNotBlank(c2cOut) && Integer.valueOf(c2cOut) == 1) {
                LOGGER.error("该用户已经被限制进行c2c 转出的操作 用户id为:{}", userId);
                throw new PlatException(Constants.OPERRATION_ERROR, "用户已经被限制c2c转出");
            }
        }
    }

    public void notAllowIn(String userId) {
        final PlatUser user = platUserDao.findById(userId);
        if (Objects.nonNull(user)) {
            final String c2cIn = user.getC2cIn();
            if (StringUtils.isNotBlank(c2cIn) && Integer.valueOf(c2cIn) == 1) {
                LOGGER.error("该用户已经被限制进行c2c 转入的操作 用户id为:{}", userId);
                throw new PlatException(Constants.OPERRATION_ERROR, "用户已经被限制c2c转入");
            }
        }
    }

    /**
     * bb 转c2c
     */
    @Override
    @Transactional
    public void in(String userId, String coinId, BigDecimal volume, String symbol) {
        if (volume.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,转入资金非法");
        }

        // 取常规划转之前账户资产，记录到transfer表source_volume字段，用于达子对账
        BigDecimal bbVolume = BigDecimal.ZERO; //
        UserCoinVolume userCoinVolume =  userCoinVolumeService.findByUserIdAndCoinSymbol(userId, symbol);
        if (Objects.nonNull(userCoinVolume)) {
            bbVolume = userCoinVolume.getVolume();
        }

        LOGGER.info("bb-c2c bb update userId: " + userId + " bbVolume:" + volume);
        userCoinVolumeService.updateOutcomeException(null,volume,userId,symbol);

//        //判断币币账户该资产
//        UserCoinVolume userCoinVolume = userCoinVolumeDao.findByUserIdAndCoinId(userId, coinId);
        //判断offline 是否有值
        OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(userId, coinId);
//        if (null != userCoinVolume) {
//            if (userCoinVolume.getVolume().compareTo(volume) >= 0) {
                if (offlineCoinVolume != null) {
                    //off add
//                    BigDecimal offlineVolume = offlineCoinVolume.getVolume().add(volume);
//                    if (offlineVolume.compareTo(BigDecimal.ZERO) < 0) {
//                        throw new PlatException(Constants.OPERRATION_ERROR, "操作非法");
//                    }
//                    Timestamp timestamp = Timestamp.valueOf(offlineCoinVolume.getUpdateDate());
//                    LOGGER.info("bb-c2c c2c update userId: " + userId + " offlineVolume:" + offlineVolume);
//                    long count = offlineCoinVolumeDao.updateVolume(userId, coinId, offlineVolume, timestamp, offlineCoinVolume.getVersion());
                    LOGGER.info("bb-c2c c2c update userId: " + userId + " addVolume:" + volume);
                    long count = offlineCoinVolumeDao.updateAddVolume(userId, coinId, volume);
                    if (count == 0) {
                        LOGGER.info("更新C2C资产失败，bb-c2c c2c update userId: "+ userId + " addVolume:" + volume);
                        throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
                    }
                } else {
                    offlineCoinVolume = new OfflineCoinVolume();
                    String id = SnowFlake.createSnowFlake().nextIdString();
                    offlineCoinVolume.setId(id);
                    offlineCoinVolume.setVolume(volume);
                    offlineCoinVolume.setAdvertVolume(BigDecimal.ZERO);
                    offlineCoinVolume.setLockVolume(BigDecimal.ZERO);
                    offlineCoinVolume.setBailVolume(BigDecimal.ZERO);
                    offlineCoinVolume.setCreateDate(LocalDateTime.now());
                    offlineCoinVolume.setUpdateDate(LocalDateTime.now());
                    offlineCoinVolume.setUserId(userId);
                    offlineCoinVolume.setCoinId(coinId);
                    offlineCoinVolume.setCoinSymbol(symbol);
                    offlineCoinVolume.setVersion(0L);
                    offlineCoinVolumeDao.insert(offlineCoinVolume);
                }
                //bb sub
//                BigDecimal bbVolume = userCoinVolume.getVolume().subtract(volume);
//                if (bbVolume.compareTo(BigDecimal.ZERO) < 0) {
//                    throw new PlatException(Constants.OPERRATION_ERROR, "操作非法");
//                }
//                Timestamp timestamp = Timestamp.valueOf(userCoinVolume.getUpdateDate());

               /* userCoinVolumeDao.updateVolume(userId, coinId, bbVolume, timestamp);
                redisCacheManager.cleanUserCoinVolume(userId, symbol);*/
                //写入 转入日志日志
                Integer type = Integer.parseInt(OfflineTransferLogEnum.COIN_TO_OFFLINE.getCode());
                this.saveLog(userId, coinId, symbol, volume, type, bbVolume);
//            } else {
//                throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,可用资金不足");
//            }
//        } else {
//            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,可用资金不足");
//        }
    }


    /**
     * 常规到保证金
     *
     * @param userId
     * @param coinId
     * @param volume
     * @param symbol
     */
    @Transactional
    public void coinToBail(String userId, String coinId, BigDecimal volume, String symbol) {

        if (volume.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,转入资金非法");
        }

        // 取划转之前账户资产，记录到transfer表source_volume字段，用于达子对账
        BigDecimal bbVolume = BigDecimal.ZERO; //
        UserCoinVolume userCoinVolume =  userCoinVolumeService.findByUserIdAndCoinSymbol(userId, symbol);
        if (Objects.nonNull(userCoinVolume)) {
            bbVolume = userCoinVolume.getVolume();
        }

        LOGGER.info("bb-bail bb update userId: " + userId + " bbVolume:" + volume);
        userCoinVolumeService.updateOutcomeException(null,volume,userId,symbol);

//        //判断币币账户该资产
//        UserCoinVolume userCoinVolume = userCoinVolumeDao.findByUserIdAndCoinId(userId, coinId);
        //判断offline 是否有值
        OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(userId, coinId);
//        if (null != userCoinVolume) {
//            if (userCoinVolume.getVolume().compareTo(volume) >= 0) {
//                //bb sub
//                BigDecimal bbVolume = userCoinVolume.getVolume().subtract(volume);
//                if (bbVolume.compareTo(BigDecimal.ZERO) < 0) {
//                    throw new PlatException(Constants.OPERRATION_ERROR, "操作非法");
//                }
                if (offlineCoinVolume != null) {
                    //bail add
//                    BigDecimal bailVolume = offlineCoinVolume.getBailVolume().add(volume);
//                    Timestamp timestamp = Timestamp.valueOf(offlineCoinVolume.getUpdateDate());
//                    LOGGER.info("bb-bail bail update userId: " + userId + " bailVolume:" + bailVolume);
//                    long count = offlineCoinVolumeDao.updateBailVolume(userId, coinId, bailVolume, timestamp, offlineCoinVolume.getVersion());
                    LOGGER.info("bb-bail bail update userId: " + userId + " addBailVolume:" + volume);
                    long count = offlineCoinVolumeDao.updateAddBailVolume(userId, coinId, volume);
                    if (count == 0) {
                        throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
                    }
                } else {
                    offlineCoinVolume = new OfflineCoinVolume();
                    String id = SnowFlake.createSnowFlake().nextIdString();
                    offlineCoinVolume.setId(id);
                    offlineCoinVolume.setVolume(BigDecimal.ZERO);
                    offlineCoinVolume.setAdvertVolume(BigDecimal.ZERO);
                    offlineCoinVolume.setLockVolume(BigDecimal.ZERO);
                    offlineCoinVolume.setBailVolume(volume);
                    offlineCoinVolume.setCreateDate(LocalDateTime.now());
                    offlineCoinVolume.setUpdateDate(LocalDateTime.now());
                    offlineCoinVolume.setUserId(userId);
                    offlineCoinVolume.setCoinId(coinId);
                    offlineCoinVolume.setCoinSymbol(symbol);
                    offlineCoinVolume.setVersion(0L);
                    offlineCoinVolumeDao.insert(offlineCoinVolume);
                }

                //更新BB资产
//                Timestamp timestamp = Timestamp.valueOf(userCoinVolume.getUpdateDate());

                /*userCoinVolumeDao.updateVolume(userId, coinId, bbVolume, timestamp);
                redisCacheManager.cleanUserCoinVolume(userId, symbol);*/
                //写入 转入日志日志
                Integer type = Integer.parseInt(OfflineTransferLogEnum.COIN_TO_BAIL.getCode());
                OfflineTransferLog offlineTransferLog = this.saveLog(userId, coinId, symbol, volume, type, bbVolume);

//            } else {
//                throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,可用资金不足");
//            }
//        } else {
//            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,可用资金不足");
//        }
    }


    /**
     * c2c转入保证金
     *
     * @param userId
     * @param coinId
     * @param inVolume
     * @param symbol
     * @param loginSource 代表登录来源，为空或者plat标识环球交易所；OTC标识WOTC那边
     */
    @Override
    @Transactional
    public void bailIn(String userId, String coinId, BigDecimal inVolume, String symbol, String loginSource) {
        if (inVolume.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,转入资金非法");
        }
        //判断offline 是否有值
        OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(userId, coinId);
        if (offlineCoinVolume != null) {
            if (!AppSourceTypeEnum.OTC.getCode().equals(loginSource)) { // 环球需要限制转入预备金最低数量，OTC不需要限制
                //查询最低转入金额
                OfflineCoin offlineCoin = offlineCoinDao.findByCoinIdForChange(coinId);
                if (inVolume.compareTo(offlineCoin.getBailVolume()) < 0) {
                    throw new PlatException(Constants.BAIL_OPERRATION_ERROR, "转入手续费预备金太小，最低" + offlineCoin.getBailVolume());
                }
            }
            //volume sub
            BigDecimal volume = offlineCoinVolume.getVolume().subtract(inVolume);
            if (volume.compareTo(BigDecimal.ZERO) < 0) {
                throw new PlatException(Constants.OPERRATION_ERROR, "可用资金不足，转入失败");
            }
            //bail_volume add
            BigDecimal bailVolume = offlineCoinVolume.getBailVolume().add(inVolume);
            Timestamp timestamp = Timestamp.valueOf(offlineCoinVolume.getUpdateDate());
            long count = offlineCoinVolumeDao.updateVolumeAndBailVolume(userId, coinId, volume, bailVolume, timestamp, offlineCoinVolume.getVersion());
            if (count == 0) {
                throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
            }
            //写入 转入日志日志
            Integer type = Integer.parseInt(OfflineTransferLogEnum.OFFLINE_TO_BAIL.getCode());
            this.saveLog(userId, coinId, symbol, inVolume, type, offlineCoinVolume.getVolume());
        } else {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,可用资金不足");
        }

    }


    /**
     * 保证金转到C2c
     *
     * @param userId
     * @param coinId
     * @param inVolume
     * @param symbol
     */
    @Override
    @Transactional
    public void bailOut(String userId, String coinId, BigDecimal inVolume, String symbol) {
        if (inVolume.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,转出资金非法");
        }
        //判断offline 是否有值
        OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(userId, coinId);
        if (offlineCoinVolume != null) {
            if (inVolume.compareTo(offlineCoinVolume.getBailVolume()) > 0) {
                throw new PlatException(Constants.BAIL_OPERRATION_ERROR, "转出手续费预备金不足，最多只能转" + offlineCoinVolume.getBailVolume());
            }

            String[] statusArray = {OfflineOrderDetailStatusEnum.NOMAL.getCode(), OfflineOrderDetailStatusEnum.CONFIRM_PAY.getCode()};
            String statusIn = StringUtils.join(statusArray, ",");
            Double sumFee = offlineOrderDetailDao.sumByStatusAndUserIdAndCoinId(statusIn, userId, coinId);
            BigDecimal residualVolume = offlineCoinVolume.getBailVolume().subtract(inVolume);
            if (residualVolume.compareTo(BigDecimal.valueOf(sumFee == null ? 0 : sumFee)) < 0) {
                throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,手续费预备金最少要剩余" + sumFee.doubleValue());
            }
            //volume add
            BigDecimal offlineVolume = offlineCoinVolume.getVolume().add(inVolume);

            //bail_volume sub
            BigDecimal bailVolume = offlineCoinVolume.getBailVolume().subtract(inVolume);
            if (bailVolume.compareTo(BigDecimal.ZERO) < 0) {
                throw new PlatException(Constants.OPERRATION_ERROR, "操作失败bailout");
            }
            Timestamp timestamp = Timestamp.valueOf(offlineCoinVolume.getUpdateDate());
            long count = offlineCoinVolumeDao.updateVolumeAndBailVolume(userId, coinId, offlineVolume, bailVolume, timestamp, offlineCoinVolume.getVersion());
            if (count == 0) {
                throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
            }
            //写入 转入日志日志
            Integer type = Integer.parseInt(OfflineTransferLogEnum.BAIL_TO_OFFLINE.getCode());
            this.saveLog(userId, coinId, symbol, inVolume, type, offlineCoinVolume.getBailVolume());
        } else {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,可用资金不足");
        }

    }

    /**
     * @param userId
     * @param coinId
     * @param coinSymbol
     * @param volume
     * @param type
     * @param sourceVolume  取划转之前from账户资产，记录到transfer表source_volume字段，用于达子对账
     * @return
     */
    private OfflineTransferLog saveLog(String userId, String coinId, String coinSymbol, BigDecimal volume, Integer type, BigDecimal sourceVolume) {
        OfflineTransferLog offlineTransferLog = new OfflineTransferLog();
        String id = SnowFlake.createSnowFlake().nextIdString();
        offlineTransferLog.setId(id);
        offlineTransferLog.setCreateDate(LocalDateTime.now());
        offlineTransferLog.setUpdateDate(LocalDateTime.now());
        offlineTransferLog.setUserId(userId);
        offlineTransferLog.setCoinSymbol(coinSymbol);
        offlineTransferLog.setVolume(volume);
        offlineTransferLog.setType(type);
        offlineTransferLog.setCoinId(coinId);
        offlineTransferLog.setSourceVolume(sourceVolume);
        offlineTransferLogDao.insert(offlineTransferLog);
        return offlineTransferLog;
    }

    /**
     * c2c 转BB
     *
     * @param userId
     * @param coinId
     * @param volume
     * @param symbol
     */
    @Override
    @Transactional
    public void out(String userId, String coinId, BigDecimal volume, String symbol) {
        if (volume.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,转出资金非法");
        }
        //判断币币账户该资产
//        UserCoinVolume userCoinVolume = userCoinVolumeDao.findByUserIdAndCoinId(userId, coinId);
        OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(userId, coinId);
        if (offlineCoinVolume != null) {
            if (offlineCoinVolume.getVolume().compareTo(volume) >= 0) {
                //off sub
                BigDecimal subVolume = offlineCoinVolume.getVolume().subtract(volume);
                if (subVolume.compareTo(BigDecimal.ZERO) < 0) {
                    throw new PlatException(Constants.OPERRATION_ERROR, "操作非法");
                }
                Timestamp timestamp = Timestamp.valueOf(offlineCoinVolume.getUpdateDate());
                LOGGER.info("c2c-bb c2c update userId: " + userId + " subVolume:" + subVolume);
                long count = offlineCoinVolumeDao.updateVolume(userId, coinId, subVolume, timestamp, offlineCoinVolume.getVersion());
                if (count == 0) {
                    throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
                }
                //bb add
                //判断币币账户是否有资产
              /*  if (userCoinVolume != null) {
                    BigDecimal addVolume = userCoinVolume.getVolume().add(volume);
                    timestamp = Timestamp.valueOf(userCoinVolume.getUpdateDate());
                    LOGGER.info("c2c-bb bb update userId: " + userId + " addVolume:" + addVolume);
                    count = userCoinVolumeDao.updateVolume(userId, coinId, addVolume, timestamp);
                    if (count == 0) {
                        throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
                    }
                    redisCacheManager.cleanUserCoinVolume(userId, symbol);
                } else {
                    userCoinVolume = new UserCoinVolume();
                    String id = SnowFlake.createSnowFlake().nextIdString();
                    userCoinVolume.setId(id);
                    userCoinVolume.setVolume(volume);
                    userCoinVolume.setLockVolume(BigDecimal.ZERO);
                    userCoinVolume.setOutLockVolume(BigDecimal.ZERO);
                    userCoinVolume.setCreateDate(LocalDateTime.now());
                    userCoinVolume.setUpdateDate(LocalDateTime.now());
                    userCoinVolume.setUserId(userId);
                    userCoinVolume.setCoinId(coinId);
                    userCoinVolume.setCoinSymbol(symbol);
                    userCoinVolume.setFlag((short) 0);

                    userCoinVolumeDao.insert(userCoinVolume);
                    redisCacheManager.cleanUserCoinVolume(userId, symbol);
                }*/
//                userCoinVolumeService.updateIncomeException(null,volume,userId,symbol);
                //写入 转入日志日志
                Integer type = Integer.parseInt(OfflineTransferLogEnum.OFFLINE_TO_COIN.getCode());
                OfflineTransferLog offlineTransferLog = this.saveLog(userId, coinId, symbol, volume, type, offlineCoinVolume.getVolume());
                //生成币币资产变更记录
                this.saveCoinVolumeBillDTO(symbol, userId, volume, "C2C转币币", offlineTransferLog.getId());
            } else {
                throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,可用资金不足");
            }
        } else {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,可用资金不足");
        }
    }


    /**
     * 保证金到币币资产
     *
     * @param userId
     * @param coinId
     * @param volume
     * @param symbol
     */
    @Transactional
    public void bailToCoin(String userId, String coinId, BigDecimal volume, String symbol) {
        if (volume.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,转出资金非法");
        }

        //判断币币账户该资产
//        UserCoinVolume userCoinVolume = userCoinVolumeDao.findByUserIdAndCoinId(userId, coinId);
        OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(userId, coinId);
        if (offlineCoinVolume != null) {
            if (offlineCoinVolume.getBailVolume().compareTo(volume) >= 0) {

                //判断保证金账户余额是否够支付未完成的订单的手续费
                String[] statusArray = {OfflineOrderDetailStatusEnum.NOMAL.getCode(), OfflineOrderDetailStatusEnum.CONFIRM_PAY.getCode()};
                String statusIn = org.apache.commons.lang3.StringUtils.join(statusArray, ",");
                Double sumFee = offlineOrderDetailDao.sumByStatusAndUserIdAndCoinId(statusIn, userId, coinId);
                BigDecimal residualVolume = volume.add(BigDecimal.valueOf(sumFee == null ? 0 : sumFee));
                if (offlineCoinVolume.getBailVolume().compareTo(residualVolume) < 0) {
                    throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,手续费预备金最少剩余" + sumFee);
                }

                //bail sub
                BigDecimal subVolume = offlineCoinVolume.getBailVolume().subtract(volume);
                if (subVolume.compareTo(BigDecimal.ZERO) < 0) {
                    throw new PlatException(Constants.OPERRATION_ERROR, "操作非法");
                }
                Timestamp timestamp = Timestamp.valueOf(offlineCoinVolume.getUpdateDate());
                LOGGER.info("bail-bb bai update userId: " + userId + " subVolume:" + subVolume);
                long count = offlineCoinVolumeDao.updateBailVolume(userId, coinId, subVolume, timestamp, offlineCoinVolume.getVersion());
                if (count == 0) {
                    throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
                }
                //bb add
                //判断币币账户是否有资产
               /* if (userCoinVolume != null) {
                    BigDecimal addVolume = userCoinVolume.getVolume().add(volume);
                    timestamp = Timestamp.valueOf(userCoinVolume.getUpdateDate());
                    LOGGER.info("bail-bb bb update userId: " + userId + " addVolume:" + addVolume);
                    count = userCoinVolumeDao.updateVolume(userId, coinId, addVolume, timestamp);
                    if (count == 0) {
                        throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
                    }
                    redisCacheManager.cleanUserCoinVolume(userId, symbol);
                } else {
                    userCoinVolume = new UserCoinVolume();
                    String id = SnowFlake.createSnowFlake().nextIdString();
                    userCoinVolume.setId(id);
                    userCoinVolume.setVolume(volume);
                    userCoinVolume.setLockVolume(BigDecimal.ZERO);
                    userCoinVolume.setOutLockVolume(BigDecimal.ZERO);
                    userCoinVolume.setCreateDate(LocalDateTime.now());
                    userCoinVolume.setUpdateDate(LocalDateTime.now());
                    userCoinVolume.setUserId(userId);
                    userCoinVolume.setCoinId(coinId);
                    userCoinVolume.setCoinSymbol(symbol);
                    userCoinVolume.setFlag((short) 0);
                    userCoinVolumeDao.insert(userCoinVolume);
                    redisCacheManager.cleanUserCoinVolume(userId, symbol);
                }*/
//                userCoinVolumeService.updateIncomeException(null,volume,userId,symbol);
                //写入 转入日志日志
                Integer type = Integer.parseInt(OfflineTransferLogEnum.BAIL_TO_COIN.getCode());
                OfflineTransferLog offlineTransferLog = this.saveLog(userId, coinId, symbol, volume, type, offlineCoinVolume.getBailVolume());
                //生成币币资产变更记录
                this.saveCoinVolumeBillDTO(symbol, userId, volume, "保证金转币币", offlineTransferLog.getId());
            } else {
                throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,可用资金不足");
            }
        } else {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,可用资金不足");
        }
    }

    @Override
    public List<OfflineCoinVolume> findAll() {
        return null;
    }

    @Override
    public OfflineCoinVolume findByUserIdAndCoinId(String userId, String coinId) {
        return offlineCoinVolumeDao.findByUserIdAndCoinId(userId, coinId);
    }

    @Override
    public OfflineCoinVolume findById(String id) {
        return offlineCoinVolumeDao.findById(id);
    }

    /**
     * OTC广告变更影响C2C账户资金变动
     *
     * @param otcOfflineOrder
     */
    @Override
    @Transactional
    public void otcOrderSaveUpdateCoinVolume(OtcOfflineOrder otcOfflineOrder, String batchNo) {
        if (!otcOfflineOrder.getExType().equals(String.valueOf(TradeEnum.SELL.ordinal()))) { // 只有OTC卖广告才影响C2C资产变动
            return;
        }
        checkOtcOrderParam(otcOfflineOrder);// 参数校验volume,lockVolume,successVolume 不能小于0
        // check该C2C币资产是否存在
        OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(otcOfflineOrder.getUserId(), otcOfflineOrder.getCoinId());
        if (Objects.isNull(offlineCoinVolume)) {
            throw new PlatException(Constants.OFFLINE_VOLUME_ERROR, "当前用户不存在该币种资产");
        }

        //C2C资产变更：可用资产要减，冻结资产要加
        offlineCoinVolumeLogService.saveLog(offlineCoinVolume.getUserId(), offlineCoinVolume.getCoinId(), batchNo);// C2C资产流水

        // C2C资产卖币方计算开始
        BigDecimal volume = null;// c2c可用资产
        BigDecimal advertVolume = null; // c2c广告冻结资产
        Timestamp updateDate = Timestamp.valueOf(offlineCoinVolume.getUpdateDate());
        // 发布广告动作
        if (otcOfflineOrder.getStatus().equals(OfflineOrderStatusEnum.NOMAL.getCode())) {

            if (offlineCoinVolume.getBailVolume().compareTo(otcOfflineOrder.getFeeVolume()) == -1) { // 发布广告，判断保证金
                throw new PlatException(Constants.OFFLINE_VOLUME_ERROR, "当前用户手续费备用金不足");
            }

            volume = offlineCoinVolume.getVolume().subtract(otcOfflineOrder.getVolume());// c2c可用资产 = c2c可用资产 - 广告数量
            advertVolume = offlineCoinVolume.getAdvertVolume().add(otcOfflineOrder.getVolume()); // c2c广告冻结资产 = c2c广告冻结资产 + 广告数量
        }
        // 取消广告动作
        else if (otcOfflineOrder.getStatus().equals(OfflineOrderStatusEnum.CANCEL.getCode())) {
            BigDecimal exVolume = otcOfflineOrder.getVolume().subtract(otcOfflineOrder.getSuccessVolume()).subtract(otcOfflineOrder.getLockVolume());// 广告剩余数量
            if (exVolume.compareTo(BigDecimal.ZERO) == -1) { //广告剩余数量 不为负数
                throw new PlatException(Constants.PARAM_ERROR);
            }
            volume = offlineCoinVolume.getVolume().add(exVolume);// c2c可用资产 = c2c可用资产 + 广告数量剩余
            advertVolume = offlineCoinVolume.getAdvertVolume().subtract(exVolume); // c2c广告冻结资产 = c2c广告冻结资产 - 广告数量剩余
        }
        // C2C资产计算结束
        // == 扣手续费？？？
        if (Objects.isNull(volume)) {
            throw new PlatException(Constants.PARAM_ERROR, "广告状态错误");
        }
        if (volume.compareTo(BigDecimal.ZERO) == -1 || advertVolume.compareTo(BigDecimal.ZERO) == -1) { // 如果volume为空，表示广告参数等有误
            throw new PlatException(Constants.PARAM_ERROR, "资金不足");
        }
        long count = offlineCoinVolumeDao.updateVolumeAndAdvertVolume(otcOfflineOrder.getUserId(), otcOfflineOrder.getCoinId(), volume, advertVolume, updateDate, offlineCoinVolume.getVersion());
        if (count != 1) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }

        offlineCoinVolumeLogService.saveLog(otcOfflineOrder.getUserId(), otcOfflineOrder.getCoinId(), batchNo);// C2C资产流水
    }

    private void checkOtcOrderParam(OtcOfflineOrder otcOfflineOrder) {
        greaterZero(otcOfflineOrder.getVolume(), null);
        greaterZero(otcOfflineOrder.getSuccessVolume(), null);
        greaterZero(otcOfflineOrder.getLockVolume(), null);
    }

    private void checkOtcDetailParam(OtcOfflineOrderDetail otcOfflineOrderDetail) {
        greaterZero(otcOfflineOrderDetail.getVolume(), null);
        if (!String.valueOf(ExTypeEnum.SELL.getCode()).equals(otcOfflineOrderDetail.getExType())) {
            throw new PlatException(Constants.PARAM_ERROR, "订单的卖币方参数错误");
        }
    }

    /**
     * OTC订单变更影响C2C账户资金变动
     *
     * @param sellOrderDetail 卖币订单（入参只接受卖币订单）
     * @param otcOfflineOrder 广告
     */
    @Override
    @Transactional
    public void otcDetailSaveUpdateCoinVolume(OtcOfflineOrderDetail sellOrderDetail, OtcOfflineOrder otcOfflineOrder, String batchNo) {
        checkOtcDetailParam(sellOrderDetail);
        checkOtcOrderParam(otcOfflineOrder);
        // check该C2C币资产是否存在
        OfflineCoinVolume sellCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(sellOrderDetail.getUserId(), sellOrderDetail.getCoinId());
        if (Objects.isNull(sellCoinVolume)) {
            throw new PlatException(Constants.OFFLINE_VOLUME_ERROR, "当前用户不存在该币种资产");
        }
        offlineCoinVolumeLogService.saveLog(sellCoinVolume, batchNo);    //C2C资产变更记录日志
        BigDecimal volume = null;
        BigDecimal lockVolume = null; // 交易冻结
        BigDecimal advertVolume = null; // 广告冻结
        BigDecimal bailVolume = Optional.ofNullable(sellCoinVolume.getBailVolume()).orElse(BigDecimal.ZERO); // 保证金
        // 下单动作
        if (sellOrderDetail.getStatus().equals(OfflineOrderDetailStatusEnum.NOMAL.getCode()) && otcOfflineOrder.getStatus().equals(OfflineOrderStatusEnum.NOMAL.getCode())) {  // 下单、广告发布 注：广告发布状态才能下单
            if (String.valueOf(TradeEnum.BUY.ordinal()).equals(otcOfflineOrder.getExType())) { // 买广告
                volume = sellCoinVolume.getVolume().subtract(sellOrderDetail.getVolume());
                advertVolume = sellCoinVolume.getAdvertVolume();
                lockVolume = sellCoinVolume.getLockVolume().add(sellOrderDetail.getVolume());
            } else if (String.valueOf(TradeEnum.SELL.ordinal()).equals(otcOfflineOrder.getExType())) { // 卖广告
                volume = sellCoinVolume.getVolume();
                advertVolume = sellCoinVolume.getAdvertVolume().subtract(sellOrderDetail.getVolume());
                lockVolume = sellCoinVolume.getLockVolume().add(sellOrderDetail.getVolume());
            }

        }
        // 取消订单动作
        else if (sellOrderDetail.getStatus().equals(OfflineOrderDetailStatusEnum.CANCEL.getCode())) {
            if (String.valueOf(TradeEnum.BUY.ordinal()).equals(otcOfflineOrder.getExType())) { // 买广告
                volume = sellCoinVolume.getVolume().add(sellOrderDetail.getVolume());
                advertVolume = sellCoinVolume.getAdvertVolume();
                lockVolume = sellCoinVolume.getLockVolume().subtract(sellOrderDetail.getVolume());
            } else if (String.valueOf(TradeEnum.SELL.ordinal()).equals(otcOfflineOrder.getExType())) { // 卖广告
                if (otcOfflineOrder.getStatus().equals(OfflineOrderStatusEnum.NOMAL.getCode())) {// 订单取消、广告发布
                    volume = sellCoinVolume.getVolume();
                    advertVolume = sellCoinVolume.getAdvertVolume().add(sellOrderDetail.getVolume());
                    lockVolume = sellCoinVolume.getLockVolume().subtract(sellOrderDetail.getVolume());
                } else if (otcOfflineOrder.getStatus().equals(OfflineOrderStatusEnum.CANCEL.getCode())) { // 订单取消、广告取消
                    volume = sellCoinVolume.getVolume().add(sellOrderDetail.getVolume());
                    advertVolume = sellCoinVolume.getAdvertVolume();
                    lockVolume = sellCoinVolume.getLockVolume().subtract(sellOrderDetail.getVolume());
                }
            }
        }
        // 订单确认收款动作
        else if (sellOrderDetail.getStatus().equals(OfflineOrderDetailStatusEnum.CONFIRM_IN.getCode())) { // 订单确认收款，广告任意状态
            volume = sellCoinVolume.getVolume();
            advertVolume = sellCoinVolume.getAdvertVolume();
            lockVolume = sellCoinVolume.getLockVolume().subtract(sellOrderDetail.getVolume()); // 卖币方资产减volume

            BigDecimal buyerSubtractFee = BigDecimal.ZERO;  // 买方扣手续费
            if (otcOfflineOrder.getUserId().equals(sellOrderDetail.getUserId())) {  // 卖方是广告方，从卖方保证金里扣手续费
                bailVolume = bailVolume.subtract(Optional.ofNullable(sellOrderDetail.getFeeVolume()).orElse(BigDecimal.ZERO));
            } else if (otcOfflineOrder.getUserId().equals(sellOrderDetail.getAskUserId())) { // 买方是广告方，从买方保证金里扣手续费
                buyerSubtractFee = sellOrderDetail.getFeeVolume();
            } else {
                throw new PlatException(Constants.PARAM_ERROR, "订单参数错误");
            }
            // 买币方C2C资产变更
            coinVolumeAddBailSub(sellOrderDetail.getAskUserId(), sellOrderDetail.getCoinId(), sellOrderDetail.getSymbol(), sellOrderDetail.getVolume(), buyerSubtractFee, batchNo);// 买币方资产加volume
        }

        // == 扣手续费？？？

        sellCoinVolume.setVolume(volume);
        sellCoinVolume.setAdvertVolume(advertVolume);
        sellCoinVolume.setLockVolume(lockVolume);
        sellCoinVolume.setBailVolume(bailVolume);
        saveCoinVolumeInfo(sellCoinVolume, batchNo); // 保存C2C资产信息
    }

    /**
     * 给C2C资产加可用资产
     *
     * @param userId
     * @param coinId
     * @param symbol
     * @param addVolume
     */
    @Transactional
    public void coinVolumeAddBailSub(String userId, String coinId, String symbol, BigDecimal addVolume, BigDecimal buyerSubtractFee, String batchNo) {
        greaterZero(addVolume, null);
        greaterZero(buyerSubtractFee, null);
        OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(userId, coinId);
        if (Objects.isNull(offlineCoinVolume)) {
            offlineCoinVolume = new OfflineCoinVolume();
            offlineCoinVolume.setId(SnowFlake.createSnowFlake().nextIdString());
            offlineCoinVolume.setCoinId(coinId);
            offlineCoinVolume.setCoinSymbol(symbol);
            offlineCoinVolume.setUserId(userId);
            offlineCoinVolume.setVolume(addVolume);
            offlineCoinVolume.setAdvertVolume(BigDecimal.ZERO);
            offlineCoinVolume.setLockVolume(BigDecimal.ZERO);
            offlineCoinVolume.setBailVolume(BigDecimal.ZERO.subtract(Optional.ofNullable(buyerSubtractFee).orElse(BigDecimal.ZERO)));
            offlineCoinVolume.setCreateDate(LocalDateTime.now());
            offlineCoinVolume.setUpdateDate(LocalDateTime.now());
            offlineCoinVolume.setVersion(0L);
            offlineCoinVolumeDao.insert(offlineCoinVolume);
            offlineCoinVolumeLogService.saveLog(offlineCoinVolume.getUserId(), offlineCoinVolume.getCoinId(), batchNo);   //资产变更记录日志
        } else {
            offlineCoinVolumeLogService.saveLog(offlineCoinVolume, batchNo);    //C2C资产变更记录日志
            offlineCoinVolume.setVolume(offlineCoinVolume.getVolume().add(addVolume));
            BigDecimal bailVolume = Optional.ofNullable(offlineCoinVolume.getBailVolume()).orElse(BigDecimal.ZERO).subtract(Optional.ofNullable(buyerSubtractFee).orElse(BigDecimal.ZERO));
            offlineCoinVolume.setBailVolume(bailVolume);

            saveCoinVolumeInfo(offlineCoinVolume, batchNo);

        }
    }

    @Transactional
    public void saveCoinVolumeInfo(OfflineCoinVolume offlineCoinVolume, String batchNo) {
        checkCoinVolumeInfo(offlineCoinVolume);
        //offlineCoinVolumeLogService.saveLog(offlineCoinVolume.getUserId(), offlineCoinVolume.getCoinId(), batchNo);// C2C资产流水
        long count = offlineCoinVolumeDao.updateCoinVolumeInfo(offlineCoinVolume);
        if (count != 1) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        offlineCoinVolumeLogService.saveLog(offlineCoinVolume.getUserId(), offlineCoinVolume.getCoinId(), batchNo);// C2C资产流水
    }

    private void checkCoinVolumeInfo(OfflineCoinVolume offlineCoinVolume) {
        greaterZero(offlineCoinVolume.getVolume(), "用户资产不足");
        greaterZero(offlineCoinVolume.getAdvertVolume(), "用户广告锁定错误");
        greaterZero(offlineCoinVolume.getLockVolume(), "用户交易锁定错误");
        greaterZero(offlineCoinVolume.getOtcAdvertVolume(), "用户OTC广告锁定错误");
        greaterZero(offlineCoinVolume.getOtcLockVolume(), "用户OTC交易锁定错误");
    }

    private void greaterZero(BigDecimal target, String errorMsg) {
        if (Objects.isNull(target) || target.compareTo(BigDecimal.ZERO) == -1) {
            LOGGER.error("参数错误{}", target);
            throw new PlatException(Constants.PARAM_ERROR, Optional.ofNullable(errorMsg).orElse("参数错误"));
        }
    }


    /**
     * 给用户的C2C资产的可用资产加资产
     *
     * @param userId
     * @param coinId
     * @param addVolume
     * @param batchNo
     */
    @Transactional
    public void coinVolumeAdd(String userId, String coinId, String symbol, BigDecimal addVolume, String batchNo) {
        if (addVolume.compareTo(BigDecimal.ZERO) < 1) {
            throw new PlatException(Constants.PARAM_ERROR, "需要转入的资产数量错误");
        }
        OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(userId, coinId);
        if (Objects.isNull(offlineCoinVolume)) {
            offlineCoinVolume = new OfflineCoinVolume();
            offlineCoinVolume.setId(SnowFlake.createSnowFlake().nextIdString());
            offlineCoinVolume.setCoinId(coinId);
            offlineCoinVolume.setCoinSymbol(symbol);
            offlineCoinVolume.setUserId(userId);
            offlineCoinVolume.setVolume(addVolume);
            offlineCoinVolume.setAdvertVolume(BigDecimal.ZERO);
            offlineCoinVolume.setLockVolume(BigDecimal.ZERO);
            offlineCoinVolume.setBailVolume(BigDecimal.ZERO);
            offlineCoinVolume.setCreateDate(LocalDateTime.now());
            offlineCoinVolume.setUpdateDate(LocalDateTime.now());
            offlineCoinVolume.setVersion(0L);
            offlineCoinVolumeDao.insert(offlineCoinVolume);
            offlineCoinVolumeLogService.saveLog(offlineCoinVolume.getUserId(), offlineCoinVolume.getCoinId(), batchNo);   //资产变更记录日志
        } else {
            offlineCoinVolumeLogService.saveLog(offlineCoinVolume, batchNo);// C2C资产流水
            offlineCoinVolume.setVolume(offlineCoinVolume.getVolume().add(addVolume));
            long count = offlineCoinVolumeDao.updateCoinVolumeInfo(offlineCoinVolume);
            if (count != 1) {
                throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
            }
            offlineCoinVolumeLogService.saveLog(userId, coinId, batchNo);// C2C资产流水
        }
    }

    /**
     * 给用户的C2C资产的可用资产减资产
     *
     * @param userId
     * @param coinId
     * @param subtractVolume
     * @param batchNo
     */
    @Transactional
    public void coinVolumeSubtract(String userId, String coinId, BigDecimal subtractVolume, String batchNo) {
        if (subtractVolume.compareTo(BigDecimal.ZERO) < 1) {
            throw new PlatException(Constants.PARAM_ERROR, "需要转出的资产数量错误");
        }
        OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(userId, coinId);
        if (Objects.isNull(offlineCoinVolume)) {
            throw new PlatException(Constants.PARAM_ERROR, "用户资产不存在");
        }
        BigDecimal volume = offlineCoinVolume.getVolume().subtract(subtractVolume);
        if (volume.compareTo(BigDecimal.ZERO) == -1) {
            throw new PlatException(Constants.PARAM_ERROR, "用户资产不足");
        }
        offlineCoinVolumeLogService.saveLog(offlineCoinVolume, batchNo);// C2C资产流水
        offlineCoinVolume.setVolume(volume);
        long count = offlineCoinVolumeDao.updateCoinVolumeInfo(offlineCoinVolume);
        if (count != 1) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        offlineCoinVolumeLogService.saveLog(userId, coinId, batchNo);// C2C资产流水
    }

    @Override
    @Transactional
    public void coinVolumeSubBailSub(String userId, String coinId, BigDecimal subVolume, BigDecimal buyerSubtractFee, String batchNo) {
        greaterZero(subVolume, null);
        greaterZero(buyerSubtractFee, null);
        OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(userId, coinId);
        if (Objects.isNull(offlineCoinVolume)) {
            throw new PlatException(Constants.PARAM_ERROR, "用户资产不存在！");
        } else {
            offlineCoinVolumeLogService.saveLog(offlineCoinVolume.getUserId(), offlineCoinVolume.getCoinId(), batchNo);    //资产变更记录日志
            if (offlineCoinVolume.getVolume().compareTo(subVolume) < 0) {
                throw new PlatException(Constants.PARAM_ERROR, "用户资产不足！");
            }

            offlineCoinVolume.setVolume(offlineCoinVolume.getVolume().subtract(subVolume));
            BigDecimal bailVolume = Optional.ofNullable(offlineCoinVolume.getBailVolume()).orElse(BigDecimal.ZERO).subtract(Optional.ofNullable(buyerSubtractFee).orElse(BigDecimal.ZERO));
            offlineCoinVolume.setBailVolume(bailVolume);

            if (bailVolume.compareTo(BigDecimal.ZERO) < 0) {
                throw new PlatException(Constants.PARAM_ERROR, "保证金余额不足！");
            }
            saveCoinVolumeInfo(offlineCoinVolume, batchNo);
        }
    }

    private void saveCoinVolumeBillDTO(String coinSymbol, String userId, BigDecimal volume, String mark, String refKey){
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
        billDTO.setSource("TRANSFER");
        userCoinVolumeBillService.insert(billDTO);
    }
}
