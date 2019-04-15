package com.biao.sql.build;


import com.biao.entity.MkAutoTradeUser;
import com.biao.sql.BaseSqlBuild;

public class MkAutoTradeUserSqlBuild extends BaseSqlBuild<MkAutoTradeUser, Integer> {

    public static final String columns = "`id`, `user_id`, `username`, `mobile`, `mail`, `real_name`, `id_card`, `remark`, `create_date`, `update_date`, `create_by`, `update_by`";

}
