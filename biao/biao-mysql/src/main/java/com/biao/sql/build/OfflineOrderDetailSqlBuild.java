package com.biao.sql.build;

import com.biao.entity.OfflineOrderDetail;
import com.biao.sql.BaseSqlBuild;

public class OfflineOrderDetailSqlBuild extends BaseSqlBuild<OfflineOrderDetail, Integer> {
    public static final String columns = "id,user_id,user_name,user_mobile,ask_user_id,ask_user_name,ask_user_mobile,fee_volume,volume,price,order_id,coin_id,symbol,status,radom_num,total_price,remarks,ex_type,advert_type,sub_order_id,alipay_no,alipay_qrcode_id,wechat_no,wechat_qrcode_id,sell_bank_no,sell_bank_name,sell_bank_branch_name,sync_date,create_by,update_by,create_date,update_date";
}
