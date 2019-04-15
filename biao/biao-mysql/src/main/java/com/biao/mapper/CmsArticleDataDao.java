package com.biao.mapper;

import com.biao.entity.CmsArticleData;
import com.biao.sql.build.CmsArticleDataSqlBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface CmsArticleDataDao {


    @SelectProvider(type = CmsArticleDataSqlBuild.class, method = "findById")
    CmsArticleData findById(String id);


}
