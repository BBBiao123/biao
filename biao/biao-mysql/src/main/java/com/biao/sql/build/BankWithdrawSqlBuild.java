package com.biao.sql.build;

import com.biao.entity.BankWithdrawLog;
import com.biao.sql.BaseSqlBuild;

public class BankWithdrawSqlBuild extends BaseSqlBuild<BankWithdrawLog, Integer> {

    public static final String columns = "id,user_id,bank_name,real_name,card_no,status,volume,create_by,update_by,create_date,update_date";

}
