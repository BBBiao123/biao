package com.biao.sql.build.otc;

import com.biao.entity.otc.OtcVolumeChangeRequestLog;
import com.biao.sql.BaseSqlBuild;

public class OtcVolumeChangeRequestLogSqlBuild extends BaseSqlBuild<OtcVolumeChangeRequestLog, Integer> {

    public static final String columns = "`id`, `publish_source`, `batch_no`, `type`, `volume`, `fee_volume`, `coin_id`, `symbol`, `buy_user_id`, `sell_user_id`, `order_id`, `ad_type`, `sub_order_id`, `status`, `result`, `remarks`, `login_user_id`, `create_date`, `update_date`, `create_by`, `update_by`" ;

}
