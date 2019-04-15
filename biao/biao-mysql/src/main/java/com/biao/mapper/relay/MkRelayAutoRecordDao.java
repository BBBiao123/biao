package com.biao.mapper.relay;

import com.biao.entity.relay.MkRelayAutoRecord;
import com.biao.sql.build.relay.MkRelayAutoRecordSqlBuild;
import com.biao.sql.build.relay.MkRelayTaskRecordSqlBuild;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MkRelayAutoRecordDao {

    @InsertProvider(type = MkRelayAutoRecordSqlBuild.class, method = "insert")
    long insert(MkRelayAutoRecord mkRelayAutoRecord);

    @UpdateProvider(type = MkRelayTaskRecordSqlBuild.class, method = "updateById")
    long update(MkRelayAutoRecord mkRelayAutoRecord);

    @SelectProvider(type = MkRelayAutoRecordSqlBuild.class, method = "findById")
    MkRelayAutoRecord findById(@Param("id") String id);

    @Select("select " + MkRelayAutoRecordSqlBuild.columns + " from mk_relay_auto_record t where (t.mail is not null or t.mobile is not null) and t.reach_date is not null and t.candidate_id = #{candidateId} order by t.create_date desc limit 1")
    MkRelayAutoRecord findByCandidateId(@Param("candidateId") String candidateId);

}
