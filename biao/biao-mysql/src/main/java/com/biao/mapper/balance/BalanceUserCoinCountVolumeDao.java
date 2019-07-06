package com.biao.mapper.balance;

import com.biao.entity.balance.BalanceUserCoinCountVolume;
import com.biao.sql.build.balance.BalanceUserCoinCountVolumeSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BalanceUserCoinCountVolumeDao {

    @InsertProvider(type = BalanceUserCoinCountVolumeSqlBuild.class, method = "insert")
    void insert(BalanceUserCoinCountVolume balanceUserCoinVolume);

    @SelectProvider(type = BalanceUserCoinCountVolumeSqlBuild.class, method = "findById")
    BalanceUserCoinCountVolume findById(String id);

    @UpdateProvider(type = BalanceUserCoinCountVolumeSqlBuild.class, method = "updateById")
    long updateById(BalanceUserCoinCountVolume balanceUserCoinVolume);

    @Select("select " + BalanceUserCoinCountVolumeSqlBuild.columns + " from js_plat_user_coin_balance_count where  user_id = #{userId} order by create_date desc")
    List<BalanceUserCoinCountVolume> findByUserId(@Param("userId") String userId);

    @Select("select " + BalanceUserCoinCountVolumeSqlBuild.columns + " from js_plat_user_coin_balance_count where  user_id = #{userId} and  coin_symbol = #{coinSymbol} order by create_date desc")
    List<BalanceUserCoinCountVolume> findByUserIdAndCoin(@Param("userId") String userId, @Param("coinSymbol") String coinSymbol);

    @Select("select " + BalanceUserCoinCountVolumeSqlBuild.columns + " from js_plat_user_coin_balance_count  where coin_balance>=200  order by create_date desc")
    List<BalanceUserCoinCountVolume> findAll();

    @Select("select " + BalanceUserCoinCountVolumeSqlBuild.columns + " from js_plat_user_coin_balance_count where coin_balance>=200 and refer_id = #{userId} and coin_symbol = #{coinSymbol} ORDER BY create_date DESC ")
    List<BalanceUserCoinCountVolume> findInvitesById(String userId, String coinSymbol);

    @Select("select " + BalanceUserCoinCountVolumeSqlBuild.columns + " from js_plat_user_coin_balance_count where   coin_symbol = #{coinSymbol} order by create_date desc")
    List<BalanceUserCoinCountVolume> findByCoin(@Param("coinSymbol") String coinSymbol);

    @Select("select "+ BalanceUserCoinCountVolumeSqlBuild.columns +",(@i := @i + 1) as ordNum from js_plat_user_coin_balance_count ,(select   @i:=0)  t2  order by coin_balance desc  LIMIT 30")
    List<BalanceUserCoinCountVolume> findByRank();

    @Select("select count(DISTINCT t.user_id) from js_plat_user_coin_balance t")
    int findByCountNum();
}
