package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

/**
 * eth token 余额 提现
 */
@SqlTable("eth_token_withdraw")
public class EthTokenWithdraw extends BaseEntity {

    @SqlField("coin_symbol")
    private String coinSymbol;

    @SqlField(value = "volume")
    private BigDecimal volume;

    @SqlField(value = "from_address")
    private String fromAddress;

    @SqlField(value = "to_address")
    private String toAddress;

    @SqlField(value = "status")
    private Integer status;

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

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
