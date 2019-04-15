package com.biao.mapper.otc;

import com.biao.entity.otc.OtcAppVersion;
import com.biao.sql.build.otc.OtcAppVersionSqlBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OtcAppVersionDao {

    @Select("SELECT " + OtcAppVersionSqlBuild.columns + " from otc_app_version where type = #{type} order by create_date desc limit 1")
    OtcAppVersion getLastestByType(@Param("type") String type);
}
