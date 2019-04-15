package com.biao.sql.build;

import com.biao.entity.PlatUserOplog;
import com.biao.sql.BaseSqlBuild;

public class PlatUserOplogSqlBuild extends BaseSqlBuild<PlatUserOplog, Integer> {
    public static final String columns = "`id`, `type`, `user_id`, `mail`, `mobile`, `real_name`, `content`, `reason`, `create_date`, `update_date`, `create_by`, `create_by_name`, `update_by`, `update_by_name`";
}
