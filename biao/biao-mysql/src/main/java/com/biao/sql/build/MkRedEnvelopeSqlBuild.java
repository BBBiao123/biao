package com.biao.sql.build;

import com.biao.entity.MkRedEnvelope;
import com.biao.entity.SuperCoinVolume;
import com.biao.sql.BaseSqlBuild;

public class MkRedEnvelopeSqlBuild extends BaseSqlBuild<MkRedEnvelope, Integer> {
    public static final String columns = "`id`, `user_id`, `mobile`, `mail`, `real_name`, `coin_id`, `coin_symbol`, `type`, `volume`, `total_number`, `receive_number`, `receive_volume`,`status`, `best_with`, `remark`, `create_date`, `update_date`, `create_by`, `update_by`, `version`";
}
