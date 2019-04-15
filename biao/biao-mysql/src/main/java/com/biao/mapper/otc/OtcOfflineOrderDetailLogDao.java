package com.biao.mapper.otc;

import com.biao.entity.otc.OtcOfflineOrderDetailLog;
import com.biao.sql.build.otc.OtcOfflineOrderDetailLogSqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OtcOfflineOrderDetailLogDao {

    @InsertProvider(type = OtcOfflineOrderDetailLogSqlBuild.class, method = "insert")
    void insert(OtcOfflineOrderDetailLog otcOfflineOrderDetail);
}
