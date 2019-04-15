package com.biao.mapper.otc;

import com.biao.entity.otc.OtcOfflineOrderPay;
import com.biao.sql.build.otc.OtcOfflineOrderPaySqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OtcOfflineOrderPayDao {
    @InsertProvider(type = OtcOfflineOrderPaySqlBuild.class, method = "insert")
    void insert(OtcOfflineOrderPay otcOfflineOrderPay);

    @InsertProvider(type = OtcOfflineOrderPaySqlBuild.class, method = "batchInsert")
    void insertBatch(@Param("listValues") List<OtcOfflineOrderPay> otcOfflineOrderPays);

    @SelectProvider(type = OtcOfflineOrderPaySqlBuild.class, method = "findById")
    OtcOfflineOrderPay findById(String id);

    @UpdateProvider(type = OtcOfflineOrderPaySqlBuild.class, method = "updateById")
    long updateById(OtcOfflineOrderPay otcOfflineOrderPay);

    @Select("SELECT " + OtcOfflineOrderPaySqlBuild.columns + " FROM otc_offline_order_pay t WHERE t.order_id = #{orderId} ")
    List<OtcOfflineOrderPay> findByOrderId(String orderId);
}
