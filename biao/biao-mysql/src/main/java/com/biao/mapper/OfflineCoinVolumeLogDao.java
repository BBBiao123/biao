package com.biao.mapper;

import com.biao.entity.OfflineCoinVolumeLog;
import com.biao.sql.build.OfflineCoinVolumeLogSqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OfflineCoinVolumeLogDao {

    @InsertProvider(type = OfflineCoinVolumeLogSqlBuild.class, method = "insert")
    void insert(OfflineCoinVolumeLog otcOfflineOrderDetail);

}
