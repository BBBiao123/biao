package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import lombok.Data;

import java.math.BigDecimal;

@SqlTable("sys_area")
@Data
public class SysArea extends BaseEntity {

    public static final String columns = "id,name,code,city_code,del_flag";

    private static final long serialVersionUID = 1L;

    @SqlField("parent_id")
    private String parentId;

    @SqlField("parent_ids")
    private String parentIds;

    @SqlField("name")
    private String name;

    @SqlField("sort")
    private BigDecimal sort;

    @SqlField("code")
    private String code;

    @SqlField("city_code")
    private String cityCode;

    @SqlField("center")
    private String center;

    @SqlField("type")
    private Integer type;

    @SqlField("remarks")
    private String remarks;

    @SqlField("del_flag")
    private Integer delFlag;

}
