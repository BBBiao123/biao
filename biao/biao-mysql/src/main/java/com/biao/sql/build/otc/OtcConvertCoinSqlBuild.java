package com.biao.sql.build.otc;

import com.biao.entity.otc.OtcConvertCoin;
import com.biao.sql.BaseSqlBuild;

public class OtcConvertCoinSqlBuild extends BaseSqlBuild<OtcConvertCoin, Integer> {

    public static final String columns = "id,user_id,batch_no,from_coin_id,from_volume,to_coin_id,to_volume,rate,fee_volume,remarks,publish_source,status,result,create_date,update_date,create_by,update_by ";
}