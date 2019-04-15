package com.biao.service.impl;

import com.biao.BaseTest;
import com.biao.constant.RedisKeyConstant;
import com.biao.enums.KlineTimeEnum;
import com.biao.enums.TradePairEnum;
import com.biao.reactive.data.mongo.domain.RedisMatchStream;
import com.biao.reactive.data.mongo.domain.kline.TimingWheel;
import com.biao.vo.KlineVO;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  ""(""611 @ qq.com)
 */

@SuppressWarnings("all")
public class KlineDataHandlerTest extends BaseTest {

    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    public void buildResult() {

        statKline("USDT", "BTC");

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


                if (vos.size() > 500) {
                    vos = vos.subList(0, 500);
                }
                //放到redis
                final String key = RedisKeyConstant.buildTaskKlineStatKey(coinMain, coinOther, "1m");
                final String hashKey = klineTimeEnum.getMsg();
                redisTemplate.opsForHash().put(key, hashKey, vos);
            }
        }
    }

}
