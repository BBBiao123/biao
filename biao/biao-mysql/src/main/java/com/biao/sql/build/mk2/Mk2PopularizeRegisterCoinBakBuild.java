package com.biao.sql.build.mk2;

import com.biao.entity.mk2.Mk2PopularizeRegisterCoinBak;
import com.biao.sql.BaseSqlBuild;

public class Mk2PopularizeRegisterCoinBakBuild extends BaseSqlBuild<Mk2PopularizeRegisterCoinBak, Integer> {

//    public static final String columns = " id,register_conf_id,conf_name,user_id,user_name,mobile,mail,volume,coin_id,coin_symbol,for_user_id,status,create_date,update_date " ;

    public static final String columns = " id,register_conf_id,conf_name,user_id,user_name,mail,volume,coin_id,coin_symbol,for_user_id,status,create_date,update_date,success_status ";
}
