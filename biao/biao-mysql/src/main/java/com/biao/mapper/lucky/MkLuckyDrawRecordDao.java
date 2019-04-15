package com.biao.mapper.lucky;

import com.biao.entity.lucky.MkLuckyDrawRecord;
import com.biao.sql.build.lucky.MkLuckyDrawRecordSqlBuild;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

@Mapper
public interface MkLuckyDrawRecordDao {

    @InsertProvider(type = MkLuckyDrawRecordSqlBuild.class, method = "insert")
    long insert(MkLuckyDrawRecord mkLuckyDrawRecord);

    @UpdateProvider(type = MkLuckyDrawRecordSqlBuild.class, method = "updateById")
    long update(MkLuckyDrawRecord mkLuckyDrawRecord);

    @SelectProvider(type = MkLuckyDrawRecordSqlBuild.class, method = "findById")
    MkLuckyDrawRecord findById(@Param("id") String id);

    @Select("select " + MkLuckyDrawRecordSqlBuild.columns + " from mk_relay_task_record where status = 1 and DATEDIFF(create_date, #{curDateTime}) = 0 order by create_date desc limit 1")
    MkLuckyDrawRecord findLastOneByDate(@Param("curDateTime") LocalDateTime curDateTime);

}
