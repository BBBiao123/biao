package com.biao.sql.build.mk2;

import com.biao.entity.mk2.Mk2PopularizeNodalMember;
import com.biao.sql.BaseSqlBuild;

public class Mk2PopularizeNodalMemberBuild extends BaseSqlBuild<Mk2PopularizeNodalMember, Integer> {

    public static final String columns = " id,user_id,mail,mobile,coin_id,coin_symbol,lock_volume,release_volume,release_begin_date,release_cycle,release_cycle_ratio,release_over,create_date,update_date ";
}
