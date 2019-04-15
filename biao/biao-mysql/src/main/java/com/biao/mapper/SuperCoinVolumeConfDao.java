package com.biao.mapper;

import com.biao.entity.SuperCoinVolumeConf;
import com.biao.sql.build.SuperCoinVolumeConfSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SuperCoinVolumeConfDao {

    @InsertProvider(type = SuperCoinVolumeConfSqlBuild.class, method = "insert")
    void insert(SuperCoinVolumeConf superCoinVolume);

    @SelectProvider(type = SuperCoinVolumeConfSqlBuild.class, method = "findById")
    SuperCoinVolumeConf findById(String id);

    @Select("SELECT " + SuperCoinVolumeConfSqlBuild.columns + " from js_plat_super_coin_volume_conf where coin_id = #{coinId} and status = 1 limit 1")
    SuperCoinVolumeConf findOneByCoin(@Param("coinId") String coinId);

    @Select("SELECT " + SuperCoinVolumeConfSqlBuild.columns + " from js_plat_super_coin_volume_conf where status = 1 limit 1")
    SuperCoinVolumeConf findOne();

    @Select("SELECT " + SuperCoinVolumeConfSqlBuild.columns + " from js_plat_super_coin_volume_conf where coin_symbol = #{coinSymbol} and status = 1 limit 1")
    SuperCoinVolumeConf findOneBySymbol(@Param("coinSymbol") String coinSymbol);

    @InsertProvider(type = SuperCoinVolumeConfSqlBuild.class, method = "batchInsert")
    void batchInsert(@Param("listValues") List<SuperCoinVolumeConf> superCoinVolumes);

    @UpdateProvider(type = SuperCoinVolumeConfSqlBuild.class, method = "updateById")
    void updateById(SuperCoinVolumeConf superCoinVolume);

}
