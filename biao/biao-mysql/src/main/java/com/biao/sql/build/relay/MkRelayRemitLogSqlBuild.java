package com.biao.sql.build.relay;

import com.biao.entity.relay.MkRelayRemitLog;
import com.biao.sql.BaseSqlBuild;

public class MkRelayRemitLogSqlBuild extends BaseSqlBuild<MkRelayRemitLog, Integer> {

    public static final String columns = "`id`, `user_id`, `mail`, `mobile`, `real_name`, `coin_id`, `coin_symbol`, `volume`, `user_type`, `is_remit`, `referee_id`, `refer_mail`, `refer_mobile`, `refer_real_name`, `remark`, `create_date`, `update_date`, `create_by`, `update_by`";
}
