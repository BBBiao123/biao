package com.biao.kafka;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.biao.constant.TradeConstant;
import com.biao.kafka.interceptor.SampleMessage;
import com.biao.kline.ExpairHandlerService;
import com.biao.kline.cache.GenericKlineCacheData;
import com.biao.kline.pool.ThreadPoolUtils;
import com.biao.kline.vo.KlineHandlerVO;
import com.biao.pojo.MatchStreamDto;

/**
 * The type Flow water consumer.
 *
 *  ""
 */
@Component
public class FlowWaterConsumer {

    private static Logger logger = LoggerFactory.getLogger(FlowWaterConsumer.class);

    /**
     * 监听流水，缓存.
     *
     * @param sampleMessages 对象
     */
    @KafkaListener(topics = TradeConstant.TRADE_RESULT_MATCH_TOPIC)
    public void processMatchResult(final List<SampleMessage> sampleMessages) {
    	GenericKlineCacheData.initBlock();
        MatchStreamDto matchStreamDto = sampleMessages.get(0).getMessage(MatchStreamDto.class);
        logger.info("consumer recevier message:{}", matchStreamDto);
        KlineHandlerVO klineHandlerVO = KlineHandlerVO.convert(matchStreamDto);
        ThreadPoolUtils.getExpairInstance()
                .select(buildSelectKey(klineHandlerVO))
                .execute(new ExpairHandlerService(klineHandlerVO));
    }


    /**
     * kline 分钟数据迁移.
     *
     * @param sampleMessages 对象
     */
    @KafkaListener(topics = TradeConstant.KLINE_MIN_TRANSFER)
    public void klineMinDataTransfer(final List<SampleMessage> sampleMessages) {
    	GenericKlineCacheData.initBlock();
        MatchStreamDto matchStreamDto = sampleMessages.get(0).getMessage(MatchStreamDto.class);
        logger.info("consumer recevier message:{}", matchStreamDto);
        KlineHandlerVO klineHandlerVO = KlineHandlerVO.convert(matchStreamDto);
        ThreadPoolUtils.getExpairInstance()
                .select(buildSelectKey(klineHandlerVO))
                .execute(new ExpairHandlerService(klineHandlerVO));
    }


    private String buildSelectKey(final KlineHandlerVO klineHandlerVO) {
        return klineHandlerVO.getCoinMain() + "_" + klineHandlerVO.getCoinOther();
    }


}
