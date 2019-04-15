package com.biao.mapper;

import com.biao.entity.SysConfig;
import com.biao.sql.build.SysConfigSqlBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysConfDao {

    @Select("SELECT " + SysConfigSqlBuild.columns + " from js_sys_conf order by create_date desc")
    List<SysConfig> findAll();

    @Select("SELECT " + SysConfigSqlBuild.columns + " from js_sys_conf order by create_date desc limit 1")
    SysConfig getOne();

}
