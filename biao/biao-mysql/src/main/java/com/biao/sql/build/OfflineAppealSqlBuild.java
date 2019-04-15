package com.biao.sql.build;

import com.biao.entity.OfflineAppeal;
import com.biao.sql.BaseSqlBuild;

public class OfflineAppealSqlBuild extends BaseSqlBuild<OfflineAppeal, Integer> {

    public static final String columns = "id, appeal_user_id, sub_order_id, sell_user_id, buy_user_id, examine_user_id, status, appeal_type, reason, image_path, image_path2, image_path3, create_date, examine_date, update_date";
}
