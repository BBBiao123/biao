package com.biao.mapper;

import com.biao.entity.CmsLink;
import com.biao.sql.build.CmsLinkSqlBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CmsLinkDao {

    @Select("select " + CmsLinkSqlBuild.columns + " from cms_link where del_flag = 0 order by weight desc")
    List<CmsLink> findAll();
}
