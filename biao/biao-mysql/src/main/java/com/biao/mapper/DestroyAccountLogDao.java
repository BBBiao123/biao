package com.biao.mapper;

import com.biao.entity.DestroyAccountLog;
import com.biao.sql.build.DestroyAccountLogSqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DestroyAccountLogDao {

    @InsertProvider(type = DestroyAccountLogSqlBuild.class, method = "insert")
    void insert(DestroyAccountLog destroyAccountLog);

    @InsertProvider(type = DestroyAccountLogSqlBuild.class, method = "batchInsert")
    void insertBatch(@Param("listValues") List<DestroyAccountLog> destroyAccountLogList);
}
