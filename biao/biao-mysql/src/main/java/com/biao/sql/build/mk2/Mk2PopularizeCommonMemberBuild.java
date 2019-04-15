package com.biao.sql.build.mk2;

import com.biao.entity.mk2.Mk2PopularizeCommonMember;
import com.biao.sql.BaseSqlBuild;

public class Mk2PopularizeCommonMemberBuild extends BaseSqlBuild<Mk2PopularizeCommonMember, Integer> {

    public static final String columns = " id,type,parent_id,user_id,mail,mobile,coin_id,coin_symbol,lock_volume,release_volume,release_begin_date,release_cycle,release_cycle_ratio,release_over,create_date,update_date ";
}
