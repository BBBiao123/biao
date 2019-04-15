package com.biao.service.impl;

import com.biao.mapper.MkDistributeRuleDao;
import com.biao.reactive.data.mongo.service.TradeDetailService;
import com.biao.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.MapUtils;

import java.util.Map;

@Service
public class MkRuleTaskCoreServiceImpl implements MkRuleTaskCoreService {

    private static Logger logger = LoggerFactory.getLogger(MkRuleTaskCoreServiceImpl.class);

    @Autowired
    private MkDistributeBeanFactory mkDistributeBeanFactory;

    @Autowired
    private MkDistributeRuleDao mkDistributeRuleDao;

    @Autowired
    private TradeDetailService tradeDetailService;

    @Override
    public void triggerMiningDayTask() {

        logger.info("开始触发挖矿规则定时任务！");

        Map<String, Object> manageMap = mkDistributeRuleDao.findDistributeRuleManage("mk_distribute_mining_manage");
        if (MapUtils.isEmpty(manageMap)) {
            logger.info("挖矿规则管理未启动！");
            return;
        }

        try {
            String serviceName = String.valueOf(manageMap.get("type"));
            MkMiningRuleTaskService mkMiningRuleTaskService = (MkMiningRuleTaskService) mkDistributeBeanFactory.getService(serviceName);
            mkMiningRuleTaskService.miningDayTaskEntry();
        } catch (Exception e) {
            logger.info("挖矿规则执行任务出现异常，" + e.getMessage());
            return;
        }

        logger.info("结束触发挖矿规则定时任务！");
    }

    @Override
    public void triggerDividendDayTask() {

        logger.info("开始触发分红规则定时任务！");

        Map<String, Object> manageMap = mkDistributeRuleDao.findDistributeRuleManage("mk_distribute_dividend_manage");
        if (MapUtils.isEmpty(manageMap)) {
            logger.info("分红规则管理未启动！");
            return;
        }

        try {
            String serviceName = String.valueOf(manageMap.get("type"));
            MkDividendRuleTaskService mkDividendRuleTaskService = (MkDividendRuleTaskService) mkDistributeBeanFactory.getService(serviceName);
            mkDividendRuleTaskService.dividendDayTaskEntry();
        } catch (Exception e) {
            logger.info("分红规则执行任务出现异常，" + e.getMessage());
            return;
        }

        logger.info("结束触分红规则定时任务！");
    }

    @Override
    public void triggerPromoteDayTask() {

        Map<String, Object> manageMap = mkDistributeRuleDao.findDistributeRuleManage("mk_distribute_promote_manage");
        if (MapUtils.isEmpty(manageMap)) {
            logger.info("会员推广管理未启动！");
            return;
        }

        try {
            String serviceName = String.valueOf(manageMap.get("type"));
            MkPromoteRuleTaskService mkPromoteRuleTaskService = (MkPromoteRuleTaskService) mkDistributeBeanFactory.getService(serviceName);
            mkPromoteRuleTaskService.promoteDayTaskEntry();
        } catch (Exception e) {
            logger.info("会员推广执行任务出现异常，" + e.getMessage());
            return;
        }

    }

}
