package com.biao.sql.build.otc;

import com.biao.entity.otc.OtcConvertCoinConf;
import com.biao.sql.BaseSqlBuild;

public class OtcConvertCoinConfSqlBuild extends BaseSqlBuild<OtcConvertCoinConf, Integer> {

    public static final String columns = "id,from_symbol,to_symbol,status,create_date,update_date,create_by,update_by ";
}
