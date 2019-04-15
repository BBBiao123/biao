package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@SqlTable("js_plat_offline_coin_volume_day")
@Data
public class OfflineCoinVolumeDay implements Serializable {

    private static final long serialVersionUID = 1L;

    @SqlField("user_id")
    private String userId;
    @SqlField("mobile")
    private String mobile;
    @SqlField("mail")
    private String mail;
    @SqlField("tag")
    private String tag;
    @SqlField("count_day")
    private LocalDateTime countDay;
    @SqlField("buy_total")
    private String buyTotal;        // 买入统计值
    @SqlField("sell_total")
    private String sellTotal;        // 卖出统计值
    @SqlField("surplus_total")
    private String surplusTotal;        // 结余统计值


}
