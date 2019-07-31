package com.biao.entity.balance;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户余币宝资产
 *
 *  ""
 */
@SqlTable("js_plat_coin_income_price")
public class BalancePlatCoinPriceVolume extends BaseEntity {

    @SqlField("price")
    private BigDecimal price;

    @SqlField("coin_plat_symbol")
    private String coinPlatSymbol;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCoinPlatSymbol() {
        return coinPlatSymbol;
    }

    public void setCoinPlatSymbol(String coinPlatSymbol) {
        this.coinPlatSymbol = coinPlatSymbol;
    }
}
