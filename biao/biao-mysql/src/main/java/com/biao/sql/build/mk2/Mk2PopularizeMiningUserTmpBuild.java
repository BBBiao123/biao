package com.biao.sql.build.mk2;

import com.biao.entity.mk2.Mk2PopularizeMiningUserTmp;
import com.biao.sql.BaseSqlBuild;

public class Mk2PopularizeMiningUserTmpBuild extends BaseSqlBuild<Mk2PopularizeMiningUserTmp, Integer> {

    public static final String columns = " id,type,user_id,sub_user_id,coin_id,coin_symbol,volume,sub_max_volume,order_no,count_date,create_date,team_hold_total,source_volume ";

}
