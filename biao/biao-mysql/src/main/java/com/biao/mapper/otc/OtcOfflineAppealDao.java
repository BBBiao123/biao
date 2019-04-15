package com.biao.mapper.otc;

import com.biao.entity.otc.OtcOfflineAppeal;
import com.biao.sql.build.otc.OtcOfflineAppealSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OtcOfflineAppealDao {

    @Select("SELECT " + OtcOfflineAppealSqlBuild.columns + " FROM otc_offline_appeal t WHERE t.sub_order_id = #{subOrderId} AND t.status in (1, 2)")
    List<OtcOfflineAppeal> findBySubOrderId(String subOrderId);

    @InsertProvider(type = OtcOfflineAppealSqlBuild.class, method = "insert")
    int insert(OtcOfflineAppeal otcOfflineAppeal);

    @Select("SELECT " + OtcOfflineAppealSqlBuild.columns + " FROM otc_offline_appeal t WHERE t.appeal_user_id = #{userId} AND t.publish_source = #{loginSource} ORDER BY t.create_date DESC")
    List<OtcOfflineAppeal> findAllAppeal(@Param("userId") String userId, @Param("loginSource") String loginSource);

    @Update("UPDATE otc_offline_appeal t SET t.status = '3' WHERE t.appeal_user_id = #{userId} AND t.id = #{appealId} AND t.status = '1' ")
    int cancelAppeal(@Param("userId") String userId, @Param("appealId") String appealId);

    @Select("SELECT " + OtcOfflineAppealSqlBuild.columns + " FROM otc_offline_appeal t WHERE t.appeal_user_id = #{userId} AND t.id = #{appealId}")
    OtcOfflineAppeal findByIdAndUserId(@Param("userId") String userId, @Param("appealId") String appealId);

    @Select("SELECT " + OtcOfflineAppealSqlBuild.columns + " FROM otc_offline_appeal t WHERE t.sub_order_id = #{appealId} AND t.status = #{status} ")
    List<OtcOfflineAppeal> findBySubOrderIdAndStatus(@Param("appealId") String appealId, @Param("status") String status);
}
