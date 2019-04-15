package com.biao.mapper.mk2;

import com.biao.entity.mk2.Mk2PopularizeReleaseLog;
import com.biao.sql.build.mk2.Mk2PopularizeReleaseLogBuild;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Mapper
public interface Mk2PopularizeReleaseLogDao {

    // ======================释放任务开始======================
    @InsertProvider(type = Mk2PopularizeReleaseLogBuild.class, method = "insert")
    long insert(Mk2PopularizeReleaseLog releaseLog);

    @Select("SELECT " + Mk2PopularizeReleaseLogBuild.columns + " FROM mk2_popularize_release_log t " +
            " WHERE t.relation_id = #{relationId} AND t.type = #{type} AND t.release_source = 0 AND t.release_cycle_date > #{beginCycleDate} AND t.release_cycle_date < #{endCycleDate} LIMIT 1 ")
    Mk2PopularizeReleaseLog findByRelationIdAndCycleTime(@Param("relationId") String relationId, @Param("type") String type, @Param("beginCycleDate") LocalDateTime beginCycleDate, @Param("endCycleDate") LocalDateTime endCycleDate);

    @Select("SELECT " + Mk2PopularizeReleaseLogBuild.columns + " FROM mk2_popularize_release_log t " +
            " WHERE t.type = #{type} AND t.release_status = '1' AND t.release_version = #{releaseVersion} ")
    List<Mk2PopularizeReleaseLog> findByTypeAndVersion(@Param("type") String type, @Param("releaseVersion") Long releaseVersion);

    @Select("SELECT " + Mk2PopularizeReleaseLogBuild.columns + " FROM mk2_popularize_release_log t " +
            "WHERE t.relation_id = #{relationId} AND t.type = #{type} AND t.release_source = 0 ORDER BY t.release_cycle_date DESC LIMIT 1 ")
    Mk2PopularizeReleaseLog findLastByMemberId(@Param("relationId") String relationId, @Param("type") String type);
    // ======================释放任务结束======================

    @Select("SELECT " + Mk2PopularizeReleaseLogBuild.columns + " FROM mk2_popularize_release_log t WHERE t.user_id = #{userId} AND t.relation_id = #{relationId} ORDER BY t.release_cycle_date DESC ")
    List<Mk2PopularizeReleaseLog> findByUserIdAndRelationId(@Param("userId") String userId, @Param("relationId") String relationId);

//    @Select("SELECT " + Mk2PopularizeReleaseLogBuild.columns + " FROM mk2_popularize_release_log t WHERE t.relation_id = #{relationId} AND t.release_status = '0' AND date_format(t.release_cycle_date, '%Y-%m') = date_format(#{releaseCycleDate}, '%Y-%m') LIMIT 1 ")
//    Mk2PopularizeReleaseLog findErrorReleaseByRelationId(@Param("relationId") String relationId, @Param("releaseCycleDate") LocalDateTime releaseCycleDate);
//
//    @Select("SELECT " + Mk2PopularizeReleaseLogBuild.columns + " FROM mk2_popularize_release_log t WHERE t.relation_id = #{relationId} AND t.release_status = '1' AND date_format(t.release_cycle_date, '%Y-%m') = date_format(#{releaseCycleDate}, '%Y-%m') LIMIT 1 ")
//    Mk2PopularizeReleaseLog findSuccessReleaseByRelationId(@Param("relationId") String relationId, @Param("releaseCycleDate") LocalDateTime releaseCycleDate);

    @Insert("INSERT INTO mk2_popularize_release_task_log (id, task_date, release_volume) VALUES (UUID(), #{taskDate}, #{releaseVolume})")
    long insertReleaseTaskLog(@Param("taskDate") LocalDateTime taskDate, @Param("releaseVolume") BigDecimal releaseVolume);

//    @Select("SELECT t.task_date FROM mk2_popularize_release_task_log t ORDER BY t.task_date DESC LIMIT 1 ")
//    LocalDateTime findLastTaskLogDate();


    @Select("SELECT t.task_date FROM mk2_popularize_release_task_log t WHERE t.task_date = #{taskDate}")
    LocalDateTime findReleaseTaskByDate(@Param("taskDate") LocalDateTime taskDate);
}
