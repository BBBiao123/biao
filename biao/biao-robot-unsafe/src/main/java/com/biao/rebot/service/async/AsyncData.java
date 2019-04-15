package com.biao.rebot.service.async;

/**
 * AsyncData.
 * <p>
 * 异步数据.
 * <p>
 * 18-12-18下午2:35
 *
 * @param <D> the type parameter
 *  "" sixh
 */
public interface AsyncData<D> {

    /**
     * Gets joinSymbol.
     *
     * @return the joinSymbol
     */
    String getSymbol();

    /**
     * Gets asks.
     *
     * @return the asks
     */
    D getAsks();

    /**
     * Gets dids.
     *
     * @return the dids
     */
    D getBids();
}
