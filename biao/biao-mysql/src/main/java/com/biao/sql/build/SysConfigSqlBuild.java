package com.biao.sql.build;

import com.biao.entity.SysConfig;
import com.biao.sql.BaseSqlBuild;

public class SysConfigSqlBuild extends BaseSqlBuild<SysConfig, Integer> {
    public static final String columns = " `id`, `offline_on_off`, `remarks`, `create_date`, `update_date`, `create_by`, `update_by` ";
}
