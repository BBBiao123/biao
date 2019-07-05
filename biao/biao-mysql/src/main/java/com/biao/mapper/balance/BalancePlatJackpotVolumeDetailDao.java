package com.biao.mapper.balance;

import com.biao.entity.balance.BalancePlatJackpotVolumeDetail;
import com.biao.sql.build.balance.BalancePlatJackpotVolumeDetailSqlBuild;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface BalancePlatJackpotVolumeDetailDao {

    @InsertProvider(type = BalancePlatJackpotVolumeDetailSqlBuild.class, method = "insert")
    void insert(BalancePlatJackpotVolumeDetail balanceUserCoinVolumeDetail);

    @SelectProvider(type = BalancePlatJackpotVolumeDetailSqlBuild.class, method = "findById")
    BalancePlatJackpotVolumeDetail findById(String id);

    @UpdateProvider(type = BalancePlatJackpotVolumeDetailSqlBuild.class, method = "updateById")
    long updateById(BalancePlatJackpotVolumeDetail balanceUserCoinVolumeDetail);

    @Select("select " + BalancePlatJackpotVolumeDetailSqlBuild.columns + " from js_plat_jackpot_income where coin_symbol = #{coinSymbol} order by create_date desc")
    List<BalancePlatJackpotVolumeDetail> findByUserIdAndCoin(@Param("coinSymbol") String coinSymbol);

}
