package com.biao.sql.build.otc;

import com.biao.entity.otc.OtcAccountSecret;
import com.biao.sql.BaseSqlBuild;

public class OtcAccountSecretSqlBuild extends BaseSqlBuild<OtcAccountSecret, Integer> {

    public static final String columns = "id,publish_source,secret,user_id,status,access_ip,remarks,create_date,update_date,create_by,update_by ";

}
