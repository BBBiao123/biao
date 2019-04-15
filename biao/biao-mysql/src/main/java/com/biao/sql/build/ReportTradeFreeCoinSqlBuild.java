package com.biao.sql.build;

import com.biao.entity.ReportTradeFreeCoin;
import com.biao.sql.BaseSqlBuild;

public class ReportTradeFreeCoinSqlBuild extends BaseSqlBuild<ReportTradeFreeCoin, String> {

    public static final String columns = "coin,sum_fee,count_time,create_time,id";

}
