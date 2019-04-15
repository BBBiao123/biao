package com.biao.sql.build;

import com.biao.entity.MkCommonTaskRecord;
import com.biao.sql.BaseSqlBuild;

public class MkCommonTaskRecordSqlBuild extends BaseSqlBuild<MkCommonTaskRecord, Integer> {

    public static final String columns = "id,type,task_date,execute_time,coin_id,coin_symbol,volume,status,remark,create_by,update_by,create_date,update_date";
}
