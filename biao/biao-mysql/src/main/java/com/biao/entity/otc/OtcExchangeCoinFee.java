package com.biao.entity.otc;

import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

/**
 * 通兑手续费
 */
@SqlTable("otc_exchange_coin_fee")
public class OtcExchangeCoinFee extends BaseEntity {

    @SqlField("publish_source")
    private String publishSource;
    @SqlField("symbol")
    private String symbol;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("ex_fee")
    private BigDecimal exFee;
    @SqlField("fee_type")
    private String feeType;
    @SqlField("ex_fee_step")
    private String exFeeStep;
    @SqlField("point_volume")
    private Integer pointVolume;
    @SqlField("disable")
    private Integer disable;

    public String getPublishSource() {
        return publishSource;
    }

    public void setPublishSource(String publishSource) {
        this.publishSource = publishSource;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public BigDecimal getExFee() {
        return exFee;
    }

    public void setExFee(BigDecimal exFee) {
        this.exFee = exFee;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getExFeeStep() {
        return exFeeStep;
    }

    public void setExFeeStep(String exFeeStep) {
        this.exFeeStep = exFeeStep;
    }

    public Integer getPointVolume() {
        return pointVolume;
    }

    public void setPointVolume(Integer pointVolume) {
        this.pointVolume = pointVolume;
    }

    public Integer getDisable() {
        return disable;
    }

    public void setDisable(Integer disable) {
        this.disable = disable;
    }
}
