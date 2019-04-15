package com.biao.mapper.otc;

import com.biao.entity.otc.OtcAccountSecret;
import com.biao.sql.build.otc.OtcAccountSecretSqlBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface OtcAccountSecretDao {

    @SelectProvider(type = OtcAccountSecretSqlBuild.class, method = "findById")
    OtcAccountSecret findById(String id);

    @Select("SELECT " + OtcAccountSecretSqlBuild.columns + " FROM otc_account_secret t WHERE t.publish_source = #{publishSource} limit 1")
    OtcAccountSecret findByPublishSource(@Param("publishSource") String publishSource);
}
