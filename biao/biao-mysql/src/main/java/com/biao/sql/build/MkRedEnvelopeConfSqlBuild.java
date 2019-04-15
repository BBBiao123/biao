package com.biao.sql.build;

import com.biao.entity.MkRedEnvelopeConf;
import com.biao.sql.BaseSqlBuild;

public class MkRedEnvelopeConfSqlBuild extends BaseSqlBuild<MkRedEnvelopeConf, Integer> {
    public static final String columns = "`id`, `name`, `coin_id`, `coin_symbol`, `single_lower_volume`, `single_higher_volume`, `lower_number`, `higher_number`, `fee`, `destroy_user_id`, `lucky_lower_volume`, `lucky_higher_volume`, `point_volume`, `status`, `remark`, `create_date`, `update_date`, `create_by`, `update_by`, `version`";
}
