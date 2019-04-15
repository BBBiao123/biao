package com.biao.sql.build.mk2;

import com.biao.entity.mk2.Mk2PopularizeRegisterConf;
import com.biao.sql.BaseSqlBuild;

public class Mk2PopularizeRegisterConfBuild extends BaseSqlBuild<Mk2PopularizeRegisterConf, Integer> {

    public static final String columns = " id,name,coin_id,coin_symbol,status,register_volume,refer_volume,total_volume,give_volume,create_by,create_date,update_date,remark ";

}
