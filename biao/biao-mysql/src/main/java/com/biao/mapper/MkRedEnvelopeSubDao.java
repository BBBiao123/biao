package com.biao.mapper;

import com.biao.entity.MkRedEnvelopeSub;
import com.biao.sql.build.MkRedEnvelopeSubSqlBuild;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface MkRedEnvelopeSubDao {

    @InsertProvider(type = MkRedEnvelopeSubSqlBuild.class, method = "insert")
    void insert(MkRedEnvelopeSub mkRedEnvelopeSub);

    @SelectProvider(type = MkRedEnvelopeSubSqlBuild.class, method = "findById")
    MkRedEnvelopeSub findById(String id);

    @InsertProvider(type = MkRedEnvelopeSubSqlBuild.class, method = "batchInsert")
    long insertBatch(@Param("listValues") List<MkRedEnvelopeSub> mkRedEnvelopeSubList);

    @Select("SELECT " + MkRedEnvelopeSubSqlBuild.columns + " FROM mk_red_envelope_sub t WHERE t.receive_user_id = #{userId} order by t.update_date desc")
    List<MkRedEnvelopeSub> findByUserId(@Param("userId") String userId);

    @Select("SELECT " + MkRedEnvelopeSubSqlBuild.columns + " from mk_red_envelope_sub where receive_user_id = #{userId} and envelope_id = #{redEnvelopeId} limit 1")
    MkRedEnvelopeSub findByUserIdAndRedEnvelopeId(@Param("userId") String userId, @Param("redEnvelopeId") String redEnvelopeId);

    @Select("SELECT " + MkRedEnvelopeSubSqlBuild.columns + " FROM mk_red_envelope_sub t WHERE t.envelope_id = #{envelopeId} and t.status = '0'")
    List<MkRedEnvelopeSub> findActiveList(@Param("envelopeId") String envelopeId);

    @Select("SELECT " + MkRedEnvelopeSubSqlBuild.columns + " FROM mk_red_envelope_sub t WHERE t.envelope_id = #{envelopeId} and t.status = '1'")
    List<MkRedEnvelopeSub> findReceiveList(@Param("envelopeId") String envelopeId);

    @Select("SELECT max(volume) FROM mk_red_envelope_sub t WHERE t.envelope_id = #{envelopeId} and t.status = '1'")
    BigDecimal findBestOne(@Param("envelopeId") String envelopeId);

    @Update("update mk_red_envelope_sub set receive_user_id = #{userId}, receive_mobile = #{mobile}, receive_mail = #{mail}, receive_real_name = #{realName}, status = '1', version = version + 1 where id = #{id} and version = #{version} and status = '0'")
    long updateOpenOne(@Param("userId") String userId, @Param("mobile") String mobile, @Param("mail") String mail, @Param("realName") String realName, @Param("id") String id, @Param("version") Long version);

    @Update("update mk_red_envelope_sub set status = '2', version = version + 1 where envelope_id = #{envelopeId} and status = '0'")
    long updateExpired(@Param("envelopeId") String envelopeId);



}
