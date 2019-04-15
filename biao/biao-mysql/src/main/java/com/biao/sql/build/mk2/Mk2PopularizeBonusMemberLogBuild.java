package com.biao.sql.build.mk2;

import com.biao.entity.mk2.Mk2PopularizeBonusMemberLog;
import com.biao.sql.BaseSqlBuild;

public class Mk2PopularizeBonusMemberLogBuild extends BaseSqlBuild<Mk2PopularizeBonusMemberLog, Integer> {

    public static final String columns = " id,type,relation_id,user_id,mail,mobile,coin_id,coin_symbol,total_volume,bonus_ratio,income_volume,befor_income_volume,area_name,join_volume,join_total_volume,befor_income_volume,bonus_date_begin,bonus_date_end,create_date,update_date,remark ";
}
