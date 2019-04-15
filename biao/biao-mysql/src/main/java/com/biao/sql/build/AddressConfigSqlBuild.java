package com.biao.sql.build;

import com.biao.entity.CoinAddress;
import com.biao.sql.BaseSqlBuild;

public class AddressConfigSqlBuild extends BaseSqlBuild<CoinAddress, Integer> {

    public static final String columns = "id,name,status,create_date,update_date";

}
