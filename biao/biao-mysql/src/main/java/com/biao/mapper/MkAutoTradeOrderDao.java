package com.biao.mapper;

import com.biao.entity.MkAutoTradeOrder;
import com.biao.sql.build.MkAutoTradeOrderSqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.UpdateProvider;

@Mapper
public interface MkAutoTradeOrderDao {

    @InsertProvider(type = MkAutoTradeOrderSqlBuild.class, method = "insert")
    long insert(MkAutoTradeOrder MkAutoTradeOrder);

    @UpdateProvider(type = MkAutoTradeOrderSqlBuild.class, method = "updateById")
    long update(MkAutoTradeOrder MkAutoTradeOrder);

}
