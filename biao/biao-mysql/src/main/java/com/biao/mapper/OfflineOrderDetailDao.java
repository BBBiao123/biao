package com.biao.mapper;

import com.biao.entity.OfflineOrderDetail;
import com.biao.sql.build.OfflineOrderDetailSqlBuild;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OfflineOrderDetailDao {

    @InsertProvider(type = OfflineOrderDetailSqlBuild.class, method = "insert")
    void insert(OfflineOrderDetail order);

    @SelectProvider(type = OfflineOrderDetailSqlBuild.class, method = "findById")
    OfflineOrderDetail findById(String id);

    @UpdateProvider(type = OfflineOrderDetailSqlBuild.class, method = "updateById")
    long updateById(OfflineOrderDetail order);

    @Update("update js_plat_offline_order_detail o set o.status = #{status}, o.update_by = #{updateBy} where o.sub_order_id = #{subOrderId}")
    long updateStatusBySubOrderId(@Param("status") Integer status, @Param("subOrderId") String subOrderId, @Param("updateBy") String updateBy);

    @Update("update js_plat_offline_order_detail o set o.status = #{status}, o.update_by = #{updateBy},o.cancel_date =  #{cancelDate} where o.sub_order_id = #{subOrderId} AND o.update_date = #{updateDate}")
    long updateStatusAndCancelDateBySubOrderId(@Param("status") Integer status, @Param("subOrderId") String subOrderId, @Param("cancelDate") LocalDateTime cancelDate, @Param("updateDate") Timestamp updateDate, @Param("updateBy") String updateBy);

    @Update("update js_plat_offline_order_detail o set o.status = #{status},o.confirm_receipt_date=#{confirmReceiptDate}, o.update_by = #{updateBy} where o.sub_order_id = #{subOrderId} AND status = #{preStatus}")
    long updateStatusAndConfirmRecepitDateBySubOrderId(@Param("preStatus") Integer preStatus, @Param("status") Integer status, @Param("confirmReceiptDate") LocalDateTime confirmReceiptDate, @Param("subOrderId") String subOrderId, @Param("updateBy") String updateBy);

    @Update("update js_plat_offline_order_detail o set o.status = #{status} where o.sub_order_id = #{subOrderId} and o.status = #{sourceStatus}")
    long updateStatusBySubOrderIdAndStatus(@Param("status") Integer status, @Param("subOrderId") String subOrderId, @Param("sourceStatus") String sourceStatus);

    @Select("select " + OfflineOrderDetailSqlBuild.columns + " from js_plat_offline_order_detail where user_id = #{userId} and order_id =#{orderId} order by create_date desc")
    List<OfflineOrderDetail> findByOrderId(@Param("userId") String userId, @Param("orderId") String orderId);

    @Select("select " + OfflineOrderDetailSqlBuild.columns + " from js_plat_offline_order_detail where user_id = #{userId} and status = #{status} limit 1")
    OfflineOrderDetail findByUserIdAndStatus(@Param("userId") String userId, @Param("status") Integer status);

    @Select("select " + OfflineOrderDetailSqlBuild.columns + " from js_plat_offline_order_detail where user_id = #{userId} and status in (${status}) limit 1")
    OfflineOrderDetail findByUserIdAndStatusIn(@Param("userId") String userId, @Param("status") String status);

    @Select("select count(1) from js_plat_offline_order_detail where user_id = #{userId} and status in (${status}) limit 1")
    long countByUserIdAndStatusIn(@Param("userId") String userId, @Param("status") String status);

    @Select("select count(1) from js_plat_offline_order_detail where user_id = #{userId} and remarks =#{remarks} and status in (${status}) limit 1")
    long countByUserIdAndRemarkAndStatusIn(@Param("userId") String userId, @Param("remarks") String remarks, @Param("status") String status);

    @Select("select " + OfflineOrderDetailSqlBuild.columns + " from js_plat_offline_order_detail where user_id = #{userId} and sub_order_id = #{subOrderId} limit 1")
    OfflineOrderDetail findByUserIdAndOrderId(@Param("userId") String userId, @Param("subOrderId") String subOrderId);


    @Select("select " + OfflineOrderDetailSqlBuild.columns + " from js_plat_offline_order_detail where user_id = #{userId} order by create_date desc")
    List<OfflineOrderDetail> findMyOrderDetail(String userId);

    @Select("SELECT " + OfflineOrderDetailSqlBuild.columns + " FROM js_plat_offline_order_detail t WHERE t.`status` = #{status} AND t.create_date < #{curDateTime}  ORDER BY remarks ASC ")
    List<OfflineOrderDetail> findLongTimeOrderDetail(@Param("status") String status, @Param("curDateTime") LocalDateTime curDateTime);

    @Select("select " + OfflineOrderDetailSqlBuild.columns + " from js_plat_offline_order_detail where user_id = #{userId} and sub_order_id = #{subOrderId} and coin_id= #{coinId}")
    OfflineOrderDetail findUserIdAndSubOrderIdAndCoinId(@Param("userId") String userId, @Param("subOrderId") String subOrderId, @Param("coinId") String coinId);

    @Select("select " + OfflineOrderDetailSqlBuild.columns + " from js_plat_offline_order_detail where user_id = #{userId} and status in (${status}) order by create_date desc")
    List<OfflineOrderDetail> findMyOrderDetailByUserIdAndStatus(@Param("userId") String userId, @Param("status") String status);

    @Update("update js_plat_offline_order_detail o set o.status = #{status},o.confirm_payment_date= #{confirmPaymentDate} where o.sub_order_id = #{subOrderId} and o.status = #{sourceStatus}")
    long updateStatusAndConfirmPaymentDateBySubOrderIdAndStatus(@Param("status") Integer status, @Param("confirmPaymentDate") LocalDateTime confirmPaymentDate, @Param("subOrderId") String subOrderId, @Param("sourceStatus") String sourceStatus);

    @Select("select sum(fee_volume) from js_plat_offline_order_detail where user_id = #{userId} and status in (${status}) and coin_id = #{coinId}")
    Double sumByStatusAndUserIdAndCoinId(@Param("status") String statusIn, @Param("userId") String sellerUserId, @Param("coinId") String coinId);

    @Select("select count(1) from js_plat_offline_order_detail where user_id = #{userId} and ex_type =#{exType} and status in (${status}) limit 1")
    long countByUserIdAndExTypeAndStatusIn(@Param("userId") String userId, @Param("exType") Integer exType, @Param("status") String status);
}
