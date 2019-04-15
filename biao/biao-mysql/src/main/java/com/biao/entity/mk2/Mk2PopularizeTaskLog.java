package com.biao.entity.mk2;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;

@SqlTable("mk2_popularize_task_log")
public class Mk2PopularizeTaskLog {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    @SqlField("type")
    private String type;

    @SqlField("type_name")
    private String typeName;

    @SqlField("param_task_date")
    private String paramTaskDate;

    @SqlField("day_give_colume")
    private Long dayGiveColume;

    @SqlField("status")
    private String status;

    @SqlField("reason")
    private String reason;

    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createDate;

    @SqlField("execute_time")
    private LocalDateTime executeTime;

    @SqlField("update_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getParamTaskDate() {
        return paramTaskDate;
    }

    public void setParamTaskDate(String paramTaskDate) {
        this.paramTaskDate = paramTaskDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public Long getDayGiveColume() {
        return dayGiveColume;
    }

    public void setDayGiveColume(Long dayGiveColume) {
        this.dayGiveColume = dayGiveColume;
    }

    public LocalDateTime getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(LocalDateTime executeTime) {
        this.executeTime = executeTime;
    }
}
