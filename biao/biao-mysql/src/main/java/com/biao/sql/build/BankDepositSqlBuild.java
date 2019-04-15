package com.biao.sql.build;

import com.biao.entity.BankDepositLog;
import com.biao.sql.BaseSqlBuild;

public class BankDepositSqlBuild extends BaseSqlBuild<BankDepositLog, Integer> {

    public static final String columns = "id,user_id,coin_id,address,coin_symbol,status,volume,tx_id,create_by,update_by,create_date,update_date";

}
