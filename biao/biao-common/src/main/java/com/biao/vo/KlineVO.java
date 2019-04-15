package com.biao.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 返回给前端的K线实体类.
 *
 *  ""
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class KlineVO implements Serializable {

    /**
     * 时间.
     */
    private String t;

    /**
     * 开盘价.
     */
    private String o;

    /**
     * 最高价.
     */
    private String h;

    /**
     * 最低价.
     */
    private String l;

    /**
     * 收盘价.
     */
    private String c;

    /**
     * 成交量.
     */
    private String v;

    private String s;
}
