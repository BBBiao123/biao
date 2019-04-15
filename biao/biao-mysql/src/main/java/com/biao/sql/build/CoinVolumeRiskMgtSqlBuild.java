package com.biao.sql.build;

import com.biao.entity.CoinVolumeRiskMgt;
import com.biao.sql.BaseSqlBuild;

public class CoinVolumeRiskMgtSqlBuild extends BaseSqlBuild<CoinVolumeRiskMgt, Integer> {
    public static final String columns = "`id`, `coin_id`, `coin_symbol`, `volume`, `remark`, `create_date`, `update_date`, `create_by`, `update_by`";
}
