package com.biao.service.kline;

import com.biao.constant.RedisKeyConstant;
import com.biao.constant.TradeConstant;
import com.biao.entity.ExPair;
import com.biao.enums.KlineTimeEnum;
import com.biao.kafka.interceptor.SampleMessage;
import com.biao.mapper.ExPairDao;
import com.biao.pojo.MatchStreamDto;
import com.biao.util.DateUtils;
import com.biao.vo.KlineVO;
import com.beust.jcommander.internal.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 *  ""(Myth)
 */
@Component
public class KlineMinDateTransfer {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(KlineMinDateTransfer.class);

    private final ExPairDao exPairDao;

    private final RedisTemplate redisTemplate;

    private final KafkaTemplate kafkaTemplate;

    @Autowired(required = false)
    public KlineMinDateTransfer(final ExPairDao exPairDao,
                                final RedisTemplate<String, Object> redisTemplate,
                                final KafkaTemplate kafkaTemplate) {
        this.exPairDao = exPairDao;
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @SuppressWarnings("unchecked")
    public void transfer() {
        List<ExPair> expairs = exPairDao.findByList();
        if (CollectionUtils.isNotEmpty(expairs)) {
            LOGGER.info("============K线分钟数据开始迁移====================");
            for (ExPair exPair : expairs) {
                String coinMain = exPair.getPairOne();
                String coinOther = exPair.getPairOther();
                final String redisKey = buildMinKey(coinMain, coinOther, KlineTimeEnum.ONE_MINUTE.getMsg());
                List<KlineVO> klineVOList = (List<KlineVO>) redisTemplate.opsForList().range(redisKey, 0, -1);
                if (CollectionUtils.isNotEmpty(klineVOList)) {
                    final List<KlineVO> rs =
                            klineVOList.stream().sorted(Comparator.comparing(KlineVO::getT))
                                    .collect(Collectors.toList());
                    for (KlineVO klineVO : rs) {
                        try {
                            List<MatchStreamDto> vos = Lists.newArrayList(4);

                            final BigDecimal volume = new BigDecimal(klineVO.getV())
                                    .divide(new BigDecimal(4));

                            final LocalDateTime localDateTime = Instant.ofEpochMilli(Long.parseLong(klineVO.getT()))
                                    .atZone(ZoneId.systemDefault()).toLocalDateTime();

                            MatchStreamDto open = new MatchStreamDto();
                            open.setCoinMain(exPair.getPairOne());
                            open.setCoinOther(coinOther);
                            open.setMinuteTime(DateUtils.formaterLocalDateTime(localDateTime));
                            open.setTradeTime(DateUtils.formaterLocalDateTime(localDateTime.plusSeconds(1)));
                            open.setPrice(new BigDecimal(klineVO.getO()));
                            open.setVolume(volume);

                            vos.add(open);

                            MatchStreamDto high = new MatchStreamDto();
                            high.setCoinMain(coinMain);
                            high.setCoinOther(coinOther);
                            high.setMinuteTime(DateUtils.formaterLocalDateTime(localDateTime));
                            high.setTradeTime(DateUtils.formaterLocalDateTime(localDateTime.plusSeconds(5)));
                            high.setPrice(new BigDecimal(klineVO.getH()));
                            high.setVolume(volume);

                            vos.add(high);

                            MatchStreamDto low = new MatchStreamDto();
                            low.setCoinMain(coinMain);
                            low.setCoinOther(coinOther);
                            low.setMinuteTime(DateUtils.formaterLocalDateTime(localDateTime));
                            low.setTradeTime(DateUtils.formaterLocalDateTime(localDateTime.plusSeconds(5)));
                            low.setPrice(new BigDecimal(klineVO.getL()));
                            low.setVolume(volume);

                            vos.add(low);

                            MatchStreamDto receive = new MatchStreamDto();
                            receive.setCoinMain(coinMain);
                            receive.setCoinOther(coinOther);
                            receive.setMinuteTime(DateUtils.formaterLocalDateTime(localDateTime));
                            receive.setTradeTime(DateUtils.formaterLocalDateTime(localDateTime.plusSeconds(50)));
                            receive.setPrice(new BigDecimal(klineVO.getC()));
                            receive.setVolume(volume);

                            vos.add(receive);

                            vos.forEach(vo ->
                                    kafkaTemplate.send(TradeConstant.KLINE_MIN_TRANSFER, redisKey, SampleMessage.build(vo)));
                        } catch (Exception e) {
                        }
                    }

                }

            }

            LOGGER.info("============分钟数据迁移完成====================");
        }
    }

    private String buildMinKey(final String coinMain, final String coinOther, final String interval) {
        return RedisKeyConstant.buildTaskKlineStatKey(coinMain, coinOther, interval);
    }
}
