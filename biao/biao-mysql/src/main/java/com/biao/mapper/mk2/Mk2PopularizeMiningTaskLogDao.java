package com.biao.mapper.mk2;

import com.biao.entity.mk2.Mk2PopularizeMiningTaskLog;
import com.biao.sql.build.mk2.Mk2PopularizeMiningTaskLogBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface Mk2PopularizeMiningTaskLogDao {

    @InsertProvider(type = Mk2PopularizeMiningTaskLogBuild.class, method = "insert")
    void insert(Mk2PopularizeMiningTaskLog coinLog);

    @InsertProvider(type = Mk2PopularizeMiningTaskLogBuild.class, method = "batchInsert")
    void insertBatch(@Param("listValues") List<Mk2PopularizeMiningTaskLog> taskLogs);

    @Select("SELECT " + Mk2PopularizeMiningTaskLogBuild.columns + " FROM mk2_popularize_mining_task_log t WHERE t.type = #{type} AND t.status = '1' ORDER BY count_date DESC LIMIT 1 ")
    Mk2PopularizeMiningTaskLog findByTypeLately(String type);
}

