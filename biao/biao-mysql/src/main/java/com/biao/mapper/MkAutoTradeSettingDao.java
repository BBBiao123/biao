package com.biao.mapper;

import com.biao.entity.MkAutoTradeSetting;
import com.biao.sql.build.MkAutoTradeSettingSqlBuild;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MkAutoTradeSettingDao {

    @InsertProvider(type = MkAutoTradeSettingSqlBuild.class, method = "insert")
    long insert(MkAutoTradeSetting MkAutoTradeSetting);

    @UpdateProvider(type = MkAutoTradeSettingSqlBuild.class, method = "updateById")
    long update(MkAutoTradeSetting MkAutoTradeSetting);

    @SelectProvider(type = MkAutoTradeSettingSqlBuild.class, method = "findById")
    MkAutoTradeSetting findById(@Param("id") String id);

    @Select("select " + MkAutoTradeSettingSqlBuild.columns + " from mk_auto_trade_setting s where s.status = 1 and s.begin_date <= #{preDateTime} and s.end_date >= #{curDateTime} and NOT EXISTS(select 1 from mk_auto_trade_monitor where setting_id = s.id and status in ('0','1'))")
    List<MkAutoTradeSetting> findLoadingAutoTrade(@Param("preDateTime") LocalDateTime preDateTime, @Param("curDateTime") LocalDateTime curDateTime);

    @Update("update mk_auto_trade_setting set status = #{status} where id = #{id}")
    long updateSettingStatus(@Param("id") String id, @Param("status") String status);

}
