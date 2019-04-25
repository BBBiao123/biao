package com.biao.sql.build.balance;

import com.biao.entity.balance.BalanceUserCoinVolume;
import com.biao.entity.balance.BalanceUserCoinVolumeDetail;
import com.biao.sql.BaseSqlBuild;

public class BalanceUserCoinVolumeDetailSqlBuild extends BaseSqlBuild<BalanceUserCoinVolumeDetail, Integer> {

    public static final String columns = "id,user_id,income_date,coin_symbol,detail_income,detail_reward,create_by,update_by,create_date,update_date,version";

}
