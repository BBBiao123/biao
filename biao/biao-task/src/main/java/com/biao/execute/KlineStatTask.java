package com.biao.execute;

import com.biao.constant.RedisKeyConstant;
import com.biao.current.threadpool.BbexThreadFactory;
import com.biao.enums.KlineTimeEnum;
import com.biao.enums.TradePairEnum;
import com.biao.reactive.data.mongo.domain.MatchStream;
import com.biao.reactive.data.mongo.domain.RedisMatchStream;
import com.biao.reactive.data.mongo.domain.kline.KlineLog;
import com.biao.reactive.data.mongo.domain.kline.TimingWheel;
import com.biao.reactive.data.mongo.service.KlineLogService;
import com.biao.reactive.data.mongo.service.MatchStreamService;
import com.biao.redis.RedisCacheManager;
import com.biao.service.KlineLogDataService;
import com.biao.service.kline.KlineLogDataConfig;
import com.biao.vo.KlineVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 *  ""(""611 @ qq.com)
 */
@Component
@SuppressWarnings("all")
public class KlineStatTask implements CommandLineRunner {

    private static final int MAX_THREAD = Runtime.getRuntime().availableProcessors() << 1;

    private static final Logger LOGGER = LoggerFactory.getLogger(KlineStatTask.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisCacheManager redisCacheManager;

    @Autowired
    private KlineLogDataService klineLogDataService;
    @Autowired
    private MatchStreamService matchStreamService;
    @Autowired
    private KlineLogService klineLogService;

    @Value("${kline.stat.time:120}")
    private int time;

    private static final int hashLeng = 500;

    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(MAX_THREAD,
            BbexThreadFactory.create("kline-stat-task", false));
    ;


    @Override
    public void run(String... args) {

      /*  List<RedisExPairVO> allExpair =
                redisCacheManager.acquireAllExpair();
        for (RedisExPairVO vo : allExpair) {
            executorService.scheduleWithFixedDelay(new Task(vo.getPairOne(), vo.getPairOther()), 0, time, TimeUnit.SECONDS);
        }*/
        //initEGOTAndUSDT();
        //initADSChainAndUSDT();

    }

    public void initEGOTAndUSDT() {
        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        final List<KlineTimeEnum> klineTimeEnums = KlineTimeEnum.acquiredList();
        List<RedisMatchStream> matchStreams = matchStreamService.findIncrementTrades("USDT",
                "EGOT", MatchStream.class, null, LocalDateTime.parse("20181106210359", ofPattern));
        //数据转移都mongo
        if (!CollectionUtils.isEmpty(matchStreams)) {
            List<KlineLog> klineLogs = matchStreams.stream().map(matchStream -> {
                KlineLog klineLog = new KlineLog();
                klineLog.setCoinMain(matchStream.getCoinMain());
                klineLog.setCoinOther(matchStream.getCoinOther());
                klineLog.setCreateTime(LocalDateTime.now());
                klineLog.setMinuteTime(matchStream.getMinuteTime());
                klineLog.setPrice(matchStream.getPrice());
                klineLog.setTradeTime(matchStream.getTradeTime());
                klineLog.setVolume(matchStream.getVolume());
                return klineLog;
            }).collect(Collectors.toList());
            klineLogService.batchInsert(klineLogs);
        }

        for (KlineTimeEnum klineTimeEnum : klineTimeEnums) {
            KlineLogDataConfig config = new KlineLogDataConfig();
            config.setCoinMain("USDT");
            config.setCoinOther("EGOT");
            config.setDayType(klineTimeEnum);
            klineLogDataService.klineLogData(config, false);
        }
    }

    public void initADSChainAndUSDT() {
        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        final List<KlineTimeEnum> klineTimeEnums = KlineTimeEnum.acquiredList();
        List<RedisMatchStream> matchStreams = matchStreamService.findIncrementTrades("USDT",
                "ADSChain", MatchStream.class, null, LocalDateTime.parse("20181106210359", ofPattern));
        //数据转移都mongo
        if (!CollectionUtils.isEmpty(matchStreams)) {
            List<KlineLog> klineLogs = matchStreams.stream().map(matchStream -> {
                KlineLog klineLog = new KlineLog();
                klineLog.setCoinMain(matchStream.getCoinMain());
                klineLog.setCoinOther(matchStream.getCoinOther());
                klineLog.setCreateTime(LocalDateTime.now());
                klineLog.setMinuteTime(matchStream.getMinuteTime());
                klineLog.setPrice(matchStream.getPrice());
                klineLog.setTradeTime(matchStream.getTradeTime());
                klineLog.setVolume(matchStream.getVolume());
                return klineLog;
            }).collect(Collectors.toList());
            klineLogService.batchInsert(klineLogs);
        }

        for (KlineTimeEnum klineTimeEnum : klineTimeEnums) {
            KlineLogDataConfig config = new KlineLogDataConfig();
            config.setCoinMain("USDT");
            config.setCoinOther("ADSChain");
            config.setDayType(klineTimeEnum);
            klineLogDataService.klineLogData(config, false);
        }
    }


    private void statKline(final String coinMain, final String coinOther) {
        String listKey = TradePairEnum.buildIncrementTradeKey(coinMain, coinOther);
        final List<RedisMatchStream> redisMatchStreams =
                redisTemplate.opsForList().range(listKey, 0, -1);

        if (CollectionUtils.isNotEmpty(redisMatchStreams)) {
            final RedisMatchStream end = redisMatchStreams
                    .stream().max(Comparator.comparing(RedisMatchStream::getTradeTime)).get();

            final RedisMatchStream start = redisMatchStreams
                    .stream().min(Comparator.comparing(RedisMatchStream::getTradeTime)).get();

            final List<KlineTimeEnum> klineTimeEnums = KlineTimeEnum.acquiredList();

            for (KlineTimeEnum klineTimeEnum : klineTimeEnums) {
                TimingWheel timingWheel;
                if (klineTimeEnum.getMsg().contains("h") && !klineTimeEnum.getMsg().contains("mth")) {
                    LocalTime endTime = LocalTime.of(end.getMinuteTime().getHour(), 0, 0);
                    final LocalDateTime endDate = LocalDateTime.of(end.getMinuteTime().toLocalDate(), endTime);

                    LocalTime startTime = LocalTime.of(start.getMinuteTime().getHour(), 0, 0);
                    final LocalDateTime startDate = LocalDateTime.of(start.getMinuteTime().toLocalDate(), startTime);

                    timingWheel = new TimingWheel(startDate, endDate,
                            klineTimeEnum.getTime(), ChronoUnit.MINUTES);
                    timingWheel.addWheels(redisMatchStreams);
                } else if (klineTimeEnum.getMsg().contains("d")) {
                    LocalTime endTime = LocalTime.of(0, 0, 0);
                    final LocalDateTime endDate = LocalDateTime.of(end.getMinuteTime().toLocalDate(), endTime);
                    LocalTime startTime = LocalTime.of(0, 0, 0);
                    final LocalDateTime startDate = LocalDateTime.of(start.getMinuteTime().toLocalDate(), startTime);
                    timingWheel = new TimingWheel(startDate, endDate,
                            1, ChronoUnit.DAYS);
                    timingWheel.addWheels(redisMatchStreams);
                } else if (klineTimeEnum.getMsg().contains("w")) {
                    LocalTime endTime = LocalTime.of(0, 0, 0);
                    final LocalDateTime endDate = LocalDateTime.of(end.getMinuteTime().toLocalDate(), endTime);
                    LocalTime startTime = LocalTime.of(0, 0, 0);
                    final LocalDateTime startDate = LocalDateTime.of(start.getMinuteTime().toLocalDate(), startTime);
                    timingWheel = new TimingWheel(startDate, endDate,
                            7, ChronoUnit.DAYS);
                    timingWheel.addWheels(redisMatchStreams);
                } else if (klineTimeEnum.getMsg().contains("m") && !klineTimeEnum.getMsg().contains("mth")) {
                    LOGGER.info("================================================{}", klineTimeEnum.getMsg());
                    timingWheel = new TimingWheel(start.getMinuteTime(), end.getMinuteTime(),
                            klineTimeEnum.getTime(), ChronoUnit.MINUTES);
                    timingWheel.addWheels(redisMatchStreams);
                } else {
                    LocalTime endTime = LocalTime.of(0, 0, 0);
                    final LocalDateTime endDate = LocalDateTime.of(end.getMinuteTime().toLocalDate(), endTime);
                    LocalTime startTime = LocalTime.of(0, 0, 0);
                    final LocalDateTime startDate = LocalDateTime.of(start.getMinuteTime().toLocalDate(), startTime);
                    timingWheel = new TimingWheel(startDate, endDate,
                            30, ChronoUnit.DAYS);
                    timingWheel.addWheels(redisMatchStreams);
                }

                final List<TimingWheel.BucketValue<KlineVO>> compute = timingWheel.compute(rms -> {
                    KlineVO klineVO = new KlineVO();
                    final RedisMatchStream maxPrice = rms.stream().max(Comparator.comparing(RedisMatchStream::getPrice)).get();
                    klineVO.setH(maxPrice.getPrice().toPlainString());

                    final RedisMatchStream minPrice = rms.stream().min(Comparator.comparing(RedisMatchStream::getPrice)).get();
                    klineVO.setL(minPrice.getPrice().toPlainString());

                    final double totalVolume = rms.stream().mapToDouble(r -> r.getVolume().doubleValue()).sum();
                    klineVO.setV(new BigDecimal(totalVolume).setScale(8, RoundingMode.HALF_DOWN).toPlainString());

                    final RedisMatchStream openPrice = rms
                            .stream().min(Comparator.comparing(RedisMatchStream::getTradeTime)).get();

                    final RedisMatchStream closePrice = rms
                            .stream().max(Comparator.comparing(RedisMatchStream::getTradeTime)).get();

                    klineVO.setO(openPrice.getPrice().toPlainString());

                    klineVO.setC(closePrice.getPrice().toPlainString());

                    return klineVO;
                });

                List<KlineVO> vos = compute.stream().map(e -> {
                    final KlineVO value = e.getValue();
                    value.setT(String.valueOf(e.getTime().toInstant(ZoneOffset.of("+8")).toEpochMilli()));
                    value.setS("ok");
                    return value;
                }).collect(Collectors.toList());

                timingWheel.clear();
                final String key = RedisKeyConstant.buildTaskKlineStatKey(coinMain, coinOther, klineTimeEnum.getMsg());
                final String hashKey = klineTimeEnum.getMsg();
                if (vos.size() > hashLeng) {
                    List<KlineVO> collect = vos.stream().skip(vos.size() - hashLeng)
                            .collect(Collectors.toList());
                    redisTemplate.opsForHash().put(key, hashKey, collect);
                } else {
                    redisTemplate.opsForHash().put(key, hashKey, vos);
                }
            }
        }
    }

    class Task implements Runnable {

        private String coinMain;

        private String coinOther;

        Task(String coinMain, String coinOther) {
            this.coinMain = coinMain;
            this.coinOther = coinOther;
        }

        @Override
        public void run() {
            statKline(coinMain, coinOther);
        }

    }


}
