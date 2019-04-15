package com.biao.mapper;

import com.biao.entity.MkDistributeLog;
import com.biao.sql.build.MkDistributeLogSqlBuild;
import com.biao.sql.build.WithdrawLogSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MkDistributeLogDao {

    @InsertProvider(type = MkDistributeLogSqlBuild.class, method = "insert")
    void insert(MkDistributeLog mkDistributeLog);

    @SelectProvider(type = MkDistributeLogSqlBuild.class, method = "findById")
    MkDistributeLog findById(String id);

    @Select("select " + WithdrawLogSqlBuild.columns + " from mk_common_distribute_log where user_id = #{userId} and coin_id = #{coinId} and status = 1 order by create_date desc")
    List<MkDistributeLog> findDistributeLogByUserIdAndCoinId(@Param("userId") String userId, @Param("coinId") String coinId);

    @Select("select " + MkDistributeLogSqlBuild.columns + " from mk_common_distribute_log where user_id = #{userId} and status = 1 order by create_date desc")
    List<MkDistributeLog> findDistributeLogByUserId(String userId);

}
