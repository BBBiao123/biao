package com.biao.sql.build;

import com.biao.entity.SuperCoinVolume;
import com.biao.sql.BaseSqlBuild;

public class SuperCoinVolumeSqlBuild extends BaseSqlBuild<SuperCoinVolume, Integer> {

    public static final String columns = "`id`, `user_id`, `coin_id`, `coin_symbol`, `volume`, `deposit_begin`, `deposit_end`, `create_date`, `update_date`, `create_by`, `update_by`, `version`";

}
