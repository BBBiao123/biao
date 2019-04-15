package com.biao.entity;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 超级账本配置表
 */
@SqlTable("js_plat_super_book_conf")
public class SuperBookConf implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    @SqlField("address")
    private String address;

    @SqlField("symbol")
    private String symbol;

    @SqlField("status")
    private String status;

    @SqlField("area_height")
    private Long areaHeight;

    @SqlField("create_date")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    protected LocalDateTime createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public Long getAreaHeight() {
        return areaHeight;
    }

    public void setAreaHeight(Long areaHeight) {
        this.areaHeight = areaHeight;
    }
}
