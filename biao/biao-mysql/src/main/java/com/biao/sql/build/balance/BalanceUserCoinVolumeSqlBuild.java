package com.biao.sql.build.balance;

import com.biao.entity.balance.BalanceUserCoinVolume;
import com.biao.sql.BaseSqlBuild;

public class BalanceUserCoinVolumeSqlBuild extends BaseSqlBuild<BalanceUserCoinVolume, Integer> {

    public static final String columns = "id,user_id,coin_balance,coin_symbol,day_rate,accumul_income,yesterday_income,accumul_reward,yesterday_reward,yesterday_statics_income,yesterday_equality_reward,yesterday_dynamics_income,yesterday_community_reward,refer_id,create_by,update_by,create_date,update_date";

}
