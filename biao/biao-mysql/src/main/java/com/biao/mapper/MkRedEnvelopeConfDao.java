package com.biao.mapper;

import com.biao.entity.MkRedEnvelopeConf;
import com.biao.sql.build.MkRedEnvelopeConfSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MkRedEnvelopeConfDao {

    @InsertProvider(type = MkRedEnvelopeConfSqlBuild.class, method = "insert")
    void insert(MkRedEnvelopeConf mkRedEnvelopeSub);

    @SelectProvider(type = MkRedEnvelopeConfSqlBuild.class, method = "findById")
    MkRedEnvelopeConf findById(String id);

    @Select("SELECT " + MkRedEnvelopeConfSqlBuild.columns + " FROM mk_red_envelope_conf where status = '1' and coin_symbol = #{coinSymbol} limit 1")
    MkRedEnvelopeConf findOneByCoinSymbol(@Param("coinSymbol") String coinSymbol);
    

}
