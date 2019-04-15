package com.biao.mapper.relay;

import com.biao.entity.relay.MkRelayAutoConfig;
import com.biao.sql.build.relay.MkRelayAutoConfigSqlBuild;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MkRelayAutoConfigDao {

    @InsertProvider(type = MkRelayAutoConfigSqlBuild.class, method = "insert")
    long insert(MkRelayAutoConfig mkRelayAutoConfig);

    @UpdateProvider(type = MkRelayAutoConfigSqlBuild.class, method = "updateById")
    long update(MkRelayAutoConfig mkRelayAutoConfig);

    @SelectProvider(type = MkRelayAutoConfigSqlBuild.class, method = "findById")
    MkRelayAutoConfig findById(@Param("id") String id);

    @Select("select " + MkRelayAutoConfigSqlBuild.columns + " from mk_relay_auto_config where status = 1 limit 1")
    MkRelayAutoConfig findActiveOne();

    @Select("select " + MkRelayAutoConfigSqlBuild.columns + " from mk_relay_auto_config where status = 0 limit 1")
    MkRelayAutoConfig findUnActiveOne();

}
