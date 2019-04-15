package com.biao.sql.build;

import com.biao.entity.DepositAddress;
import com.biao.sql.BaseSqlBuild;

public class DepositAddressSqlBuild extends BaseSqlBuild<DepositAddress, Integer> {

    public static final String columns = "id,user_id,coin_id,address,coin_symbol,status,create_date,update_date";

}
