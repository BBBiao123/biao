package com.biao.sql.build.otc;

import com.biao.entity.otc.OtcOfflineOrderDetailPay;
import com.biao.sql.BaseSqlBuild;

public class OtcOfflineOrderDetailPaySqlBuild extends BaseSqlBuild<OtcOfflineOrderDetailPay, Integer> {

    public static final String columns = "id,sub_order_id,detail_id,support_currency_code,order_id,type,user_id,real_name,mobile,pay_no,qrcode_id,bank_name,branch_bank_name,remarks,create_date,update_date,create_by,update_by ";
}
