package com.biao.sql.build.otc;

import com.biao.entity.otc.OtcOfflineOrderPay;
import com.biao.sql.BaseSqlBuild;

public class OtcOfflineOrderPaySqlBuild extends BaseSqlBuild<OtcOfflineOrderPay, Integer> {

    public static final String columns = "id,order_id,type,user_id,real_name,mobile,pay_no,qrcode_id,bank_name,branch_bank_name,remarks,create_date,update_date,create_by,update_by ";

}
