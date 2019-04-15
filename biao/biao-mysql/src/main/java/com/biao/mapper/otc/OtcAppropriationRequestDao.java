package com.biao.mapper.otc;

import com.biao.entity.otc.OtcAppropriationRequest;
import com.biao.sql.build.otc.OtcAppropriationRequestSqlBuild;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OtcAppropriationRequestDao {

    @InsertProvider(type = OtcAppropriationRequestSqlBuild.class, method = "insert")
    int insert(OtcAppropriationRequest otcAppropriationRequest);

    @SelectProvider(type = OtcAppropriationRequestSqlBuild.class, method = "findById")
    OtcAppropriationRequest findById(String id);

    @Select("SELECT " + OtcAppropriationRequestSqlBuild.columns + " FROM otc_appropriation_request t WHERE t.batch_no = #{batchNo}")
    OtcAppropriationRequest findByBatchNo(@Param("batchNo") String batchNo);
}
