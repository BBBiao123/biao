package com.biao.mapper.mkcommon;

import com.biao.entity.mkcommon.MkCommonPlatIncomeLog;
import com.biao.sql.build.mkcommon.MkCommonPlatIncomeLogBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MkCommonPlatIncomeLogDao {

    @InsertProvider(type = MkCommonPlatIncomeLogBuild.class, method = "insert")
    void insert(MkCommonPlatIncomeLog commonCoinRate);

    @InsertProvider(type = MkCommonPlatIncomeLogBuild.class, method = "batchInsert")
    void insertBatch(@Param("listValues") List<MkCommonPlatIncomeLog> incomeLogs);
}
