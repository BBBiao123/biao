package com.biao.sql.build;

import com.biao.entity.OfflineOrder;
import com.biao.sql.BaseSqlBuild;

public class OfflineOrderSqlBuild extends BaseSqlBuild<OfflineOrder, Integer> {
    public static final String columns = "id,real_name,card_no,wechat_no,alipay_no,user_id,coin_id,symbol,volume,lock_volume,success_volume,min_ex_volume,price,total_price,status,flag,ex_type,remarks,create_by,update_by,create_date,update_date,version";
}

