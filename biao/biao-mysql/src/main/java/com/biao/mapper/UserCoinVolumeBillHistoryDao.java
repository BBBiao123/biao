package com.biao.mapper;

import com.biao.entity.UserCoinVolumeBillHistory;
import com.biao.sql.build.UserCoinVolumeBillHistorySqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UserCoinVolumeBillHistoryDao.
 * <p>
 * 用户申请记录历史表处理.
 * <p>
 * 19-1-3下午5:47
 *
 *  "" sixh
 */
@Mapper
public interface UserCoinVolumeBillHistoryDao {
    /**
     * Insert long.
     *
     * @param userCoinVolumeBills the user coin volume bills
     * @return the long
     */
    @InsertProvider(type = UserCoinVolumeBillHistorySqlBuild.class, method = "batchInsert")
    long insertBatch(@Param("listValues") List<UserCoinVolumeBillHistory> userCoinVolumeBills);
}
