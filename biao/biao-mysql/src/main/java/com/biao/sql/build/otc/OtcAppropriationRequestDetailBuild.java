package com.biao.sql.build.otc;

import com.biao.entity.otc.OtcAppropriationRequestDetail;
import com.biao.sql.BaseSqlBuild;

public class OtcAppropriationRequestDetailBuild extends BaseSqlBuild<OtcAppropriationRequestDetail, Integer> {

    public static final String columns = "id,batch_no,symbol,coin_id,user_id,volume,status,create_date,update_date,create_by,update_by ";

}
