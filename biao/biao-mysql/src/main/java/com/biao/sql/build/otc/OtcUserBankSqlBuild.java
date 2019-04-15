package com.biao.sql.build.otc;

import com.biao.entity.otc.OtcUserBank;
import com.biao.sql.BaseSqlBuild;

public class OtcUserBankSqlBuild extends BaseSqlBuild<OtcUserBank, Integer> {

    public static final String columns = "id,type,pay_no,qrcode_id,bank_name,branch_bank_name,country,status,mobile,real_name,card_no,support_currency_code,user_id,create_date,update_date,create_by,update_by ";

}
