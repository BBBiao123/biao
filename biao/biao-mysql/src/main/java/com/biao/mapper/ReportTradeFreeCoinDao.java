package com.biao.mapper;

import com.biao.entity.ReportTradeFreeCoin;
import com.biao.sql.build.ReportTradeFreeCoinSqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportTradeFreeCoinDao {

    @InsertProvider(type = ReportTradeFreeCoinSqlBuild.class, method = "insert")
    void insert(ReportTradeFreeCoin reportTradeFreeCoin);

    @InsertProvider(type = ReportTradeFreeCoinSqlBuild.class, method = "batchInsert")
    void batchInsert(@Param("listValues") List<ReportTradeFreeCoin> reportTradeFreeCoins);
}
