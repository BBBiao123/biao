package com.biao.sql.build.mkcommon;

import com.biao.entity.mkcommon.MkCommonUserCoinFee;
import com.biao.sql.BaseSqlBuild;

public class MkCommonUserCoinFeeBuild extends BaseSqlBuild<MkCommonUserCoinFee, Integer> {

    public static final String columns = " id,user_id,mail,mobile,id_card,real_name,coin_id,coin_symbol,volume,ex_usdt_vol,begin_date,end_date,create_date ";

}
