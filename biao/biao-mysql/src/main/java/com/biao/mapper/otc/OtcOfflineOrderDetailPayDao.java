package com.biao.mapper.otc;

import com.biao.entity.otc.OtcOfflineOrderDetailPay;
import com.biao.sql.build.otc.OtcOfflineOrderDetailPaySqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OtcOfflineOrderDetailPayDao {

    @InsertProvider(type = OtcOfflineOrderDetailPaySqlBuild.class, method = "insert")
    void insert(OtcOfflineOrderDetailPay otcOfflineOrderDetailPay);

    @InsertProvider(type = OtcOfflineOrderDetailPaySqlBuild.class, method = "batchInsert")
    void insertBatch(@Param("listValues") List<OtcOfflineOrderDetailPay> otcOfflineOrderDetailPays);

    @Select("SELECT " + OtcOfflineOrderDetailPaySqlBuild.columns + " FROM otc_offline_order_detail_pay t WHERE t.sub_order_id = #{subOrderId} ")
    List<OtcOfflineOrderDetailPay> findBySubOrderId(@Param("subOrderId") String subOrderId);
}
