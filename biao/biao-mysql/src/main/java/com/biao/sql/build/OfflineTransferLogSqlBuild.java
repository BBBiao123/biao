package com.biao.sql.build;

import com.biao.entity.OfflineTransferLog;
import com.biao.sql.BaseSqlBuild;

public class OfflineTransferLogSqlBuild extends BaseSqlBuild<OfflineTransferLog, Integer> {

    public static final String columns = "id,user_id,coin_id,coin_symbol,volume,fee_volume,type,create_by,update_by,create_date,update_date";

}
