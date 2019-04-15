package com.biao.sql.build;

import com.biao.entity.CoinAddress;
import com.biao.sql.BaseSqlBuild;

public class CoinAddressSqlBuild extends BaseSqlBuild<CoinAddress, Integer> {

    public static final String columns = "id,user_id,coin_id,symbol,status,type,address,create_date,update_date";


}
