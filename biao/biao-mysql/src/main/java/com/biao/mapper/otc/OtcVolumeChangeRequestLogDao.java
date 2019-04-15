package com.biao.mapper.otc;

import com.biao.entity.otc.OtcVolumeChangeRequestLog;
import com.biao.sql.build.otc.OtcVolumeChangeRequestLogSqlBuild;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OtcVolumeChangeRequestLogDao {

    @InsertProvider(type= OtcVolumeChangeRequestLogSqlBuild.class, method = "insert")
    int insert(OtcVolumeChangeRequestLog otcAppropriationRequestLog);

    @SelectProvider(type=OtcVolumeChangeRequestLogSqlBuild.class,method = "findById")
    OtcVolumeChangeRequestLog findById(String id) ;

    @Select("SELECT " + OtcVolumeChangeRequestLogSqlBuild.columns + " FROM otc_volume_change_request_log t WHERE t.batch_no = #{batchNo}")
    OtcVolumeChangeRequestLog findByBatchNo(@Param("batchNo") String batchNo);
}
