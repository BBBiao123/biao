package com.biao.mapper;

import com.biao.entity.PlatNeoUserError;
import com.biao.sql.build.PlatNeoUserErrorSqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PlatNeoUserErrorDao {

    @InsertProvider(type = PlatNeoUserErrorSqlBuild.class, method = "insert")
    void insert(PlatNeoUserError platNeoUserError);

    @InsertProvider(type = PlatNeoUserErrorSqlBuild.class, method = "batchInsert")
    void insertBatch(@Param("listValues") List<PlatNeoUserError> platNeoUserErrors);

    @Update("UPDATE js_plat_neo_user_error set status = '1' WHERE id = #{id} AND status = '0' ")
    long updateStatus2Success(@Param("id") String id);
}
