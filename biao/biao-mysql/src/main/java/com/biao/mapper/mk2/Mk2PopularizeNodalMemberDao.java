package com.biao.mapper.mk2;

import com.biao.entity.mk2.Mk2PopularizeNodalMember;
import com.biao.sql.build.mk2.Mk2PopularizeNodalMemberBuild;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface Mk2PopularizeNodalMemberDao {

    @SelectProvider(type = Mk2PopularizeNodalMemberBuild.class, method = "findById")
    Mk2PopularizeNodalMember findById(String id);

    @Select("SELECT " + Mk2PopularizeNodalMemberBuild.columns + " FROM mk2_popularize_nodal_member t WHERE t.type = '2' AND t.lock_status = '1' AND date_add(t.release_begin_date, interval 1 MONTH) < NOW() AND t.lock_volume > IFNULL(t.release_volume, 0) ")
    List<Mk2PopularizeNodalMember> findReleaseNodalMember();

    @Select("SELECT " + Mk2PopularizeNodalMemberBuild.columns + " FROM mk2_popularize_nodal_member t where t.type = '1' ")
    List<Mk2PopularizeNodalMember> findAllNodalMember();

    @Update("UPDATE mk2_popularize_nodal_member t set t.release_volume = IFNULL(t.release_volume, 0) + #{releaseVolume}, t.release_over = #{releaseOver} WHERE t.id = #{id} AND t.lock_volume >= t.release_volume + #{releaseVolume} ")
    long updateReleaseVol(@Param("releaseVolume") BigDecimal releaseVolume, @Param("releaseOver") String releaseOver, @Param("id") String id);
}
