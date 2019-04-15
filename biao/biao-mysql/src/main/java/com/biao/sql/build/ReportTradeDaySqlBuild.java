package com.biao.sql.build;

import com.biao.entity.ReportTradeDay;
import com.biao.sql.BaseSqlBuild;

public class ReportTradeDaySqlBuild extends BaseSqlBuild<ReportTradeDay, String> {

    public static final String columns = "coin_main,coin_other,latest_price,first_price,highest_price,lower_price,day_count,count_time,id,create_by,update_by,create_date,update_date";

}
