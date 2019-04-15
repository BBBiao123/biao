package com.biao.sql.build;

import com.biao.entity.UserInvited;
import com.biao.sql.BaseSqlBuild;

public class UserInvitedSqlBuild extends BaseSqlBuild<UserInvited, Integer> {

    public static final String columns = "id,user_id,coin_id,address,coin_symbol,status,volume,tx_id,create_by,update_by,create_date,update_date";

}
