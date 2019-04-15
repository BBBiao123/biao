package com.biao.mapper.relay;

import com.biao.entity.relay.MkRelayTaskRecord;
import com.biao.sql.build.relay.MkRelayTaskRecordSqlBuild;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

@Mapper
public interface MkRelayTaskRecordDao {

    @InsertProvider(type = MkRelayTaskRecordSqlBuild.class, method = "insert")
    long insert(MkRelayTaskRecord MkRelayTaskRecord);

    @UpdateProvider(type = MkRelayTaskRecordSqlBuild.class, method = "updateById")
    long update(MkRelayTaskRecord MkRelayTaskRecord);

    @SelectProvider(type = MkRelayTaskRecordSqlBuild.class, method = "findById")
    MkRelayTaskRecord findById(@Param("id") String id);

    @Select("select " + MkRelayTaskRecordSqlBuild.columns + " from mk_relay_task_record where status = 1 and DATEDIFF(create_date, #{curDateTime}) = 0 order by create_date desc limit 1")
    MkRelayTaskRecord findLastOneByDate(@Param("curDateTime") LocalDateTime curDateTime);

    @Select("select count(1) from mk_relay_task_record where status = 1 and prize_number > 0 and create_date > #{dateTime}")
    Integer countByDate(@Param("dateTime") LocalDateTime dateTime);

}
