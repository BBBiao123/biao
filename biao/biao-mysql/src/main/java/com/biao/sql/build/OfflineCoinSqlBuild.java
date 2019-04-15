package com.biao.sql.build;

import com.biao.entity.OfflineCoin;
import com.biao.sql.BaseSqlBuild;

public class OfflineCoinSqlBuild extends BaseSqlBuild<OfflineCoin, Integer> {
    public static final String columns = "id,coin_id,symbol," +
            "max_price,min_price,point_price,max_volume,min_volume,bail_volume,buy_fee,sell_fee,fee_type,buy_fee_step,sell_fee_step,point_volume,day_inc_price,is_change_account,real_day_limit,non_real_day_limit,change_fee_type,change_fee_step,change_fee,change_min_volume,disable,is_volume";
}
