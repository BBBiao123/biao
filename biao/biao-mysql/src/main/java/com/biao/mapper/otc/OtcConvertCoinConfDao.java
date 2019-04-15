package com.biao.mapper.otc;

import com.biao.entity.otc.OtcConvertCoinConf;
import com.biao.sql.build.otc.OtcConvertCoinConfSqlBuild;
import com.biao.sql.build.otc.OtcConvertCoinSqlBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OtcConvertCoinConfDao {

    @Select("SELECT " + OtcConvertCoinConfSqlBuild.columns + " FROM otc_convert_coin_conf t WHERE t.from_symbol = #{fromSymbol} AND t.to_symbol = #{toSymbol} AND t.status = '1' LIMIT 1")
    OtcConvertCoinConf findBySymbol(@Param("fromSymbol") String fromSymbol, @Param("toSymbol") String toSymbol);
}
