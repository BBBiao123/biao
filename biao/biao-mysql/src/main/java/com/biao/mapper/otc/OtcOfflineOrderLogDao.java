package com.biao.mapper.otc;

import com.biao.entity.otc.OtcOfflineOrderLog;
import com.biao.sql.build.otc.OtcOfflineOrderLogSqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OtcOfflineOrderLogDao {

    @InsertProvider(type = OtcOfflineOrderLogSqlBuild.class, method = "insert")
    void insert(OtcOfflineOrderLog otcOfflineOrderLog);
}
