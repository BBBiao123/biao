package com.biao.sql.build;

import com.biao.entity.TraderVolumeSnapshot;
import com.biao.sql.BaseSqlBuild;

public class TraderVolumeSnapshotSqlBuild extends BaseSqlBuild<TraderVolumeSnapshot, Integer> {

    public static final String columns = "`id`, `snap_date`, `user_id`, `user_tag`, `coin_id`, `coin_symbol`, `trade_volume`, `offline_volume`, `lock_volume`, `total_volume`, `bill_sum_volume`, `balance`, `bobi_volume`, `remark`, `create_date`, `create_by`";

}
