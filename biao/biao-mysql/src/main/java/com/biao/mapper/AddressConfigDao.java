package com.biao.mapper;

import com.biao.entity.AddressConfig;
import com.biao.sql.build.AddressConfigSqlBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AddressConfigDao {

    @Select("select " + AddressConfigSqlBuild.columns + " from address_config where name = #{name} limit 1 ")
    AddressConfig findByName(@Param("name") String name);


}
