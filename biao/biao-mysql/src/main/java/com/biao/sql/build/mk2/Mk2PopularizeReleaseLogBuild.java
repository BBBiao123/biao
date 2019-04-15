package com.biao.sql.build.mk2;

import com.biao.entity.mk2.Mk2PopularizeReleaseLog;
import com.biao.sql.BaseSqlBuild;

public class Mk2PopularizeReleaseLogBuild extends BaseSqlBuild<Mk2PopularizeReleaseLog, Integer> {

    public static final String columns = " id,relation_id,type,user_id,mail,mobile,coin_id,coin_symbol,release_volume,release_cycle_date,release_status,release_version,area_name,create_date,update_date,remark ";
}
