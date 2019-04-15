package com.biao.sql.build.relay;

import com.biao.entity.relay.MkRelayTaskRecord;
import com.biao.sql.BaseSqlBuild;

public class MkRelayTaskRecordSqlBuild extends BaseSqlBuild<MkRelayTaskRecord, Integer> {

    public static final String columns = "`id`, `type`, `status`, `begin_date`, `end_date`, `increase_number`, `increase_volume`, `pool_volume`, `prize_number`, `coin_id`, `coin_symbol`, `remark`, `create_date`, `update_date`, `create_by`, `update_by`";
}
