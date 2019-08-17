package com.biao.mapper.balance;

import com.biao.entity.balance.BalancePlatCoinPriceVolume;
import com.biao.sql.build.balance.BalancePlatCoinPriceVolumeSqlBuild;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface BalancePlatCoinPriceVolumeDao {

    @InsertProvider(type = BalancePlatCoinPriceVolumeSqlBuild.class, method = "insert")
    void insert(BalancePlatCoinPriceVolume balanceUserCoinVolume);

    @SelectProvider(type = BalancePlatCoinPriceVolumeSqlBuild.class, method = "findById")
    BalancePlatCoinPriceVolume findById(String id);

    @UpdateProvider(type = BalancePlatCoinPriceVolumeSqlBuild.class, method = "updateById")
    long updateById(BalancePlatCoinPriceVolume balanceUserCoinVolume);

    @Select("SELECT price FROM js_plat_ex_order T WHERE coin_symbol = 'MG' AND coin_main = 'USDT' AND coin_other = 'MG' AND (status = 1 OR status = 2) AND T.price>0 ORDER BY update_date DESC ,id DESC LIMIT 1")
    BigDecimal findPriceByUpdateDate();

    @Select("SELECT price FROM js_plat_ex_order T WHERE coin_symbol = #{coinSymbol} AND coin_main = 'USDT' AND coin_other = #{coinSymbol} AND (status = 1 OR status = 2) AND T.price>0 ORDER BY update_date DESC ,id DESC LIMIT 1")
    BigDecimal findPriceByCoinSymbolUpdateDate(String coinSymbol);

}
