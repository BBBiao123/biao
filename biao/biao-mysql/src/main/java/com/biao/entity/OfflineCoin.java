package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户c2c挂单表
 */
@SqlTable("js_plat_offline_coin")
@Data
public class OfflineCoin extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @SqlField("symbol")
    private String symbol;
    @SqlField("coin_id")
    private String coinId;
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
    @SqlField("is_change_account")
    private String isChangeAccount;
    @SqlField("real_day_limit")
    private BigDecimal realDayLimit;
    @SqlField("non_real_day_limit")
    private BigDecimal nonRealDayLimit;
    @SqlField("change_fee_type")
    private String changeFeeType;
    @SqlField("change_fee_step")
    private String changeFeeStep;
    @SqlField("change_fee")
    private BigDecimal changeFee;
    @SqlField("change_min_volume")
    private BigDecimal changeMinVolume;
    @SqlField("is_volume")
    private Integer isVolume;


}
