package com.biao.sql.build.relay;

import com.biao.entity.relay.MkRelayAutoRecord;
import com.biao.sql.BaseSqlBuild;

public class MkRelayAutoRecordSqlBuild extends BaseSqlBuild<MkRelayAutoRecord, Integer> {

    public static final String columns = "`id`, `status`, `begin_date`, `end_date`, `user_id`, `mail`, `mobile`, `real_name`, `volume`, `coin_id`, `coin_symbol`, `reach_date`, `candidate_id`, `remark`, `create_date`, `update_date`, `create_by`, `update_by`";
}
