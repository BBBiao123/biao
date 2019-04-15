package com.biao.entity;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SqlTable("js_plat_balance_sheet_snapshot")
public class BalanceSheetSnapshot extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("snap_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime snapDate;
    @SqlField("coin_symbol")
    private String coinSymbol;
    @SqlField("total_volume")
    private BigDecimal totalVolume;
    @SqlField("income")
    private BigDecimal income;
    @SqlField("trade_fee")
    private BigDecimal tradeFee;
    @SqlField("deposit_volume")
    private BigDecimal depositVolume;
    @SqlField("offline_fee")
    private BigDecimal offlineFee;
    @SqlField("deduct_volume")
    private BigDecimal deductVolume;
    @SqlField("withdraw_fee")
    private BigDecimal withdrawFee;
    @SqlField("expense")
    private BigDecimal expense;
    @SqlField("withdraw_volume")
    private BigDecimal withdrawVolume;
    @SqlField("withdraw_block_fee")
    private BigDecimal withdrawBlockFee;
    @SqlField("deposit_allocation_fee")
    private BigDecimal depositAllocationFee;
    @SqlField("remit_volume")
    private BigDecimal remitVolume;
    @SqlField("mining_volume")
    private BigDecimal miningVolume;
    @SqlField("register_volume")
    private BigDecimal registerVolume;
    @SqlField("relay_volume")
    private BigDecimal relayVolume;
    @SqlField("remark")
    private String remark;

    public LocalDateTime getSnapDate() {
        return snapDate;
    }

    public void setSnapDate(LocalDateTime snapDate) {
        this.snapDate = snapDate;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public BigDecimal getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(BigDecimal totalVolume) {
        this.totalVolume = totalVolume;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getTradeFee() {
        return tradeFee;
    }

    public void setTradeFee(BigDecimal tradeFee) {
        this.tradeFee = tradeFee;
    }

    public BigDecimal getDepositVolume() {
        return depositVolume;
    }

    public void setDepositVolume(BigDecimal depositVolume) {
        this.depositVolume = depositVolume;
    }

    public BigDecimal getOfflineFee() {
        return offlineFee;
    }

    public void setOfflineFee(BigDecimal offlineFee) {
        this.offlineFee = offlineFee;
    }

    public BigDecimal getDeductVolume() {
        return deductVolume;
    }

    public void setDeductVolume(BigDecimal deductVolume) {
        this.deductVolume = deductVolume;
    }

    public BigDecimal getWithdrawFee() {
        return withdrawFee;
    }

    public void setWithdrawFee(BigDecimal withdrawFee) {
        this.withdrawFee = withdrawFee;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }

    public BigDecimal getWithdrawVolume() {
        return withdrawVolume;
    }

    public void setWithdrawVolume(BigDecimal withdrawVolume) {
        this.withdrawVolume = withdrawVolume;
    }

    public BigDecimal getWithdrawBlockFee() {
        return withdrawBlockFee;
    }

    public void setWithdrawBlockFee(BigDecimal withdrawBlockFee) {
        this.withdrawBlockFee = withdrawBlockFee;
    }

    public BigDecimal getDepositAllocationFee() {
        return depositAllocationFee;
    }

    public void setDepositAllocationFee(BigDecimal depositAllocationFee) {
        this.depositAllocationFee = depositAllocationFee;
    }

    public BigDecimal getRemitVolume() {
        return remitVolume;
    }

    public void setRemitVolume(BigDecimal remitVolume) {
        this.remitVolume = remitVolume;
    }

    public BigDecimal getMiningVolume() {
        return miningVolume;
    }

    public void setMiningVolume(BigDecimal miningVolume) {
        this.miningVolume = miningVolume;
    }

    public BigDecimal getRegisterVolume() {
        return registerVolume;
    }

    public void setRegisterVolume(BigDecimal registerVolume) {
        this.registerVolume = registerVolume;
    }

    public BigDecimal getRelayVolume() {
        return relayVolume;
    }

    public void setRelayVolume(BigDecimal relayVolume) {
        this.relayVolume = relayVolume;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
