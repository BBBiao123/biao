package com.biao.mapper;

import com.biao.entity.PlatLink;
import com.biao.sql.build.PlatLinkSqlBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PlatLinkDao {

    @Select("SELECT " + PlatLinkSqlBuild.columns + ", (SELECT t.label FROM sys_dict t WHERE t.type = 'plat_link' AND t.value = a.typeId LIMIT 1) AS typeName FROM js_plat_link a ORDER BY show_order ")
    List<PlatLink> findAll();
}
