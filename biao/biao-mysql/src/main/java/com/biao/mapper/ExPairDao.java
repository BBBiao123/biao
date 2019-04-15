package com.biao.mapper;

import com.biao.entity.ExPair;
import com.biao.sql.build.ExPairSqlBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface ExPairDao {


    @SelectProvider(type = ExPairSqlBuild.class, method = "findById")
    ExPair findById(String id);


    List<ExPair> findAll();


    @Select("select " + ExPairSqlBuild.columns + " from  js_plat_ex_pair where status ='1'")
    List<ExPair> findByList();


}
