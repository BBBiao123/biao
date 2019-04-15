package com.biao.mapper;

import com.biao.entity.CmsSite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CmsSiteDao {

    @Select("select cs.id,cs.name,cs.title,cs.logo,cs.domain,cs.description,cs.keywords,cs.theme,cs.copyright from cms_site cs where cs.name = '默认站点' limit 1")
    CmsSite findCmsSite();
}
