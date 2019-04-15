package com.biao.sql.build;

import com.biao.entity.SuperBook;
import com.biao.sql.BaseSqlBuild;

public class SuperBookSqlBuild extends BaseSqlBuild<SuperBook, String> {

    public static final String columns = "id,user_id,address,symbol,create_date ";
}
