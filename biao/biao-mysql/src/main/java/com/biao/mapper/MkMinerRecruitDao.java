package com.biao.mapper;

import com.biao.entity.MkMinerRecruit;
import com.biao.sql.build.MkMinerRecruitSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MkMinerRecruitDao {

    @InsertProvider(type = MkMinerRecruitSqlBuild.class, method = "insert")
    long insert(MkMinerRecruit mkMinerRecruit);

    @UpdateProvider(type = MkMinerRecruitSqlBuild.class, method = "updateById")
    long update(MkMinerRecruit mkMinerRecruit);

    @SelectProvider(type = MkMinerRecruitSqlBuild.class, method = "findById")
    MkMinerRecruit findById(@Param("id") String id);

    @Select("select " + MkMinerRecruitSqlBuild.columns + " from mk_miner_recruit where user_id = #{userId} limit 1")
    MkMinerRecruit findByUserId(@Param("userId") String userId);

    @Select("select " + MkMinerRecruitSqlBuild.columns + " from mk_miner_recruit where is_standard = '0' or invite_number < 30 or reach_number < 30")
    List<MkMinerRecruit> findUnStandardList();

    @Select("select " + MkMinerRecruitSqlBuild.columns + " from mk_miner_recruit order by create_date desc")
    List<MkMinerRecruit> findList();

}
