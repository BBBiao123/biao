package com.biao.previous.main;

import com.biao.constant.TradeConstant;
import com.biao.kafka.interceptor.SampleMessage;
import com.biao.pojo.KafkaPlatOrderDTO;
import com.biao.pojo.TradeDto;
import com.biao.previous.domain.ProcessData;

/**
 * 关于数据回退的处理;
 *
 */
public class FallbackFilter extends AbstractFilter {

    /**
     * 关于当前Filter的处理;
     *
     * @param data  数据;
     * @param chain 处理链
     */
    @Override
    public void doFilter0(TradeDto data, ProcessData processData, DataChain chain) {
        if (!processData.getOrderComplete()) {
            fallback(data);
        }
    }

    /**
     * 回归处理，重新放入到队列中；
     *
     * @param tradeDto 回归对象；
     */
    private void fallback(TradeDto tradeDto) {
        //计算出当前的因子;
        /*BigDecimal multiply = TradeCompute.multiply(tradeDto.getPrice(), new BigDecimal( 100000000), 0);
        String on = Joiner.on(".").join(String.valueOf(multiply.longValue()), String.valueOf(tradeDto.getConcurrentTime()));
        Double sc = Double.parseDouble(on);*/
        //发送一个消息通知。
        kafkaTemplate.send(TradeConstant.TRADE_KAFKA_ORDER_TOPIC, tradeDto.ackKey(),
                SampleMessage.build(KafkaPlatOrderDTO.buildPut(tradeDto)));
        redisTemplate.opsForZSet()
                .add(tradeDto.getType().redisKey(tradeDto.getCoinMain(), tradeDto.getCoinOther()),
                        tradeDto.getOrderNo(),
                        tradeDto.getPrice().doubleValue());
    }
}
