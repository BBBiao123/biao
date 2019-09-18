package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SqlTable("sys_dict")
public class Sysdict extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @SqlField("value")
    private String value;

    @SqlField("label")
    private String label;

    @SqlField("type")
    private String type;

    @SqlField("sort")
    private BigDecimal sort;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getSort() {
        return sort;
    }

    public void setSort(BigDecimal sort) {
        this.sort = sort;
    }
}
