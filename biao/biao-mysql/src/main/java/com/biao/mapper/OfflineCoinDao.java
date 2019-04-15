package com.biao.mapper;

import com.biao.entity.OfflineCoin;
import com.biao.sql.build.OfflineCoinSqlBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

@Mapper
public interface OfflineCoinDao {

    @Select("select " + OfflineCoinSqlBuild.columns + " from js_plat_offline_coin where disable = 0 ORDER BY sort ASC ")
    List<OfflineCoin> findAll();

    @Select("select " + OfflineCoinSqlBuild.columns + " from js_plat_offline_coin where is_volume = 0 ORDER BY sort ASC ")
    List<OfflineCoin> findAccountAll();

    @Select("select " + OfflineCoinSqlBuild.columns + " from js_plat_offline_coin where disable = 0 and coin_id = #{coinId}")
    OfflineCoin findByCoinId(String coinId);

    @Select("select " + OfflineCoinSqlBuild.columns + " from js_plat_offline_coin where is_volume = 0 and coin_id = #{coinId}")
    OfflineCoin findByCoinIdForChange(String coinId);

    @Select("select " + OfflineCoinSqlBuild.columns + " from js_plat_offline_coin where disable = 0 and symbol = #{symbol}")
    OfflineCoin findBySymbol(String symbol);

    @UpdateProvider(type = OfflineCoinSqlBuild.class, method = "updateById")
    long update(OfflineCoin offlineCoin);
}
