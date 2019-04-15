package com.biao.mapper;

import com.biao.entity.MainCnb;
import com.biao.sql.build.MainCnbBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MainCnbDao {

    @Select("SELECT " + MainCnbBuild.columns + " FROM js_plat_main_cnb t ")
    List<MainCnb> findAll();
}
