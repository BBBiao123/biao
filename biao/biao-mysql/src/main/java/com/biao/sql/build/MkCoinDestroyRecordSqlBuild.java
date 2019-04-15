package com.biao.sql.build;

import com.biao.entity.MkCoinDestroyRecord;
import com.biao.sql.BaseSqlBuild;

public class MkCoinDestroyRecordSqlBuild extends BaseSqlBuild<MkCoinDestroyRecord, Integer> {

    public static final String columns = "`id`, `symbol`, `coin_id`, `volume`, `remark`, `create_date`, `update_date`, `create_by`, `update_by`";
}
