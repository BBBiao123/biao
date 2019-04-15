package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.mk2.Mk2PopularizeBonusAccount;
import com.biao.entity.mkcommon.MkCommonPlatIncomeLog;
import com.biao.entity.mkcommon.MkCommonPlatIncomeTaskLog;
import com.biao.enums.OrderEnum;
import com.biao.exception.PlatException;
import com.biao.mapper.PlatUserDao;
import com.biao.mapper.mk2.Mk2PopularizeBonusAccountDao;
import com.biao.mapper.mkcommon.MkCommonPlatIncomeLogDao;
import com.biao.mapper.mkcommon.MkCommonPlatIncomeTaskLogDao;
import com.biao.reactive.data.mongo.service.TradeDetailService;
import com.biao.service.MkCommonService;
import com.biao.service.UserCoinVolumeExService;
import com.biao.util.DateUtils;
import com.biao.util.SnowFlake;
import com.biao.vo.UserTradeVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

@Service
public class MkCommonServiceImpl implements MkCommonService {

    private Logger logger = LoggerFactory.getLogger(MkCommonServiceImpl.class);

    @Autowired
    private TradeDetailService tradeDetailService;

    @Autowired
    private UserCoinVolumeExService userCoinVolumeService;

    @Autowired
    private PlatUserDao platUserDao;

    @Autowired
    private MkCommonPlatIncomeTaskLogDao mkCommonPlatIncomeTaskLogDao;

    @Autowired
    private MkCommonPlatIncomeLogDao mkCommonPlatIncomeLogDao;

    @Autowired
    private Mk2PopularizeBonusAccountDao mk2PopularizeBonusAccountDao;

    @Autowired
    private MkCommonService mkCommonService;

    @Override
    public void statisticsCoin2User(LocalDateTime beginDate, LocalDateTime endDate, String userId, BiConsumer<String, BigDecimal> action) {
        String beginDateStr = DateUtils.formaterLocalDateTime(beginDate);
        String endDateStr = DateUtils.formaterLocalDateTime(endDate);
        List<UserTradeVO> tradeVOList = tradeDetailService.findByDate(beginDateStr, endDateStr);
        Map<String, BigDecimal> coinFeeMap = new HashMap<>();
        tradeVOList.forEach(userTradeVO -> {
            String curCoinSymbol = "";
            if (userTradeVO.getExType() == 0) { //买
                curCoinSymbol = userTradeVO.getCoinOther();
            } else if (userTradeVO.getExType() == 1) { //卖
                curCoinSymbol = userTradeVO.getCoinMain();
            }

            if (userTradeVO.getExFee().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal feeVol = Optional.of(coinFeeMap.get(curCoinSymbol)).orElse(BigDecimal.ZERO).add(userTradeVO.getExFee());
                coinFeeMap.put(curCoinSymbol, feeVol);
            }
        });

        coinFeeMap.forEach((coinSymbol, feeVol) -> {
            if (feeVol.compareTo(BigDecimal.ZERO) == 1) {
                //把分红打款给用户
//                long count = userCoinVolumeService.updateIncome(OrderEnum.OrderStatus.ALL_SUCCESS, feeVol, userId, coinSymbol);
                long count = 0L;
                if (count <= 0) {
                    throw new PlatException(Constants.DISTRIBUTE_TASK_ERROR, String.format("[%s-%s]统计平台收入存入分红账户执行失败，更新用户资产", beginDateStr, endDateStr));
                }
            }
            action.accept(coinSymbol, feeVol);
        });
    }

    @Transactional
    public void mk2StatisticsCoin2User() {
        LocalDateTime curDateTime = LocalDateTime.now();
        LocalDateTime lastDateBeginTime = LocalDateTime.of(curDateTime.minusDays(1).toLocalDate(), LocalTime.MIN);// 昨天0点0分0秒
        LocalDateTime lastDateEndTime = LocalDateTime.of(curDateTime.minusDays(1).toLocalDate(), LocalTime.MAX);// 昨天23点59分59秒

        MkCommonPlatIncomeTaskLog taskLog = mkCommonPlatIncomeTaskLogDao.findLately();
        if (taskLog != null) {
            lastDateBeginTime = taskLog.getEndDate();
        }
        if (lastDateBeginTime.isAfter(lastDateEndTime) || lastDateBeginTime.isEqual(lastDateEndTime)) {
            mkCommonService.saveTaskResult(lastDateBeginTime, lastDateEndTime, "0", "该时间段有误或已经执行过平台手续费归集！");
            return;
        }
        Mk2PopularizeBonusAccount platBonusAccount = mk2PopularizeBonusAccountDao.findByTpye("1");
        try {
            doMk2StatisticsCoin2User(platBonusAccount, lastDateBeginTime, lastDateEndTime);
            mkCommonService.saveTaskResult(lastDateBeginTime, lastDateEndTime, "1", "每天平台收入存入账户TASK成功！");
        } catch (PlatException e) {
            logger.error("每天平台收入存入账户TASK失败", e);
            String msg = e.getMsg().length() > 200 ? e.getMsg().substring(0, 200) : e.getMsg();
            mkCommonService.saveTaskResult(lastDateBeginTime, lastDateEndTime, "0", msg);
            throw new PlatException(Constants.PARAM_ERROR, "每天平台收入存入账户TASK失败");
        } catch (Exception e) {
            logger.error("每天平台收入存入账户TASK失败", e);
            String msg = e.toString();
            msg = msg.length() > 200 ? msg.substring(0, 200) : msg;
            mkCommonService.saveTaskResult(lastDateBeginTime, lastDateEndTime, "0", msg);
            throw new PlatException(Constants.PARAM_ERROR, "每天平台收入存入账户TASK失败");
        }

    }

    private void doMk2StatisticsCoin2User(Mk2PopularizeBonusAccount platBonusAccount, LocalDateTime lastDateBeginTime, LocalDateTime lastDateEndTime) {
        statisticsCoin2User(lastDateBeginTime, lastDateEndTime, platBonusAccount.getUserId(), (coinSymbol, feeVol) -> {
            MkCommonPlatIncomeLog log = new MkCommonPlatIncomeLog();
            log.setId(SnowFlake.createSnowFlake().nextIdString());
            log.setUserId(platBonusAccount.getUserId());
            log.setMail(platBonusAccount.getMail());
            log.setMobile(platBonusAccount.getMobile());
            log.setIdCard(platBonusAccount.getIdCard());
            log.setRealName(platBonusAccount.getRealName());
            //log.setCoinId(platBonusAccount.);
            log.setCoinSymbol(coinSymbol);
            log.setVolume(feeVol);
            log.setBeginDate(lastDateBeginTime);
            log.setEndDate(lastDateEndTime);
            log.setStatus("1");
            log.setRemark("执行成功");
            mkCommonPlatIncomeLogDao.insert(log);
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveTaskResult(LocalDateTime beginDate, LocalDateTime endDate, String status, String remark) {
        MkCommonPlatIncomeTaskLog taskLog = new MkCommonPlatIncomeTaskLog();
        taskLog.setId(SnowFlake.createSnowFlake().nextIdString());
        taskLog.setBeginDate(beginDate);
        taskLog.setEndDate(endDate);
        taskLog.setStatus(status);
        taskLog.setRemark(remark);
        mkCommonPlatIncomeTaskLogDao.insert(taskLog);
    }
}
