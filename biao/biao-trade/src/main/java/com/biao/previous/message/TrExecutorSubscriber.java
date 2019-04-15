package com.biao.previous.message;

import com.biao.previous.domain.TrParent;
import com.biao.previous.domain.TrType;
import reactor.core.publisher.Flux;

import java.util.Collection;

/**
 * TrExecutorSubscriber.
 * <p>
 * <p>
 * 18-12-29下午5:20
 *
 * @param <T> the type parameter
 *  "" sixh
 */
public interface TrExecutorSubscriber<T extends TrParent> extends TradeExecutorSubscriber<T>{

    @Override
    Flux<String> executor(Collection<? extends T> collections);


    /**
     * Gets type.
     *
     * @return the type
     */
    TrType getType();
}
