package com.biao.sql.build.relay;

import com.biao.entity.relay.MkRelayPrizeCandidate;
import com.biao.sql.BaseSqlBuild;

public class MkRelayPrizeCandidateSqlBuild extends BaseSqlBuild<MkRelayPrizeCandidate, Integer> {

    public static final String columns = "`id`, `elector_id`, `status`, `user_id`, `mail`, `mobile`, `real_name`, `refer_id`, `refer_mail`, `refer_mobile`, `refer_real_name`, `coin_id`, `coin_symbol`, `volume`, `achieve_date`, `is_prize`, `prize_volume`, `lost_time`, `begin_date`, `end_date`, `remark`, `create_date`, `update_date`, `create_by`, `update_by`";
}
