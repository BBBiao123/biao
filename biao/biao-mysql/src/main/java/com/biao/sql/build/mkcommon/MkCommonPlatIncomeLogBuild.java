package com.biao.sql.build.mkcommon;

import com.biao.entity.mkcommon.MkCommonPlatIncomeLog;
import com.biao.sql.BaseSqlBuild;

public class MkCommonPlatIncomeLogBuild extends BaseSqlBuild<MkCommonPlatIncomeLog, Integer> {

    public static final String columns = " id,user_id,mail,mobile,id_card,real_name,coin_id,coin_symbol,volume,begin_date,end_date,status,create_date,update_date,remark ";
}
