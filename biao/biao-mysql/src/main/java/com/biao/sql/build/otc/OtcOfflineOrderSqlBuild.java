package com.biao.sql.build.otc;

import com.biao.entity.otc.OtcOfflineOrder;
import com.biao.sql.BaseSqlBuild;

public class OtcOfflineOrderSqlBuild extends BaseSqlBuild<OtcOfflineOrder, Integer> {

    public static final String columns = "id,support_pay,support_currency_code,publish_source,user_id,tag,real_name," +
            "mobile,coin_id,symbol,volume,lock_volume,success_volume,price,total_price,min_volume,max_volume,fee_volume,status,flag,ex_type,remarks,create_date,update_date,create_by,update_by ";

}
