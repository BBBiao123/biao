package com.biao.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 交易对数据结构.
 *
 *  ""
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradePairBaseVO implements Serializable {

    /**
     * 交易对中的主区.
     */
    private String coinMain;

    /**
     * 交易对中的币种.
     */
    private String coinOther;


}
