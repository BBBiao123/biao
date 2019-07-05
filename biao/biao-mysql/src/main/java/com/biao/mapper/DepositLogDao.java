package com.biao.mapper;

import com.biao.entity.DepositLog;
import com.biao.pojo.DepdrawLogVO;
import com.biao.sql.build.DepositLogSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DepositLogDao {

    @InsertProvider(type = DepositLogSqlBuild.class, method = "insert")
    void insert(DepositLog depositLog);

    @SelectProvider(type = DepositLogSqlBuild.class, method = "findById")
    DepositLog findById(String id);

    @UpdateProvider(type = DepositLogSqlBuild.class, method = "updateById")
    long updateById(DepositLog depositLog);

    @Select("select " + DepositLogSqlBuild.columns + " from js_plat_user_deposit_log where coin_id = #{coinId} and user_id = #{userId} order by create_date desc")
    List<DepositLog> findDepositLogListByUserIdAndCoinId(@Param("userId") String userId, @Param("coinId") String coinId);

    @Select("select " + DepositLogSqlBuild.columns + " from js_plat_user_deposit_log where user_id = #{userId} order by create_date desc")
    List<DepositLog> findDepositLogListByUserId(String userId);

    @InsertProvider(type = DepositLogSqlBuild.class, method = "batchInsert")
    void batchInsert(@Param("listValues") List<DepositLog> list);

    @Select("select " + DepositLogSqlBuild.columns + " from js_plat_user_deposit_log where status = #{status} and coin_type = #{coinType}")
    List<DepositLog> findStatusAndCoinType(@Param("status") Integer status, @Param("coinType") String coinType);

    @Select("select " + DepositLogSqlBuild.columns + " from js_plat_user_deposit_log where tx_id = #{txId}")
    DepositLog findByTxId(@Param("txId") String txId);


    @Select("select " + DepositLogSqlBuild.columns + " from js_plat_user_deposit_log where tx_id = #{txId} and coin_symbol =#{coinSymbol}")
    DepositLog findByTxIdAndSymbol(@Param("txId") String txId, @Param("coinSymbol") String coinSymbol);

    @Select("select a.id as id, a.coinId as coinId, a.symbol as symbol, a.volume as volume, a.status as status, a.type as type, a.createDate as createDate, a.address as address, a.fee as fee, a.txId as txId from ( " +
            "select id as id, coin_id as coinId, coin_symbol as symbol, volume as volume, status as status, '0' as type, create_date as createDate, address as address, 0 as fee, tx_id as txId from js_plat_user_deposit_log where coin_id = #{coinId} and user_id = #{userId} " +
            " UNION ALL " +
            "select id as id, coin_id as coinId, coin_symbol as symbol, volume as volume, status as status, '1' as type, create_date as createDate, address as address, fee as fee, tx_id from js_plat_user_withdraw_log where coin_id = #{coinId} and user_id = #{userId} " +
            " ) a order by a.createDate desc")
    List<DepdrawLogVO> findDepdrawLogListByUserIdAndCoinId(@Param("userId") String userId, @Param("coinId") String coinId);

    @Select("select a.id as id, a.coinId as coinId, a.symbol as symbol, a.volume as volume, a.status as status, a.type as type, a.createDate as createDate, a.address as address, a.fee as fee, a.txId as txId from ( " +
            "select id as id, coin_id as coinId, coin_symbol as symbol, volume as volume, status as status, '0' as type, create_date as createDate, address as address, 0 as fee, tx_id as txId from js_plat_user_deposit_log where user_id = #{userId}" +
            " UNION ALL " +
            "select id as id, coin_id as coinId, coin_symbol as symbol, volume as volume, status as status, '1' as type, create_date as createDate, address as address, fee as fee, tx_id as txId from js_plat_user_withdraw_log where user_id = #{userId}" +
            " ) a order by a.createDate desc")
    List<DepdrawLogVO> findDepdrawLogListByUserId(String userId);

    @Select("select " + DepositLogSqlBuild.columns + " from js_plat_user_deposit_log where raise_status = #{raiseStatus} and coin_symbol = #{coinSymbol}")
    List<DepositLog> findAllByCoinSymbolAndRaiseStatus(@Param("coinSymbol") String coinSymbol,@Param("raiseStatus") Integer raiseStatus);

    @Update("update js_plat_user_deposit_log set raise_status=#{raiseStatus} where id=#{id} ")
    long updateRaiseStatusById(@Param("raiseStatus") Integer raiseStatus,@Param("id") String id);

    @Select("select " + DepositLogSqlBuild.columns + " from js_plat_user_deposit_log where coin_type = #{coinType} and raise_status = #{raiseStatus}")
    List<DepositLog> findAllByCoinTypeAndRaiseStatus(@Param("coinType") String coinType, @Param("raiseStatus") Integer raiseStatus);


    @Update("update js_plat_user_deposit_log set raise_status=2 where user_id=#{userId}  and coin_symbol= #{coinSymbol} and raise_status =1 ")
    int updateRaiseStatusSuccess(@Param("userId") String userId, @Param("coinSymbol") String coinSymbol);

}
