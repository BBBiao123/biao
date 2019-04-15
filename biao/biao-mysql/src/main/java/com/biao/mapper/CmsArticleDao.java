package com.biao.mapper;

import com.biao.entity.CmsArticle;
import com.biao.sql.build.CmsArticleSqlBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface CmsArticleDao {


    @SelectProvider(type = CmsArticleSqlBuild.class, method = "findById")
    CmsArticle findById(String id);

    @Select("select " + CmsArticleSqlBuild.columns + " from cms_article where category_id = #{categoryId} and language = #{language} and del_flag = 0 order by weight desc ")
    List<CmsArticle> findAll(@Param("categoryId") String categoryId, @Param("language") String language);

    @Select("select " + CmsArticleSqlBuild.columns + " from cms_article where category_id = #{categoryId} and language = #{language} and del_flag = 0 and description = #{description} order by weight desc ")
    List<CmsArticle> findList(@Param("categoryId") String categoryId, @Param("language") String language, @Param("description") String description);
}
