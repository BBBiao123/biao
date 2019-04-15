package com.biao.mapper;

import com.biao.entity.OfflineOrder;
import com.biao.sql.build.OfflineOrderSqlBuild;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OfflineOrderDao {

    @InsertProvider(type = OfflineOrderSqlBuild.class, method = "insert")
    void insert(OfflineOrder order);

    @SelectProvider(type = OfflineOrderSqlBuild.class, method = "findById")
    OfflineOrder findById(String id);

    @UpdateProvider(type = OfflineOrderSqlBuild.class, method = "updateById")
    long updateById(OfflineOrder order);

    @Select("select " + OfflineOrderSqlBuild.columns + " from js_plat_offline_order where user_id = #{userId} order by create_date desc")
    List<OfflineOrder> findMyAdvertList(String userId);

    @Update("update js_plat_offline_order set lock_volume = #{lockVolume},version= version+1 where id = #{id} and update_date = #{updateDate} and version= #{version}")
    long updateLockVolumeById(@Param("id") String id, @Param("lockVolume") BigDecimal lockVolume, @Param("updateDate") Timestamp updateDate, @Param("version") Long version);

    @Update("update js_plat_offline_order set success_volume = #{successVolume}, lock_volume = #{lockVolume},status =#{orderStatus},version= version+1 where id = #{id} and update_date = #{updateDate} and version= #{version}")
    long updateSuccessVolumeAndLockVolumeAndStatusById(@Param("id") String id, @Param("successVolume") BigDecimal successVolume, @Param("lockVolume") BigDecimal lockVolume, @Param("orderStatus") Integer orderStatus, @Param("updateDate") Timestamp updateDate, @Param("version") Long version);

    @Update("update js_plat_offline_order set success_volume = #{successVolume},status = #{orderStatus},version= version+1 where id = #{id} and update_date = #{updateDate} and version= #{version}")
    long updateSuccessVolumeAndStatusById(@Param("id") String id, @Param("successVolume") BigDecimal successVolume, @Param("orderStatus") Integer orderStatus, @Param("updateDate") Timestamp updateDate, @Param("version") Long version);

    @Select("select " + OfflineOrderSqlBuild.columns +
            " from js_plat_offline_order t FORCE INDEX (index_coin_status_type) where t.coin_id = #{coinId} and t.ex_type = #{exType} and t.status in ('0','2') and t.volume>(t.lock_volume+t.success_volume) and (t.volume - t.lock_volume-t.success_volume)>= t.min_ex_volume order by t.create_date desc")
    List<OfflineOrder> findAdvertListByCoinId(@Param("coinId") String coinId, @Param("exType") Integer exType);


    @Select("select " + OfflineOrderSqlBuild.columns +
            " from js_plat_offline_order t FORCE INDEX (index_coin_status_type) where t.coin_id = #{coinId} and t.ex_type = #{exType} and t.status in ('0','2') and t.volume>(t.lock_volume+t.success_volume) and (t.volume - t.lock_volume-t.success_volume)>= t.min_ex_volume order by t.price desc,t.create_date desc")
    List<OfflineOrder> findAdvertListByCoinIdPriceDesc(@Param("coinId") String coinId, @Param("exType") Integer exType);


    @Select("select " + OfflineOrderSqlBuild.columns +
            " from js_plat_offline_order t FORCE INDEX (index_coin_status_type) where t.coin_id = #{coinId} and t.ex_type = #{exType} and t.status in ('0','2') and t.volume>(t.lock_volume+t.success_volume) and (t.volume - t.lock_volume-t.success_volume)>= t.min_ex_volume order by t.price asc ,t.create_date desc")
    List<OfflineOrder> findAdvertListByCoinIdPriceAsc(@Param("coinId") String coinId, @Param("exType") Integer exType);

    @Update("UPDATE js_plat_offline_order SET status = #{status}, cancel_date = #{cancelDate},version= version+1 WHERE id = #{id} and update_date = #{updateDate} and version= #{version}")
    long updateOrderCancelStatusById(@Param("id") String id, @Param("status") Integer status, @Param("cancelDate") LocalDateTime cancelDate, @Param("updateDate") Timestamp updateDate, @Param("version") Long version);

    @Select("select " + OfflineOrderSqlBuild.columns +
            " from js_plat_offline_order t  where t.ex_type = #{exType} and t.status = #{status}")
    List<OfflineOrder> findByStatusAndExType( @Param("status") Integer status, @Param("exType") Integer exType);

    @Update("update js_plat_offline_order set status = #{status},version= version+1 where id = #{id} and version= #{version}")
    long updateStatusById( @Param("status") Integer status,@Param("id") String id, @Param("version") Long version);
}
