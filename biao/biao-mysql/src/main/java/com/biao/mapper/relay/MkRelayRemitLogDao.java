package com.biao.mapper.relay;

import com.biao.entity.relay.MkRelayRemitLog;
import com.biao.sql.build.relay.MkRelayRemitLogSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MkRelayRemitLogDao {

    @InsertProvider(type = MkRelayRemitLogSqlBuild.class, method = "insert")
    long insert(MkRelayRemitLog MkRelayRemitLog);

    @UpdateProvider(type = MkRelayRemitLogSqlBuild.class, method = "updateById")
    long update(MkRelayRemitLog MkRelayRemitLog);

    @SelectProvider(type = MkRelayRemitLogSqlBuild.class, method = "findById")
    MkRelayRemitLog findById(@Param("id") String id);

    @InsertProvider(type = MkRelayRemitLogSqlBuild.class, method = "batchInsert")
    long batchInsert(@Param("listValues") List<MkRelayRemitLog> list);

    @Select("select ifnull(mobile, mail) as mail, volume, user_type, create_date from mk_relay_remit_log where user_type in ('0','1') and is_remit = '1' order by create_date desc")
    List<MkRelayRemitLog> findList();

}
