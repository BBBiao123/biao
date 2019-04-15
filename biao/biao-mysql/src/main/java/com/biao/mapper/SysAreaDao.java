package com.biao.mapper;

import com.biao.entity.SysArea;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysAreaDao {

    @Select("select " + SysArea.columns + " from sys_area where name = #{name} and parent_id = #{parentId} and del_flag = 0 limit 1")
    SysArea findByNameAndParentId(@Param("name") String name, @Param("parentId") String parentId);
}
