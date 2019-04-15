package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

/**
 * coin address
 */
@SqlTable("address_config")
public class AddressConfig extends BaseEntity {


    @SqlField("name")
    private String name;

    @SqlField("status")
    private Integer status;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
