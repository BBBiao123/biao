package com.biao.sql.build;

import com.biao.entity.OfflineChangeLog;
import com.biao.sql.BaseSqlBuild;

public class OfflineChangeLogSqlBuild extends BaseSqlBuild<OfflineChangeLog, Integer> {

    public static final String columns = "`id`, `change_no`, `status`, `user_id`, `account`, `real_name`, `coin_id`, `coin_symbol`, `volume`, `fee`, `type`, `other_user_id`, `other_account`, `other_real_name`, `create_date`, `update_date`, `create_by`, `update_by`";

}
