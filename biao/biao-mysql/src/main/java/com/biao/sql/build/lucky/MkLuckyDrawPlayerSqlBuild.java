package com.biao.sql.build.lucky;

import com.biao.entity.lucky.MkLuckyDrawPlayer;
import com.biao.sql.BaseSqlBuild;

public class MkLuckyDrawPlayerSqlBuild extends BaseSqlBuild<MkLuckyDrawPlayer, Integer> {

    public static final String columns = "`id`, `periods`, `status`, `user_id`, `mail`, `mobile`, `real_name`, `coin_id`, `coin_symbol`, `volume`, `deduct_fee`, `lucky_volume`, `remark`, `draw_date`, `create_date`, `update_date`, `create_by`, `update_by`";
}
