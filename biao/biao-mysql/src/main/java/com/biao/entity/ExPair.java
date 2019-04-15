package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 交易对
 */
@SqlTable("js_plat_ex_pair")
@Data
public class ExPair extends BaseEntity {

    @SqlField("other_coin_id")
    private String otherCoinId;

    @SqlField("pair_other")
    private String pairOther;

    @SqlField("coin_id")
    private String coinId;

    @SqlField("pair_one")
    private String pairOne;

    @SqlField("status")
    private String status;

    @SqlField("last_price")
    private BigDecimal lastPrice;

    @SqlField("change")
    private String change;


    @SqlField("high")
    private BigDecimal high;

    @SqlField("low")
    private BigDecimal low;

    @SqlField("volume")
    private BigDecimal volume;

    @SqlField("type")
    private String type;

    @SqlField("remarks")
    private String remarks;

    @SqlField("sort")
    private Integer sort;

    @SqlField("fee")
    private String fee;

    @SqlField("max_volume")
    private String maxVolume;

    @SqlField("min_volume")
    private String minVolume;

    @SqlField("price_precision")
    private Integer pricePrecision;

    @SqlField("volume_precision")
    private Integer volumePrecision;

    @SqlField("volume_percent")
    private Integer volumePercent;


}