package com.biao.mapper;

import com.biao.entity.CoinVolumeRiskMgt;
import com.biao.pojo.CoinVolumeReconciliationVO;
import com.biao.sql.build.CoinVolumeRiskMgtSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CoinVolumeRiskMgtDao {

    @InsertProvider(type = CoinVolumeRiskMgtSqlBuild.class, method = "insert")
    void insert(CoinVolumeRiskMgt coinVolumeRiskMgt);

    @SelectProvider(type = CoinVolumeRiskMgtSqlBuild.class, method = "findById")
    CoinVolumeRiskMgt findById(String id);

    @Select("SELECT " + CoinVolumeRiskMgtSqlBuild.columns + " from js_plat_coin_volume_risk_mgt where coin_id = #{coinId} limit 1")
    CoinVolumeRiskMgt findByCoinId(@Param("coinId") String coinId);

    @Select("SELECT " + CoinVolumeRiskMgtSqlBuild.columns + " from js_plat_coin_volume_risk_mgt where coin_symbol = #{coinSymbol} limit 1")
    CoinVolumeRiskMgt findByCoinSymbol(@Param("coinSymbol") String coinSymbol);

    @Select("select coin_symbol, \n" +
            "       sum(transferVolume) as transferVolume,\n" +
            "\t\t\t sum(depositVolume) as depositVolume,\n" +
            "\t\t\t sum(withdrawVolume) as  withdrawVolume,\n" +
            "\t\t\t sum(tradeVolume) as tradeVolume,\n" +
            "\t\t\t sum(offlineVolume) as offlineVolume,\n" +
            "\t\t\t sum(lockVolume) as lockVolume,\n" +
            "\t\t\t sum(minerVolume) as minerVolume,\n" +
            "\t     sum(presentVolume) as presentVolume,\n" +
            "\t\t\t sum(transferVolume+depositVolume+presentVolume+minerVolume- withdrawVolume) as realVolume,\n" +
            "\t\t\t sum(tradeVolume+offlineVolume+lockVolume) as accountVolume,\n" +
            "\t\t\t sum(transferVolume+depositVolume+presentVolume+minerVolume- withdrawVolume) - sum(tradeVolume+offlineVolume+lockVolume) as balance\n" +
            "from (\n" +
            "    #查transferVolume\n" +
            "    select coin_symbol,sum(volume) as transferVolume,0 as depositVolume,0 as  withdrawVolume,0 as presentVolume,0 as tradeVolume,\n" +
            "      0 as offlineVolume,0 as lockVolume,0 as minerVolume \n" +
            "    from js_plat_user_coin_volume_history where type != 'manual_scene' and type != 'income_scene' group by coin_symbol \n" +
            "    #depositVolume\n" +
            "    union all \n" +
            "    select coin_symbol,0 as transferVolume,sum(volume) as depositVolume,0 as  withdrawVolume,0 as presentVolume,0 as tradeVolume,\n" +
            "      0 as offlineVolume,0 as lockVolume ,0 as minerVolume \n" +
            "    from js_plat_user_deposit_log where status = 1  group by coin_symbol \n" +
            "    # withdrawVolume\n" +
            "    union all \n" +
            "    select coin_symbol,0 as transferVolume,0 as depositVolume,sum(volume) as  withdrawVolume,0 as presentVolume,0 as tradeVolume,\n" +
            "      0 as offlineVolume,0 as lockVolume ,0 as minerVolume \n" +
            "    from js_plat_user_withdraw_log where status in (3)  group by coin_symbol \n" +
            "    #presentVolume--注册送币\n" +
            "    UNION ALL \n" +
            "    select coin_symbol,0 as transferVolume,0 as depositVolume,0 as  withdrawVolume,sum(volume) as presentVolume,0 as tradeVolume,\n" +
            "      0 as offlineVolume,0 as lockVolume ,0 as minerVolume \n" +
            "    from mk2_popularize_register_coin where status = 2  group by coin_symbol \n" +
            "    #presentVolume--注册红包\n" +
            "    UNION ALL \t\n" +
            "    select coin_symbol,0 as transferVolume,0 as depositVolume,0 as  withdrawVolume,sum(real_volume) as presentVolume,0 as tradeVolume,\n" +
            "      0 as offlineVolume,0 as lockVolume ,0 as minerVolume \n" +
            "    from mk_user_register_lottery_log   group by coin_symbol \n" +
            "    #币币帐户\n" +
            "    union all \n" +
            "    select coin_symbol,0 as transferVolume,0 as depositVolume,0 as  withdrawVolume,0 as presentVolume,sum(volume +  lock_volume + ifnull(out_lock_volume,0)) as tradeVolume,\n" +
            "      0 as offlineVolume,0 as lockVolume ,0 as minerVolume \n" +
            "    from js_plat_user_coin_volume  group by coin_symbol \n" +
            "     union all \n" +
            "    #c2c帐户\n" +
            "    select coin_symbol,0 as transferVolume,0 as depositVolume,0 as 取币,0 as presentVolume,0 as tradeVolume,\n" +
            "       sum(volume + advert_volume + lock_volume + bail_volume) as offlineVolume,0 as lockVolume ,0 as minerVolume \n" +
            "    from js_plat_offline_coin_volume   group by coin_symbol \n" +
            "    UNION ALL \n" +
            "    #冻结帐户\n" +
            "    select coin_symbol,0 as transferVolume,0 as depositVolume,0 as 取币,0 as presentVolume,0 as tradeVolume,\n" +
            "      0 as offlineVolume,sum(lock_volume) as lockVolume ,0 as minerVolume \n" +
            "    FROM mk2_popularize_common_member t WHERE t.lock_status = '1' group by coin_symbol\n" +
            "    UNION ALL  \n" +
            "    select coin_symbol,0 as transferVolume,0 as depositVolume,0 as 取币,0 as presentVolume,0 as tradeVolume,\n" +
            "      0 as offlineVolume,sum(lock_volume) as lockVolume ,0 as minerVolume\n" +
            "    FROM mk2_popularize_nodal_member t WHERE t.type = '2' AND t.lock_status = '1'  group by coin_symbol\n" +
            "    UNION ALL \n" +
            "    select coin_symbol,0 as transferVolume,0 as depositVolume,0 as 取币,0 as presentVolume,0 as tradeVolume,\n" +
            "      0 as offlineVolume,sum(lock_volume) as lockVolume ,0 as minerVolume \n" +
            "    FROM mk2_popularize_area_member t WHERE t.type = '1' AND t.`status` = '1'    group by coin_symbol\n" +
            "    UNION ALL \n" +
            "    #minerVolume\n" +
            "    select coin_symbol,0 as transferVolume,0 as depositVolume,0 as 取币,0 as presentVolume,0 as tradeVolume,\n" +
            "      0 as offlineVolume,0 as lockVolume ,sum(volume) as minerVolume \n" +
            "    FROM  mk2_popularize_mining_give_coin_log t  group by coin_symbol \n" +
            "    UNION ALL \t\n" +
            "\t#接力撞奖\n" +
            "\tselect coin_symbol,0 as transferVolume,0 as depositVolume,0 as  withdrawVolume,sum(volume) as presentVolume,0 as tradeVolume,\n" +
            "      0 as offlineVolume,0 as lockVolume ,0 as minerVolume \n" +
            "     from mk_relay_remit_log   group by coin_symbol \n" +
            ") t group by coin_symbol")
    List<CoinVolumeReconciliationVO> getReconciliation();


}
