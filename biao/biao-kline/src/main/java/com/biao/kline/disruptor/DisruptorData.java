package com.biao.kline.disruptor;

import com.lmax.disruptor.EventFactory;
import lombok.Data;

/**
 * The type Disruptor data.
 */
@Data
public class DisruptorData {

    /**
     * The constant FACTORY_INSTANCE.
     */
    public static DisruptorFactory FACTORY_INSTANCE = new DisruptorFactory();
    //定义数据结构
    //数据类型
    private Integer type;

    private Object data;

    /**
     * The type Disruptor factory.
     */
    public static class DisruptorFactory implements EventFactory<DisruptorData> {
        @Override
        public DisruptorData newInstance() {
            return new DisruptorData();
        }

    }
}

