package com.biao.binance;

import com.biao.binance.config.KlinePullConfig;
import com.biao.dao.KlinePullConfigDao;
import com.biao.kline.ExpairHandlerService;
import com.biao.kline.pool.ThreadPoolUtils;
import com.biao.kline.vo.KlineHandlerVO;
import com.biao.util.DateUtils;
import com.biao.util.http.OkHttpTools;
import com.beust.jcommander.internal.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

/**
 * The type Kline data service.
 *
 *  ""(Myth)
 */
@Service("klineDataService")
@SuppressWarnings("all")
public class KlineDataServiceImpl implements KlineDataService {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(KlineDataServiceImpl.class);

    private static final Gson GOSN = new Gson();

    private static final String BINANCE_KLINE = "BINANCE:KLINE:PULLDATE:";

    private final RedisTemplate redisTemplate;

    /**
     * Instantiates a new Kline data service.
     *
     * @param klinePullConfigDao the kline pull config dao
     * @param redisTemplate      the redis template
     */
    @Autowired(required = false)
    public KlineDataServiceImpl(final KlinePullConfigDao klinePullConfigDao,
                                final RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    @SuppressWarnings("unchecked")
    public void handlerPullData(final KlinePullConfig config) throws ParseException {
        final String coinMain = config.getCoinMain();
        final String coinOther = config.getCoinOther();
        List<List<Object>> resultList = pullData(config, "&interval=1m");
        if (CollectionUtils.isNotEmpty(resultList)) {
            LocalDateTime lastExeTime = null;
            String lastExeTimeStr = (String) redisTemplate.opsForValue().get(buildKey(coinOther, coinMain));
            if (StringUtils.isNotEmpty(lastExeTimeStr)) {
                lastExeTime = DateUtils.parseLocalDateTime(lastExeTimeStr);
            }
            for (List<Object> objects : resultList) {
                final Long timestamp = ((Double) objects.get(0)).longValue();
                final LocalDateTime dateTime = LocalDateTime
                        .ofEpochSecond(timestamp / 1000, 0, ZoneOffset.ofHours(8));
                if (Objects.isNull(lastExeTime)) {
                    push(coinMain, coinOther, dateTime, objects);
                } else {
                    if (dateTime.isAfter(lastExeTime)) {
                        push(coinMain, coinOther, dateTime, objects);
                    }
                }
            }
            redisTemplate.opsForValue().set(buildKey(config.getCoinOther(), config.getCoinMain()),
                    DateUtils.formaterLocalDateTime(LocalDateTime.now()));
        }
    }

    private List<List<Object>> pullData(final KlinePullConfig config, final String interval) {
        final String coinMain = config.getCoinMain();
        final String coinOther = config.getCoinOther();
        String url = config.getPullUrl() + "?symbol=" + coinOther + coinMain + interval;
        //本地测试使用 proxyGet 服务端使用 get
        LOGGER.info("拉取变kline数据，url:{}", url);
        try {
            final String result;
            if (config.getProxyed()) {
                result = OkHttpTools.getInstance().proxyGet(url);
            } else {
                result = OkHttpTools.getInstance().get(url);
            }
            Type type = new TypeToken<List<List<Object>>>() {
            }.getType();
            if (StringUtils.isNoneBlank(result)) {
                return GOSN.fromJson(result, type);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("币安拉取失败:{}", url);
            return null;
        }

    }

    private void push(final String coinMain, final String coinOther,
                      final LocalDateTime dateTime,
                      final List<Object> objects) {

        List<KlineHandlerVO> vos = Lists.newArrayList(4);

        final BigDecimal divide = new BigDecimal(objects.get(5).toString())
                .divide(new BigDecimal(4), 2, RoundingMode.HALF_UP);

        KlineHandlerVO open = new KlineHandlerVO();
        open.setCoinMain(coinMain);
        open.setCoinOther(coinOther);
        open.setMinuteTime(DateUtils.formaterLocalDateTime(dateTime));
        open.setTradeTime(DateUtils.formaterLocalDateTime(dateTime.plusSeconds(1)));
        open.setPrice(new BigDecimal(objects.get(1).toString()));
        open.setVolume(divide);

        vos.add(open);

        KlineHandlerVO high = new KlineHandlerVO();
        high.setCoinMain(coinMain);
        high.setCoinOther(coinOther);
        high.setMinuteTime(DateUtils.formaterLocalDateTime(dateTime));
        high.setTradeTime(DateUtils.formaterLocalDateTime(dateTime.plusSeconds(5)));
        high.setPrice(new BigDecimal(objects.get(2).toString()));
        high.setVolume(divide);

        vos.add(high);

        KlineHandlerVO low = new KlineHandlerVO();
        low.setCoinMain(coinMain);
        low.setCoinOther(coinOther);
        low.setMinuteTime(DateUtils.formaterLocalDateTime(dateTime));
        low.setTradeTime(DateUtils.formaterLocalDateTime(dateTime.plusSeconds(5)));
        low.setPrice(new BigDecimal(objects.get(3).toString()));
        low.setVolume(divide);

        vos.add(low);

        KlineHandlerVO receive = new KlineHandlerVO();
        receive.setCoinMain(coinMain);
        receive.setCoinOther(coinOther);
        receive.setMinuteTime(DateUtils.formaterLocalDateTime(dateTime));
        receive.setTradeTime(DateUtils.formaterLocalDateTime(dateTime.plusSeconds(50)));
        receive.setPrice(new BigDecimal(objects.get(4).toString()));
        receive.setVolume(divide);

        vos.add(receive);

        String key = coinMain + "_" + coinOther;
        vos.forEach(vo -> ThreadPoolUtils.getExpairInstance()
                .select(key)
                .execute(new ExpairHandlerService(vo)));
    }

    private String buildKey(final String coinOther, final String coinMain) {
        return String.join("", BINANCE_KLINE, coinOther, coinMain);
    }

}
