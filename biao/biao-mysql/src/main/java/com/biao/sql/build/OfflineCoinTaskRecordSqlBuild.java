package com.biao.sql.build;

import com.biao.entity.OfflineCoinTaskRecord;
import com.biao.sql.BaseSqlBuild;

public class OfflineCoinTaskRecordSqlBuild extends BaseSqlBuild<OfflineCoinTaskRecord, Integer> {

    public static final String columns = "`id`, `status`, `symbol`, `coin_id`, `before_max_price`, `before_min_price`, `day_inc_price`, `remark`, `create_date`, `update_date`, `create_by`, `update_by`";
}
