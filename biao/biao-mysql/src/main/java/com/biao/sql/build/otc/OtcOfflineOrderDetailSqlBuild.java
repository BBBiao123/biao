package com.biao.sql.build.otc;

import com.biao.entity.otc.OtcOfflineOrderDetail;
import com.biao.sql.BaseSqlBuild;

public class OtcOfflineOrderDetailSqlBuild extends BaseSqlBuild<OtcOfflineOrderDetail, Integer> {

    public static final String columns = "id,order_id,order_user_id,sub_order_id,support_currency_code,publish_source,volume,fee_volume,coin_id,symbol,price,total_price," +
            "user_mobile,real_name,user_id,ex_type,ask_user_mobile,ask_real_name,ask_user_id,remarks,status,radom_num,create_date,update_date,confirm_receipt_date,confirm_payment_date,create_by,update_by ";

}
