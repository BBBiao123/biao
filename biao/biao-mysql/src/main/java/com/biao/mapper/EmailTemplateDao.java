package com.biao.mapper;

import com.biao.entity.EmailTemplate;
import com.biao.sql.build.EmailTemplateBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface EmailTemplateDao {

    @SelectProvider(type = EmailTemplateBuild.class, method = "findById")
    EmailTemplate findById(String id);

    @Select("select " + EmailTemplateBuild.columns + " from email_template where code = #{code}")
    EmailTemplate findByCode(String code);


}
