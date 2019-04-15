package com.biao.mapper;

import com.biao.entity.ExPair;
import com.biao.entity.MkAutoTradeUser;
import com.biao.sql.build.MkAutoTradeUserSqlBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface MkAutoTradeUserDao {


    @SelectProvider(type = MkAutoTradeUserSqlBuild.class, method = "findById")
    ExPair findById(String id);


    List<ExPair> findAll();

    @Select("select " + MkAutoTradeUserSqlBuild.columns + " from  mk_auto_trade_user")
    List<MkAutoTradeUser> findByList();


}
