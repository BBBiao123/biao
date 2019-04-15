package com.biao.sql.build.mk2;

import com.biao.entity.mk2.Mk2PopularizeMemberRule;
import com.biao.sql.BaseSqlBuild;

public class Mk2PopularizeMemberRuleBuild extends BaseSqlBuild<Mk2PopularizeMemberRule, Integer> {

    public static final String columns = " id,type,release_open,release_version,release_day,release_week,release_month,release_year,release_type," +
            "total_member,sold_member,bonus_ratio,phone_bonus_ratio,refer_bonus_ratio,create_date,update_date ";
}

