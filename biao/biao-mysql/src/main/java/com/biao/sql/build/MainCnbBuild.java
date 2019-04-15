package com.biao.sql.build;

import com.biao.entity.MainCnb;
import com.biao.sql.BaseSqlBuild;

public class MainCnbBuild extends BaseSqlBuild<MainCnb, Integer> {

    public static final String columns = " id,coin_id,coin_symbol,cnb_rate,create_date,update_date ";

}
