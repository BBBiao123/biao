package com.biao.sql.build;

import com.biao.entity.PlatNeoUserError;
import com.biao.sql.BaseSqlBuild;

public class PlatNeoUserErrorSqlBuild extends BaseSqlBuild<PlatNeoUserError, Integer> {

    public static final String columns = " id,user_id,refer_id,status,create_date,update_date ";

}
