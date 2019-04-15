package com.biao.sql.build;

import com.biao.entity.DepositLog;
import com.biao.sql.BaseSqlBuild;

public class DepositLogSqlBuild extends BaseSqlBuild<DepositLog, Integer> {

    public static final String columns = "id,user_id,coin_id,address,coin_symbol,status,raise_status,volume,tx_id,coin_type,confirms,block_number,create_by,update_by,create_date,update_date";

}
