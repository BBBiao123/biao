package com.biao.mapper.otc;

import com.biao.entity.otc.OtcOfflineOrderDetail;
import com.biao.sql.build.otc.OtcOfflineOrderDetailSqlBuild;
import com.biao.vo.otc.OtcOfflineDetailCountVO;
import com.biao.vo.otc.OtcOfflineOrderDetailVO;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OtcOfflineOrderDetailDao {

    @InsertProvider(type = OtcOfflineOrderDetailSqlBuild.class, method = "insert")
    void insert(OtcOfflineOrderDetail otcOfflineOrderDetail);

    @InsertProvider(type = OtcOfflineOrderDetailSqlBuild.class, method = "batchInsert")
    void insertBatch(@Param("listValues") List<OtcOfflineOrderDetail> otcOfflineOrderDetails);

    @SelectProvider(type = OtcOfflineOrderDetailSqlBuild.class, method = "findById")
    OtcOfflineOrderDetail findById(String id);

    @UpdateProvider(type = OtcOfflineOrderDetailSqlBuild.class, method = "updateById")
    long updateById(OtcOfflineOrderDetail otcOfflineOrderDetail);

    @Select("SELECT " + OtcOfflineOrderDetailSqlBuild.columns + " FROM otc_offline_order_detail t WHERE t.user_id = #{userId} ORDER BY t.create_date DESC ")
    List<OtcOfflineOrderDetail> findByUserId(String userId);

    @Select("SELECT " + OtcOfflineOrderDetailSqlBuild.columns + " FROM otc_offline_order_detail t WHERE t.user_id = #{userId} AND t.sub_order_id = #{subOrderId} AND t.publish_source = #{publishSource} ")
    OtcOfflineOrderDetail findByUserIdSubOrderId(@Param("userId") String userId, @Param("subOrderId") String subOrderId, @Param("publishSource") String publishSource);

    @Select("SELECT " + OtcOfflineOrderDetailSqlBuild.columns + " FROM otc_offline_order_detail t WHERE t.sub_order_id = #{subOrderId} ")
    List<OtcOfflineOrderDetail> findBySubOrderId(@Param("subOrderId") String subOrderId);

    @Update("UPDATE otc_offline_order_detail SET status = #{status}, confirm_payment_date = #{confirmPaymentDate} " +
            "WHERE sub_order_id = #{subOrderId} AND status = '0' AND update_date = #{updateDate} AND user_id = #{userId} ")
    long updateDetailPayment(OtcOfflineOrderDetail orderDetail);

    @Update("UPDATE otc_offline_order_detail SET status = #{status}, confirm_receipt_date = #{confirmReceiptDate} " +
            "WHERE sub_order_id = #{subOrderId} AND status = '1' AND update_date = #{updateDate} AND user_id = #{userId} ")
    long updateDetailReceipt(OtcOfflineOrderDetail orderDetail);

    @Update("UPDATE otc_offline_order_detail SET status = #{status}, cancel_date = #{cancelDate}, update_by = #{updateBy}  " +
            "WHERE sub_order_id = #{subOrderId} AND status IN ('0', '1') AND update_date = #{updateDate} AND user_id = #{userId} ")
    long updateDetailCancel(OtcOfflineOrderDetail orderDetail);

    @Select("SELECT " + OtcOfflineOrderDetailSqlBuild.columns + " FROM otc_offline_order_detail t WHERE t.user_id = #{userId} AND t.publish_source = #{publishSource} AND t.status IN ('0', '1') ORDER BY t.create_date DESC ")
    List<OtcOfflineOrderDetail> findTiming(@Param("userId") String userId, @Param("publishSource") String publishSource);

    @Select("SELECT " + OtcOfflineOrderDetailSqlBuild.columns + " FROM otc_offline_order_detail t WHERE t.user_id = #{userId} AND t.publish_source = #{publishSource} ORDER BY t.create_date DESC ")
    List<OtcOfflineOrderDetail> findAllSubOrders(@Param("userId") String userId, @Param("publishSource") String publishSource);

    @Select("SELECT " + OtcOfflineOrderDetailSqlBuild.columns + " FROM otc_offline_order_detail t WHERE t.user_id = #{userId} AND t.publish_source = #{publishSource} AND t.status = #{status} ORDER BY t.create_date DESC ")
    List<OtcOfflineOrderDetail> findMySuborders(@Param("userId") String userId, @Param("publishSource") String publishSource, @Param("status") String status);

    @Select("SELECT count(1) FROM otc_offline_order_detail t WHERE t.user_id = #{userId} AND t.publish_source = #{publishSource} AND t.create_date > #{beginTime} AND t.create_date < #{endTime} ")
    long countDayDetail(@Param("userId") String userId, @Param("publishSource") String publishSource, @Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    @Select("SELECT sum(t.volume) FROM otc_offline_order_detail t WHERE t.user_id = #{userId} AND t.publish_source = #{publishSource} AND t.create_date > #{beginTime} AND t.create_date < #{endTime} ")
    BigDecimal sumDayDetailVolume(@Param("userId") String userId, @Param("publishSource") String publishSource, @Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    @Select("SELECT count(1) FROM otc_offline_order_detail t WHERE t.user_id = #{userId} AND t.publish_source = #{publishSource} AND t.`status` IN ('0', '1') ")
    long countNoCompleteDetail(@Param("userId") String userId, @Param("publishSource") String publishSource);

    @Select("SELECT #{userId} AS 'userId'," +
            " (SELECT count(1) FROM otc_offline_order_detail t WHERE t.user_id = #{userId} AND t.status = '0') AS 'noPayDetailCount'," +
            " (SELECT count(1) FROM otc_offline_order_detail t WHERE t.user_id = #{userId} AND t.status = '1') AS 'payDetailCount' " +
            " FROM dual")
    OtcOfflineDetailCountVO countDetail(@Param("userId") String userId);

    @Select("SELECT " + OtcOfflineOrderDetailSqlBuild.columns + " FROM otc_offline_order_detail t WHERE t.`status` = #{status} AND t.create_date < #{curDateTime} AND t.ex_type = '0' ")
    List<OtcOfflineOrderDetail> findLongTimeOrderDetail(@Param("status") String status, @Param("curDateTime") LocalDateTime curDateTime);

    @Select("<script>" +
            "SELECT " + OtcOfflineOrderDetailSqlBuild.columns + " FROM otc_offline_order_detail t " +
            "WHERE t.publish_source = #{publishSource} " +
            " <if test=\"orderId != null and orderId != ''\"> " +
            " AND t.order_id = #{orderId} " +
            " </if>" +
            " <if test=\"userMobile != null and userMobile != ''\"> " +
            " AND t.user_mobile = #{userMobile} " +
            " </if>" +
            " <if test=\"symbol != null and symbol != ''\"> " +
            " AND t.symbol = #{symbol} " +
            " </if>" +
            " <if test=\"subOrderId != null and subOrderId != ''\"> " +
            " AND t.sub_order_id = #{subOrderId} " +
            " </if>" +
            " <if test=\"exType != null and exType != ''\"> " +
            " AND t.ex_type = #{exType} " +
            " </if>" +
            " <if test=\"status != null and status !=''\"> " +
            " AND t.status = #{status} " +
            " </if>" +
            " ORDER BY t.create_date DESC " +
            "</script>")
    List<OtcOfflineOrderDetail> findDetailByCondition(OtcOfflineOrderDetailVO otcOfflineOrderDetailVO);
}
