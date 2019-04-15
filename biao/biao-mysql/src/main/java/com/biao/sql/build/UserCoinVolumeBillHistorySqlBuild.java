package com.biao.sql.build;

import com.biao.entity.UserCoinVolumeBillHistory;
import com.biao.sql.BaseSqlBuild;

/**
 * UserCoinVolumeBillSqlBuild.
 * <p>
 *     UserCoinVolumeSqlBuild sql 构建器.
 * <p>
 * 19-1-2下午2:53
 *
 *  "" sixh
 */
public class UserCoinVolumeBillHistorySqlBuild extends BaseSqlBuild<UserCoinVolumeBillHistory, String> {

    public static final String ALL_COLUMES = "id, user_id, coin_symbol, priority, ref_key, op_sign, op_volume,op_lock_volume,source, mark, status,retry_count, force_lock,create_date, update_date, hash";

}

