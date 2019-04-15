package com.biao.sql.build.relay;

import com.biao.entity.relay.MkRelayPrizeConfig;
import com.biao.sql.BaseSqlBuild;

public class MkRelayPrizeConfigSqlBuild extends BaseSqlBuild<MkRelayPrizeConfig, Integer> {

    public static final String columns = "`id`, `name`, `status`, `volume`, `start_volume`, `step_add_volume`, `begin_time`, `end_time`, `coin_id`, `coin_symbol`, `user_id`, `mail`, `mobile`, `real_name`, `is_remit`, `min_volume`, `grant_volume`, `cur_pool_volume`, `remark`, `create_date`, `update_date`, `create_by`, `update_by`";
}
