package com.biao.sql.build.otc;

import com.biao.entity.otc.OtcShAccountSecret;
import com.biao.sql.BaseSqlBuild;

public class OtcShAccountSecretSqlBuild extends BaseSqlBuild<OtcShAccountSecret, Integer> {

    public static final String columns = "id,pay_code,user_id,secret,access_ip,status,remark,create_date,update_date,create_by,update_by ";
}
