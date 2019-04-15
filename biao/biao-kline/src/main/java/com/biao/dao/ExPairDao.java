package com.biao.dao;

import com.biao.binance.config.ExPair;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExPairDao {

    @Select("select other_coin_id,pair_other,coin_id,pair_one,status from  js_plat_ex_pair where status ='1'")
    List<ExPair> findByList();
}
