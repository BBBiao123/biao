package com.biao.sql.build;


import com.biao.entity.ExPair;
import com.biao.sql.BaseSqlBuild;

public class ExPairSqlBuild extends BaseSqlBuild<ExPair, Integer> {

    public static final String columns = "id,other_coin_id," +
            "pair_other,coin_id,pair_one,`status`,last_price," +
            "`change`,high,low,volume,type,create_by,update_by,create_date," +
            "update_date," +
            "remarks,sort,fee,max_volume,min_volume,price_precision,volume_precision,volume_percent";

}
