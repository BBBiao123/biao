package com.biao.sql.build;

import com.biao.entity.MkMinerRecruit;
import com.biao.sql.BaseSqlBuild;

public class MkMinerRecruitSqlBuild extends BaseSqlBuild<MkMinerRecruit, Integer> {

    public static final String columns = "`id`, `user_id`, `mail`, `mobile`, `real_name`, `volume`, `is_standard`, `invite_number`, `reach_number`, `remark`, `create_date`, `update_date`, `create_by`, `update_by`";
}
