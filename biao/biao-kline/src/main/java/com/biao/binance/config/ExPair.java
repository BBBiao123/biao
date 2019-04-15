package com.biao.binance.config;

import lombok.Data;

/**
 * 交易对.
 */
@Data
public class ExPair {

    /**
     * The Id.
     */
    protected String id;

    private String otherCoinId;

    private String pairOther;

    private String coinId;

    private String pairOne;

    private String status;

}