package com.biao.sql.build.otc;

import com.biao.entity.otc.OtcExchangeCoinFee;
import com.biao.sql.BaseSqlBuild;

public class OtcExchangeCoinFeeSqlBuild extends BaseSqlBuild<OtcExchangeCoinFee, Integer> {

    public static final String columns = "id, publish_source, symbol,coin_id,ex_fee,fee_type,ex_fee_step,point_volume,create_date,update_date,create_by,update_by,disable ";
}
