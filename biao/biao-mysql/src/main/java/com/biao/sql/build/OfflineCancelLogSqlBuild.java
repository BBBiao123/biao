package com.biao.sql.build;

import com.biao.entity.OfflineCancelLog;
import com.biao.sql.BaseSqlBuild;

public class OfflineCancelLogSqlBuild extends BaseSqlBuild<OfflineCancelLog, Integer> {

    public static final String columns = "id,user_id,type,date";

}
