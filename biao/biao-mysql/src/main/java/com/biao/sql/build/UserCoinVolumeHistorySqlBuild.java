package com.biao.sql.build;

import com.biao.entity.UserCoinVolumeHistory;
import com.biao.sql.BaseSqlBuild;

public class UserCoinVolumeHistorySqlBuild extends BaseSqlBuild<UserCoinVolumeHistory, Integer> {

    public static final String columns = "`id`, `user_id`, `account`, `type`, `coin_id`, `coin_symbol`, `volume`, `create_date`, `update_date`, `create_by`, `update_by`, `remark`";

}
