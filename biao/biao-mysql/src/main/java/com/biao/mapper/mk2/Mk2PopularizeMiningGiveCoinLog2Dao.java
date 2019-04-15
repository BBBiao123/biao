package com.biao.mapper.mk2;

import com.biao.entity.mk2.Mk2PopularizeMiningGiveCoinLog2;
import com.biao.sql.build.mk2.Mk2PopularizeMiningGiveCoinLog2Build;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface Mk2PopularizeMiningGiveCoinLog2Dao {

    @InsertProvider(type = Mk2PopularizeMiningGiveCoinLog2Build.class, method = "insert")
    void insert(Mk2PopularizeMiningGiveCoinLog2 coinLog);

    @InsertProvider(type = Mk2PopularizeMiningGiveCoinLog2Build.class, method = "batchInsert")
    void insertBatch(@Param("listValues") List<Mk2PopularizeMiningGiveCoinLog2> coinLogs);


}
