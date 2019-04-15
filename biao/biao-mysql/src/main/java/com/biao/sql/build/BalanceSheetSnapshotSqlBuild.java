package com.biao.sql.build;

import com.biao.entity.BalanceSheetSnapshot;
import com.biao.sql.BaseSqlBuild;

public class BalanceSheetSnapshotSqlBuild extends BaseSqlBuild<BalanceSheetSnapshot, Integer> {

    public static final String columns = "`id`, `snap_date`, `coin_symbol`, `total_volume`, `income`, `trade_fee`, `deposit_volume`, `offline_fee`, `deduct_volume`, `withdraw_fee`, `expense`, `withdraw_volume`, `withdraw_block_fee`, `deposit_allocation_fee`, `remit_volume`, `mining_volume`, `register_volume`, `relay_volume`, `remark`, `create_date`, `create_by`";

}
