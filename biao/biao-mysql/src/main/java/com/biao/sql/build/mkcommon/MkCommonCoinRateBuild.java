package com.biao.sql.build.mkcommon;

import com.biao.entity.mkcommon.MkCommonCoinRate;
import com.biao.sql.BaseSqlBuild;

public class MkCommonCoinRateBuild extends BaseSqlBuild<MkCommonCoinRate, Integer> {

    public static final String columns = " id,main_coin_id,main_coin_symbol,other_coin_id,other_coin_symbol,rate,begin_date,end_date,create_date ";

}
