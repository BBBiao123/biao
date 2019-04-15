package com.biao.mapper;

import com.biao.entity.MobileTemplate;
import com.biao.sql.build.MobileTemplateBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface MobileTemplateDao {

    @SelectProvider(type = MobileTemplateBuild.class, method = "findById")
    MobileTemplate findById(String id);

    @Select("select " + MobileTemplateBuild.columns + " from mobile_template where code = #{code}")
    MobileTemplate findByCode(String code);
}
