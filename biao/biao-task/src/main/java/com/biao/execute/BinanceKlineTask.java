package com.biao.execute;

import com.biao.current.threadpool.BbexThreadFactory;
import com.biao.enums.TradePairEnum;
import com.biao.reactive.data.mongo.domain.RedisMatchStream;
import com.biao.util.http.OkHttpTools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * BinanceKlineTask.
 *
 *  ""(""611 @ qq.com)
 */
@Component
@ConditionalOnProperty(name = "binance.kline.enabled", matchIfMissing = true)
public class BinanceKlineTask {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BinanceKlineTask.class);

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${binance.kline.proxy:false}")
    private Boolean proxy;

    @Value("${binance.kline.time:300}")
    private int time;

    private static final Gson GOSN = new Gson();

    private static final String BTC = "BTC";

    private static final String ETH = "ETH";

    private static final String LTC = "LTC";

    private static final String OMG = "OMG";

    private static final String ZIL = "ZIL";

    private static final String ZRX = "ZRX";

    private static final String AE = "AE";

    private static final String TRX = "TRX";

    private static final String KNC = "KNC";

    private static final String BNT = "BNT";

    private static final String LRC = "LRC";

    private static final String LOOM = "LOOM";

    private static final String POA = "POA";

    private static final String BINANCE_KLINE = "BINANCE:KLINE:";

    private static final EnumMap<TradePairEnum, String[]> enumMap = new EnumMap(TradePairEnum.class);

    private static ScheduledExecutorService scheduledExecutorService;


    static {
        enumMap.put(TradePairEnum.USDT, new String[]{BTC, ETH, LTC, TRX});
        enumMap.put(TradePairEnum.BTC, new String[]{ETH, LTC, OMG, ZIL, "THETA", "POWR", "MCO", "AION", "REP", "BLZ", AE});
        enumMap.put(TradePairEnum.ETH, new String[]{BTC, LTC, OMG, ZIL, ZRX, AE, TRX, KNC, BNT, LRC, LOOM, POA,
                "THETA", "POWR", "MCO", "AION", "REP", "BLZ"});
        AtomicInteger le = new AtomicInteger();
        enumMap.forEach((k, v) -> le.addAndGet(v.length));
        scheduledExecutorService =
                new ScheduledThreadPoolExecutor(le.get(), BbexThreadFactory.create("binance-kline-task", true));

    }

    @Autowired
    public BinanceKlineTask(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
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
            pullBinanceKline(coinMain, coinOther);
        }
    }

    @PostConstruct
    public void runTask() {
      /*  enumMap.forEach((k, v) -> {
            for (String other : v) {
                scheduledExecutorService.scheduleWithFixedDelay(new Task(k.getKey(), other),
                        30, time, TimeUnit.SECONDS);
            }
        });*/

    }


    private void pullBinanceKline(final String coinMain, final String coinOther) {
        String url = "https://www.binance.com/api/v1/klines?symbol=" + coinOther + coinMain + "&interval=1m";
        LOGGER.info("拉取变kline数据，url:{}", url);
        try {
            //本地测试使用 proxyGet 服务端使用 get
            final String result;
            if (proxy) {
                result = OkHttpTools.getInstance().proxyGet(url);
            } else {
                result = OkHttpTools.getInstance().get(url);
            }
            Type type = new TypeToken<List<List<Object>>>() {
            }.getType();
            List<List<Object>> r = GOSN.fromJson(result, type);

            final LocalDateTime now = LocalDateTime.now();
            for (List<Object> objects : r) {
                final Long timestamp = ((Double) objects.get(0)).longValue();
                final LocalDateTime dateTime = LocalDateTime
                        .ofEpochSecond(timestamp / 1000, 0, ZoneOffset.ofHours(8));
                final Boolean inited = (Boolean) redisTemplate.opsForValue().get(buildKey(coinOther, coinMain));
                if (Objects.isNull(inited) || !inited) {
                    saveRedis(coinMain, coinOther, dateTime, objects);
                } else {
                    final LocalDateTime pullTime = now.minusSeconds(time);
                    if (dateTime.isAfter(pullTime)) {
                        saveRedis(coinMain, coinOther, dateTime, objects);
                    }
                }
            }
            redisTemplate.opsForValue().set(buildKey(coinOther, coinMain), Boolean.TRUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String buildKey(String coinOther, String coinMain) {
        return String.join("", BINANCE_KLINE, coinOther, coinMain);
    }

    private void saveRedis(String coinMain, String coinOther, LocalDateTime dateTime, List<Object> objects) {
        String pairVOKey = TradePairEnum.buildIncrementTradeKey(coinMain, coinOther);
        final BigDecimal divide = new BigDecimal(objects.get(5).toString())
                .divide(new BigDecimal(4), 2, RoundingMode.HALF_UP);

        RedisMatchStream open = new RedisMatchStream();
        open.setCoinMain(coinMain);
        open.setCoinOther(coinOther);
        open.setMinuteTime(dateTime);
        open.setTradeTime(dateTime.plusSeconds(1));
        open.setPrice(new BigDecimal(objects.get(1).toString()));
        open.setVolume(divide);
        redisTemplate.opsForList().leftPush(pairVOKey, open);


        RedisMatchStream high = new RedisMatchStream();
        high.setCoinMain(coinMain);
        high.setCoinOther(coinOther);
        high.setMinuteTime(dateTime);
        high.setTradeTime(dateTime.plusSeconds(5));
        high.setPrice(new BigDecimal(objects.get(2).toString()));
        high.setVolume(divide);
        redisTemplate.opsForList().leftPush(pairVOKey, high);


        RedisMatchStream low = new RedisMatchStream();
        low.setCoinMain(coinMain);
        low.setCoinOther(coinOther);
        low.setMinuteTime(dateTime);
        low.setTradeTime(dateTime.plusSeconds(5));
        low.setPrice(new BigDecimal(objects.get(3).toString()));
        low.setVolume(divide);
        redisTemplate.opsForList().leftPush(pairVOKey, low);

        RedisMatchStream receive = new RedisMatchStream();
        receive.setCoinMain(coinMain);
        receive.setCoinOther(coinOther);
        receive.setMinuteTime(dateTime);
        receive.setTradeTime(dateTime.plusSeconds(50));
        receive.setPrice(new BigDecimal(objects.get(4).toString()));
        receive.setVolume(divide);
        redisTemplate.opsForList().leftPush(pairVOKey, receive);

    }

}
