package com.biao.sql.build.otc;

import com.biao.entity.otc.OtcExchangeOrder;
import com.biao.sql.BaseSqlBuild;

public class OtcExchangeOrderSqlBuild extends BaseSqlBuild<OtcExchangeOrder, Integer> {

    public static final String columns = "id, publish_source, batch_no,symbol,coin_id,volume,real_volume,user_id,real_name,mobile,mail,fee_volume,ask_user_mobile,ask_user_mail,ask_real_name,ask_user_id,ask_fee_volume,remarks,status,result,create_date,update_date,create_by,update_by ";
}
