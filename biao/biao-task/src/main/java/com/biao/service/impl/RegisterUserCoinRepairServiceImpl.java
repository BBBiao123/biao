package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.mk2.Mk2PopularizeRegisterConf;
import com.biao.entity.mk2.Mk2PopularizeTaskLog;
import com.biao.exception.PlatException;
import com.biao.mapper.mk2.Mk2PopularizeRegisterCoinBakDao;
import com.biao.mapper.mk2.Mk2PopularizeRegisterCoinDao;
import com.biao.mapper.mk2.Mk2PopularizeRegisterConfDao;
import com.biao.mapper.mk2.Mk2PopularizeTaskLogDao;
import com.biao.service.RegisterUserCoinRepairService;
import com.biao.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

//import com.biao.service.UserCoinVolumeService;

@Service
public class RegisterUserCoinRepairServiceImpl implements RegisterUserCoinRepairService {

    private Logger logger = LoggerFactory.getLogger(RegisterUserCoinRepairServiceImpl.class);

    @Autowired
    private Mk2PopularizeRegisterCoinDao mk2PopularizeRegisterCoinDao;

    @Autowired
    private Mk2PopularizeRegisterCoinBakDao mk2PopularizeRegisterCoinBakDao;

    @Autowired
    private Mk2PopularizeTaskLogDao mk2PopularizeTaskLogDao;

//    @Autowired
//    private UserCoinVolumeService userCoinVolumeService;

    @Autowired
    private Mk2PopularizeRegisterConfDao mk2PopularizeRegisterConfDao;

    @Autowired
    private RegisterUserCoinRepairService registerUserCoinRepairService;


//    @Override
//    @Transactional
//    public void doRegisterRepair() {
//        try {
//            repairRegisters();
//            repairRefers();
//            checkSuccess();
//            registerUserCoinRepairService.taskLog("0", "OK",  "2018-08-16", 0L, null, LocalDateTime.now());
//        } catch (PlatException e) {
//            logger.error("本次修复送币失败", e);
//            String msg = e.getMsg().length() > 200 ? e.getMsg().substring(0, 200) : e.getMsg();
//            registerUserCoinRepairService.taskLog("0", msg,  "2018-08-16", 0L, null, LocalDateTime.now());
//            throw new PlatException(Constants.PARAM_ERROR, "本次修复送币失败!");
//        } catch (Exception e) {
//            logger.error("本次修复送币失败", e);
//            String msg = e.toString();
//            msg = msg.length() > 200 ? msg.substring(0, 200) : msg;
//            registerUserCoinRepairService.taskLog("0", msg,  "2018-08-16", 0L, null, LocalDateTime.now());
//            throw new PlatException(Constants.PARAM_ERROR, "本次修复送币失败!");
//        }
//
//    }
//
//    @Transactional
//    public void repairRegisters() {
//        List<String> userIds = mk2PopularizeRegisterCoinDao.findRepeatRegisterGives();
//        List<Mk2PopularizeRegisterCoin> registerCoins = null;
//        for (String userId : userIds) {// 第一条不用删，后面的全部删除
//            registerCoins = mk2PopularizeRegisterCoinDao.findRepeatRegisters(userId);
//            deleteRegisterCoins(userId, registerCoins);
//        }
//    }
//
//    private void deleteRegisterCoins(String userId, List<Mk2PopularizeRegisterCoin> registerCoins) {
//        List<Mk2PopularizeRegisterCoinBak> baks = new ArrayList<Mk2PopularizeRegisterCoinBak>();
//        long totalVol = 0L;
//        Mk2PopularizeRegisterCoin coin = null;
//        Mk2PopularizeRegisterCoinBak coinBak = null;
//        for (int index = 1; index < registerCoins.size(); index ++) {
//            coin = registerCoins.get(index);
//            totalVol = totalVol + coin.getVolume();
//            coinBak = new Mk2PopularizeRegisterCoinBak();
//            BeanUtils.copyProperties(coin, coinBak);
//            baks.add(coinBak);
//            mk2PopularizeRegisterCoinDao.deleteRegisterCoinById(coin.getId());
//        }
//        mk2PopularizeRegisterCoinBakDao.insertBatch(baks);
//        beforeSubtractCoin(userId, registerCoins.get(0).getCoinSymbol(), new BigDecimal(totalVol));
//        afterSubtractCoin(userId, registerCoins.get(0).getCoinSymbol(), new BigDecimal(totalVol));
//    }
//
//    @Transactional
//    public void repairRefers() {
//        List<String> forUserIds = mk2PopularizeRegisterCoinDao.findRepeatReferGives();
//        List<Mk2PopularizeRegisterCoin> registerCoins = null;
//        List<String> distinctUserId = null;
//        for (String forUserId : forUserIds) {// 第一条不用删，后面的全部删除
//            registerCoins = mk2PopularizeRegisterCoinDao.findRepeatRefers(forUserId);
//            distinctUserId = mk2PopularizeRegisterCoinDao.repeatUserIdByForUserId(forUserId);
//            if (distinctUserId.size() > 1) {
//                throw new PlatException(Constants.OPERRATION_ERROR, "推荐人异常![" + forUserId + "]");
//            }
//            deleteRegisterCoins(registerCoins.get(0).getUserId(), registerCoins);
//        }
//    }
//
//    @Transactional
//    public void checkSuccess() {
//        List<Mk2PopularizeRegisterConf> confList = mk2PopularizeRegisterConfDao.findConf();
//        Mk2PopularizeRegisterConf conf = confList.get(0);
//        long giveTotal = mk2PopularizeRegisterCoinDao.countGiveCoinTotal();
//        if (giveTotal != conf.getGiveVolume()) {
//            throw new PlatException(Constants.OPERRATION_ERROR, "本次修复未成功");
//        }
//
//    }

    public void beforeSubtractCoin(String userId, String coinSymbol, BigDecimal volume) {
        // 锁分红账户资产
//        long count = userCoinVolumeService.addLockVolume(userId, coinSymbol, volume, true);
//        if (count != 1) {
//            throw new PlatException(Constants.OPERRATION_ERROR, "addLock用户资产异常![" + userId + "]");
//        }
    }

    public void afterSubtractCoin(String userId, String coinSymbol, BigDecimal volume) {
//        long count = userCoinVolumeService.subtractLockVolume(userId, coinSymbol, volume, false);
//        if (count != 1) {
//            throw new PlatException(Constants.OPERRATION_ERROR, "subtractLock用户资产异常![" + userId + "]");
//        }
    }

    /**
     * 记录本次执行任务执行结果
     *
     * @param status
     * @param reason
     * @param curDate
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void taskLog(String status, String reason, String curDate, Long dayGiveColume, Mk2PopularizeRegisterConf conf, LocalDateTime curDateTime) {
        Mk2PopularizeTaskLog taskLog = new Mk2PopularizeTaskLog();
        taskLog.setId(SnowFlake.createSnowFlake().nextIdString());
        taskLog.setType("9");
        taskLog.setTypeName(conf != null ? conf.getName() : "");
        taskLog.setDayGiveColume(dayGiveColume);
        taskLog.setParamTaskDate(curDate);
        taskLog.setStatus("9");
        taskLog.setReason(reason);
        taskLog.setExecuteTime(curDateTime);
        mk2PopularizeTaskLogDao.insert(taskLog);
    }


    @Transactional
    public void doTimeRepair() {
        try {
            long totalVol = 0L;
            List<String> userIds = mk2PopularizeRegisterCoinBakDao.distinctUsers();
            for (String userId : userIds) {
                long volume = mk2PopularizeRegisterCoinBakDao.sumUserVol(userId);
                mk2PopularizeRegisterCoinBakDao.updateSuccessByUserId(userId);
                beforeSubtractCoin(userId, "UES", new BigDecimal(volume));
                afterSubtractCoin(userId, "UES", new BigDecimal(volume));
                totalVol = totalVol + volume;
            }
            mk2PopularizeRegisterCoinBakDao.deleteRepairCoin();
            registerUserCoinRepairService.taskLog("0", "OK", "2018-08-16", totalVol, null, LocalDateTime.now());
        } catch (PlatException e) {
            logger.error("本次修复送币失败", e);
            String msg = e.getMsg().length() > 200 ? e.getMsg().substring(0, 200) : e.getMsg();
            registerUserCoinRepairService.taskLog("0", msg, "2018-08-16", 0L, null, LocalDateTime.now());
            throw new PlatException(Constants.PARAM_ERROR, "本次修复送币失败!");
        } catch (Exception e) {
            logger.error("本次修复送币失败", e);
            String msg = e.toString();
            msg = msg.length() > 200 ? msg.substring(0, 200) : msg;
            registerUserCoinRepairService.taskLog("0", msg, "2018-08-16", 0L, null, LocalDateTime.now());
            throw new PlatException(Constants.PARAM_ERROR, "本次修复送币失败!");
        }

    }

}
