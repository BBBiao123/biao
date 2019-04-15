package com.biao.mapper;

import com.biao.entity.OfflineCoinTaskRecord;
import com.biao.sql.build.OfflineCoinTaskRecordSqlBuild;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

@Mapper
public interface OfflineCoinTaskRecordDao {

    @InsertProvider(type = OfflineCoinTaskRecordSqlBuild.class, method = "insert")
    long insert(OfflineCoinTaskRecord offlineCoinTaskRecord);

    @UpdateProvider(type = OfflineCoinTaskRecordSqlBuild.class, method = "updateById")
    long update(OfflineCoinTaskRecord offlineCoinTaskRecord);

    @SelectProvider(type = OfflineCoinTaskRecordSqlBuild.class, method = "findById")
    OfflineCoinTaskRecord findById(@Param("id") String id);

    @Select("select " + OfflineCoinTaskRecordSqlBuild.columns + " from js_plat_offline_coin_task_record where status = 1 and DATE(create_date) = DATE(#{createDate}) limit 1")
    OfflineCoinTaskRecord findByCreateDate(@Param("createDate") LocalDateTime createDate);


}
