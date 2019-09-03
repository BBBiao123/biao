package com.biao.mapper.balance;

import com.biao.entity.balance.BalanceChangeUserCoinVolume;
import com.biao.sql.build.balance.BalanceChangeUserCoinVolumeSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BalanceChangeUserCoinVolumeDao {

    @InsertProvider(type = BalanceChangeUserCoinVolumeSqlBuild.class, method = "insert")
    void insert(BalanceChangeUserCoinVolume balanceUserCoinVolume);

    @SelectProvider(type = BalanceChangeUserCoinVolumeSqlBuild.class, method = "findById")
    BalanceChangeUserCoinVolume findById(String id);

    @UpdateProvider(type = BalanceChangeUserCoinVolumeSqlBuild.class, method = "updateById")
    long updateById(BalanceChangeUserCoinVolume balanceUserCoinVolume);


    @Select("select " + BalanceChangeUserCoinVolumeSqlBuild.columns + " from js_plat_user_coin_balancechange where flag=0    order by create_date desc LIMIT 30 ")
    List<BalanceChangeUserCoinVolume> findAll();

    @Select("select " + BalanceChangeUserCoinVolumeSqlBuild.columns + " from js_plat_user_coin_balancechange where user_id = #{userId} and  coin_symbol = #{coinSymbol} and flag=0    order by create_date desc")
    List<BalanceChangeUserCoinVolume> findByUserId(String userId, String coinSymbol);

    @Select("select " + BalanceChangeUserCoinVolumeSqlBuild.columns + " from js_plat_user_coin_balancechange where user_id = #{userId} and flag=0   and take_out_date is null order by create_date desc")
    List<BalanceChangeUserCoinVolume> findChangeByUserId(String userId);

    @Select("select " + BalanceChangeUserCoinVolumeSqlBuild.columns + " from js_plat_user_coin_balancechange where user_id = #{userId}   order by create_date desc")
    List<BalanceChangeUserCoinVolume> findChangeAllByUserId(String userId);

    @Select("select " + BalanceChangeUserCoinVolumeSqlBuild.columns + " from js_plat_user_coin_balancechange t where  t.flag=2  and take_out_date is null and  TIMESTAMPDIFF(DAY,t.create_date,NOW())>=t.contract_time  order by create_date desc")
    List<BalanceChangeUserCoinVolume> findChangeLockOut();

    @Select("select " + BalanceChangeUserCoinVolumeSqlBuild.columns + " from js_plat_user_coin_balancechange t where user_id = #{userId} and   t.flag=2  and t.take_out_date is null and  TIMESTAMPDIFF(DAY,t.create_date,NOW())<t.contract_time  order by create_date desc")
    List<BalanceChangeUserCoinVolume> findChangeLockByUserId(String userId);
}
