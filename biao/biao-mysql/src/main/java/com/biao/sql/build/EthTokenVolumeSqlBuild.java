package com.biao.sql.build;

import com.biao.entity.EthTokenVolume;
import com.biao.sql.BaseSqlBuild;

public class EthTokenVolumeSqlBuild extends BaseSqlBuild<EthTokenVolume, String> {

    public static final String columns = "id,name,volume,address";

}
