package com.biao.mapper;

import com.biao.entity.UserCoinVolumeHistory;
import com.biao.sql.build.UserCoinVolumeHistorySqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * 用户资产的操作信息；
 *
 *
 */
@Mapper
public interface UserCoinVolumeHistoryDao {

    @InsertProvider(type = UserCoinVolumeHistorySqlBuild.class, method = "insert")
    long insert(UserCoinVolumeHistory userCoinVolumeHistory);

    @UpdateProvider(type = UserCoinVolumeHistorySqlBuild.class, method = "updateById")
    long update(UserCoinVolumeHistory userCoinVolumeHistory);

    @SelectProvider(type = UserCoinVolumeHistorySqlBuild.class, method = "findById")
    UserCoinVolumeHistory findById(String id);

}
