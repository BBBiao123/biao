package com.biao.previous.queue;


import com.lmax.disruptor.EventFactory;

/**
 *
 */
public class QueueEventFactory<T> implements EventFactory<QueueEvent> {

    @Override
    public QueueEvent newInstance() {
        return new QueueEvent();
    }
}
