package com.biao.mapper.balance;

import com.biao.entity.DepositLog;
import com.biao.entity.balance.BalanceUserCoinVolume;
import com.biao.pojo.DepdrawLogVO;
import com.biao.sql.build.DepositLogSqlBuild;
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

    @Select("select " + BalanceUserCoinVolumeSqlBuild.columns + " from js_plat_user_coin_balance where coin_balance>=200 and refer_id = #{userId} and coin_symbol = #{coinSymbol} ORDER BY create_date DESC ")
    List<BalanceUserCoinVolume> findInvitesById(String userId, String coinSymbol);

    @Select("select " + BalanceUserCoinVolumeSqlBuild.columns + " from js_plat_user_coin_balance where   coin_symbol = #{coinSymbol} order by create_date desc")
    List<BalanceUserCoinVolume> findByCoin(@Param("coinSymbol") String coinSymbol);
}
