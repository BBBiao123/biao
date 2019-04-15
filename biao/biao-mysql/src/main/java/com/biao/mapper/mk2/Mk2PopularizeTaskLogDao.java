package com.biao.mapper.mk2;

import com.biao.entity.mk2.Mk2PopularizeTaskLog;
import com.biao.sql.build.mk2.Mk2PopularizeTaskLogBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface Mk2PopularizeTaskLogDao {

    @InsertProvider(type = Mk2PopularizeTaskLogBuild.class, method = "insert")
    void insert(Mk2PopularizeTaskLog taskLog);

    @Select("SELECT COUNT(1) FROM mk2_popularize_task_log t WHERE t.status = 1 ")
    long queryCountAll();

    @Select("SELECT " + Mk2PopularizeTaskLogBuild.columns + " FROM mk2_popularize_task_log t WHERE t.param_task_date = #{curDate} AND t.status = 1 ")
    List<Mk2PopularizeTaskLog> findByDate(String curDate);

    @Select("SELECT " + Mk2PopularizeTaskLogBuild.columns + " FROM mk2_popularize_task_log t WHERE t.status = 1 order by execute_time desc  limit 1")
    Mk2PopularizeTaskLog getLastSuccessLog();
}
