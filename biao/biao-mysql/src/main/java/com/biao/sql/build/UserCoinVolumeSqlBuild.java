package com.biao.sql.build;

import com.biao.entity.UserCoinVolume;
import com.biao.sql.BaseSqlBuild;
import com.google.common.base.Joiner;

/**
 * The type User coin volume sql build.
 */
public class UserCoinVolumeSqlBuild extends BaseSqlBuild<UserCoinVolume, Integer> {

    /**
     * The constant columns.
     */
    public static final String columns = "id,user_id,coin_id,version,coin_symbol,volume,lock_volume,out_lock_volume,flag,flag_mark,create_by,update_by,create_date,update_date";

    /**
     * Replace volume and lock volume string.
     *
     * @param volume the volume
     * @return the string
     */
    public String replaceVolumeAndLockVolume(UserCoinVolume volume) {
        String column = Joiner.on(",").join("ID", "user_id", "coin_symbol", "volume", "lock_volume","coin_id");
        String value = Joiner.on(",").join("'" + volume.getId() + "'",
                "'" + volume.getUserId() + "'",
                "'" + volume.getCoinSymbol() + "'", volume.getVolume(), volume.getLockVolume(), "'"+volume.getCoinId()+"'");
        return new StringBuilder()
                .append("REPLACE INTO js_plat_user_coin_volume (").append(column)
                .append(") VALUES(").append(value)
                .append(")")
                .toString();
    }
}
