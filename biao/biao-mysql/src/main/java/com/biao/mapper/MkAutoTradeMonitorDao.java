package com.biao.mapper;

import com.biao.entity.MkAutoTradeMonitor;
import com.biao.sql.build.MkAutoTradeMonitorSqlBuild;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MkAutoTradeMonitorDao {

    @InsertProvider(type = MkAutoTradeMonitorSqlBuild.class, method = "insert")
    long insert(MkAutoTradeMonitor MkAutoTradeMonitor);

    @UpdateProvider(type = MkAutoTradeMonitorSqlBuild.class, method = "updateById")
    long update(MkAutoTradeMonitor MkAutoTradeMonitor);

    @SelectProvider(type = MkAutoTradeMonitorSqlBuild.class, method = "findById")
    MkAutoTradeMonitor findById(@Param("id") String id);

    @Select("select " + MkAutoTradeMonitorSqlBuild.columns + " from mk_auto_trade_monitor where status in ('0','1')")
    List<MkAutoTradeMonitor> findActiveMonitor();

    @Select("select " + MkAutoTradeMonitorSqlBuild.columns + " from mk_auto_trade_monitor where status = '4' and id = #{id}")
    MkAutoTradeMonitor findEndMonitorById(@Param("id") String id);

    @Update("update mk_auto_trade_monitor set order_volume = ifnull(order_volume,0) + #{volume}, order_number = ifnull(order_number,0) + 1, order_price = ifnull(order_price,0) + #{price} where id = #{id} and status = 1")
    long updateOrderInfo(@Param("id") String id, @Param("volume") BigDecimal volume, @Param("price") BigDecimal price);

    @Update("update mk_auto_trade_monitor set status = #{status}, order_begin_date = #{beginDate}, remark = #{remark} where id = #{id}")
    long updateMonitorBegin(@Param("id") String id, @Param("status") String status, @Param("beginDate") LocalDateTime beginDate, @Param("remark") String remark);

    @Update("update mk_auto_trade_monitor set status = #{status}, order_end_date = #{endDate}, remark = #{remark} where id = #{id}")
    long updateMonitorEnd(@Param("id") String id, @Param("status") String status, @Param("endDate") LocalDateTime endDate, @Param("remark") String remark);

}
