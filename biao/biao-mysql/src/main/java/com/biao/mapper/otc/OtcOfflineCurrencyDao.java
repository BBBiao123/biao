package com.biao.mapper.otc;

import com.biao.entity.otc.OtcOfflineCurrency;
import com.biao.sql.build.otc.OtcOfflineCurrencySqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OtcOfflineCurrencyDao {

    @InsertProvider(type = OtcOfflineCurrencySqlBuild.class, method = "insert")
    void insert(OtcOfflineCurrency currency);

    @SelectProvider(type = OtcOfflineCurrencySqlBuild.class, method = "findById")
    OtcOfflineCurrency findById(String id);

    @UpdateProvider(type = OtcOfflineCurrencySqlBuild.class, method = "updateById")
    long updateById(OtcOfflineCurrency currency);

    @Select("SELECT " + OtcOfflineCurrencySqlBuild.columns + " FROM otc_offline_currency WHERE status = '1'")
    List<OtcOfflineCurrency> findAll();

    @Select("SELECT " + OtcOfflineCurrencySqlBuild.columns + " FROM otc_offline_currency t WHERE t.code = #{code} ")
    OtcOfflineCurrency findByCode(String code);
}
