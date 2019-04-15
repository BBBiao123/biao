package com.biao.mapper.mkcommon;

import com.biao.entity.mkcommon.MkCommonCoinRate;
import com.biao.sql.build.mkcommon.MkCommonCoinRateBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MkCommonCoinRateDao {

    @InsertProvider(type = MkCommonCoinRateBuild.class, method = "insert")
    void insert(MkCommonCoinRate commonCoinRate);

    @InsertProvider(type = MkCommonCoinRateBuild.class, method = "batchInsert")
    void insertBatch(@Param("listValues") List<MkCommonCoinRate> coinRateList);
}
