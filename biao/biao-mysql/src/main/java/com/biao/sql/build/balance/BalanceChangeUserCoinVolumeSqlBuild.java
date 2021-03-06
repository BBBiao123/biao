package com.biao.sql.build.balance;

import com.biao.entity.balance.BalanceChangeUserCoinVolume;
import com.biao.sql.BaseSqlBuild;

public class BalanceChangeUserCoinVolumeSqlBuild extends BaseSqlBuild<BalanceChangeUserCoinVolume, Integer> {

    public static final String columns = "id,user_id,coin_symbol,coin_plat_symbol,create_date,coin_num,accumul_income,flag,mobile,mail,take_out_date,balance_id,contract_time";

}
