package com.biao.sql.build;

import com.biao.entity.MkDistributeLog;
import com.biao.sql.BaseSqlBuild;

public class MkDistributeLogSqlBuild extends BaseSqlBuild<MkDistributeLog, Integer> {
    public static final String columns = "id, type, user_id, coin_id, coin_symbol, volume, create_date, update_date";
}
