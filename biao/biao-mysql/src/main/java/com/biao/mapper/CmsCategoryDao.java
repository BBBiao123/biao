package com.biao.mapper;

import com.biao.entity.CmsCategory;
import com.biao.sql.build.CmsCategorySqlBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface CmsCategoryDao {


    @SelectProvider(type = CmsCategorySqlBuild.class, method = "findById")
    CmsCategory findById(String id);

    @Select("select " + CmsCategorySqlBuild.columns + " from cms_category where module = #{module} and keywords = #{keyword}")
    CmsCategory findByModuleAndKeyword(@Param("module") String module, @Param("keyword") String keyword);

}
