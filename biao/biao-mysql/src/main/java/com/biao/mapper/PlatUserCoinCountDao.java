package com.biao.mapper;

import com.biao.entity.PlatUserCoinCount;
import com.biao.sql.build.PlatUserCoinCountSqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PlatUserCoinCountDao {

    @InsertProvider(type = PlatUserCoinCountSqlBuild.class, method = "insert")
    void insert(PlatUserCoinCount coinCount);

    @InsertProvider(type = PlatUserCoinCountSqlBuild.class, method = "batchInsert")
    void insertBatch(@Param("listValues") List<PlatUserCoinCount> coinCounts);

    @Select("select 'UES流通数量' as typeDesc, '1' AS type, 'UES' AS 'coinSymbol', ifnull(count(*),0) as personCount, ifnull(sum(volume),0) as holdCoinVolume from js_plat_user_coin_volume where coin_symbol = 'UES' " +
            "union all " +
            "select '小于500' as typeDesc, '2' AS type, 'UES' AS 'coinSymbol', ifnull(count(*),0) as personCount, ifnull(sum(volume),0) as holdCoinVolume from js_plat_user_coin_volume where coin_symbol = 'UES' and volume < 500 " +
            "union all " +
            "select '大于等于500小于1000' as typeDesc, '3' AS type, 'UES' AS 'coinSymbol', ifnull(count(*),0) as personCount, ifnull(sum(volume),0) as holdCoinVolume from js_plat_user_coin_volume where coin_symbol = 'UES' and volume >= 500 and volume < 1000 " +
            "union all " +
            "select '大于等于1000小于2000' as typeDesc, '4' AS type, 'UES' AS 'coinSymbol', ifnull(count(*),0) as personCount, ifnull(sum(volume),0) as holdCoinVolume from js_plat_user_coin_volume where coin_symbol = 'UES' and volume >= 1000 and volume < 2000 " +
            "union all " +
            "select '大于等于2000小于5000' as typeDesc, '5' AS type, 'UES' AS 'coinSymbol', ifnull(count(*),0) as personCount, ifnull(sum(volume),0) as holdCoinVolume from js_plat_user_coin_volume where coin_symbol = 'UES' and volume >= 2000 and volume < 5000 " +
            "union all " +
            "select '大于5000' as typeDesc, '6' AS type, 'UES' AS 'coinSymbol', ifnull(count(*),0) as personCount, ifnull(sum(volume),0) as holdCoinVolume from js_plat_user_coin_volume where coin_symbol = 'UES' and volume >= 5000")
    List<PlatUserCoinCount> countByType();
}
