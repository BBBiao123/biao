package com.biao.mapper;

import com.biao.entity.ReportTradeFreeRecord;
import com.biao.sql.build.ReportTradeFreeRecordSqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportTradeFreeRecordDao {

    @InsertProvider(type = ReportTradeFreeRecordSqlBuild.class, method = "insert")
    void insert(ReportTradeFreeRecord reportTradeFreeRecord);

    @InsertProvider(type = ReportTradeFreeRecordSqlBuild.class, method = "batchInsert")
    void batchInsert(@Param("listValues") List<ReportTradeFreeRecord> reportTradeFreeRecords);
}
