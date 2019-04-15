package com.biao.sql.build.lucky;

import com.biao.entity.lucky.MkLuckyDrawRecord;
import com.biao.sql.BaseSqlBuild;

public class MkLuckyDrawRecordSqlBuild extends BaseSqlBuild<MkLuckyDrawRecord, Integer> {

    public static final String columns = "`id`, `status`, `periods`, `volume`, `grant_volume`, `pool_volume`, `player_number`, `lucky_volume`, `deduct_fee`, `coin_id`, `coin_symbol`, `user_id`, `mail`, `mobile`, `real_name`, `remark`, `create_date`, `update_date`, `create_by`, `update_by`";
}
