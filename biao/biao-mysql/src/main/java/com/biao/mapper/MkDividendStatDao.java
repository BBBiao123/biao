package com.biao.mapper;

import com.biao.entity.MkDividendStat;
import com.biao.sql.build.MkDividendStatSqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface MkDividendStatDao {

    @InsertProvider(type = MkDividendStatSqlBuild.class, method = "insert")
    long insert(MkDividendStat mkDividendStat);

    @InsertProvider(type = MkDividendStatSqlBuild.class, method = "updateById")
    long update(MkDividendStat mkDividendStat);

}
