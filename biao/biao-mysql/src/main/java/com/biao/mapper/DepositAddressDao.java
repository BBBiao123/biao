package com.biao.mapper;

import com.biao.entity.DepositAddress;
import com.biao.sql.build.DepositAddressSqlBuild;
import org.apache.ibatis.annotations.*;

@Mapper
public interface DepositAddressDao {

    @InsertProvider(type = DepositAddressSqlBuild.class, method = "insert")
    void insert(DepositAddress withdrawAddress);

    @SelectProvider(type = DepositAddressSqlBuild.class, method = "findById")
    DepositAddress findById(String id);

    @UpdateProvider(type = DepositAddressSqlBuild.class, method = "updateById")
    long updateById(DepositAddress withdrawAddress);

    @Select("select " + DepositAddressSqlBuild.columns + " from js_plat_user_deposit_address where user_id =#{userId} and coin_id = #{coinId} limit 1")
    DepositAddress findByUserIdAndCoinId(@Param("userId") String userId, @Param("coinId") String coinId);

    @Select("select " + DepositAddressSqlBuild.columns + " from js_plat_user_deposit_address where address =#{address} limit 1")
    DepositAddress findByAddress(@Param("address") String address);

}
