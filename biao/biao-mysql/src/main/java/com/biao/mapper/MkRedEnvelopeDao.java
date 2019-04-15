package com.biao.mapper;

import com.biao.entity.MkRedEnvelope;
import com.biao.sql.build.MkRedEnvelopeSqlBuild;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface MkRedEnvelopeDao {

    @InsertProvider(type = MkRedEnvelopeSqlBuild.class, method = "insert")
    void insert(MkRedEnvelope mkRedEnvelope);

    @SelectProvider(type = MkRedEnvelopeSqlBuild.class, method = "findById")
    MkRedEnvelope findById(String id);

    @Select("SELECT " + MkRedEnvelopeSqlBuild.columns + " FROM mk_red_envelope t WHERE t.id = #{id} and t.user_id = #{userId} limit 1")
    MkRedEnvelope findByIdAndUserId(@Param("id") String id, @Param("userId") String userId);

    @Select("SELECT " + MkRedEnvelopeSqlBuild.columns + " FROM mk_red_envelope t WHERE t.user_id = #{userId} order by t.create_date desc")
    List<MkRedEnvelope> findByUserId(@Param("userId") String userId);

    @Select("SELECT " + MkRedEnvelopeSqlBuild.columns + " FROM mk_red_envelope t WHERE t.status = '0' and TIMESTAMPDIFF(HOUR, t.create_date, now()) >= 24 order by t.create_date desc")
    List<MkRedEnvelope> findExpiredList();

    @Select("SELECT " + MkRedEnvelopeSqlBuild.columns + " from mk_red_envelope where user_id = #{userId} and coin_id = #{coinId} limit 1")
    MkRedEnvelope findByUserIdAndCoinId(@Param("userId") String userId, @Param("coinId") String coinId);

    @Update("update mk_red_envelope set receive_number = receive_number + 1, receive_volume = receive_volume + #{volume}, version = version + 1 where id = #{id} and receive_number < total_number and receive_volume + #{volume} <= volume and status = '0'")
    long updateOpenOne(@Param("id") String id, @Param("volume") BigDecimal volume);

    @Update("update mk_red_envelope set status = '1', version = version + 1 where id = #{id} and receive_number = total_number and receive_volume <= volume and status = '0'")
    long updateForEnd(@Param("id") String id);

    @Update("update mk_red_envelope set status = '2', version = version + 1 where id = #{id} and status = '0' and version = #{version}")
    long updateExpiredOne(@Param("id") String id, @Param("version") Long version);


}
