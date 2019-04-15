package com.biao.sql.build;

import com.biao.entity.PlatUserCoinCount;
import com.biao.sql.BaseSqlBuild;

public class PlatUserCoinCountSqlBuild extends BaseSqlBuild<PlatUserCoinCount, Integer> {

    public static final String columns = " id,coin_symbol,type,type_desc,person_count,hold_coin_volume,count_date,create_date,update_date ";

}
