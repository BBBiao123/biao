package com.biao.mapper;

import com.biao.entity.OrderDetail;
import com.biao.sql.build.OrderDetailSqlBuild;
import com.biao.sql.build.UserInvitedSqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

@Mapper
public interface OrderDetailDao {

    @InsertProvider(type = OrderDetailSqlBuild.class, method = "insert")
    void insert(OrderDetail orderDetail);

    @SelectProvider(type = OrderDetailSqlBuild.class, method = "findById")
    OrderDetail findById(String id);

    @UpdateProvider(type = UserInvitedSqlBuild.class, method = "updateById")
    long updateById(OrderDetail orderDetail);


}
