package com.biao.mapper.otc;

import com.biao.entity.otc.OtcVolumeChangeRequest;
import com.biao.sql.build.otc.OtcVolumeChangeRequestSqlBuild;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OtcVolumeChangeRequestDao {

    @InsertProvider(type= OtcVolumeChangeRequestSqlBuild.class, method = "insert")
    int insert(OtcVolumeChangeRequest otcAppropriationRequest);

    @SelectProvider(type=OtcVolumeChangeRequestSqlBuild.class,method = "findById")
    OtcVolumeChangeRequest findById(String id) ;

    @Select("SELECT " + OtcVolumeChangeRequestSqlBuild.columns + " FROM otc_volume_change_request t WHERE t.batch_no = #{batchNo}")
    OtcVolumeChangeRequest findByBatchNo(@Param("batchNo") String batchNo);
}
