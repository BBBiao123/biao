package com.biao.mapper.mk2;

import com.biao.entity.mk2.Mk2PopularizeBonusAccountLog;
import com.biao.sql.build.mk2.Mk2PopularizeBonusAccountLogBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Mk2PopularizeBonusAccountLogDao {

    @InsertProvider(type = Mk2PopularizeBonusAccountLogBuild.class, method = "insert")
    long insert(Mk2PopularizeBonusAccountLog log);
}
