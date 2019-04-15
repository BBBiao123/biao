package com.biao.mapper;

import com.biao.entity.CoinAddress;
import com.biao.sql.build.CoinAddressSqlBuild;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface CoinAddressDao {

    @Select("select " + CoinAddressSqlBuild.columns + " from js_plat_coin_address where coin_id = #{coinId} and user_id = '0' and status = 0 limit 1")
    CoinAddress findByCoinId(String coinId);

    @UpdateProvider(type = CoinAddressSqlBuild.class, method = "updateById")
    Long updateById(CoinAddress coinAddress);

    @Update("update js_plat_coin_address set user_id = #{userId}, status = #{status} where id = #{id} and update_date = #{updateDate}")
    long updateUserIdAndStatusByIdAndUpdateDate(@Param("userId") String userId, @Param("status") int i, @Param("id") String id, @Param("updateDate") Timestamp updateDate);

    @InsertProvider(type = CoinAddressSqlBuild.class, method = "batchInsert")
    void batchInsert(@Param("listValues") List<CoinAddress> list);

    @InsertProvider(type = CoinAddressSqlBuild.class, method = "insert")
    void insert(CoinAddress coinAddress);

    @Select("select " + CoinAddressSqlBuild.columns + " from js_plat_coin_address where address = #{address} limit 1")
    CoinAddress findByAddress(String address);

    @Select("select " + CoinAddressSqlBuild.columns + " from js_plat_coin_address where type = #{type}")
    List<CoinAddress> findByType(Integer type);

}
