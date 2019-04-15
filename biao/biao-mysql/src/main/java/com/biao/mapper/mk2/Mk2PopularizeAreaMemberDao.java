package com.biao.mapper.mk2;

import com.biao.entity.mk2.Mk2PopularizeAreaMember;
import com.biao.sql.build.mk2.Mk2PopularizeAreaMemberBuild;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface Mk2PopularizeAreaMemberDao {

    @SelectProvider(type = Mk2PopularizeAreaMemberBuild.class, method = "findById")
    Mk2PopularizeAreaMember findById(String id);

    @Select("SELECT " + Mk2PopularizeAreaMemberBuild.columns + " FROM mk2_popularize_area_member t where t.`status` = '1' AND t.type = '1' AND date_add(t.release_begin_date, interval 1 MONTH) < NOW() AND t.lock_volume > IFNULL(t.release_volume, 0) ")
    List<Mk2PopularizeAreaMember> findReleaseAreaMember();

    @Select("SELECT " + Mk2PopularizeAreaMemberBuild.columns + " FROM mk2_popularize_area_member t where t.`status` = '1' AND t.type = '1' ")
    List<Mk2PopularizeAreaMember> findAllAreaMember();

    @Select("SELECT " + Mk2PopularizeAreaMemberBuild.columns + " FROM mk2_popularize_area_member t WHERE t.parent_id = #{id} AND t.type = '2' ")
    List<Mk2PopularizeAreaMember> findShareholderByAreaMemberId(String id);

    @Select("SELECT DISTINCT(t.user_id) FROM mk2_popularize_area_member t WHERE t.status = '1' ")
    List<String> findDistinctAreaMembers();

    @Update("UPDATE mk2_popularize_area_member t SET t.release_volume = IFNULL(t.release_volume, 0) + #{releaseVolume}, t.release_over = #{releaseOver}  WHERE t.id = #{id} AND t.lock_volume >= t.release_volume + #{releaseVolume}")
    long updateReleaseVol(@Param("releaseVolume") BigDecimal releaseVolume, @Param("releaseOver") String releaseOver, @Param("id") String id);

}
