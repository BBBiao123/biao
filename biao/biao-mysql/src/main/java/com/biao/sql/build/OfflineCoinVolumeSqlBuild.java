package com.biao.sql.build;

import com.biao.entity.OfflineCoinVolume;
import com.biao.sql.BaseSqlBuild;

public class OfflineCoinVolumeSqlBuild extends BaseSqlBuild<OfflineCoinVolume, Integer> {

    public static final String columns = "id,user_id,coin_id,coin_symbol,volume,advert_volume,lock_volume,bail_volume,otc_advert_volume,otc_lock_volume,create_by,update_by,create_date,update_date,version";

}
