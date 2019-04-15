package com.biao.entity;

import com.biao.sql.PrimaryKey;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

import java.math.BigDecimal;

/**
 * 区域合伙人
 */
@SqlTable("mk2_popularize_area_member")
public class AreaSell {

    @PrimaryKey(insertIsSkip = false)
    @SqlField("id")
    protected String id;

    //@SqlField("area_id")
    private String areaId;//

    //@SqlField("area_name")
    private String areaName;//

    //@SqlField("area_paraent_id")
    private String areaParaentId;//

    //@SqlField("area_paraent_name")
    private String areaParaentName;//

    //@SqlField("lock_volume")
    private BigDecimal sellPrice;//

    private Long sellPriceLong;

    //@SqlField("status")
    private String sold;//

    private String center;// 中心坐标

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaParaentId() {
        return areaParaentId;
    }

    public void setAreaParaentId(String areaParaentId) {
        this.areaParaentId = areaParaentId;
    }

    public String getAreaParaentName() {
        return areaParaentName;
    }

    public void setAreaParaentName(String areaParaentName) {
        this.areaParaentName = areaParaentName;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Long getSellPriceLong() {
        return sellPriceLong;
    }

    public void setSellPriceLong(Long sellPriceLong) {
        this.sellPriceLong = sellPriceLong;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }
}
