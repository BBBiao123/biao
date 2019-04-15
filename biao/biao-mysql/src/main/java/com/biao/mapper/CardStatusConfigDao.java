package com.biao.mapper;

import com.biao.entity.CardStatusConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CardStatusConfigDao {

    @Select("select id,limit_out,label,value,create_by,update_by,create_date,update_date from js_plat_card_status_config where value = #{value} limit 1")
    CardStatusConfig findByValue(@Param("value") Integer value);

    @Select("select id,limit_out,label,value,create_by,update_by,create_date,update_date from js_plat_card_status_config where id = #{id}")
    CardStatusConfig findById(@Param("id") String id);
}
