package com.biao.sql.build.kline;

import com.biao.entity.kline.KlinePullConfig;
import com.biao.sql.BaseSqlBuild;

/**
 *  ""
 */
public class KlinePullConfigSqlBuild extends BaseSqlBuild<KlinePullConfig, String> {

    public static final String columns = " id,coin_main,coin_other,exchange_name,pull_url," +
            "proxyed,status";


}
