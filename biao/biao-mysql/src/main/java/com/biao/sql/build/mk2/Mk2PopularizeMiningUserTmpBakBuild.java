package com.biao.sql.build.mk2;

import com.biao.entity.mk2.Mk2PopularizeMiningUserTmpBak;
import com.biao.sql.BaseSqlBuild;

public class Mk2PopularizeMiningUserTmpBakBuild extends BaseSqlBuild<Mk2PopularizeMiningUserTmpBak, Integer> {

    public static final String columns = " id,type,user_id,sub_user_id,coin_id,coin_symbol,volume,sub_max_volume,order_no,count_date,create_date ";

}
