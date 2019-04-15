package com.biao.mapper;

import com.biao.entity.PlatUserOplog;
import com.biao.sql.build.PlatUserOplogSqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

@Mapper
public interface PlatUserOplogDao {

    @InsertProvider(type = PlatUserOplogSqlBuild.class, method = "insert")
    void insert(PlatUserOplog platUserOplog);

    @SelectProvider(type = PlatUserOplogSqlBuild.class, method = "findById")
    PlatUserOplog findById(String id);

    @UpdateProvider(type = PlatUserOplogSqlBuild.class, method = "updateById")
    long updateById(PlatUserOplog platUserOplog);

}
