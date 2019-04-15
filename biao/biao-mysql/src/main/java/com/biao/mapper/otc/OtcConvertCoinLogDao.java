package com.biao.mapper.otc;

import com.biao.entity.otc.OtcConvertCoinLog;
import com.biao.sql.build.otc.OtcConvertCoinLogSqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OtcConvertCoinLogDao {

    @InsertProvider(type = OtcConvertCoinLogSqlBuild.class, method = "insert")
    void insert(OtcConvertCoinLog otcConvertCoinLog);
}
