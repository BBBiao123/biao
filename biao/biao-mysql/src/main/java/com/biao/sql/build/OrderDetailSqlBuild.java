package com.biao.sql.build;

import com.biao.entity.OrderDetail;
import com.biao.sql.BaseSqlBuild;

public class OrderDetailSqlBuild extends BaseSqlBuild<OrderDetail, Integer> {

    public static final String columns = "id,user_id,coin_id,address,coin_symbol,status,volume,create_by,update_by,create_date,update_date";

}
