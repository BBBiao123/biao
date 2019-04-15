package com.biao.mapper;

import com.biao.entity.OfflineAppeal;
import com.biao.sql.build.OfflineAppealSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OfflineAppealDao {

    @Select("SELECT " + OfflineAppealSqlBuild.columns + " FROM js_plat_offline_appeal t WHERE t.sub_order_id = #{subOrderId} AND t.status in (1, 2)")
    List<OfflineAppeal> findBySubOrderId(String subOrderId);

    @InsertProvider(type = OfflineAppealSqlBuild.class, method = "insert")
    int insert(OfflineAppeal order);

    @Select("SELECT " + OfflineAppealSqlBuild.columns + " FROM js_plat_offline_appeal t WHERE t.appeal_user_id = #{userId} ORDER BY t.create_date DESC")
    List<OfflineAppeal> findAllAppeal(String userId);

    @Update("UPDATE js_plat_offline_appeal t SET t.status = '3', sync_key = UUID() WHERE t.appeal_user_id = #{userId} AND t.id = #{appealId} AND t.status = '1' ")
    int cancelAppeal(@Param("userId") String userId, @Param("appealId") String appealId);

    @Select("SELECT " + OfflineAppealSqlBuild.columns + " FROM js_plat_offline_appeal t WHERE t.appeal_user_id = #{userId} AND t.id = #{appealId}")
    OfflineAppeal findByIdAndUserId(@Param("userId") String userId, @Param("appealId") String appealId);

    @Select("SELECT " + OfflineAppealSqlBuild.columns + " FROM js_plat_offline_appeal t WHERE t.sub_order_id = #{appealId} AND t.status = #{status} ")
    List<OfflineAppeal> findBySubOrderIdAndStatus(@Param("appealId") String appealId, @Param("status") String status);
}
