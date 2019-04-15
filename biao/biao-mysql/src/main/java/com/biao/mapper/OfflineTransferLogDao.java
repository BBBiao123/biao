package com.biao.mapper;

import com.biao.entity.OfflineTransferLog;
import com.biao.sql.build.OfflineTransferLogSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OfflineTransferLogDao {

    @InsertProvider(type = OfflineTransferLogSqlBuild.class, method = "insert")
    void insert(OfflineTransferLog offlineTransferLog);

    @SelectProvider(type = OfflineTransferLogSqlBuild.class, method = "findById")
    OfflineTransferLog findById(String id);

    @UpdateProvider(type = OfflineTransferLogSqlBuild.class, method = "updateById")
    long updateById(OfflineTransferLog offlineTransferLog);

    @Select("select " + OfflineTransferLogSqlBuild.columns + " from js_plat_offline_transfer_log where user_id = #{userId} and type < 90 order by create_date desc")
    List<OfflineTransferLog> findOfflineTransferLogByUserId(String userId);

    @Select("select " + OfflineTransferLogSqlBuild.columns + " from js_plat_offline_transfer_log where coin_id = #{coinId} and user_id = #{userId} and type < 90 order by create_date desc")
    List<OfflineTransferLog> findOfflineTransferLogByUserIdAndCoinId(@Param("userId") String userId, @Param("coinId") String coinId);
}
