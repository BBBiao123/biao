package com.biao.rebot;

import lombok.Data;

/**
 * The type Symbol info.
 *
 */
@Data
public class SymbolInfo {
    /**
     * 主键
     */
    private String id;

    /**
     * 价格小数位.
     */
    private Integer priceScale;

    /**
     * 数量小数位.
     */
    private Integer volumeScale;

    /**
     * 交易对.
     */
    private String symbol;
}
