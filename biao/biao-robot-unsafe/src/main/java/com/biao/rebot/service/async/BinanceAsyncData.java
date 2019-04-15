package com.biao.rebot.service.async;

import lombok.Setter;

import java.util.List;

/**
 * BinanceAsyncData.
 * <p>
 *     关于闭相关的数据处理.
 * <p>
 * 18-12-18下午2:35
 *
 *  "" sixh
 */
@Setter
public class BinanceAsyncData<D extends AsyncData> implements AsyncData<List<D>> {

    private String symbol;

    private List<D> asks;

    private List<D> bids;

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public List<D> getAsks() {
        return asks;
    }

    @Override
    public List<D> getBids() {
        return bids;
    }

    @Override
    public String toString() {
        return "BinanceAsyncData{" +
                "joinSymbol='" + symbol + '\'' +
                ", asks=" + asks +
                ", bids=" + bids +
                '}';
    }
}
