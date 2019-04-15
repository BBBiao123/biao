package com.biao.mapper;

import com.biao.entity.CoinBlock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CoinBlockDao {

    @Select("select  id ,symbol ,block_height  from js_plat_coin_block where symbol =#{symbol} limit 1")
    CoinBlock findBySmybol(String symbol);

    @Update("update js_plat_coin_block set block_height = #{blockHeight} where symbol =#{symbol} ")
    int updateHeight(@Param("symbol") String symbol, @Param("blockHeight") Integer blockHeight);


}
