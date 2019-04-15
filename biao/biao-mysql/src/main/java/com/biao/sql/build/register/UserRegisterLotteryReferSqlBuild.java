package com.biao.sql.build.register;

import com.biao.entity.register.UserRegisterLotteryRefer;
import com.biao.sql.BaseSqlBuild;

/**
 *  ""
 */
public class UserRegisterLotteryReferSqlBuild extends BaseSqlBuild<UserRegisterLotteryRefer, String> {

    public static final String columns = " id,user_id,refer_id,lottery_id,lottery_name,rule_id,coin_symbol," +
            "volume,create_date";


}
