package com.biao.previous.queue;

import lombok.Data;

/**
 *
 */
@Data
public class QueueEvent<T> {
    private T t;

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
