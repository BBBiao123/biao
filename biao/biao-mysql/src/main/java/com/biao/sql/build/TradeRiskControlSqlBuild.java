package com.biao.sql.build;

import com.biao.entity.TradeRiskControl;
import com.biao.sql.BaseSqlBuild;

import java.util.Map;

/**
 *  ""
 */
public class TradeRiskControlSqlBuild extends BaseSqlBuild<TradeRiskControl, Integer> {

    public static final String columns =
            "id,coin_main,coin_other,ex_pair_id,user_ids,fixed_volume,risk_ratio ";


    public String findByUserIdsAndCoinSymbol(Map<String, Object> paramMap) {

        return "select coin_symbol," +
                "sum(coinVolume) as coinVolume,sum(offlineVolume) as offlineVolume,sum(coinVolume)+sum(offlineVolume) as totalVolume " +
                "from (" +
                "select coin_symbol,sum(volume+lock_volume) as coinVolume,0 as offlineVolume from js_plat_user_coin_volume " +
                " where " +
                "user_id in (" + paramMap.get("userIds") + ")" +
                "and coin_symbol = '" + paramMap.get("coinSymbol") + "'" +
                "group by coin_symbol " +
                "union all " +
                "select coin_symbol,0 as coinVolume,sum(volume + advert_volume + lock_volume) as  offlineVolume from js_plat_offline_coin_volume where " +
                "user_id in (" + paramMap.get("userIds") + ")" +
                "and coin_symbol = '" + paramMap.get("coinSymbol") + "'" +
                "group by coin_symbol " +
                ") t " +
                "group by coin_symbol ";

    }

    public String findTotalRiskVolumeBySymbol(Map<String, Object> paramMap) {

        return "select coin_symbol, sum(historyVolume) as historyVolume," +
                " sum(depositVolume) as depositVolume,sum(withdrawVolume) as withdrawVolume," +
                "  sum(historyVolume+depositVolume-withdrawVolume) as  totalVolume" +
                " from (" +
                "    select coin_symbol,sum(volume) as historyVolume,0 as depositVolume," +
                "  0 as  withdrawVolume" +
                "    from js_plat_user_coin_volume_history where type != 'manual_scene'" +
                " and coin_symbol = '" + paramMap.get("coinSymbol") + "'" +
                " group by coin_symbol " +
                "    union all " +
                "    select coin_symbol,0 as historyVolume,sum(volume) as depositVolume,0 as withdrawVolume " +
                "    from js_plat_user_deposit_log where status = 1" +
                " and coin_symbol = '" + paramMap.get("coinSymbol") + "'" +
                " group by coin_symbol " +
                "    union all " +
                "    select coin_symbol,0 as historyVolume,0 as depositVolume,sum(volume) as withdrawVolume " +
                "    from js_plat_user_withdraw_log where status in (3)" +
                " and coin_symbol = '" + paramMap.get("coinSymbol") + "'" +
                " group by coin_symbol " +
                ") t ";

    }
}
