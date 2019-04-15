package com.biao.mapper;

import com.biao.entity.Coin;
import com.biao.sql.build.CoinSqlBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface CoinDao {


    @SelectProvider(type = CoinSqlBuild.class, method = "findById")
    Coin findById(String id);

    @Select("select " + CoinSqlBuild.columns + " from js_plat_coin where name =#{name} limit 1")
    Coin findByName(String name);

    @Select("select " + CoinSqlBuild.columns + " from js_plat_coin")
    List<Coin> findAll();

    @Select("select " + CoinSqlBuild.columns + " from js_plat_coin where parent_id = #{parentId}")
    List<Coin> findByParentId(String parentId);
}
