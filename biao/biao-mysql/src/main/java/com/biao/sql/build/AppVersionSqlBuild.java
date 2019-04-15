package com.biao.sql.build;

import com.biao.entity.AppVersion;
import com.biao.sql.BaseSqlBuild;

public class AppVersionSqlBuild extends BaseSqlBuild<AppVersion, Integer> {
    public static final String columns = " id, type, version, address, is_upgrade, remarks, create_by, update_by, create_date, update_date ";
}
