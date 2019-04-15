package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

@SqlTable("js_plat_card_status_config")
public class CardStatusConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @SqlField("limit_out")
    private String limitOut;

    @SqlField("label")
    private String label;

    @SqlField("value")
    private Integer value;

    public String getLimitOut() {
        return limitOut;
    }

    public void setLimitOut(String limitOut) {
        this.limitOut = limitOut;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }


}
