package com.biao.mapper.balance;

import com.biao.entity.balance.BalanceUserCoinVolumeDetail;
import com.biao.sql.build.balance.BalanceUserCoinVolumeDetailSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BalanceUserCoinVolumeDetailDao {

    @InsertProvider(type = BalanceUserCoinVolumeDetailSqlBuild.class, method = "insert")
    void insert(BalanceUserCoinVolumeDetail balanceUserCoinVolumeDetail);

    @SelectProvider(type = BalanceUserCoinVolumeDetailSqlBuild.class, method = "findById")
    BalanceUserCoinVolumeDetail findById(String id);

    @UpdateProvider(type = BalanceUserCoinVolumeDetailSqlBuild.class, method = "updateById")
    long updateById(BalanceUserCoinVolumeDetail balanceUserCoinVolumeDetail);

    @Select("select " + BalanceUserCoinVolumeDetailSqlBuild.columns + " from js_plat_user_coin_incomedetail where  user_id = #{userId} order by create_date desc")
    List<BalanceUserCoinVolumeDetail> findByUserId(@Param("userId") String userId);

    @Select("select " + BalanceUserCoinVolumeDetailSqlBuild.columns + " from js_plat_user_coin_incomedetail where  user_id = #{userId} and coin_symbol = #{coinSymbol} and version=1 order by create_date desc")
    List<BalanceUserCoinVolumeDetail> findByUserIdAndCoin(@Param("userId") String userId,@Param("coinSymbol") String coinSymbol);
}
