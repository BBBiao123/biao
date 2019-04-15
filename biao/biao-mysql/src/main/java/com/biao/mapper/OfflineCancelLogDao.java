package com.biao.mapper;

import com.biao.entity.OfflineCancelLog;
import com.biao.sql.build.OfflineCancelLogSqlBuild;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;

@Mapper
public interface OfflineCancelLogDao {

    @InsertProvider(type = OfflineCancelLogSqlBuild.class, method = "insert")
    void insert(OfflineCancelLog offlineCancelLog);

    @SelectProvider(type = OfflineCancelLogSqlBuild.class, method = "findById")
    OfflineCancelLog findById(String id);

    @Select("select count(*) from offline_cancel_log where user_id = #{userId} and type = #{type} and date = #{date}")
    long findCountByUserIdAndTypeAndDate(@Param("userId") String userId, @Param("type") String type, @Param("date") LocalDate date);

}
