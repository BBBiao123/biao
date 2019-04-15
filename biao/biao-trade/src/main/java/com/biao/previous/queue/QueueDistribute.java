package com.biao.previous.queue;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.function.Supplier;

/**
 * 消息分发器；
 */
public class QueueDistribute {
    /**
     * 保存提供者处理器；
     */
    private Map<QueueType, QueueProvider<Object>> providers = Maps.newEnumMap(QueueType.class);

    /**
     * 返回一个单例实体；
     */
    private static class QueueDistributeHolder {

        private static final QueueDistribute instance = new QueueDistribute();
    }

    /**
     * 返回分发处理器；
     *
     * @return QueueDispator
     */
    public static QueueDistribute get() {
        return QueueDistributeHolder.instance;
    }

    /**
     * 返回消息提供者；
     *
     * @param lier 消息提供者实现；
     * @param <T>  消息处理器；
     * @return
     */
    public <T> void provider(QueueType type, Supplier<QueueProvider<T>> lier) {
        QueueProvider queueProvider = lier.get();
        providers.put(type, queueProvider);
    }

    /**
     * 发送消息；
     *
     * @param type 类型；
     * @param t    消息；
     * @param <T>  类型；
     */
    public <T> void send(QueueType type, T t) {
        if (providers.containsKey(type)) {
            providers.get(type).onData(f -> f.setT(t));
        }
    }

}
