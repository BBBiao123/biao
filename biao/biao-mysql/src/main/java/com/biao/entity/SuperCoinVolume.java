package com.biao.entity;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SqlTable("js_plat_super_coin_volume")
public class SuperCoinVolume extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("user_id")
    private String userId;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("coin_symbol")
    private String coinSymbol;
    @SqlField("volume")
    private BigDecimal volume;

    @SqlField("deposit_begin")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime depositBegin;

    @SqlField("deposit_end")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime depositEnd;

    @SqlField("version")
    private Long version;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public LocalDateTime getDepositBegin() {
        return depositBegin;
    }

    public void setDepositBegin(LocalDateTime depositBegin) {
        this.depositBegin = depositBegin;
    }

    public LocalDateTime getDepositEnd() {
        return depositEnd;
    }

    public void setDepositEnd(LocalDateTime depositEnd) {
        this.depositEnd = depositEnd;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
