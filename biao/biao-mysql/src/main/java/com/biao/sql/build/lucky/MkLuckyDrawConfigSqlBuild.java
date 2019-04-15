package com.biao.sql.build.lucky;

import com.biao.entity.lucky.MkLuckyDrawConfig;
import com.biao.sql.BaseSqlBuild;

public class MkLuckyDrawConfigSqlBuild extends BaseSqlBuild<MkLuckyDrawConfig, Integer> {

    public static final String columns = "`id`, `name`, `periods`, `status`, `volume`, `start_volume`, `step_add_volume`, `coin_id`, `coin_symbol`, `grant_volume`, `pool_volume`, `min_volume`, `deduct_fee`, `player_number`, `fee`, `remark`, `create_date`, `update_date`, `create_by`, `update_by`";
}
