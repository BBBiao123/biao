package com.biao.sql.build;

import com.biao.entity.Coin;
import com.biao.sql.BaseSqlBuild;

public class CoinSqlBuild extends BaseSqlBuild<Coin, Integer> {

    public static final String columns = "id,parent_id,name,full_name,domain,whitepaper_url,status,token_status,token_volume,withdraw_max_volume,withdraw_min_volume,withdraw_day_max_volume,withdraw_fee_type,withdraw_fee,ico_price,remarks,coin_type,circulate_volume";

}
