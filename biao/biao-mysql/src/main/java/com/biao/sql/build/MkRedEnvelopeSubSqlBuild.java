package com.biao.sql.build;

import com.biao.entity.MkRedEnvelopeSub;
import com.biao.sql.BaseSqlBuild;

public class MkRedEnvelopeSubSqlBuild extends BaseSqlBuild<MkRedEnvelopeSub, Integer> {
    public static final String columns = "`id`, `envelope_id`, `receive_user_id`, `receive_mobile`, `receive_mail`, `receive_real_name`, `coin_id`, `coin_symbol`, `type`, `volume`, `status`, `send_user_id`, `send_mobile`, `send_mail`, `send_real_name`, `remark`, `create_date`, `update_date`, `create_by`, `update_by`, `version`";
}
