package com.biao.sql.build;

import com.biao.entity.MkAutoTradeSetting;
import com.biao.sql.BaseSqlBuild;

public class MkAutoTradeSettingSqlBuild extends BaseSqlBuild<MkAutoTradeSetting, Integer> {

    public static final String columns = "`id`, `type`, `status`, `user_id`, `username`, `pass`,`mail`, `mobile`, `coin_main_id`, `coin_main_symbol`, `coin_other_id`, `coin_other_symbol`, `begin_date`, `end_date`, `min_volume`, `max_volume`, `min_price`, `max_price`, `price_precision`, `volume_precision`,`frequency`, `time_unit`, `remark`, `create_date`, `update_date`, `create_by`, `create_by_name`, `update_by_name`";
}
