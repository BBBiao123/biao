package com.biao.sql.build;

import com.biao.entity.ReportTradeFreeRecord;
import com.biao.sql.BaseSqlBuild;

public class ReportTradeFreeRecordSqlBuild extends BaseSqlBuild<ReportTradeFreeRecord, String> {

    public static final String columns = "coin_main,coin_other,main_free,other_free,count_time,create_time,id";

}
