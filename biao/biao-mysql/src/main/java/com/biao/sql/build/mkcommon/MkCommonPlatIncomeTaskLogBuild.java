package com.biao.sql.build.mkcommon;

import com.biao.entity.mkcommon.MkCommonPlatIncomeTaskLog;
import com.biao.sql.BaseSqlBuild;

public class MkCommonPlatIncomeTaskLogBuild extends BaseSqlBuild<MkCommonPlatIncomeTaskLog, Integer> {

    public static final String columns = " id,begin_date,end_date,status,create_date,update_date,remark ";
}
