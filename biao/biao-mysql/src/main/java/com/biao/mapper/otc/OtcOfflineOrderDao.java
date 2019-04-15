package com.biao.mapper.otc;

import com.biao.entity.otc.OtcOfflineOrder;
import com.biao.sql.build.otc.OtcOfflineOrderSqlBuild;
import com.biao.vo.otc.OtcOfflineOrderVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OtcOfflineOrderDao {

    @InsertProvider(type = OtcOfflineOrderSqlBuild.class, method = "insert")
    void insert(OtcOfflineOrder otcOfflineOrder);

    @SelectProvider(type = OtcOfflineOrderSqlBuild.class, method = "findById")
    OtcOfflineOrder findById(String id);

    @UpdateProvider(type = OtcOfflineOrderSqlBuild.class, method = "updateById")
    long updateById(OtcOfflineOrder otcOfflineOrder);

    @Select("SELECT " + OtcOfflineOrderSqlBuild.columns + " FROM otc_offline_order t WHERE t.user_id = #{userId} AND t.publish_source = #{loginSource} AND t.ex_type = #{exType} ORDER BY t.create_date DESC ")
    List<OtcOfflineOrder> findByUserId(@Param("userId") String userId, @Param("loginSource") String loginSource, @Param("exType") String exType);

    @Select("SELECT " + OtcOfflineOrderSqlBuild.columns +
            " FROM otc_offline_order t WHERE t.coin_id = #{coinId} and t.ex_type = #{exType} and t.publish_source = #{loginSource} " +
            " and support_currency_code = #{supportCurrencyCode} and t.status in (0) and t.volume>(t.lock_volume+t.success_volume) order by t.price DESC,t.create_date DESC")
    List<OtcOfflineOrder> findAdvertListByCoinIdPriceDesc(OtcOfflineOrderVO otcOfflineOrderVO);

    @Select("SELECT " + OtcOfflineOrderSqlBuild.columns +
            " FROM otc_offline_order t WHERE t.coin_id = #{coinId} and t.ex_type = #{exType} and t.publish_source = #{loginSource} " +
            " and support_currency_code = #{supportCurrencyCode}  and t.status in (0) and t.volume>(t.lock_volume+t.success_volume) order by t.price ASC,t.create_date DESC")
    List<OtcOfflineOrder> findAdvertListByCoinIdPriceAsc(OtcOfflineOrderVO otcOfflineOrderVO);

    @Update("UPDATE otc_offline_order SET lock_volume = #{lockVolume}, success_volume = #{successVolume}, status = #{status} WHERE id = #{id} AND status != '1' AND update_date = #{updateDate} ")
    long updateOtcOrder(OtcOfflineOrder otcOfflineOrder);

    @Update("UPDATE otc_offline_order SET status = #{status}, cancel_date = #{cancelDate} WHERE id = #{id} AND update_date = #{updateDate} ")
    long cancelOtcOrder(OtcOfflineOrder otcOfflineOrder);

}
