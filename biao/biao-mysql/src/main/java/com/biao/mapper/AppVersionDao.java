package com.biao.mapper;

import com.biao.entity.AppVersion;
import com.biao.sql.build.AppVersionSqlBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AppVersionDao {

    @Select("SELECT " + AppVersionSqlBuild.columns + " from js_sys_app_version order by create_date desc")
    List<AppVersion> findAll();

    @Select("SELECT " + AppVersionSqlBuild.columns + " from js_sys_app_version where type = #{type} order by create_date desc limit 1")
    AppVersion getLastestByType(@Param("type") String type);

}
