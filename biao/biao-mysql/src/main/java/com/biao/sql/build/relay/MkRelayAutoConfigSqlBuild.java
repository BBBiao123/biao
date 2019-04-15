package com.biao.sql.build.relay;

import com.biao.entity.relay.MkRelayAutoConfig;
import com.biao.sql.BaseSqlBuild;

public class MkRelayAutoConfigSqlBuild extends BaseSqlBuild<MkRelayAutoConfig, Integer> {

    public static final String columns = "`id`, `status`, `remark`, `start_reward_number`, `create_date`, `update_date`, `create_by`, `update_by`";
}
