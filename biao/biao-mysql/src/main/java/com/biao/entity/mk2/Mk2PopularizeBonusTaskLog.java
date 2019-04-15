package com.biao.entity.mk2;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 分红Task运行记录
 *
 *  ""ongfeng
 * @version 2018-07-19
 */
@SqlTable("mk2_popularize_bonus_task_log")
public class Mk2PopularizeBonusTaskLog {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    @SqlField("status")
    private String status;        //

    @SqlField("bonus_volume")
    private BigDecimal bonusVolume;        //

    @SqlField("coin_id")
    private String coinId;        //

    @SqlField("coin_symbol")
    private String coinSymbol;        //

    @SqlField("begin_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime beginDate;        //

    @SqlField("end_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime endDate;        //

    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createDate;        //

    @SqlField("remark")
    private String remark;        //

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getBonusVolume() {
        return bonusVolume;
    }

    public void setBonusVolume(BigDecimal bonusVolume) {
        this.bonusVolume = bonusVolume;
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

    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDateTime beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
