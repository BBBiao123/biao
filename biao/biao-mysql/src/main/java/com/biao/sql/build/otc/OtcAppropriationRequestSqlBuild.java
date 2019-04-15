package com.biao.sql.build.otc;

import com.biao.entity.otc.OtcAppropriationRequest;
import com.biao.sql.BaseSqlBuild;

public class OtcAppropriationRequestSqlBuild extends BaseSqlBuild<OtcAppropriationRequest, Integer> {

    public static final String columns = "id,publish_source,batch_no,symbol,coin_id,user_id_str,volume_str,status,remarks,result,create_date,update_date,create_by,update_by ";

}
