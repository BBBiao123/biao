package com.biao.sql.build;

import com.biao.entity.OfflineOrderFee;
import com.biao.sql.BaseSqlBuild;

public class OfflineOrderFeeSqlBuild extends BaseSqlBuild<OfflineOrderFee, String> {
    public static final String columns = "id,user_id,order_id,coin_id,coin_symbol,volume,fee_volume,status,ex_type,remarks,create_by,update_by,create_date,update_date";
}
