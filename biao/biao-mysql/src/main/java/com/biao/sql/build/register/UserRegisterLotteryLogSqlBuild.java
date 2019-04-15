package com.biao.sql.build.register;

import com.biao.entity.register.UserRegisterLotteryLog;
import com.biao.sql.BaseSqlBuild;

/**
 *  ""
 */
public class UserRegisterLotteryLogSqlBuild extends BaseSqlBuild<UserRegisterLotteryLog, String> {

    public static final String columns = " id,user_id,lottery_name,rule_id,coin_symbol," +
            "real_volume,reason,reason_type,mail,phone,recommend_id,phone_date,source,create_date";


}
