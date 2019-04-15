package com.biao.sql.build.mk2;

import com.biao.entity.mk2.Mk2PopularizeMiningTaskLog;
import com.biao.sql.BaseSqlBuild;

public class Mk2PopularizeMiningTaskLogBuild extends BaseSqlBuild<Mk2PopularizeMiningTaskLog, Integer> {

    public static final String columns = " id,type,coin_id,coin_symbol,mining_volume,grant_volume,count_date,status,create_date,update_date,remark ";

}
