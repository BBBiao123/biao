package com.biao.sql.build;

import com.biao.entity.ReportTradeFree;
import com.biao.sql.BaseSqlBuild;

public class ReportTradeFreeSqlBuild extends BaseSqlBuild<ReportTradeFree, String> {

    public static final String columns = "coin_main,coin_other,coin,sum_fee,count_time,create_time,id";

}
