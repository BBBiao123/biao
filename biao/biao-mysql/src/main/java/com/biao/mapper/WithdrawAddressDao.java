package com.biao.mapper;

import com.biao.entity.WithdrawAddress;
import com.biao.sql.build.WithdrawAddressSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WithdrawAddressDao {

    @InsertProvider(type = WithdrawAddressSqlBuild.class, method = "insert")
    void insert(WithdrawAddress withdrawAddress);

    @SelectProvider(type = WithdrawAddressSqlBuild.class, method = "findById")
    WithdrawAddress findById(String id);

    @UpdateProvider(type = WithdrawAddressSqlBuild.class, method = "updateById")
    long updateById(WithdrawAddress withdrawAddress);

    @Update("update js_plat_user_withdraw_address set status=#{status} where id=#{id} and user_id =#{userId}")
    long updateStatusByUserIdAndId(@Param("status") String status, @Param("userId") String userId, @Param("id") String id);

    @Select("select " + WithdrawAddressSqlBuild.columns + " from js_plat_user_withdraw_address where user_id = #{userId} and status = #{status} order by create_date desc")
    List<WithdrawAddress> findAll(@Param("userId") String userId, @Param("status") String status);

    @Select("select " + WithdrawAddressSqlBuild.columns + " from js_plat_user_withdraw_address where user_id = #{userId} and status = #{status} and coin_id = #{coinId} order by create_date desc")
    List<WithdrawAddress> findByUserIdAndCoinId(@Param("userId") String userId, @Param("status") String status, @Param("coinId") String coinId);

    @Select("select " + WithdrawAddressSqlBuild.columns + " from js_plat_user_withdraw_address where user_id = #{userId} and status = #{status} and type = #{type} order by create_date desc")
    List<WithdrawAddress> findByUserIdAndType(@Param("userId") String userId, @Param("status") String status, @Param("type") String type);
}
