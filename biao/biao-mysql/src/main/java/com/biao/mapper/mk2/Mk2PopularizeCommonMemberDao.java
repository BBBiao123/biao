package com.biao.mapper.mk2;

import com.biao.entity.mk2.Mk2PopularizeCommonMember;
import com.biao.sql.build.mk2.Mk2PopularizeCommonMemberBuild;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface Mk2PopularizeCommonMemberDao {

    @InsertProvider(type = Mk2PopularizeCommonMemberBuild.class, method = "insert")
    void insert(Mk2PopularizeCommonMember mk2PopularizeCommonMember);

    @SelectProvider(type = Mk2PopularizeCommonMemberBuild.class, method = "findById")
    Mk2PopularizeCommonMember findById(String id);

    @Select("SELECT " + Mk2PopularizeCommonMemberBuild.columns + " FROM mk2_popularize_common_member t WHERE t.lock_status = '1' AND t.release_over = '0' AND t.release_begin_date < NOW() ")
    List<Mk2PopularizeCommonMember> findReleaseCommonMember();

    @Select("SELECT " + Mk2PopularizeCommonMemberBuild.columns + " FROM mk2_popularize_common_member t ")
    List<Mk2PopularizeCommonMember> findAllCommonMember();

    @Update("UPDATE mk2_popularize_common_member t set t.release_volume = IFNULL(t.release_volume, 0) + #{releaseVolume}, t.release_over = #{releaseOver} WHERE t.id = #{id} AND t.lock_volume >= t.release_volume + #{releaseVolume} ")
    long updateReleaseVol(@Param("releaseVolume") BigDecimal releaseVolume, @Param("releaseOver") String releaseOver, @Param("id") String id);

    @Update("UPDATE mk2_popularize_common_member t set t.release_volume = #{releaseVolume}, t.release_over = #{releaseOver} WHERE t.id = #{id} AND t.lock_volume >= #{releaseVolume} ")
    long updateMemberRelease(Mk2PopularizeCommonMember mk2PopularizeCommonMember);

}
