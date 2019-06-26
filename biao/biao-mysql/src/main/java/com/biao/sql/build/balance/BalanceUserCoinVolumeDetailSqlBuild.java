package com.biao.sql.build.balance;

import com.biao.entity.balance.BalanceUserCoinVolume;
import com.biao.entity.balance.BalanceUserCoinVolumeDetail;
import com.biao.sql.BaseSqlBuild;

public class BalanceUserCoinVolumeDetailSqlBuild extends BaseSqlBuild<BalanceUserCoinVolumeDetail, Integer> {

    public static final String columns = "id,user_id,refer_id,income_date,coin_symbol,detail_income,detail_reward,statics_income,team_record,team_community_record,team_level,community_statics_income,node_number,reality_statics_income,community_manage_reward,equality_reward,dynamics_income,sum_revenue,valid_num,community_sum_manage_reward,team_coin_record,level_difference_reward,one_level_income,create_by,update_by,create_date,update_date,version";

}
