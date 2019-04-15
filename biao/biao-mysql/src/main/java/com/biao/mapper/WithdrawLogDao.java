package com.biao.mapper;

import com.biao.entity.WithdrawLog;
import com.biao.sql.build.WithdrawLogSqlBuild;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface WithdrawLogDao {

    @InsertProvider(type = WithdrawLogSqlBuild.class, method = "insert")
    void insert(WithdrawLog withdrawLog);

    @SelectProvider(type = WithdrawLogSqlBuild.class, method = "findById")
    WithdrawLog findById(String id);

    @UpdateProvider(type = WithdrawLogSqlBuild.class, method = "updateById")
    long updateById(WithdrawLog withdrawLog);

    @Update("update js_plat_user_withdraw_log set status=#{status} where id=#{id} and status =#{sourceStatus} and update_date<=#{updateDate}")
    long updateStatusByIdAndStatus(@Param("id") String id, @Param("status") String status, @Param("sourceStatus") String sourceStatus, @Param("updateDate") Timestamp updateDate);

    @Select("select " + WithdrawLogSqlBuild.columns + " from js_plat_user_withdraw_log where coin_id = #{coinId} and user_id = #{userId} and status != 8 order by create_date desc")
    List<WithdrawLog> findWithdrawListByUserIdAndCoinId(@Param("userId") String userId, @Param("coinId") String coinId);

    @Select("select " + WithdrawLogSqlBuild.columns + " from js_plat_user_withdraw_log where user_id = #{userId} and status != 8 order by create_date desc")
    List<WithdrawLog> findWithdrawListByUserId(String userId);

    @Select("select count(volume) from js_plat_user_withdraw_log where user_id = #{userId} and coin_id = #{coinId} and status in (0,1,3) ")
    long countDayVolumeByUserIdAndCoinIdAndStatus(@Param("userId") String userId, @Param("coinId") String coinId);

    @Select("select count(*) from js_plat_user_withdraw_log where user_id = #{userId} and address = #{address} and status = 3")
    long countAddressByStatusAndUserId(@Param("userId") String userId, @Param("address") String address);

    @Update("update js_plat_user_withdraw_log set status=#{status} where id=#{id} ")
    long updateStatusById(@Param("status") String status, @Param("id") String id);

    @Update("update js_plat_user_withdraw_log set status=#{status},tx_id = #{txId} where id=#{id} ")
    long updateTxIdAndStatusById(@Param("txId") String txId, @Param("status") String status, @Param("id") String id);

    @Select("select " + WithdrawLogSqlBuild.columns + " from js_plat_user_withdraw_log where coin_symbol = #{symbol} and status=#{status} ")
    List<WithdrawLog> findAllByCoinSymbolAndStatus(@Param("symbol") String symbol, @Param("status") Integer status);

    @Select("select " + WithdrawLogSqlBuild.columns + " from js_plat_user_withdraw_log where coin_type = #{coinType} and status=#{status} limit 1")
    List<WithdrawLog> findAllByCoinTypeAndStatus(@Param("coinType") String coinType, @Param("status") Integer status);

    @Select("select " + WithdrawLogSqlBuild.columns + " from js_plat_user_withdraw_log where coin_type = #{coinType} and confirm_status=#{confirmStatus} limit 1")
    List<WithdrawLog> findWithdrawLogByConfirmStatusAndCoinType(@Param("confirmStatus") Integer confirmStatus, @Param("coinType") String coinType);


    @Select("select " + WithdrawLogSqlBuild.columns + " from js_plat_user_withdraw_log where coin_type = #{coinType} and status=#{status} ")
    List<WithdrawLog> findListByCoinTypeAndStatus(@Param("coinType") String coinType, @Param("status") Integer status);

}
