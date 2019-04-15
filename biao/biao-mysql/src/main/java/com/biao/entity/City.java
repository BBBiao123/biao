package com.biao.entity;

import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.io.Serializable;

/**
 * 城市实体类
 * 测试模块
 * Created by bysocket on 07/02/2017.
 */
@SqlTable("city")
public class City implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 城市编号
     */
    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    private String id;

    /**
     * 省份编号
     */
    @SqlField("province_id")
    private Long provinceId;

    /**
     * 城市名称
     */
    @SqlField("city_name")
    private String cityName;

    /**
     * 描述
     */
    @SqlField("description")
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", provinceId=" + provinceId +
                ", cityName='" + cityName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
