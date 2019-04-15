package com.biao.mapper;

import com.biao.entity.CmsComment;
import com.biao.sql.build.CmsCommentSqlBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface CmsCommentDao {


    @SelectProvider(type = CmsCommentSqlBuild.class, method = "findById")
    CmsComment findById(String id);


}
