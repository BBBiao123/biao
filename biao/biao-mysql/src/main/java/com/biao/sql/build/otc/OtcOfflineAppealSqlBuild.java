package com.biao.sql.build.otc;

import com.biao.entity.otc.OtcOfflineAppeal;
import com.biao.sql.BaseSqlBuild;

public class OtcOfflineAppealSqlBuild extends BaseSqlBuild<OtcOfflineAppeal, Integer> {

    public static final String columns = "id, publish_source, appeal_user_id, sub_order_id, sell_user_id, buy_user_id, examine_user_id, status, appeal_type, reason, create_date, examine_date, update_date";
}
