package com.biao.mapper.otc;

import com.biao.entity.otc.OtcUserBank;
import com.biao.sql.build.otc.OtcUserBankSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OtcUserBankDao {

    @InsertProvider(type = OtcUserBankSqlBuild.class, method = "insert")
    void insert(OtcUserBank userBank);

    @SelectProvider(type = OtcUserBankSqlBuild.class, method = "findById")
    OtcUserBank findById(String id);

    @UpdateProvider(type = OtcUserBankSqlBuild.class, method = "updateById")
    long updateById(OtcUserBank userBank);

    @Select("<script>" +
            "SELECT " + OtcUserBankSqlBuild.columns + " FROM otc_user_bank t " +
            "WHERE t.user_id = #{userId} " +
            " <if test=\"type != null and type != ''\"> " +
            " AND t.type = #{type} " +
            " </if>" +
            " <if test=\"supportCurrencyCode != null and supportCurrencyCode != ''\"> " +
            " AND FIND_IN_SET(#{supportCurrencyCode}, t.support_currency_code) " +
            " </if>" +
            " <if test=\"status != null and status !=''\"> " +
            " AND t.status = #{status} " +
            " </if>" +
            "</script>")
    List<OtcUserBank> findByParam(OtcUserBank userBank);

    @Select("SELECT " + OtcUserBankSqlBuild.columns + " FROM otc_user_bank t WHERE t.user_id = #{userId} AND t.status = #{status}")
    List<OtcUserBank> findByUserIdAndStatus(@Param("userId") String userId, @Param("status") String status);

    @Select("SELECT " + OtcUserBankSqlBuild.columns + " FROM otc_user_bank t WHERE t.user_id = #{userId} AND t.status = '1' AND FIND_IN_SET(#{supportCurrencyCode}, t.support_currency_code) ")
    List<OtcUserBank> findByUserIdAndCurrencyCode(@Param("userId") String userId, @Param("supportCurrencyCode") String supportCurrencyCode);
}
