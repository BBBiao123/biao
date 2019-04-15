package com.biao.sql.build;

import com.biao.entity.SuperCoinVolumeConf;
import com.biao.sql.BaseSqlBuild;

public class SuperCoinVolumeConfSqlBuild extends BaseSqlBuild<SuperCoinVolumeConf, Integer> {
    public static final String columns = "id, name, coin_id, coin_symbol, in_min_volume, out_min_volume, multiple, lock_cycle, frozen_day, break_ratio, destroy_user_id, transfer_status, status, member_lock_multiple, create_date, update_date, create_by, update_by ";
}
