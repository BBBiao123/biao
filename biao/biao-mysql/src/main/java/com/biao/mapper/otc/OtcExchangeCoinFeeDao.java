package com.biao.mapper.otc;

import com.biao.entity.otc.OtcExchangeCoinFee;
import com.biao.sql.build.otc.OtcExchangeCoinFeeSqlBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface OtcExchangeCoinFeeDao {

    @SelectProvider(type = OtcExchangeCoinFeeSqlBuild.class, method = "findById")
    OtcExchangeCoinFee findById(String id);

    @Select("SELECT " + OtcExchangeCoinFeeSqlBuild.columns + " FROM otc_exchange_coin_fee t WHERE t.coin_id = #{coinId} ")
    OtcExchangeCoinFee findByCoinId(@Param("coinId") String coinId);

    @Select("SELECT " + OtcExchangeCoinFeeSqlBuild.columns + " FROM otc_exchange_coin_fee t WHERE t.symbol = #{symbol} limit 1")
    OtcExchangeCoinFee findBySymbol(@Param("symbol") String symbol);
}
