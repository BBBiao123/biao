package com.biao.mapper.otc;

import com.biao.entity.otc.OtcConvertCoin;
import com.biao.sql.build.otc.OtcConvertCoinSqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OtcConvertCoinDao {

    @InsertProvider(type = OtcConvertCoinSqlBuild.class, method = "insert")
    void insert(OtcConvertCoin otcConvertCoin);

    @Select("SELECT " + OtcConvertCoinSqlBuild.columns + " FROM otc_convert_coin_request t WHERE t.batch_no = #{batchNo} limit 1")
    OtcConvertCoin findByBatchNo(@Param("batchNo") String batchNo);
}
