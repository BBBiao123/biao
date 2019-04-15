package com.biao.mapper;

import com.biao.entity.OfflineChangeLog;
import com.biao.sql.build.OfflineChangeLogSqlBuild;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OfflineChangeLogDao {

    @InsertProvider(type = OfflineChangeLogSqlBuild.class, method = "insert")
    void insert(OfflineChangeLog offlineChangeLog);

    @SelectProvider(type = OfflineChangeLogSqlBuild.class, method = "findById")
    OfflineChangeLog findById(String id);

    @UpdateProvider(type = OfflineChangeLogSqlBuild.class, method = "updateById")
    long updateById(OfflineChangeLog offlineChangeLog);

    @InsertProvider(type = OfflineChangeLogSqlBuild.class, method = "batchInsert")
    long insertBatch(@Param("listValues") List<OfflineChangeLog> offlineChangeLogList);

    @Select("select " + OfflineChangeLogSqlBuild.columns + " from js_plat_offline_change_log where user_id = #{userId} order by create_date desc")
    List<OfflineChangeLog> findOfflineChangeLogByUserId(String userId);

    @Select("select " + OfflineChangeLogSqlBuild.columns + " from js_plat_offline_change_log where coin_id = #{coinId} and user_id = #{userId} order by create_date desc")
    List<OfflineChangeLog> findOfflineChangeLogByUserAndCoin(@Param("coinId") String coinId, @Param("userId") String userId);

    @Select("select " + OfflineChangeLogSqlBuild.columns + " from js_plat_offline_change_log where coin_id = #{coinId} and user_id = #{userId} and Date(create_date) = Date(#{curDateTime}) and type = '1' order by create_date desc")
    List<OfflineChangeLog> findOfflineChangeLogByUserIdAndCoinId(@Param("coinId") String coinId, @Param("userId") String userId, @Param("curDateTime") LocalDateTime curDateTime);
}
