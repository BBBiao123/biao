package com.biao.mapper.relay;

import com.biao.entity.relay.MkRelayPrizeConfig;
import com.biao.sql.build.relay.MkRelayPrizeConfigSqlBuild;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MkRelayPrizeConfigDao {

    @InsertProvider(type = MkRelayPrizeConfigSqlBuild.class, method = "insert")
    long insert(MkRelayPrizeConfig mkRelayPrizeConfig);

    @UpdateProvider(type = MkRelayPrizeConfigSqlBuild.class, method = "updateById")
    long update(MkRelayPrizeConfig mkRelayPrizeConfig);

    @SelectProvider(type = MkRelayPrizeConfigSqlBuild.class, method = "findById")
    MkRelayPrizeConfig findById(@Param("id") String id);

    @Select("select " + MkRelayPrizeConfigSqlBuild.columns + " from mk_relay_prize_config where status = 1 limit 1")
    MkRelayPrizeConfig findActiveOne();

}
