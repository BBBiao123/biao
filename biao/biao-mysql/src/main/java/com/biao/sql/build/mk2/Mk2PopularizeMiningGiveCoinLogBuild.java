package com.biao.sql.build.mk2;

import com.biao.entity.mk2.Mk2PopularizeMiningGiveCoinLog;
import com.biao.sql.BaseSqlBuild;

public class Mk2PopularizeMiningGiveCoinLogBuild extends BaseSqlBuild<Mk2PopularizeMiningGiveCoinLog, Integer> {

    public static final String columns = " id,type,user_id,mail,mobile,id_card,real_name,coin_id,coin_symbol,volume,total_volume," +
            "ratio,order_no,join_volume,max_sub_volume,count_date,create_date,in_address,out_address,area_height,tx_hash,status ";

}
