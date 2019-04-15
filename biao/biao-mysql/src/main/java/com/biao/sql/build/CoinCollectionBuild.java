package com.biao.sql.build;

import com.biao.entity.CoinCollection;
import com.biao.sql.BaseSqlBuild;

public class CoinCollectionBuild extends BaseSqlBuild<CoinCollection, String> {

    public static final String columns = "id,symbol,user_id,address,volume,status";

}
