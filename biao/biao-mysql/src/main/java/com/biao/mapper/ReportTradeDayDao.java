package com.biao.mapper;

import com.biao.entity.ReportTradeDay;
import com.biao.sql.build.ReportTradeDaySqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface ReportTradeDayDao {

    @InsertProvider(type = ReportTradeDaySqlBuild.class, method = "insert")
    void insert(ReportTradeDay reportTradeDay);

    @InsertProvider(type = ReportTradeDaySqlBuild.class, method = "batchInsert")
    void batchInsert(@Param("listValues") List<ReportTradeDay> reportTradeDays);

    @SelectProvider(type = ReportTradeDaySqlBuild.class, method = "findById")
    ReportTradeDay findById(String id);
}
