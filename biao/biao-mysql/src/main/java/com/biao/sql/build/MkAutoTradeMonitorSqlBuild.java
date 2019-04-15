package com.biao.sql.build;

import com.biao.entity.MkAutoTradeMonitor;
import com.biao.sql.BaseSqlBuild;

public class MkAutoTradeMonitorSqlBuild extends BaseSqlBuild<MkAutoTradeMonitor, Integer> {

    public static final String columns = "`id`, `setting_id`, `type`, `status`, `user_id`, `mail`, `mobile`, `coin_main_symbol`, `coin_other_symbol`, `begin_date`, `end_date`, `min_volume`, `max_volume`, `min_price`, `max_price`, `frequency`, `time_unit`, `order_number`, `order_volume`, `remark`, `create_date`, `update_date`, `create_by`, `update_by`";
}
