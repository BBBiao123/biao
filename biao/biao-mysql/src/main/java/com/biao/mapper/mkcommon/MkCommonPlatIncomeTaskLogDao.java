package com.biao.mapper.mkcommon;

import com.biao.entity.mkcommon.MkCommonPlatIncomeTaskLog;
import com.biao.sql.build.mkcommon.MkCommonPlatIncomeTaskLogBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MkCommonPlatIncomeTaskLogDao {

    @InsertProvider(type = MkCommonPlatIncomeTaskLogBuild.class, method = "insert")
    void insert(MkCommonPlatIncomeTaskLog commonCoinRate);

    @InsertProvider(type = MkCommonPlatIncomeTaskLogBuild.class, method = "batchInsert")
    void insertBatch(@Param("listValues") List<MkCommonPlatIncomeTaskLog> taskLogs);

    @Select("SELECT " + MkCommonPlatIncomeTaskLogBuild.columns + " FROM mk_common_plat_income_task_log t WHERE t.status = '1' ORDER BY t.end_date DESC LIMIT 1")
    MkCommonPlatIncomeTaskLog findLately();

}
