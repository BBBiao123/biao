package com.biao.execute;

import com.biao.coin.CoinMainService;
import com.biao.config.AliYunCardCheckConfig;
import com.biao.config.BalanceDayRateConfig;
import com.biao.entity.balance.BalancePlatCoinPriceVolume;
import com.biao.enums.CardStatusEnum;
import com.biao.enums.KlineTimeEnum;
import com.biao.enums.TradePairEnum;
import com.biao.enums.UserCardStatusEnum;
import com.biao.pojo.CardStatuScanCheckDTO;
import com.biao.redis.RedisCacheManager;
import com.biao.service.*;
import com.biao.service.balance.BalanceUserCoinVolumeDetailService;
import com.biao.service.impl.MkDividendRuleTaskServiceImpl;
import com.biao.service.impl.MkPromoteRuleTaskServiceImpl;
import com.biao.service.impl.UserCoinVolumeBillTaskServiceImpl;
import com.biao.service.kline.KlineMinDateTransfer;
import com.biao.service.register.UserRegisterLotteryService;
import com.biao.util.DateUtils;
import com.biao.vo.KlineVO;
import com.biao.vo.OfflineCoinVolumeDayVO;
import com.biao.vo.TradePairVO;
import com.biao.vo.redis.RedisExPairVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ScheduledTasks {

    private static Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private StatisticsTradeTaskService statisticsTradeTaskService;

    @Autowired
    private BalanceUserCoinVolumeDetailService balanceUserCoinVolumeDetailService;

    @Autowired
    private MkRuleTaskCoreService mkRuleTaskCoreService;

    @Autowired
    private MkDividendRuleTaskServiceImpl mkDividendRuleTaskService;

    @Autowired
    private MkPromoteRuleTaskServiceImpl mkPromoteRuleTaskService;

    @Autowired
    private OfflineOrderDetailCancelTaskService offlineOrderDetailCancelTaskService;

    @Autowired
    private TradeRiskControlService tradeRiskControlService;

    @Autowired
    private MkRelayPrizeTaskService mkRelayPrizeTaskService;

    @Autowired
    private PlatUserCoinCountService platUserCoinCountService;

    @Autowired
    private OfflineCoinTaskService offlineCoinTaskService;

    @Autowired
    private MkMinerRecruitTaskService mkMinerRecruitTaskService;

    @Autowired
    private OfflineCoinVolumeDayService offlineCoinVolumeDayService;
    @Autowired
    private KlineLogDataService klineLogDataService;

    @Autowired
    private KlineDataService klineDataService;

    @Autowired
    private UserRegisterLotteryService userRegisterLotteryService;

    @Autowired
    private Mk2MiningTeamSortTaskService mk2MiningTeamSortTaskService;

    @Autowired
    private MkNeo4jUserService mkNeo4jUserService;

    @Autowired
    private Mk2MiningTeamService mk2MiningTeamService;

    @Value("${maincoinC2c:USDT,UES}")
    private String maincoinC2c;

    @Autowired
    private PlatUserService platUserService;

    @Autowired
    private AliYunCardCheckConfig aliYunCardCheckConfig;

    @Autowired
    private BalanceDayRateConfig balanceDayRateConfig;

    @Autowired
    private TraderVolumeSnapshotTaskService traderVolumeSnapshotTaskService;

    @Autowired
    private KlineMinDateTransfer klineMinDateTransfer;

    @Autowired
    private SuperCoinVolumeTaskService superCoinVolumeTaskService;

    @Autowired
    private UserCoinVolumeBillTaskServiceImpl userCoinVolumeBillTaskService;

    @Autowired
    private OfflineRedEnvelopeService offlineRedEnvelopeService;

    @Autowired
    private Mk2MemberReleaseService mk2MemberReleaseService;

    @Autowired
    private RegisterUserCoinService registerUserCoinService;

    private final RedisCacheManager redisCacheManager;

    private final RedisTemplate redisTemplate;

    @Autowired
    private CoinMainService coinMainService;

    @Autowired
    public ScheduledTasks(
            final RedisTemplate redisTemplate,
            final RedisCacheManager redisCacheManager
    ) {

        this.redisTemplate = redisTemplate;
        this.redisCacheManager = redisCacheManager;
    }



    // @Scheduled(cron = "0 0/1 * * * ?")
    public void createKlineDataForOneMinute() {
        if (logger.isDebugEnabled()) {
            logger.debug("========== createKlineDataForOneMinute  start ==========");
        }
        klineLogDataService.executePullKlineLogData(KlineTimeEnum.ONE_MINUTE,false,true);
        if (logger.isDebugEnabled()) {
            logger.debug("========== createKlineDataForOneMinute  end   ==========");
        }
    }
    @Autowired
    private BalanceSheetSnapshotTaskService balanceSheetSnapshotTaskService;

    @Autowired
    private CoinVolumeRiskMgtTaskService coinVolumeRiskMgtTaskService;


/*    @Scheduled(cron = "0 30 3 27 12 ?")
    public void synKlineTaskExe() {
        if (logger.isDebugEnabled()) {
            logger.info("exexute synKlineTaskExe  start ....");
        }
        klineLogDataService.executePullKlineLogData(KlineTimeEnum.ONE_HOUR, true);
        klineLogDataService.executePullKlineLogData(KlineTimeEnum.TWO_HOUR, true);
        klineLogDataService.executePullKlineLogData(KlineTimeEnum.FOUR_HOUR, true);
        klineLogDataService.executePullKlineLogData(KlineTimeEnum.SIX_HOUR, true);
        klineLogDataService.executePullKlineLogData(KlineTimeEnum.EIGHT_HOUR, true);
        klineLogDataService.executePullKlineLogData(KlineTimeEnum.TWELVE_HOUR, true);
        klineLogDataService.executePullKlineLogData(KlineTimeEnum.ONE_DAY, true);
        klineLogDataService.executePullKlineLogData(KlineTimeEnum.ONE_WEEK, true);
        klineLogDataService.executePullKlineLogData(KlineTimeEnum.ONE_MONTH, true);
        if (logger.isDebugEnabled()) {
            logger.info("exexute synKlineTaskExe  start ....");
        }
    }*/

    /**
     * 1分钟 k线增量数据定时任务
     */
    //@Scheduled(cron = "0 0/1 * * * ?")
    // public void incrementStatisticsTradeTaskOneMinute() {
    //    if (logger.isDebugEnabled()) {
    //        logger.debug("exexute incrementStatisticsTradeTaskOneMinute  start ....");
    //    }
    //     klineLogDataService.executePullKlineLogData(KlineTimeEnum.ONE_MINUTE);
    //   if (logger.isDebugEnabled()) {
    //        logger.debug("exexute incrementStatisticsTradeTaskOneMinute  end   ....");
    //    }
    //}

    /**
     * 5分钟 k线增量数据定时任务
     */
    //@Scheduled(cron = "5 0/5 * * * ?")
    //public void incrementStatisticsTradeTaskFiveMinute() {
    //   if (logger.isDebugEnabled()) {
    //       logger.debug("exexute incrementStatisticsTradeTaskFiveMinute  start ....");
    //   }
    //    klineLogDataService.executePullKlineLogData(KlineTimeEnum.FIVE_MINUTE);
    //   if (logger.isDebugEnabled()) {
    //       logger.debug("exexute incrementStatisticsTradeTaskFiveMinute  end   ....");
    //    }
    //}

    /**
     * 15分钟 k线增量数据定时任务
     */
    //@Scheduled(cron = "10 0/15 * * * ?")
    //public void incrementStatisticsTradeTaskFifteenMinute() {
    //    if (logger.isDebugEnabled()) {
    //       logger.debug("exexute incrementStatisticsTradeTaskFifteenMinute  start ....");
    //   }
    //    klineLogDataService.executePullKlineLogData(KlineTimeEnum.FIFTEEN_MINUTE);
    //    if (logger.isDebugEnabled()) {
    //       logger.debug("exexute incrementStatisticsTradeTaskFifteenMinute  end   ....");
    //   }
    //}

    /**
     * 30分钟 k线增量数据定时任务
     */
    //@Scheduled(cron = "15 0/30 * * * ?")
    //public void incrementStatisticsTradeTaskThirtyMinute() {
    //    if (logger.isDebugEnabled()) {
    //        logger.info("exexute incrementStatisticsTradeTaskThirtyMinute  start ....");
    //   }
    //   klineLogDataService.executePullKlineLogData(KlineTimeEnum.THIRTY_MINUTE);
    //   if (logger.isDebugEnabled()) {
    //        logger.info("exexute incrementStatisticsTradeTaskThirtyMinute  start ....");
    //    }
    //}

    /**
     * 1小时 k线增量数据定时任务
     */
    //@Scheduled(cron = "20 0 0/1 * * ?")
    //public void incrementStatisticsTradeTaskOneHour() {
    //    if (logger.isDebugEnabled()) {
    //        logger.info("exexute incrementStatisticsTradeTaskOneHour  start ....");
    //    }
    //    klineLogDataService.executePullKlineLogData(KlineTimeEnum.ONE_HOUR);
    //   if (logger.isDebugEnabled()) {
    //        logger.info("exexute incrementStatisticsTradeTaskOneHour  start ....");
    //    }
    //}

    /**
     * 2小时 k线增量数据定时任务
     */
    //@Scheduled(cron = "30 0 0/2 * * ?")
    //public void incrementStatisticsTradeTaskTwoHour() {
    //   if (logger.isDebugEnabled()) {
    //       logger.info("exexute incrementStatisticsTradeTaskTwoHour  start ....");
    //   }
    //   klineLogDataService.executePullKlineLogData(KlineTimeEnum.TWO_HOUR);
    //    if (logger.isDebugEnabled()) {
    //       logger.info("exexute incrementStatisticsTradeTaskTwoHour  start ....");
    //   }
    //}

    /**
     * 4小时 k线增量数据定时任务
     */
    //@Scheduled(cron = "40 0 0/4 * * ?")
    //public void incrementStatisticsTradeTaskFourHour() {
    //    if (logger.isDebugEnabled()) {
    //        logger.info("exexute incrementStatisticsTradeTaskFourHour  start ....");
    //   }
    //   klineLogDataService.executePullKlineLogData(KlineTimeEnum.FOUR_HOUR);
    //   if (logger.isDebugEnabled()) {
    //       logger.info("exexute incrementStatisticsTradeTaskFourHour  start ....");
    //   }
    //}

    /**
     * 6小时 k线增量数据定时任务
     */
    //@Scheduled(cron = "50 0 0/6 * * ?")
    //public void incrementStatisticsTradeTaskEightHour() {
    //    if (logger.isDebugEnabled()) {
    //       logger.info("exexute incrementStatisticsTradeTaskEightHour  start ....");
    //   }
    //   klineLogDataService.executePullKlineLogData(KlineTimeEnum.SIX_HOUR);
    //    if (logger.isDebugEnabled()) {
    //       logger.info("exexute incrementStatisticsTradeTaskEightHour  start ....");
    //   }
    //}

    /**
     * 日线 k线增量数据定时任务
     */
    //@Scheduled(cron = "0 0 0 * * ?")
    //public void incrementStatisticsTradeTaskOneDay() {
    //   if (logger.isDebugEnabled()) {
    //       logger.info("exexute incrementStatisticsTradeTaskOneDay  start ....");
    //   }
    //    klineLogDataService.executePullKlineLogData(KlineTimeEnum.ONE_DAY);
    //   if (logger.isDebugEnabled()) {
    //        logger.info("exexute incrementStatisticsTradeTaskOneDay  start ....");
    //    }
    //}

    /**
     * 周线 k线增量数据定时任务
     */
    //@Scheduled(cron = "0 0 0 ? * MON")
    //public void incrementStatisticsTradeTaskOneWeek() {
    //    if (logger.isDebugEnabled()) {
    //        logger.info("exexute incrementStatisticsTradeTaskOneWeek  start ....");
    //   }
    //   klineLogDataService.executePullKlineLogData(KlineTimeEnum.ONE_WEEK);
    //   if (logger.isDebugEnabled()) {
    //        logger.info("exexute incrementStatisticsTradeTaskOneWeek  start ....");
    //    }
    //}

    /**
     * 月线 k线增量数据定时任务
     */
    //@Scheduled(cron = "0 0 0 1 * ?")
    //public void incrementStatisticsTradeTaskOneMonth() {
    //    if (logger.isDebugEnabled()) {
    //        logger.info("exexute incrementStatisticsTradeTaskOneMonth  start ....");
    //   }
    //    klineLogDataService.executePullKlineLogData(KlineTimeEnum.ONE_MONTH);
    //    if (logger.isDebugEnabled()) {
    //       logger.info("exexute incrementStatisticsTradeTaskOneMonth  start ....");
    //   }
    //}

    /**
     * k线增量数据定时任务
     * fixedRate,表示任务开始执行时间间隔，单位毫米
     */
    //@Scheduled(cron="59 59 23 * * ?")
    //@Scheduled(fixedRate = 20000)
    public void everyMinForDayTradeTaskUSDT() {
        logger.info("exexute everyMinForDayTradeTaskExe  start ....");
        statisticsTradeTaskService.everyMinForDayTrade(TradePairEnum.USDT.getKey());
        logger.info("exexute everyMinForDayTradeTaskExe  end   ....");
    }


    /**
     * fixedRate,表示任务开始执行时间间隔，单位毫米
     */
    //@Scheduled(cron="59 59 23 * * ?")
    //@Scheduled(fixedRate = 20000)
    public void everyMinForDayTradeTaskETH() {
        logger.info("exexute everyMinForDayTradeTaskExe  start ....");
        statisticsTradeTaskService.everyMinForDayTrade(TradePairEnum.ETH.getKey());
        logger.info("exexute everyMinForDayTradeTaskExe  end   ....");
    }

    /**
     * fixedRate,表示任务开始执行时间间隔，单位毫米
     */
    //@Scheduled(cron="59 59 23 * * ?")
    //@Scheduled(fixedRate = 20000)
    public void everyMinForDayTradeTaskBTC() {
        logger.info("exexute everyMinForDayTradeTaskExe  start ....");
        statisticsTradeTaskService.everyMinForDayTrade(TradePairEnum.BTC.getKey());
        logger.info("exexute everyMinForDayTradeTaskExe  end   ....");
    }



//	/**
//	 * 每天注册审核通过送币（送糖果，每小时执行一次）
//	 */
//	@Scheduled(cron="0 0 0/1 * * ?")
	public void regiterCoinGive() {
		logger.info("exexute regiterCoinGive  start ....");
		registerUserCoinService.registerGiveCoin();
		logger.info("exexute regiterCoinGive  end   ....");
	}

    /**
     * 生成用户资产快照
     */
    @Scheduled(cron = "0 55 23 * * ?")
    public void triggerUserCoinVolumeSnapshot() {
        logger.info("exexute triggerUserCoinVolumeSnapshot  start ....");
        mkDividendRuleTaskService.triggerUserCoinVolumeSnapshot();
        logger.info("exexute triggerUserCoinVolumeSnapshot  end   ....");
    }

    /**
     * 初始化用户层级关系
     */
    //@Scheduled(cron = "0 40 20 4 9 ?")
    //@Scheduled(fixedRate = 20000)
    public void triggerInitPlatUserRelation() {
        logger.info("exexute triggerInitPlatUserRelation  start ....");
        mkPromoteRuleTaskService.initPlatUserRelation();
        logger.info("exexute triggerInitPlatUserRelation  end   ....");
    }

    /**
     * 手续费打款至指定账户(每小时执行一次)
     */
    // @Scheduled(cron = "0 0 0/1 * * ?")
    public void triggerRemitFeeToPlatAccount() {
        logger.info("exexute triggerRemitFeeToPlatAccount  start ....");
        mkDividendRuleTaskService.triggerRemitFeeToPlatAccount();
        logger.info("exexute triggerRemitFeeToPlatAccount  end   ....");
    }

    /**
     * 挖矿定时任务
     */
    // @Scheduled(cron = "0 0 2 * * ?")
    public void triggerMiningTask() {
        logger.info("exexute triggerMiningDayTask  start ....");
        mkRuleTaskCoreService.triggerMiningDayTask();
        logger.info("exexute triggerMiningDayTask  end   ....");
    }

    /**
     * 挖矿团队排名争霸
     */
    // @Scheduled(cron = "0 0 4 * * ?")
    public void triggerTeamMinningSortTask() {
        logger.info("exexute triggerTeamMinningSortTask  start ....");
        mk2MiningTeamSortTaskService.doSortTeamMinging();
        logger.info("exexute triggerTeamMinningSortTask  end ....");
    }

    /**
     * 会员币释放
     */
    // @Scheduled(cron = "0 0 4 * * ?")
    public void triggerReleaseTask() {
        logger.info("exexute triggerReleaseTask  start ....");
        mk2MemberReleaseService.releaseLockVolume();
        logger.info("exexute triggerReleaseTask  end ....");
    }

//    /**
//     * 团队挖矿定时任务，测试Neo4j团队挖矿
//     */
//    @Scheduled(cron = "0 0 4 * * ?")
//    public void triggerTeamMiningTask() {
//        logger.info("exexute triggerTeamMiningTask  start ....");
//        mk2MiningTeamService.doTeamCoinMining();
//        logger.info("exexute triggerTeamMiningTask  end   ....");
//    }

    /**
     * Neo4j用户手动检查补偿
     */
    //@Scheduled(cron = "0 0 1 * * ?")
    public void repairMissNeo4jEveryDayTask() {
        logger.info("exexute repairMissNeo4jEveryDayTask  start ....");
        mkNeo4jUserService.repairMissNeo4jEveryDay();
        logger.info("exexute repairMissNeo4jEveryDayTask  end   ....");
    }

//    /**
//     * 一次性补Neo4j节点用户,只需要执行一次
//     */
//    @Scheduled(cron = "0 40 23 18 12 *")
//    public void triggerRepairOnceTask() {
//        logger.info("exexute triggerRepairOnceTask  start ....");
//        mkNeo4jUserService.repairMissNeo4jOnce();
//        logger.info("exexute triggerRepairOnceTask  end ....");
//    }

//    /**
//     * 初始化Neo4j节点用户,只需要执行一次
//     */
//    @Scheduled(cron = "0 40 23 22 11 *")
//    public void triggerInitNeo4jUserTask() {
//        logger.info("exexute triggerInitNeo4jUserTask  start ....");
//        mkNeo4jUserService.initNeo4jUser();
//        logger.info("exexute triggerInitNeo4jUserTask  end   ....");
//    }


    /**
     * 分红定时任务
     */
    //@Scheduled(cron = "0 30 10 * * ?")
    public void triggerDividendTask() {
        logger.info("exexute triggerDividendDayTask  start ....");
        mkRuleTaskCoreService.triggerDividendDayTask();
        logger.info("exexute triggerDividendDayTask  end   ....");
    }

    /**
     * 会员推广定时任务(每小时执行一次)
     */
     @Scheduled(cron = "0 0 0/1 * * ?")
    @Scheduled(fixedRate = 20000)
    public void triggerPromoteTask() {
        logger.info("exexute triggerPromoteDayTask  start ....");
        mkRuleTaskCoreService.triggerPromoteDayTask();
        logger.info("exexute triggerPromoteDayTask  end   ....");
    }

    /**
     * 用户币种交易手续费   占时不统计
     */
    //@Scheduled(cron="0 30 0 * * ?")
    public void userCoinTradeFreesTask() {
        logger.info("exexute userCoinTradeFreesTask  start ....");
        statisticsTradeTaskService.statisticsUserTradeFreesTask();
        logger.info("exexute userCoinTradeFreesTask  end   ....");
    }

    /**
     * 补全用户电话所属地区
     * 改任务只需执行一次
     */
    //@Scheduled(cron = "0 30 21 04 09 *")
    public void userGeoOneTask() {
        logger.info("exexute userGeoOneTask  start ....");
        statisticsTradeTaskService.userGeoOneTask();
        logger.info("exexute userGeoOneTask  end   ....");
    }

    /**
     * 迁移用户身份证
     * 改任务只需执行一次
     */
    //@Scheduled(cron="30 20 19 23 08 *")
    //@Scheduled(fixedRateString = "30000")
    public void userPhoteTransferTask() {
        logger.info("exexute userPhoteTransferTask  start ....");
        statisticsTradeTaskService.userPhoteTransferTask();
        logger.info("exexute userPhoteTransferTask  end   ....");
    }

    /**
     * 用户同步任务
     */

    //@Scheduled(cron="0 30 23 18 08 *")
    //@Scheduled(fixedRateString = "30000")
	/*public void userSynTask() {
		logger.info("exexute userGeoOneTask  start ....");
		statisticsTradeTaskService.userSynTask();
		logger.info("exexute userGeoOneTask  end   ....");
	}*/

//    /**
//     * 注册送币修复
//     */
//    @Scheduled(cron="0 45 20 16 8 *")
//    public void userCoinGiveRepairSynTask() {
//        logger.info("exexute userCoinGiveRepairSynTask  start ....");
//        registerUserCoinRepairService.doTimeRepair();
//        logger.info("exexute userCoinGiveRepairSynTask  end   ....");
//    }

    /**
     * 用户同步发送短信（初始化密码）
     */
    //@Scheduled(cron="0 0 10 19 08 *")
    //@Scheduled(fixedRateString = "30000")
	/*public void userSynSendMessageTask() {
		logger.info("exexute userSynSendMessageTask  start ....");
		statisticsTradeTaskService.userSynSendMessageTask();
		logger.info("exexute userSynSendMessageTask  end   ....");
	}*/


    /**
     * 清理验证码图片任务
     */
    //@Scheduled(cron="0 0 0/1 * * ?")
	/*public void clearValidPhotoTask() {
		logger.info("exexute clearValidPhotoTask  start ....");
		statisticsTradeTaskService.clearValidPhotoTask();
		logger.info("exexute clearValidPhotoTask  end   ....");
	}*/




    //@Scheduled(cron = "0 0/5 * * * ?")
    public void tradeRiskControlTask() {
        logger.info("开始执行交易风险控制 execute  start ....");
        tradeRiskControlService.execute();
        logger.info("结束执行交易风险控制 execute  end ....");
    }

    /**
     * 接力撞奖
     */
    //@Scheduled(cron = "0 0/2 * * * ? ")
    //@Scheduled(fixedRate = 20000)
    public void relayPrizeTaskEntry() {
        logger.info("exexute relayPrizeTaskEntry  start ....");
        mkRelayPrizeTaskService.relayPrizeTaskEntry();
        logger.info("exexute relayPrizeTaskEntry  end   ....");
    }

    /**
     * 接力自动撞奖
     */
    //@Scheduled(cron = "0 1/2 * * * ? ")
    //@Scheduled(fixedRate = 20000)
    public void relayAutoTaskEntry() {
        logger.info("exexute relayAutoTaskEntry  start ....");
        mkRelayPrizeTaskService.relayAutoTaskEntry();
        logger.info("exexute relayAutoTaskEntry  end   ....");
    }

    /**
     * 持币统计入库定时任务
     */
    //@Scheduled(cron = "0 0 1 * * ?")
    public void holdCoinCountTask() {
        logger.info("exexute holdCoinCountTask  start ....");
        platUserCoinCountService.countUserCoin();
        logger.info("exexute holdCoinCountTask  end   ....");
    }

    /**
     * 更新C2C[UES]资产价格,每天6:00AM
     */
    //@Scheduled(cron = "0 0 6 * * ?")
    public void updateOfflineCoinTaskEntry() {
        logger.info("exexute updateOfflineCoinTaskEntry  start ....");
        offlineCoinTaskService.updateOfflineCoinTaskEntry();
        logger.info("exexute updateOfflineCoinTaskEntry  end   ....");
    }

    /**
     * 矿主达标统计,每天6:00AM
     */
    //@Scheduled(cron = "0 0 6 * * ?")
    public void reachStandardTaskEntry() {
        logger.info("exexute reachStandardTaskEntry  start ....");
        mkMinerRecruitTaskService.reachStandardTaskEntry();
        logger.info("exexute reachStandardTaskEntry  end   ....");
    }

    /**
     * 每天凌晨1点，对账银商和广告商的c2c的资产
     */
    //@Scheduled(cron = "0 0 1 * * ?")
    public void offlineCoinVolumeDay() {
        logger.info("exexute offlineCoinVolumeDay  start ....");
        OfflineCoinVolumeDayVO coinVolumeDayVO = new OfflineCoinVolumeDayVO();
        coinVolumeDayVO.setStartTime(LocalDate.now().minusDays(1));
        coinVolumeDayVO.setEndTime(LocalDate.now());
        StringBuffer symbolsBuufer = new StringBuffer();
        Stream.of(maincoinC2c.split(",")).distinct().forEach(mainCoin -> {
            symbolsBuufer.append("'" + mainCoin.trim() + "'" + ",");
        });
        String symbols = symbolsBuufer.toString();
        coinVolumeDayVO.setSymbols(symbols.substring(0, symbols.length() - 1));
        offlineCoinVolumeDayService.batchInsertSelect(coinVolumeDayVO);
        logger.info("exexute offlineCoinVolumeDay  end   ....");
    }


    /**
     * 用户注册推荐人送奖励任务  每天凌晨3点执行
     */
    // @Scheduled(cron = "0 0 3 * * ?")
    public void registerReferLottery() {
        logger.info("exexute registerReferLottery  start ....");
        userRegisterLotteryService.executeLotteryRefer();
        logger.info("exexute registerReferLottery  end   ....");
    }



    /**
     * 生成操盘手资产快照
     */
    //@Scheduled(cron = "0 0 0 * * ?")
    public void triggerTraderVolumeSnapshotEntry() {
        logger.info("exexute triggerTraderVolumeSnapshotEntry  start ....");
        traderVolumeSnapshotTaskService.triggerTraderVolumeSnapshotEntry();
        logger.info("exexute triggerTraderVolumeSnapshotEntry  end   ....");
    }

    //@Scheduled(cron = "0 30 3 27 12 ?")
    public void klineMinDateTransfer() {
        logger.info("exexute klineMinDateTransfer  start ....");
        try {
            klineMinDateTransfer.transfer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("exexute klineMinDateTransfer  end   ....");
    }

    /**
     * 超级钱包到期处理
     */
    //@Scheduled(cron = "0 0 0 * * ?")
    public void triggerHandleExpireAccountEntry() {
        logger.info("exexute triggerHandleExpireAccountEntry  start ....");
        superCoinVolumeTaskService.triggerHandleExpireAccountEntry();
        logger.info("exexute triggerHandleExpireAccountEntry  end   ....");
    }



    /**
     * 生成资产负债快照
     */
    //@Scheduled(cron = "0 30 1 * * ?")
    public void triggerBalanceSheetSnapshotEntry() {
        logger.info("exexute triggerBalanceSheetSnapshotEntry  start ....");
        balanceSheetSnapshotTaskService.triggerBalanceSheetSnapshotEntry();
        logger.info("exexute triggerBalanceSheetSnapshotEntry  end   ....");
    }

    /**
     * 资产对账风控报警
     */
    //@Scheduled(cron = "0 0 0/1 * * ?")
    public void triggerCoinVolumeRiskMgtEntry() {
        logger.info("exexute triggerCoinVolumeRiskMgtEntry  start ....");
        coinVolumeRiskMgtTaskService.triggerCoinVolumeRiskMgtEntry();
        logger.info("exexute triggerCoinVolumeRiskMgtEntry  end   ....");
    }

    /**
     * 红包退回，每小时一次
     */
    //@Scheduled(cron = "0 0 0/1 * * ?")
    public void triggerHandleExpiredEntry() {
        logger.info("exexute triggerHandleExpiredEntry  start ....");
        offlineRedEnvelopeService.handleExpired();
        logger.info("exexute triggerHandleExpiredEntry  end   ....");
    }


    /**
     * 每天交易对的交易量统计，存入mysql
     */
    @Scheduled(cron = "0 1 0 * * ?")
    public void statisticsTradeDayExe() {
        logger.info("exexute statisticsTradeDayExe  start ....");
        statisticsTradeTaskService.statisticsTradeDay();
        logger.info("exexute statisticsTradeDayExe  end   ....");
    }

    /**
     * 交易对和币种的手续费统计
     */
    @Scheduled(cron = "0 1 0 * * ?")
    public void statisticsTradeFreesExe() {
        //统计交易手续费  0:买入  1:卖出   TradeDetail:交易流水
        logger.info("exexute statisticsTradeDetailFree  start ....");
        statisticsTradeTaskService.statisticsTradeDetailFree();
        logger.info("exexute statisticsTradeDetailFree  end   ....");
    }


    /**
     * 取消C2C订单(每分钟执行一次)
     */
//    @Scheduled(cron = "0 0/1 * * * ?")
    //@Scheduled(fixedRate = 20000)
    public void doCancelOrderDetail() {

        offlineOrderDetailCancelTaskService.doCancelOrderDetail();
        logger.info("exexute doCancelOrderDetail  end   ....");
    }

    @Scheduled(fixedRate = 1800 * 1000)
    public void scheduledUserCoinVolumebillHistory() {
        logger.info("请除userCoinVolumeBill的历史记录开始....");
        long execute = userCoinVolumeBillTaskService.execute();
        logger.info("请除userCoinVolumeBill的历史记录结束,处理{}条....", execute);
    }


    /**
     * 用户身份证审核  5分钟执行一次
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void userCardStatusTask() {
        logger.info("exexute userCardStatusTask  start ....");
        CardStatuScanCheckDTO cardStatuScanCheckDTO = new CardStatuScanCheckDTO();
        cardStatuScanCheckDTO.setAppKey(aliYunCardCheckConfig.getAppKey());
        cardStatuScanCheckDTO.setAppSecret(aliYunCardCheckConfig.getAppSecret());
        cardStatuScanCheckDTO.setUserId(aliYunCardCheckConfig.getUserId());
        cardStatuScanCheckDTO.setVerifyKey(aliYunCardCheckConfig.getVerifyKey());
        cardStatuScanCheckDTO.setCardStatusCheckTime(aliYunCardCheckConfig.getMaxCheckTime());

        cardStatuScanCheckDTO.setCardLevel(CardStatusEnum.CARD_STATUS_ZERO.getCode());
        cardStatuScanCheckDTO.setCardStatus(Integer.parseInt(UserCardStatusEnum.APPLY.getCode()));
        cardStatuScanCheckDTO.setContryCode("00");

        platUserService.userCardStatuScanCheck(cardStatuScanCheckDTO);
        logger.info("exexute userCardStatusTask  end   ....");
    }

    /**
     * 余币宝收益,每天00:00AM
     */
    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(cron = "0/59 0 0/1 * * ?")
    public void balanceIncomeDetail() {
        logger.info("exexute balanceIncomeDetail  start ....");
//        balanceUserCoinVolumeDetailService.balanceIncomeDetail();
        //静态收益和平级奖的利率支持配置
        Map<String, BigDecimal> dayRateMap=new HashMap<String, BigDecimal>();
        dayRateMap.put("oneDayRate",balanceDayRateConfig.getOneDayRate());
        dayRateMap.put("secondDayRate",balanceDayRateConfig.getSecondDayRate());
        dayRateMap.put("threeDayRate",balanceDayRateConfig.getThreeDayRate());
        dayRateMap.put("equalReward",balanceDayRateConfig.getEqualReward());
        //每天收益和奖励计算
        Map<String, List<TradePairVO>>  allTrade= buildAllTradePair();
        Map<String,TradePairVO>  tradePairMap=new HashMap<String,TradePairVO>();
        if(allTrade !=null && allTrade.size()>0){
            for(String key : allTrade.keySet()){
                List<TradePairVO> list=allTrade.get(key);
                for (TradePairVO vo:list){
                    tradePairMap.put(vo.getCoinOther(),vo);
                }
            }
        }
        BigDecimal platPrice=BigDecimal.ZERO;
        TradePairVO tradePair=tradePairMap.get("MG");
        if(tradePair!=null && tradePair.getLatestPrice() != null && tradePair.getLatestPrice().compareTo(BigDecimal.ZERO)>0){
            platPrice=platPrice.add(tradePair.getLatestPrice());
        }
        if(platPrice.compareTo(BigDecimal.ZERO)<=0){
            platPrice=platPrice.add(balanceUserCoinVolumeDetailService.findPriceByUpdateDate());
        }
        balanceUserCoinVolumeDetailService.balanceIncomeDetailNew(dayRateMap,platPrice);
        balanceUserCoinVolumeDetailService.balanceIncomeCount();
        BalancePlatCoinPriceVolume balancePlat=new BalancePlatCoinPriceVolume();
        balancePlat.setPrice(platPrice);
        balancePlat.setCoinPlatSymbol("MG");
        balanceUserCoinVolumeDetailService.insertPlatPrice(balancePlat);
        logger.info("exexute balanceIncomeDetail  end   ....");
    }

    /**
     * 余币宝统计,每天8:00AM
     */
//    @Scheduled(cron = "0 0 8 * * ?")
    public void balanceIncomeCount() {
        logger.info("exexute balanceIncomeCount  start ....");
        Map<String, List<TradePairVO>>  allTrade= buildAllTradePair();
        Map<String,TradePairVO>  tradePairMap=new HashMap<String,TradePairVO>();
        if(allTrade !=null && allTrade.size()>0){
            for(String key : allTrade.keySet()){
                List<TradePairVO> list=allTrade.get(key);
                for (TradePairVO vo:list){
                    tradePairMap.put(vo.getCoinOther(),vo);
                }
            }
        }

        logger.info("exexute balanceIncomeCount  end   ....");
    }
    /**
     * 交易界面交易对信息.
     *
     * @return Map map
     */

    public Map<String, List<TradePairVO>> buildAllTradePair() {
        List<RedisExPairVO> allExpair = redisCacheManager.acquireAllExpair();

        final List<RedisExPairVO> sortList = allExpair.stream()
                .sorted(Comparator.comparing(RedisExPairVO::getSort))
                .sorted(Comparator.comparing(RedisExPairVO::getType))
                .collect(Collectors.toList());

        Map<String, List<RedisExPairVO>> listMap = sortList
                .stream()
                .collect(Collectors.groupingBy(RedisExPairVO::getPairOne));

        Map<String, List<RedisExPairVO>> sortMap = Maps.newLinkedHashMap();

        for (String main : coinMainService.getList()) {
            if (CollectionUtils.isNotEmpty(listMap.get(main))) {
                sortMap.put(main, listMap.get(main));
            }
        }

        Map<String, List<TradePairVO>> resultMap = Maps.newLinkedHashMap();

        sortMap.forEach((k, v) -> {
            List<TradePairVO> vos = Lists.newArrayList();
            for (RedisExPairVO exPair : v) {
                final KlineVO klineVO = (KlineVO) redisTemplate.opsForHash().get(buildKey(exPair.getPairOne(), exPair.getPairOther()),
                        DateUtils.formaterLocalDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0))));
                TradePairVO vo = new TradePairVO();
                vo.setCoinMain(exPair.getPairOne());
                vo.setCoinOther(exPair.getPairOther());
                vo.setPricePrecision(exPair.getPricePrecision());
                vo.setVolumePrecision(exPair.getVolumePrecision());
                vo.setVolumePercent(exPair.getVolumePercent());
                vo.setType(exPair.getType());
                if (Objects.nonNull(klineVO)) {
                    vo.setHighestPrice(new BigDecimal(klineVO.getH()));
                    vo.setDayCount(new BigDecimal(klineVO.getV()));
                    vo.setLowerPrice(new BigDecimal(klineVO.getL()));
                    vo.setFirstPrice(new BigDecimal(klineVO.getO()));
                    vo.setLatestPrice(new BigDecimal(klineVO.getC()));
                    BigDecimal rise = new BigDecimal(0);
                    if (vo.getFirstPrice().compareTo(new BigDecimal(0)) != 0) {
                        rise = vo.getLatestPrice().subtract(vo.getFirstPrice())
                                .divide(vo.getFirstPrice(), 8, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                    }
                    vo.setRise(rise.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%");
                } else {
                    vo.setDayCount(new BigDecimal(0.00));
                    vo.setFirstPrice(new BigDecimal(0.00));
                    vo.setLowerPrice(new BigDecimal(0.00));
                    vo.setHighestPrice(new BigDecimal(0.00));
                    vo.setLatestPrice(new BigDecimal(0.00));
                    vo.setRise("0.00");
                }

                vos.add(vo);
            }
            resultMap.put(k, vos);
        });
        return resultMap;
    }
    public String buildKey(String coinMain, String coinOther) {
        return "kline:" + coinMain + "_" + coinOther + ":1d";
    }

    /**
     * 挖矿开奖设置，每10天执行一次开奖
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
//    @Scheduled(cron = "0 4 16 * * ?")
    public void balanceJackpotIncomeCount() {
        logger.info("exexute balanceJackpotIncomeCount  start ....");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse(balanceDayRateConfig.getRewardDateStr(),df);
        Duration duration = Duration.between(ldt,now);
        long days = duration.toDays(); //相差的天数
        long hours = duration.toHours();//相差的小时数
        long minutes = duration.toMinutes();//相差的分钟数
        long millis = duration.toMillis();//相差毫秒数
        if(millis<0){
          return;
        }
        long chaHours=hours-days*24;
        long chaMin=minutes-days*24*60;
        if(days%10==0 && chaHours <1){
            Map<String, List<TradePairVO>>  allTrade= buildAllTradePair();
            Map<String,TradePairVO>  tradePairMap=new HashMap<String,TradePairVO>();
            if(allTrade !=null && allTrade.size()>0){
                for(String key : allTrade.keySet()){
                    List<TradePairVO> list=allTrade.get(key);
                    for (TradePairVO vo:list){
                        tradePairMap.put(vo.getCoinOther(),vo);
                    }
                }
            }
            balanceUserCoinVolumeDetailService.balanceJackpotIncomeCount(tradePairMap);
        }
        logger.info("exexute balanceJackpotIncomeCount  end   ....");
    }
}
