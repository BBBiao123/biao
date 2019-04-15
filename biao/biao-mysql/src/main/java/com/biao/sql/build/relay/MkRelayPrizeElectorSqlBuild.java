package com.biao.sql.build.relay;

import com.biao.entity.relay.MkRelayPrizeElector;
import com.biao.sql.BaseSqlBuild;

public class MkRelayPrizeElectorSqlBuild extends BaseSqlBuild<MkRelayPrizeElector, Integer> {

    public static final String columns = "`id`, `user_id`, `mail`, `mobile`, `real_name`, `refer_id`, `refer_mail`, `refer_mobile`, `refer_real_name`, `coin_id`, `coin_symbol`, `volume`, `reach_date`, `begin_date`, `end_date`, `remark`, `create_date`, `update_date`, `create_by`, `update_by`";
}
