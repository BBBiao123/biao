package com.biao.mapper;

import com.biao.entity.MobileSendLog;
import com.biao.sql.build.MobileSendLogBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MobileSendLogDao {

    @InsertProvider(type = MobileSendLogBuild.class, method = "insert")
    void insert(MobileSendLog mobileSendLog);
}
