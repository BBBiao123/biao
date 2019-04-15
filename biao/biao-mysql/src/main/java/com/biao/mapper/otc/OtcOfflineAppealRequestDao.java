package com.biao.mapper.otc;

import com.biao.entity.otc.OtcOfflineAppealRequest;
import com.biao.sql.build.otc.OtcOfflineAppealRequestSqlBuild;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OtcOfflineAppealRequestDao {

    @InsertProvider(type = OtcOfflineAppealRequestSqlBuild.class, method = "insert")
    int insert(OtcOfflineAppealRequest otcOfflineAppealRequest);

    @SelectProvider(type = OtcOfflineAppealRequestSqlBuild.class, method = "findById")
    OtcOfflineAppealRequest findById(String id);

    @Select("SELECT " + OtcOfflineAppealRequestSqlBuild.columns + " FROM otc_offline_appeal_request t WHERE t.batch_no = #{batchNo} limit 1")
    OtcOfflineAppealRequest findByBatchNo(@Param("batchNo") String batchNo);

}
