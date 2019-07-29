package com.biao.mapper.balance;

import com.biao.entity.balance.BalanceUserCoinVolume;
import com.biao.sql.build.balance.BalanceUserCoinVolumeSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BalanceUserCoinVolumeDao {

    @InsertProvider(type = BalanceUserCoinVolumeSqlBuild.class, method = "insert")
    void insert(BalanceUserCoinVolume balanceUserCoinVolume);

    @SelectProvider(type = BalanceUserCoinVolumeSqlBuild.class, method = "findById")
    BalanceUserCoinVolume findById(String id);

    @UpdateProvider(type = BalanceUserCoinVolumeSqlBuild.class, method = "updateById")
    long updateById(BalanceUserCoinVolume balanceUserCoinVolume);

    @Select("select " + BalanceUserCoinVolumeSqlBuild.columns + " from js_plat_user_coin_balance where  user_id = #{userId} order by create_date desc")
    List<BalanceUserCoinVolume> findByUserId(@Param("userId") String userId);

    @Select("select " + BalanceUserCoinVolumeSqlBuild.columns + " from js_plat_user_coin_balance where  user_id = #{userId} and  coin_symbol = #{coinSymbol} order by create_date desc")
    List<BalanceUserCoinVolume> findByUserIdAndCoin(@Param("userId") String userId,@Param("coinSymbol") String coinSymbol);

    @Select("select " + BalanceUserCoinVolumeSqlBuild.columns + " from js_plat_user_coin_balance  where coin_balance>=200  order by create_date desc")
    List<BalanceUserCoinVolume> findAll();

    @Select("select " + BalanceUserCoinVolumeSqlBuild.columns + " from js_plat_user_coin_balance    order by create_date desc")
    List<BalanceUserCoinVolume> findCoinAll();

    @Select("select " + BalanceUserCoinVolumeSqlBuild.columns + " from js_plat_user_coin_balance where coin_balance>=200 and refer_id = #{userId} and coin_symbol = #{coinSymbol} ORDER BY create_date DESC ")
    List<BalanceUserCoinVolume> findInvitesById(String userId, String coinSymbol);

    @Select("select " + BalanceUserCoinVolumeSqlBuild.columns + " from js_plat_user_coin_balance where   coin_symbol = #{coinSymbol} order by create_date desc")
    List<BalanceUserCoinVolume> findByCoin(@Param("coinSymbol") String coinSymbol);

//    @Select("select "+ BalanceUserCoinVolumeSqlBuild.columns +",(@i := @i + 1) as ordNum from js_plat_user_coin_balance ,(select   @i:=0)  t2  order by coin_balance desc  LIMIT 30")
//    List<BalanceUserCoinVolume> findByRank();
    @Select("select tt.coinBalance,tt.userId,tt2.mobile,tt2.mail from (select sum(t.deposit_value) coinBalance ,t.user_id userId FROM js_plat_user_coin_balance t  GROUP BY t.user_id order BY coinBalance desc  ,t.user_id asc LIMIT 30) tt,js_plat_user tt2 where tt.userId=tt2.id ")
    List<BalanceUserCoinVolume> findByRank();

    @Select("select count(DISTINCT t.user_id) from js_plat_user_coin_balance t")
    int findByCountNum();

    @Select("select " + BalanceUserCoinVolumeSqlBuild.columns + " from js_plat_user_coin_balance where refer_id = #{userId}  ORDER BY create_date DESC ")
    List<BalanceUserCoinVolume> findInvitesByUserId(String userId);

    @Select("select sum(t.deposit_value) coinBalance ,t.user_id userId FROM js_plat_user_coin_balance t  GROUP BY t.user_id order BY coinBalance desc  ,t.user_id asc LIMIT 10")
    List<BalanceUserCoinVolume> findByAllRank();

    @Delete("DELETE FROM js_plat_user_coin_balance WHERE id IN ( SELECT t2.balance_id FROM js_plat_user_coin_balancechange t2 WHERE t2.id =#{changeId} )")
    long deleteByBalanceId(@Param("changeId") String changeId);
}
