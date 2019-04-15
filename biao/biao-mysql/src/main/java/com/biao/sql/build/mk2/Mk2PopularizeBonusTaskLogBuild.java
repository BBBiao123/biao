package com.biao.sql.build.mk2;

import com.biao.entity.mk2.Mk2PopularizeBonusTaskLog;
import com.biao.sql.BaseSqlBuild;

public class Mk2PopularizeBonusTaskLogBuild extends BaseSqlBuild<Mk2PopularizeBonusTaskLog, Integer> {

    public static final String columns = " id,status,bonus_volume,coin_id,coin_symbol,begin_date,end_date,create_date,remark ";
}
