package com.biao.mapper;

import com.biao.entity.Order;
import com.biao.query.UserTradeQuery;
import com.biao.sql.build.OrderSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderDao {

    @InsertProvider(type = OrderSqlBuild.class, method = "insert")
    long insert(Order order);

    @SelectProvider(type = OrderSqlBuild.class, method = "findById")
    Order findById(String id);

    @UpdateProvider(type = OrderSqlBuild.class, method = "updateById")
    long updateById(Order order);

    /**
     * 修改用户状态；
     *
     * @param id     主键
     * @param status 状态；
     * @return 成功数；
     */
    @Update("update js_plat_ex_order set status=#{status} where id=#{id} and (status=0 or status=1)")
    Long updateOrderStatus(@Param("id") String id, @Param("status") Integer status);

    /**
     * 修改订单结果信息；
     * 包括修改的内容：
     * 1.成功挂单 successVolume
     * 2.交易成功数 toCoinVolume
     * 3.手续费 exFee
     * 4.状态 status
     * 条件为：主键 id;
     *
     * @param order 订单信息；
     * @return 成功数；
     */
    @Update("update js_plat_ex_order set success_volume=#{successVolume}," +
            "to_coin_volume=#{toCoinVolume},ex_fee=#{exFee},status=#{status},cancel_lock=#{cancelLock},spent_volume=#{spentVolume} where id=#{id}")
    Long updateResultOrder(Order order);

    @SelectProvider(type = OrderSqlBuild.class, method = "findListByQuery")
    List<Order> findListByQuery(UserTradeQuery userTradeQuery);

    @Select("select " + OrderSqlBuild.USER_ORDER + " FROM  js_plat_ex_order WHERE ex_type = #{type} AND status = '0 ' order by create_date desc limit 0,7")
    List<Order> findTop7(Integer type);
}
