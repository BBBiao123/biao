package com.biao.sql.build.otc;

import com.biao.entity.otc.OtcAppVersion;
import com.biao.sql.BaseSqlBuild;

public class OtcAppVersionSqlBuild extends BaseSqlBuild<OtcAppVersion, Integer> {

    public static final String columns = "id, type, version, version_value, address,is_upgrade,create_date,update_date,create_by,update_by ";
}
