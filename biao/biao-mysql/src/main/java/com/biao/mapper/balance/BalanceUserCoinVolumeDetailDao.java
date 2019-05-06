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

    @Select("SELECT max(t.team_record) FROM js_plat_user_coin_incomedetail t WHERE t.user_id IN ( " +
            " SELECT t2.user_id FROM js_plat_user_coin_balance t2 where t2.refer_id=#{referId} ) and t.coin_symbol=#{coinSymbol} and t.version=1 ")
    BigDecimal findByReferId(@Param("referId") String referId, @Param("coinSymbol") String coinSymbol);

    @Select("SELECT " + BalanceUserCoinVolumeDetailSqlBuild.columns + " FROM js_plat_user_coin_incomedetail t WHERE t.user_id IN ( " +
            " SELECT t2.user_id FROM js_plat_user_coin_balance t2 where t2.refer_id=#{referId} ) and t.coin_symbol=#{coinSymbol} and t.version=1 ")
    List<BalanceUserCoinVolumeDetail>  findAllByReferId(@Param("referId") String referId, @Param("coinSymbol") String coinSymbol);

    @Select("select " + BalanceUserCoinVolumeDetailSqlBuild.columns + " from js_plat_user_coin_incomedetail where  team_level=5 and  version=1 order by create_date desc")
    List<BalanceUserCoinVolumeDetail> findGlobalByCoin();
}
