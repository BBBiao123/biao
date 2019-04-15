package com.biao.sql.build;

import com.biao.entity.AreaSell;
import com.biao.sql.BaseSqlBuild;

public class AreaSellSqlBuild extends BaseSqlBuild<AreaSell, Integer> {
    public static final String columns = " id,area_id,area_name,area_paraent_id,area_paraent_name,lock_volume,status ";
}
