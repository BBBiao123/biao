package com.biao.sql.build.balance;

import com.biao.entity.balance.BalancePlatCoinPriceVolume;
import com.biao.sql.BaseSqlBuild;

public class BalancePlatCoinPriceVolumeSqlBuild extends BaseSqlBuild<BalancePlatCoinPriceVolume, Integer> {

    public static final String columns = "id,coin_plat_symbol,create_date,price";

}
