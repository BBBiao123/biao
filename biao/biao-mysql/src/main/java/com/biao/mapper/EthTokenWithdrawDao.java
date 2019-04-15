package com.biao.mapper;

import com.biao.entity.EthTokenWithdraw;
import com.biao.sql.build.EthTokenWithdrawSqlBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EthTokenWithdrawDao {


    @Select("select " + EthTokenWithdrawSqlBuild.columns + " from eth_token_withdraw where symbol = #{symbol} and status = #{status}")
    List<EthTokenWithdraw> findBySymbolAndStatus(@Param("symbol") String symbol, @Param("status") Integer status);

    @Select("select " + EthTokenWithdrawSqlBuild.columns + " from eth_token_withdraw where status = #{status}")
    List<EthTokenWithdraw> findByStatus(@Param("status") Integer status);

    @Update("update eth_token_withdraw set tx_id = #{txId},status = #{status} where id = #{id}")
    long updateTxIdAndStatusById(@Param("txId") String txId, @Param("status") Integer status, @Param("id") String id);

}
