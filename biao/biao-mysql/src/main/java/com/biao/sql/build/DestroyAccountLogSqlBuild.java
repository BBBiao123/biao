package com.biao.sql.build;

import com.biao.entity.DestroyAccountLog;
import com.biao.sql.BaseSqlBuild;

public class DestroyAccountLogSqlBuild extends BaseSqlBuild<DestroyAccountLog, Integer> {

    public static final String columns = " id,user_id,coin_id,coin_symbol,mobile,mail,volume,type,create_date,update_date,create_by,update_by ";

}