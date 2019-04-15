package com.biao.sql.build.otc;

import com.biao.entity.otc.OtcOfflineCoin;
import com.biao.sql.BaseSqlBuild;

public class OtcOfflineCoinSqlBuild extends BaseSqlBuild<OtcOfflineCoin, Integer> {
    public static final String columns = "id,publish_source,coin_id,symbol,support_currency_code," +
            "max_price,min_price,point_price,max_volume,min_volume,bail_volume,buy_fee,sell_fee,fee_type,buy_fee_step,sell_fee_step,point_volume,day_inc_price";
}
