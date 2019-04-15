package com.biao.mapper;

import com.biao.entity.ReportTradeFree;
import com.biao.entity.ReportTradeFreeCoin;
import com.biao.sql.build.ReportTradeFreeSqlBuild;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ReportTradeFreeDao {

    @InsertProvider(type = ReportTradeFreeSqlBuild.class, method = "insert")
    void insert(ReportTradeFree reportTradeFree);

    @InsertProvider(type = ReportTradeFreeSqlBuild.class, method = "batchInsert")
    void batchInsert(@Param("listValues") List<ReportTradeFree> reportTradeFrees);

    @SelectProvider(type = ReportTradeFreeSqlBuild.class, method = "findById")
    ReportTradeFree findById(String id);

    @Select("select sum(sum_fee) as sum_fee,coin from report_trade_fee where count_time = #{countTime} group by coin")
    List<ReportTradeFreeCoin> countTradeFreeCoins(LocalDate countTime);

    @Select("select coin_main,coin_other from report_trade_fee where count_time = #{countTime} group by coin_main,coin_other")
    List<ReportTradeFree> groupTradeListByCountTime(LocalDate countTime);

    @Select("select " + ReportTradeFreeSqlBuild.columns + " from report_trade_fee where count_time = #{countTime} and coin_main = #{coinMain} and coin_other = #{coinOther}")
    List<ReportTradeFree> groupTradeList(@Param("countTime") LocalDate countTime, @Param("coinMain") String coinMain, @Param("coinOther") String coinOther);
}
