package com.biao.sql.build.balance;

import com.biao.entity.balance.BalanceUserCoinVolumeDetail;
import com.biao.entity.balance.BalanceUserVolumeIncomeDetail;
import com.biao.sql.BaseSqlBuild;

public class BalanceUserVolumeIncomeDetailSqlBuild extends BaseSqlBuild<BalanceUserVolumeIncomeDetail, Integer> {

    public static final String columns = "id,user_id,income_date,coin_symbol,detail_reward,reward_type,create_by,update_by,create_date,update_date,version";

}
