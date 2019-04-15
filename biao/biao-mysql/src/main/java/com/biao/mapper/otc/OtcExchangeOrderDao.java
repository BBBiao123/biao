package com.biao.mapper.otc;

import com.biao.entity.otc.OtcExchangeOrder;
import com.biao.sql.build.otc.OtcExchangeOrderSqlBuild;
import org.apache.ibatis.annotations.*;


@Mapper
public interface OtcExchangeOrderDao {

    @InsertProvider(type = OtcExchangeOrderSqlBuild.class, method = "insert")
    void insert(OtcExchangeOrder otcExchangeOrder);

    @SelectProvider(type = OtcExchangeOrderSqlBuild.class, method = "findById")
    OtcExchangeOrder findById(String id);

    @Select("SELECT " + OtcExchangeOrderSqlBuild.columns + " FROM otc_exchange_request t WHERE t.batch_no = #{batchNo} limit 1")
    OtcExchangeOrder findByBatchNo(@Param("batchNo") String batchNo);

    @Update("UPDATE otc_exchange_request SET real_volume = #{realVolume}, fee_volume = #{feeVolume}, ask_fee_volume = #{askFeeVolume}, status = #{status}, result = #{result} WHERE t.id = #{id} AND update_date = #{updateDate} ")
    long updateExchange(OtcExchangeOrder otcExchangeOrder);
}
