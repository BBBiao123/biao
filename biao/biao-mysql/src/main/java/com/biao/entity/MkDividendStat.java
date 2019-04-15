package com.biao.entity;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SqlTable("mk_distribute_dividend_stat")
public class MkDividendStat extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("stat_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime statDate;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("coin_symbol")
    private String coinSymbol;
    @SqlField("volume")
    private BigDecimal volume;
    @SqlField("usdt_volume")
    private BigDecimal usdtVolume;
    @SqlField("btc_volume")
    private BigDecimal btcVolume;
    @SqlField("eth_volume")
    private BigDecimal ethVolume;
    @SqlField("usdt_real_volume")
    private BigDecimal usdtRealVolume;
    @SqlField("btc_real_volume")
    private BigDecimal btcRealVolume;
    @SqlField("eth_real_volume")
    private BigDecimal ethRealVolume;
    @SqlField("usdt_per_volume")
    private BigDecimal usdtPerVolume;
    @SqlField("btc_per_volume")
    private BigDecimal btcPerVolume;
    @SqlField("eth_per_volume")
    private BigDecimal ethPerVolume;
    @SqlField("per")
    private BigDecimal per;
    @SqlField("remark")
    private String remark;

    public LocalDateTime getStatDate() {
        return statDate;
    }

    public void setStatDate(LocalDateTime statDate) {
        this.statDate = statDate;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getUsdtVolume() {
        return usdtVolume;
    }

    public void setUsdtVolume(BigDecimal usdtVolume) {
        this.usdtVolume = usdtVolume;
    }

    public BigDecimal getBtcVolume() {
        return btcVolume;
    }

    public void setBtcVolume(BigDecimal btcVolume) {
        this.btcVolume = btcVolume;
    }

    public BigDecimal getEthVolume() {
        return ethVolume;
    }

    public void setEthVolume(BigDecimal ethVolume) {
        this.ethVolume = ethVolume;
    }

    public BigDecimal getUsdtRealVolume() {
        return usdtRealVolume;
    }

    public void setUsdtRealVolume(BigDecimal usdtRealVolume) {
        this.usdtRealVolume = usdtRealVolume;
    }

    public BigDecimal getBtcRealVolume() {
        return btcRealVolume;
    }

    public void setBtcRealVolume(BigDecimal btcRealVolume) {
        this.btcRealVolume = btcRealVolume;
    }

    public BigDecimal getEthRealVolume() {
        return ethRealVolume;
    }

    public void setEthRealVolume(BigDecimal ethRealVolume) {
        this.ethRealVolume = ethRealVolume;
    }

    public BigDecimal getUsdtPerVolume() {
        return usdtPerVolume;
    }

    public void setUsdtPerVolume(BigDecimal usdtPerVolume) {
        this.usdtPerVolume = usdtPerVolume;
    }

    public BigDecimal getBtcPerVolume() {
        return btcPerVolume;
    }

    public void setBtcPerVolume(BigDecimal btcPerVolume) {
        this.btcPerVolume = btcPerVolume;
    }

    public BigDecimal getEthPerVolume() {
        return ethPerVolume;
    }

    public void setEthPerVolume(BigDecimal ethPerVolume) {
        this.ethPerVolume = ethPerVolume;
    }

    public BigDecimal getPer() {
        return per;
    }

    public void setPer(BigDecimal per) {
        this.per = per;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
