package com.biao.mapper;

import com.biao.entity.OfflineOrderFee;
import com.biao.sql.build.OfflineOrderFeeSqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

@Mapper
public interface OfflineOrderFeeDao {

    @InsertProvider(type = OfflineOrderFeeSqlBuild.class, method = "insert")
    void insert(OfflineOrderFee offlineOrderFee);

    @SelectProvider(type = OfflineOrderFeeSqlBuild.class, method = "findById")
    OfflineOrderFee findById(String id);

    @UpdateProvider(type = OfflineOrderFeeSqlBuild.class, method = "updateById")
    long updateById(OfflineOrderFee offlineOrderFee);


}
