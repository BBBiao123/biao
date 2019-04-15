package com.biao.mapper.mk2;

import com.biao.entity.mk2.Mk2PopularizeBonusTaskLog;
import com.biao.sql.build.mk2.Mk2PopularizeBonusTaskLogBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface Mk2PopularizeBonusTaskLogDao {

    @InsertProvider(type = Mk2PopularizeBonusTaskLogBuild.class, method = "insert")
    long insert(Mk2PopularizeBonusTaskLog log);

    @Select("SELECT " + Mk2PopularizeBonusTaskLogBuild.columns + " FROM mk2_popularize_bonus_task_log t WHERE t.status = '1' ORDER BY t.end_date DESC LIMIT 1 ")
    Mk2PopularizeBonusTaskLog findLatelyBonusTask();
}
