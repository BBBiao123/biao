package com.biao.mapper;

import com.biao.entity.MkCommonTaskRecord;
import com.biao.sql.build.MkCommonTaskRecordSqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface MkCommonTaskRecordDao {

    @InsertProvider(type = MkCommonTaskRecordSqlBuild.class, method = "insert")
    long insert(MkCommonTaskRecord mkCommonTaskRecord);

    @InsertProvider(type = MkCommonTaskRecordSqlBuild.class, method = "updateById")
    long update(MkCommonTaskRecord mkCommonTaskRecord);

    @Select("select " + MkCommonTaskRecordSqlBuild.columns + " from mk_common_task_record  where type = #{type} and status = 1 order by execute_time desc limit 1")
    MkCommonTaskRecord findOne(@Param("type") String type);

    @Select("select " + MkCommonTaskRecordSqlBuild.columns + " from mk_common_task_record  where task_date = #{taskDate} and type = #{type} and status = 1 order by execute_time desc limit 1")
    MkCommonTaskRecord findByTaskDateAndType(@Param("taskDate") LocalDateTime taskDate, @Param("type") String type);

}
