package com.biao.mapper;

import com.biao.entity.MkCoinDestroyRecord;
import com.biao.sql.build.MkCoinDestroyRecordSqlBuild;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

@Mapper
public interface MkCoinDestroyRecordDao {

    @InsertProvider(type = MkCoinDestroyRecordSqlBuild.class, method = "insert")
    long insert(MkCoinDestroyRecord mkCoinDestroyRecord);

    @UpdateProvider(type = MkCoinDestroyRecordSqlBuild.class, method = "updateById")
    long update(MkCoinDestroyRecord mkCoinDestroyRecord);

    @SelectProvider(type = MkCoinDestroyRecordSqlBuild.class, method = "findById")
    MkCoinDestroyRecord findById(@Param("id") String id);

    @Select("select ifnull(sum(volume),0.00) from mk_coin_destroy_record where symbol = #{coinSymbol}")
    BigDecimal statByCoinSymbol(@Param("coinSymbol") String coinSymbol);


}
