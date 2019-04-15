package com.biao.mapper.mk2;

import com.biao.entity.mk2.Mk2PopularizeBonusMemberLog;
import com.biao.sql.build.mk2.Mk2PopularizeBonusMemberLogBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface Mk2PopularizeBonusMemberLogDao {

    @InsertProvider(type = Mk2PopularizeBonusMemberLogBuild.class, method = "insert")
    long insert(Mk2PopularizeBonusMemberLog log);

    @InsertProvider(type = Mk2PopularizeBonusMemberLogBuild.class, method = "batchInsert")
    void insertBatch(@Param("listValues") List<Mk2PopularizeBonusMemberLog> mk2PopularizeBonusMemberLogs);
}
