package com.biao.sql.build.otc;

import com.biao.entity.otc.OtcOfflineAppealRequest;
import com.biao.sql.BaseSqlBuild;

public class OtcOfflineAppealRequestSqlBuild extends BaseSqlBuild<OtcOfflineAppealRequest, Integer> {

    public static final String columns = "id,publish_source,batch_no,sub_order_id,result_user_id,result_ex_type,status,remarks,result,create_date,update_date,create_by,update_by ";

}
