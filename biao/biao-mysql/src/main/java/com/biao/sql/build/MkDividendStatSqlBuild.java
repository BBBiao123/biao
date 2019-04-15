package com.biao.sql.build;

import com.biao.entity.MkDividendStat;
import com.biao.sql.BaseSqlBuild;

public class MkDividendStatSqlBuild extends BaseSqlBuild<MkDividendStat, Integer> {

    public static final String columns = "id, stat_date, coin_id, coin_symbol, volume, usdt_volume, btc_volume, eth_volume, usdt_real_volume, btc_real_volume, eth_real_volume, usdt_per_volume, btc_per_volume, eth_per_volume, per, remark, create_date, update_date, create_by, update_by";
}
