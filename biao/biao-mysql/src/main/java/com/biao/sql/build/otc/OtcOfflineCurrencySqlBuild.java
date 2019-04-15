package com.biao.sql.build.otc;

import com.biao.entity.otc.OtcOfflineCurrency;
import com.biao.sql.BaseSqlBuild;

public class OtcOfflineCurrencySqlBuild extends BaseSqlBuild<OtcOfflineCurrency, Integer> {

    public static final String columns = "id,code,name_en,name_cn,country,status,create_date,update_date,create_by,update_by ";

}
