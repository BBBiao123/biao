package com.biao.sql.build;

import com.biao.entity.MkAutoTradeOrder;
import com.biao.sql.BaseSqlBuild;

public class MkAutoTradeOrderSqlBuild extends BaseSqlBuild<MkAutoTradeOrder, Integer> {

    public static final String columns = "`id`, `setting_id`, `monitor_id`, `user_id`, `mail`, `mobile`, `type`, `status`, `coin_main_symbol`, `coin_other_symbol`, `price`, `volume`, `create_date`, `update_date`, `create_by`, `update_by`";
}
