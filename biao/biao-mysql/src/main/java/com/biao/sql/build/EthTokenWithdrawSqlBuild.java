package com.biao.sql.build;

import com.biao.entity.EthTokenVolume;
import com.biao.sql.BaseSqlBuild;

public class EthTokenWithdrawSqlBuild extends BaseSqlBuild<EthTokenVolume, Integer> {

    public static final String columns = "id,coin_symbol,volume,from_address,to_address,status,tx_id";

}
