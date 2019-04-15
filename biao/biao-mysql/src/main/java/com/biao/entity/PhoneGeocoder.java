package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import lombok.Data;

@Data
@SqlTable("js_plat_phone_geocoder")
public class PhoneGeocoder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public static final String columns = "id,user_parents,user_parent,city_name,city_id,province_id,province_name,name,create_by,update_by,create_date,update_date";

    @SqlField("user_id")
    private String userId;

    //用户的所有上级
    @SqlField("user_parents")
    private String userParents;

    //用户的直属上级
    @SqlField("user_parent")
    private String userParent;

    @SqlField("city_name")
    private String cityName;

    @SqlField("city_id")
    private String cityId;

    @SqlField("province_id")
    private String provinceId;

    @SqlField("province_name")
    private String provinceName;

    @SqlField("name")
    private String name;

    private String referId;
}
