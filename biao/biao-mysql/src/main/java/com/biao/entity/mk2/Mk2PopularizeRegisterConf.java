/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.biao.entity.mk2;

import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;

/**
 * 注册送币规则Entity
 *
 *  ""ongfeng
 * @version 2018-07-19
 */
@SqlTable("mk2_popularize_register_coin")
public class Mk2PopularizeRegisterConf extends BaseEntity {

    @SqlField("name")
    private String name;        // 推广名称

    @SqlField("coin_id")
    private String coinId;        // 币ID

    @SqlField("coin_symbol")
    private String coinSymbol;        // 币名称

    @SqlField("status")
    private String status;        // 状态

    @SqlField("register_volume")
    private Integer registerVolume;        // 注册送币个数

    @SqlField("refer_volume")
    private Integer referVolume;        // 推荐送币个数

    @SqlField("total_volume")
    private Long totalVolume;        // 总送币量

    @SqlField("give_volume")
    private Long giveVolume;        // 已送币量

    @SqlField("remark")
    private String remark;        // 描述

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRegisterVolume() {
        return registerVolume;
    }

    public void setRegisterVolume(Integer registerVolume) {
        this.registerVolume = registerVolume;
    }

    public Integer getReferVolume() {
        return referVolume;
    }

    public void setReferVolume(Integer referVolume) {
        this.referVolume = referVolume;
    }

    public Long getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Long totalVolume) {
        this.totalVolume = totalVolume;
    }

    public Long getGiveVolume() {
        return giveVolume;
    }

    public void setGiveVolume(Long giveVolume) {
        this.giveVolume = giveVolume;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}