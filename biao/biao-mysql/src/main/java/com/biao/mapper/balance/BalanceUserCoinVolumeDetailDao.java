package com.biao.mapper.balance;

import com.biao.entity.balance.BalanceUserCoinVolumeDetail;
import com.biao.sql.build.balance.BalanceUserCoinVolumeDetailSqlBuild;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
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

    @Select("select " + BalanceUserCoinVolumeDetailSqlBuild.columns + " from js_plat_user_coin_incomedetail where  version=1 order by create_date desc")
    List<BalanceUserCoinVolumeDetail> findAll();

    @Select("SELECT max(t.team_coin_record) FROM js_plat_user_coin_incomedetail t WHERE t.refer_id=#{referId}  and t.coin_symbol=#{coinSymbol} and t.version=1 ")
    BigDecimal findByReferId(@Param("referId") String referId, @Param("coinSymbol") String coinSymbol);

    @Select("SELECT " + BalanceUserCoinVolumeDetailSqlBuild.columns + " FROM js_plat_user_coin_incomedetail t WHERE t.refer_id=#{referId}  and t.coin_symbol=#{coinSymbol} and t.version=1 ")
    List<BalanceUserCoinVolumeDetail>  findAllByReferId(@Param("referId") String referId, @Param("coinSymbol") String coinSymbol);

    @Select("select " + BalanceUserCoinVolumeDetailSqlBuild.columns + " from js_plat_user_coin_incomedetail where  team_level=5 and coin_symbol=#{coinSymbol} and  version=1 order by create_date desc")
    List<BalanceUserCoinVolumeDetail> findGlobalByCoin(@Param("coinSymbol") String coinSymbol);

    @Select("select " + BalanceUserCoinVolumeDetailSqlBuild.columns + " from js_plat_user_coin_incomedetail t2 WHERE (t2.refer_id NOT IN" +
            "( SELECT t.user_id FROM js_plat_user_coin_incomedetail t where t.version=1 ) or  t2.refer_id is null )and t2.version=1  order by t2.create_date desc")
    List<BalanceUserCoinVolumeDetail> findSuprer();

    @Select("SELECT " + BalanceUserCoinVolumeDetailSqlBuild.columns + " FROM js_plat_user_coin_incomedetail t WHERE t.team_level >= 1 and t.version=1 AND  " +
            "t.user_id NOT IN ( SELECT t2.refer_id FROM js_plat_user_coin_incomedetail t2 where t2.team_level>=1 and t2.version=1) order by t.create_date desc")
    List<BalanceUserCoinVolumeDetail> findByNotReferId();

    @Select("select " + BalanceUserCoinVolumeDetailSqlBuild.columns + " from js_plat_user_coin_incomedetail where  user_id = #{userId} and version=1 order by create_date desc")
    List<BalanceUserCoinVolumeDetail> findByVersionUserId(@Param("userId") String userId);
}
