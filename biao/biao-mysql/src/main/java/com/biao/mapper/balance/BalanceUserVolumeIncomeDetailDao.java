package com.biao.mapper.balance;

import com.biao.entity.balance.BalanceUserCoinVolumeDetail;
import com.biao.entity.balance.BalanceUserVolumeIncomeDetail;
import com.biao.sql.build.balance.BalanceUserCoinVolumeDetailSqlBuild;
import com.biao.sql.build.balance.BalanceUserVolumeIncomeDetailSqlBuild;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface BalanceUserVolumeIncomeDetailDao {

    @InsertProvider(type = BalanceUserVolumeIncomeDetailSqlBuild.class, method = "insert")
    void insert(BalanceUserVolumeIncomeDetail balanceUserCoinVolumeDetail);

    @SelectProvider(type = BalanceUserVolumeIncomeDetailSqlBuild.class, method = "findById")
    BalanceUserVolumeIncomeDetail findById(String id);

    @UpdateProvider(type = BalanceUserVolumeIncomeDetailSqlBuild.class, method = "updateById")
    long updateById(BalanceUserVolumeIncomeDetail balanceUserCoinVolumeDetail);

    @Select("select " + BalanceUserVolumeIncomeDetailSqlBuild.columns + " from js_plat_user_income_incomedetail where  user_id = #{userId}  and DATEDIFF(income_date,NOW())<=0 and DATEDIFF(income_date,NOW())>-10 order by income_date desc,reward_type ")
    List<BalanceUserVolumeIncomeDetail> findByUserId(@Param("userId") String userId);

    @Select("select " + BalanceUserVolumeIncomeDetailSqlBuild.columns + " from js_plat_user_income_incomedetail where  user_id = #{userId} and coin_symbol = #{coinSymbol} and version=1 order by create_date desc")
    List<BalanceUserCoinVolumeDetail> findByUserIdAndCoin(@Param("userId") String userId, @Param("coinSymbol") String coinSymbol);

    @Select("select " + BalanceUserVolumeIncomeDetailSqlBuild.columns + " from js_plat_user_income_incomedetail where  version=1 order by create_date desc")
    List<BalanceUserVolumeIncomeDetail> findAll();

    @Select("SELECT max(t.team_record) FROM js_plat_user_income_incomedetail t WHERE t.refer_id=#{referId}  and t.coin_symbol=#{coinSymbol} and t.version=1 ")
    BigDecimal findByReferId(@Param("referId") String referId, @Param("coinSymbol") String coinSymbol);

    @Select("SELECT " + BalanceUserVolumeIncomeDetailSqlBuild.columns + " FROM js_plat_user_income_incomedetail t WHERE t.refer_id=#{referId}  and t.coin_symbol=#{coinSymbol} and t.version=1 ")
    List<BalanceUserVolumeIncomeDetail>  findAllByReferId(@Param("referId") String referId, @Param("coinSymbol") String coinSymbol);

    @Select("select " + BalanceUserVolumeIncomeDetailSqlBuild.columns + " from js_plat_user_income_incomedetail where  team_level=5 and coin_symbol=#{coinSymbol} and  version=1 order by create_date desc")
    List<BalanceUserVolumeIncomeDetail> findGlobalByCoin(@Param("coinSymbol") String coinSymbol);

    @Select("select " + BalanceUserVolumeIncomeDetailSqlBuild.columns + " from js_plat_user_income_incomedetail t2 WHERE (t2.refer_id NOT IN" +
            "( SELECT t.user_id FROM js_plat_user_coin_incomedetail t where t.version=1 ) or  t2.refer_id is null )and t2.version=1  order by t2.create_date desc")
    List<BalanceUserVolumeIncomeDetail> findSuprer();
}
