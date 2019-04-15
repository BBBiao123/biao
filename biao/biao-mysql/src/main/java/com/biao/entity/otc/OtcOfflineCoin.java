package com.biao.entity.otc;

import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户c2c挂单表
 */
@SqlTable("otc_offline_coin")
@Data
public class OtcOfflineCoin extends BaseEntity {
    @SqlField("publish_source")
    private String publishSource;
    @SqlField("symbol")
    private String symbol;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("support_currency_code")
    private String supportCurrencyCode;
    @SqlField("max_price")
    private BigDecimal maxPrice;
    @SqlField("min_price")
    private BigDecimal minPrice;
    @SqlField("point_price")
    private Integer pointPrice;
    @SqlField("max_volume")
    private BigDecimal maxVolume;
    @SqlField("min_volume")
    private BigDecimal minVolume;
    @SqlField("point_volume")
    private Integer pointVolume;
    @SqlField("bail_volume")
    private BigDecimal bailVolume;
    @SqlField("disable")
    private Integer disable;
    @SqlField("buy_fee")
    private BigDecimal buyFee;
    @SqlField("sell_fee")
    private BigDecimal sellFee;
    @SqlField("fee_type")
    private String feeType;
    @SqlField("buy_fee_step")
    private String buyFeeStep;
    @SqlField("sell_fee_step")
    private String sellFeeStep;
    @SqlField("day_inc_price")
    private BigDecimal dayIncPrice;
}
