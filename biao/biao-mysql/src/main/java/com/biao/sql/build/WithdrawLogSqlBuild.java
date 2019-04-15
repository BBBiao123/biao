package com.biao.sql.build;

import com.biao.entity.WithdrawLog;
import com.biao.sql.BaseSqlBuild;

public class WithdrawLogSqlBuild extends BaseSqlBuild<WithdrawLog, String> {

    public static final String columns = "id,remark,user_id,coin_id,coin_type,address,tag,coin_symbol,status,confirm_status,volume,real_volume,fee,tx_id,create_by,update_by,create_date,update_date";

}
