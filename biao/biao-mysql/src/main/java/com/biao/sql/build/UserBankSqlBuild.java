package com.biao.sql.build;

import com.biao.entity.UserBank;
import com.biao.sql.BaseSqlBuild;

public class UserBankSqlBuild extends BaseSqlBuild<UserBank, Integer> {

    public static final String columns = "id,user_id,bank_name,branch_bank_name,status,mobile,real_name,card_no,create_by,update_by,create_date,update_date";

}
