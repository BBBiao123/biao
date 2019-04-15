package com.biao.sql.build.mk2;

import com.biao.entity.mk2.Mk2PopularizeAreaMember;
import com.biao.sql.BaseSqlBuild;

public class Mk2PopularizeAreaMemberBuild extends BaseSqlBuild<Mk2PopularizeAreaMember, Integer> {

    public static final String columns = " id,type,parent_id,ratio,coin_id,coin_symbol,area_id,area_name,area_paraent_id,area_paraent_name,status,user_id,mail,mobile,lock_volume,release_volume,release_begin_date,release_cycle,release_cycle_ratio,release_over,create_date,update_date ";
}
