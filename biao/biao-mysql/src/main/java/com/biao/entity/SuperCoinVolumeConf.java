package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

@SqlTable("js_plat_super_coin_volume_conf")
public class SuperCoinVolumeConf extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("name")
    private String name;
    @SqlField("coin_id")
    private String coinId;
    @SqlField("coin_symbol")
    private String coinSymbol;
    @SqlField("in_min_volume")
    private BigDecimal inMinVolume;
    @SqlField("out_min_volume")
    private BigDecimal outMinVolume;
    @SqlField("multiple")
    private BigDecimal multiple;
    @SqlField("lock_cycle")
    private Integer lockCycle;
    @SqlField("frozen_day")
    private Integer frozenDay;
    @SqlField("break_ratio")
    private BigDecimal breakRatio;
    @SqlField("destroy_user_id")
    private String destroyUserId;
    @SqlField("transfer_status")
    private String transferStatus;
    @SqlField("status")
    private String status;
    @SqlField("member_lock_multiple")
    private BigDecimal memberLockMultiple;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public BigDecimal getInMinVolume() {
        return inMinVolume;
    }

    public void setInMinVolume(BigDecimal inMinVolume) {
        this.inMinVolume = inMinVolume;
    }

    public BigDecimal getOutMinVolume() {
        return outMinVolume;
    }

    public void setOutMinVolume(BigDecimal outMinVolume) {
        this.outMinVolume = outMinVolume;
    }

    public BigDecimal getMultiple() {
        return multiple;
    }

    public void setMultiple(BigDecimal multiple) {
        this.multiple = multiple;
    }

    public Integer getLockCycle() {
        return lockCycle;
    }

    public void setLockCycle(Integer lockCycle) {
        this.lockCycle = lockCycle;
    }

    public Integer getFrozenDay() {
        return frozenDay;
    }

    public void setFrozenDay(Integer frozenDay) {
        this.frozenDay = frozenDay;
    }

    public BigDecimal getBreakRatio() {
        return breakRatio;
    }

    public void setBreakRatio(BigDecimal breakRatio) {
        this.breakRatio = breakRatio;
    }

    public String getDestroyUserId() {
        return destroyUserId;
    }

    public void setDestroyUserId(String destroyUserId) {
        this.destroyUserId = destroyUserId;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getMemberLockMultiple() {
        return memberLockMultiple;
    }

    public void setMemberLockMultiple(BigDecimal memberLockMultiple) {
        this.memberLockMultiple = memberLockMultiple;
    }
}
