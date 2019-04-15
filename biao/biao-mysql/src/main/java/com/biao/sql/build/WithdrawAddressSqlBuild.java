package com.biao.sql.build;

import com.biao.entity.WithdrawAddress;
import com.biao.sql.BaseSqlBuild;

public class WithdrawAddressSqlBuild extends BaseSqlBuild<WithdrawAddress, Integer> {

    public static final String columns = "id,user_id,coin_id,address,coin_symbol,status,type,tag,create_date,update_date";

}
