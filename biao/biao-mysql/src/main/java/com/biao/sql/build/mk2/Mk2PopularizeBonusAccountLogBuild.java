package com.biao.sql.build.mk2;

import com.biao.entity.mk2.Mk2PopularizeBonusAccountLog;
import com.biao.sql.BaseSqlBuild;

public class Mk2PopularizeBonusAccountLogBuild extends BaseSqlBuild<Mk2PopularizeBonusAccountLog, Integer> {

    public static final String columns = " id,user_id,mail,mobile,id_card,real_name,bonus_date_begin,bonus_date_end,coin_id,coin_symbol,income_volume,befor_income_volume,status,create_date,update_date,remark ";
}
