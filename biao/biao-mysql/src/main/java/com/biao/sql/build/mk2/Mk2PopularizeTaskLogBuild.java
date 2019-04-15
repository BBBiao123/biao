package com.biao.sql.build.mk2;

import com.biao.entity.mk2.Mk2PopularizeTaskLog;
import com.biao.sql.BaseSqlBuild;

public class Mk2PopularizeTaskLogBuild extends BaseSqlBuild<Mk2PopularizeTaskLog, Integer> {

    public static final String columns = " id,type,execute_time,type_name,day_give_colume,param_task_date,create_date,update_date,status,reason ";
}
