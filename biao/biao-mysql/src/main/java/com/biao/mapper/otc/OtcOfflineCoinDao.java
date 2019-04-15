package com.biao.mapper.otc;

import com.biao.entity.otc.OtcOfflineCoin;
import com.biao.sql.build.otc.OtcOfflineCoinSqlBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

@Mapper
public interface OtcOfflineCoinDao {
    @Select("select " + OtcOfflineCoinSqlBuild.columns + " from otc_offline_coin where disable = 0 ")
    List<OtcOfflineCoin> findAll();

    @Select("select " + OtcOfflineCoinSqlBuild.columns + " from otc_offline_coin where disable = 0 AND publish_source = #{publishSource} ")
    List<OtcOfflineCoin> findAllByPublishSource(@Param("publishSource") String publishSource);

    @Select("select " + OtcOfflineCoinSqlBuild.columns + " from otc_offline_coin where disable = 0 and coin_id = #{coinId}")
    OtcOfflineCoin findByCoinId(String coinId);

    @Select("select " + OtcOfflineCoinSqlBuild.columns + " from otc_offline_coin where disable = 0 and symbol = #{symbol}")
    OtcOfflineCoin findBySymbol(String symbol);

    @UpdateProvider(type = OtcOfflineCoinSqlBuild.class, method = "updateById")
    long update(OtcOfflineCoin offlineCoin);
}
