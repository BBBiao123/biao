package com.biao.sql.build.mk2;

import com.biao.entity.mk2.Mk2PopularizeMiningConf;
import com.biao.sql.BaseSqlBuild;

public class Mk2PopularizeMiningConfBuild extends BaseSqlBuild<Mk2PopularizeMiningConf, Integer> {

    public static final String columns = " id,type,total_volume,grant_volume,show_grant_volume,delay_show_multiple,per_volume,greater_volume,leader_greater_volume,base_volume,income_hold_ratio,base_multiple,coin_id,coin_symbol,create_date,status,remark ";

}
