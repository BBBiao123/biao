package com.biao.mapper;

import com.biao.entity.TraderVolumeSnapshot;
import com.biao.sql.build.TraderVolumeSnapshotSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TraderVolumeSnapshotDao {

    @InsertProvider(type = TraderVolumeSnapshotSqlBuild.class, method = "insert")
    void insert(TraderVolumeSnapshot traderVolumeSnapshot);

    @InsertProvider(type = TraderVolumeSnapshotSqlBuild.class, method = "batchInsert")
    void batchInsert(@Param("listValues") List<TraderVolumeSnapshot> traderVolumeSnapshots);

    @UpdateProvider(type = TraderVolumeSnapshotSqlBuild.class, method = "updateById")
    void updateById(TraderVolumeSnapshot traderVolumeSnapshot);

    @Insert("INSERT INTO `js_plat_trader_volume_snapshot` (`id`, `snap_date`, `user_id`, `user_tag`, `coin_id`, `coin_symbol`, `trade_volume`, `offline_volume`, `lock_volume`, `total_volume`, `bill_sum_volume`, `balance`, `bobi_volume`, `remark`, `create_date`, `create_by`)\n" +
            "select REPLACE(UUID(),'-','') as id, #{date} as snap_date, '' as user_id, 'UES-TRADER' as user_tag, '' as coin_id, coin_symbol, sum(trade_volume) as trade_volume, sum(offline_volume) as offline_volume, sum(lock_volume) as lock_volume,sum(trade_volume)+sum(offline_volume)+sum(lock_volume) as total_volume,sum(bill_sum_volume) as bill_sum_volume,sum(trade_volume)+sum(offline_volume)+sum(lock_volume)-sum(bill_sum_volume) as balance,sum(bobi_volume) as bobi_volume, '' as remark, now(), 'task'\n" +
            "from (\n" +
            "select coin_symbol,0 as trade_volume,0 as offline_volume,0 as lock_volume,0  as bill_sum_volume,sum(volume) as bobi_volume from js_plat_user_coin_volume_history where \n" +
            "user_id in (select id from js_plat_user where source='UES-TRADER') \n" +
            "group by coin_symbol \n" +
            "union all \n" +
            "select coin_symbol,sum(volume+lock_volume+ifnull(out_lock_volume,0)) as trade_volume,0 as offline_volume,0 as lock_volume,0  as bill_sum_volume,0 as bobi_volume from js_plat_user_coin_volume where \n" +
            "user_id in (select id from js_plat_user where source='UES-TRADER') \n" +
            "group by coin_symbol \n" +
            "union all \n" +
            "select coin_symbol,0 as trade_volume,sum(volume + advert_volume + lock_volume + bail_volume) as  offline_volume,0 as lock_volume,0 as bill_sum_volume,0 as bobi_volume from js_plat_offline_coin_volume where \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER') \n" +
            "group by coin_symbol \n" +
            "union all \n" +
            "#冻结\n" +
            "select coin_symbol,0 as trade_volume,0 as offline_volume ,sum(volume) as lock_volume ,0 as bill_sum_volume ,0 as bobi_volume\n" +
            "from (\n" +
            "    SELECT coin_symbol, t.lock_volume AS volume FROM mk2_popularize_common_member t WHERE t.lock_status = '1' and \n" +
            "    t.user_id in(select id from js_plat_user where source='UES-TRADER') \n" +
            "    UNION ALL  \n" +
            "    SELECT coin_symbol,t.lock_volume AS volume FROM mk2_popularize_nodal_member t WHERE t.type = '2' AND t.lock_status = '1' and \n" +
            "    t.user_id in(select id from js_plat_user where source='UES-TRADER') \n" +
            "    UNION ALL \n" +
            "    SELECT coin_symbol,t.lock_volume AS volume FROM mk2_popularize_area_member t WHERE t.type = '1' AND t.`status` = '1'  and \n" +
            "    t.user_id in(select id from js_plat_user where source='UES-TRADER') \n" +
            ") f \n" +
            "group by coin_symbol \n" +
            "union all \n" +
            "#############个人帐户bill_sum_volume##################\n" +
            "select  coin_symbol,0 as trade_volume,0 as offline_volume ,0 as lock_volume ,sum(volume) as bill_sum_volume,0 as bobi_volume from (\n" +
            "#查充币\n" +
            "select create_date,'充币' as type ,coin_symbol,volume from js_plat_user_deposit_log where status = 1 and \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER') \n" +
            "#查提币\n" +
            "union all \n" +
            "select create_date,'提币' as type,coin_symbol,0-volume as volume from js_plat_user_withdraw_log where status = 3 and  \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER') \n" +
            "#查赠币\n" +
            "union all \n" +
            "select create_date,'赠币' as type,coin_symbol,volume from mk2_popularize_register_coin where status = 2 and   \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER')  \n" +
            "#查手工转币\n" +
            "union all \n" +
            "select create_date,'手工转币' as type,coin_symbol,volume from js_plat_user_coin_volume_history where   type != 'manual_scene' and type != 'income_scene' and \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER') \n" +
            "#查转入C2C\n" +
            "#select create_date,'转入C2C' as type,coin_symbol,0-volume as volume from js_plat_offline_transfer_log where type =0 and \n" +
            "#user_id in(select id from js_plat_user where source='UES-TRADER')  \n" +
            "#union all \n" +
            "#查C2C转出\n" +
            "#select create_date,'C2C转出' as type,coin_symbol,volume as volume from js_plat_offline_transfer_log where type =1 and \n" +
            "#user_id in(select id from js_plat_user where source='UES-TRADER')  \n" +
            "#union all \n" +
            "#查买入交易记录\n" +
            "union all \n" +
            "SELECT create_date,'买入支出' as type,coin_main as coin_symbol,0-spent_volume as volume from js_plat_ex_order where ex_type = 0 and `status` in(1,2,3) and \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER') \n" +
            "union all \n" +
            "SELECT create_date,'买入收入' as type,coin_other as coin_symbol,to_coin_volume as volume from js_plat_ex_order where ex_type = 0 and `status` in(1,2,3) and \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER') \n" +
            "#查卖出交易记录\n" +
            "union all \n" +
            "SELECT create_date,'卖出支出' as type,coin_other as coin_symbol,0-spent_volume as volume from js_plat_ex_order where ex_type = 1 and `status` in(1,2,3) and \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER')\n" +
            "union all  \n" +
            "SELECT create_date,'卖出收入' as type,coin_main as coin_symbol,to_coin_volume as volume from js_plat_ex_order where ex_type = 1 and `status` in(1,2,3) and \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER')\n" +
            "#冻结\n" +
            "#SELECT create_date,'个人冻结' as type,coin_symbol, t.lock_volume AS volume FROM mk2_popularize_common_member t WHERE t.lock_status = '1' and \n" +
            "#t.user_id in(select id from js_plat_user where source='UES-TRADER') \n" +
            "#UNION ALL  \n" +
            "#SELECT create_date,'节点人冻结' as type, coin_symbol,t.lock_volume AS volume FROM mk2_popularize_nodal_member t WHERE t.type = '2' AND t.lock_status = '1' and \n" +
            "#t.user_id in(select id from js_plat_user where source='UES-TRADER')\n" +
            "#UNION ALL \n" +
            "#SELECT update_date as create_date,'区域合伙人冻结' as type, coin_symbol,t.lock_volume AS volume FROM mk2_popularize_area_member t WHERE t.type = '1' AND t.`status` = '1'  and \n" +
            "#t.user_id in(select id from js_plat_user where source='UES-TRADER') \n" +
            "#UNION ALL \n" +
            "#释放\n" +
            "#SELECT t.release_cycle_date as create_date,'释放' as type,coin_symbol,t.release_volume AS volume FROM     mk2_popularize_release_log t WHERE     t.release_status = '1' AND \n" +
            "#t.user_id in(select id from js_plat_user where source='UES-TRADER') \n" +
            "#union all \n" +
            "#查买入交易记录\n" +
            "UNION ALL \n" +
            "SELECT create_date,'C2C买入收入' as type,symbol as coin_symbol, volume from js_plat_offline_order_detail where  `status` = 2 and remarks='buy'  and \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER') \n" +
            "#查卖出交易记录\n" +
            "union all \n" +
            "SELECT create_date,'C2C卖出支出' as type,symbol as coin_symbol,0-volume-fee_volume as volume from js_plat_offline_order_detail where  `status` = 2 and remarks='sell'  and \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER') \n" +
            "#个人挖矿\n" +
            "union all \n" +
            "select create_date,'持币挖矿' as type ,coin_symbol,volume  from mk2_popularize_mining_give_coin_log where type = 1 and \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER') \n" +
            "#多元挖矿\n" +
            "union all \n" +
            "select create_date,'多元挖矿' as type ,coin_symbol,volume  from mk2_popularize_mining_give_coin_log where type = 2 and \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER') \n" +
            "#查OTC交易记录\n" +
            "union all \n" +
            "SELECT create_date,'otc买入收入' as type,symbol as coin_symbol, volume from otc_offline_order_detail where  `status` = 2 and remarks='buy'  and \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER') \n" +
            "union all \n" +
            "SELECT create_date,'otc卖出支出' as type,symbol as coin_symbol,0-volume as volume from otc_offline_order_detail where  `status` = 2 and remarks='sell'  and \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER') \n" +
            "#查转帐转入记录\n" +
            "union all \n" +
            "select create_date,'转帐转入' as type,coin_symbol ,(volume+fee) as volume from js_plat_offline_change_log where type = 0 and \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER')\n" +
            "union all \n" +
            "select create_date,'转帐转出' as type,coin_symbol ,0-(volume+fee) as volume from js_plat_offline_change_log where type = 1 and \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER')\n" +
            "union all \n" +
            "select create_date,'红包支出' as type,coin_symbol ,0-(volume+fee) as volume from js_plat_offline_change_log where type = '2' and \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER')\n" +
            "union all \n" +
            "select create_date,'红包收入' as type,coin_symbol ,(volume+fee) as volume from js_plat_offline_change_log where type = '3' and \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER')\n" +
            "union all \n" +
            "select create_date,'红包退回' as type,coin_symbol ,(volume+fee) as volume from js_plat_offline_change_log where type = '4' and \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER')\n" +
            "#查广告手续费\n" +
            "union all \n" +
            "select create_date,'广告手续费' as type,symbol as coin_symbol,0-fee_volume as volume from js_plat_offline_order where fee_volume >0 and \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER')\n" +
            "#注册红包\n" +
            "union all \n" +
            "select create_date,'注册红包' as type,coin_symbol,real_volume as volume  from mk_user_register_lottery_log  where \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER') \n" +
            "#接力撞奖 \n" +
            "union all \n" +
            "select create_date,'接力撞奖' as type,coin_symbol, volume  from mk_relay_remit_log  where \n" +
            "user_id in(select id from js_plat_user where source='UES-TRADER') \n" +
            "#通兑接口--转入 \n" +
            "union all \n" +
            "select create_time as create_date,'通兑转入' as type,coin_type as coin_symbol,(amount_coin-to_fee) as volume from otc_hq_pay_third_order where \n" +
            "to_user_id in(select id from js_plat_user where source='UES-TRADER') \n" +
            "#通兑接口--转出 \n" +
            "union all \n" +
            "select create_time as create_date,'通兑转入' as type,coin_type as coin_symbol,(0-amount_coin-from_fee) as volume from otc_hq_pay_third_order where \n" +
            "from_user_id in(select id from js_plat_user where source='UES-TRADER') \n" +
            "union all \n" +
            "select create_date as create_date,'OTC转入' as type,symbol as coin_symbol,(volume) as volume from otc_volume_change_request where \n" +
            "buy_user_id in(select id from js_plat_user where source='UES-TRADER')  and `status`='1' and type='6'\n" +
            "#通兑接口--转出 \n" +
            ") t\n" +
            "group by t.coin_symbol\n" +
            ") t \n" +
            "group by coin_symbol")
    long insertTraderVolumeSnapshot(@Param("date") String date);

    @Insert("INSERT INTO `js_plat_trader_volume_snapshot` (`id`, `snap_date`, `user_id`, `user_tag`, `coin_id`, `coin_symbol`, `trade_volume`, `offline_volume`, `lock_volume`, `total_volume`, `bill_sum_volume`, `balance`, `bobi_volume`, `remark`, `create_date`, `create_by`)\n" +
            "select REPLACE(UUID(),'-','') as id,  #{date} as snap_date, '' as user_id, 'ALL' as user_tag, '' as coin_id, coin_symbol, sum(trade_volume) as trade_volume, sum(offline_volume) as offline_volume, sum(lock_volume) as lock_volume,sum(trade_volume)+sum(offline_volume)+sum(lock_volume) as total_volume,sum(bill_sum_volume) as bill_sum_volume,sum(trade_volume)+sum(offline_volume)+sum(lock_volume)-sum(bill_sum_volume) as balance,sum(bobi_volume) as bobi_volume, '' as remark, now(), 'task'\n" +
            "from (\n" +
            "\n" +
            "select coin_symbol,0 as trade_volume,0 as offline_volume,0 as lock_volume,0  as bill_sum_volume,sum(volume) as bobi_volume from js_plat_user_coin_volume_history \n" +
            "group by coin_symbol \n" +
            "union all \n" +
            "select coin_symbol,sum(volume+lock_volume+ifnull(out_lock_volume,0)) as trade_volume,0 as offline_volume,0 as lock_volume,0  as bill_sum_volume,0 as bobi_volume from js_plat_user_coin_volume \n" +
            "group by coin_symbol \n" +
            "union all \n" +
            "select coin_symbol,0 as trade_volume,sum(volume + advert_volume + lock_volume + bail_volume) as  offline_volume,0 as lock_volume,0 as bill_sum_volume,0 as bobi_volume from js_plat_offline_coin_volume \n" +
            "group by coin_symbol \n" +
            "union all \n" +
            "#冻结\n" +
            "select coin_symbol,0 as trade_volume,0 as offline_volume ,sum(volume) as lock_volume ,0 as bill_sum_volume ,0 as bobi_volume\n" +
            "from (\n" +
            "    SELECT coin_symbol, t.lock_volume AS volume FROM mk2_popularize_common_member t WHERE t.lock_status = '1' \n" +
            "    UNION ALL  \n" +
            "    SELECT coin_symbol,t.lock_volume AS volume FROM mk2_popularize_nodal_member t WHERE t.type = '2' AND t.lock_status = '1' \n" +
            "    UNION ALL \n" +
            "    SELECT coin_symbol,t.lock_volume AS volume FROM mk2_popularize_area_member t WHERE t.type = '1' AND t.`status` = '1' \n" +
            ") f \n" +
            "group by coin_symbol \n" +
            "union all \n" +
            "#############个人帐户bill_sum_volume##################\n" +
            "select  coin_symbol,0 as trade_volume,0 as offline_volume ,0 as lock_volume ,sum(volume) as bill_sum_volume,0 as bobi_volume from (\n" +
            "#查充币\n" +
            "select create_date,'充币' as type ,coin_symbol,volume from js_plat_user_deposit_log where status = 1 \n" +
            "#查提币\n" +
            "union all \n" +
            "select create_date,'提币' as type,coin_symbol,0-volume as volume from js_plat_user_withdraw_log where status = 3\n" +
            "#查赠币\n" +
            "union all \n" +
            "select create_date,'赠币' as type,coin_symbol,volume from mk2_popularize_register_coin where status = 2 \n" +
            "#查手工转币\n" +
            "union all \n" +
            "select create_date,'手工转币' as type,coin_symbol,volume from js_plat_user_coin_volume_history where   type != 'manual_scene' and type != 'income_scene' \n" +
            "#查转入C2C\n" +
            "#select create_date,'转入C2C' as type,coin_symbol,0-volume as volume from js_plat_offline_transfer_log where type =0 and \n" +
            "#user_id in(select id from js_plat_user where source='UES-TRADER')  \n" +
            "#union all \n" +
            "#查C2C转出\n" +
            "#select create_date,'C2C转出' as type,coin_symbol,volume as volume from js_plat_offline_transfer_log where type =1 and \n" +
            "#user_id in(select id from js_plat_user where source='UES-TRADER')  \n" +
            "#union all \n" +
            "#查买入交易记录\n" +
            "union all \n" +
            "SELECT create_date,'买入支出' as type,coin_main as coin_symbol,0-spent_volume as volume from js_plat_ex_order where ex_type = 0 and `status` in(1,2,3) \n" +
            "union all \n" +
            "SELECT create_date,'买入收入' as type,coin_other as coin_symbol,to_coin_volume as volume from js_plat_ex_order where ex_type = 0 and `status` in(1,2,3) \n" +
            "#查卖出交易记录\n" +
            "union all \n" +
            "SELECT create_date,'卖出支出' as type,coin_other as coin_symbol,0-spent_volume as volume from js_plat_ex_order where ex_type = 1 and `status` in(1,2,3) \n" +
            "union all  \n" +
            "SELECT create_date,'卖出收入' as type,coin_main as coin_symbol,to_coin_volume as volume from js_plat_ex_order where ex_type = 1 and `status` in(1,2,3) \n" +
            "UNION ALL \n" +
            "#冻结\n" +
            "SELECT create_date,'个人冻结' as type,coin_symbol, t.lock_volume AS volume FROM mk2_popularize_common_member t WHERE t.lock_status = '1' \n" +
            "UNION ALL  \n" +
            "SELECT create_date,'节点人冻结' as type, coin_symbol,t.lock_volume AS volume FROM mk2_popularize_nodal_member t WHERE t.type = '2' AND t.lock_status = '1' \n" +
            "UNION ALL \n" +
            "SELECT update_date as create_date,'区域合伙人冻结' as type, coin_symbol,t.lock_volume AS volume FROM mk2_popularize_area_member t WHERE t.type = '1' AND t.`status` = '1'  \n" +
            "UNION ALL \n" +
            "#释放\n" +
            "SELECT t.release_cycle_date as create_date,'释放' as type,coin_symbol,t.release_volume AS volume FROM     mk2_popularize_release_log t WHERE     t.release_status = '1' \n" +
            "#查买入交易记录\n" +
            "UNION ALL \n" +
            "SELECT create_date,'C2C买入收入' as type,symbol as coin_symbol, volume from js_plat_offline_order_detail where  `status` = 2 and remarks='buy'  \n" +
            "#查卖出交易记录\n" +
            "union all \n" +
            "SELECT create_date,'C2C卖出支出' as type,symbol as coin_symbol,0-volume-fee_volume as volume from js_plat_offline_order_detail where  `status` = 2 and remarks='sell' \n" +
            "#个人挖矿\n" +
            "union all \n" +
            "select create_date,'持币挖矿' as type ,coin_symbol,volume  from mk2_popularize_mining_give_coin_log where type = 1 \n" +
            "#多元挖矿\n" +
            "union all \n" +
            "select create_date,'多元挖矿' as type ,coin_symbol,volume  from mk2_popularize_mining_give_coin_log where type = 2 \n" +
            "#查OTC交易记录\n" +
            "union all \n" +
            "SELECT create_date,'otc买入收入' as type,symbol as coin_symbol, volume from otc_offline_order_detail where  `status` = 2 and remarks='buy' \n" +
            "union all \n" +
            "SELECT create_date,'otc卖出支出' as type,symbol as coin_symbol,0-volume as volume from otc_offline_order_detail where  `status` = 2 and remarks='sell' \n" +
            "#查转帐转入记录\n" +
            "union all \n" +
            "select create_date,'转帐转入' as type,coin_symbol ,(volume+fee) as volume from js_plat_offline_change_log where type = 0 \n" +
            "union all \n" +
            "select create_date,'转帐转出' as type,coin_symbol ,0-(volume+fee) as volume from js_plat_offline_change_log where type = 1 \n" +
            "union all \n" +
            "select create_date,'红包支出' as type,coin_symbol ,0-(volume+fee) as volume from js_plat_offline_change_log where type = '2' \n" +
            "union all \n" +
            "select create_date,'红包收入' as type,coin_symbol ,(volume+fee) as volume from js_plat_offline_change_log where type = '3' \n" +
            "union all \n" +
            "select create_date,'红包退回' as type,coin_symbol ,(volume+fee) as volume from js_plat_offline_change_log where type = '4' \n" +
            "#查广告手续费\n" +
            "union all \n" +
            "select create_date,'广告手续费' as type,symbol as coin_symbol,0-fee_volume as volume from js_plat_offline_order where fee_volume >0 \n" +
            "#注册红包\n" +
            "union all \n" +
            "select create_date,'注册红包' as type,coin_symbol,real_volume as volume  from mk_user_register_lottery_log  \n" +
            "#接力撞奖 \n" +
            "union all \n" +
            "select create_date,'接力撞奖' as type,coin_symbol, volume  from mk_relay_remit_log  \n" +
            "#通兑接口--转入 \n" +
            "union all \n" +
            "select create_time as create_date,'通兑转入' as type,coin_type as coin_symbol,(amount_coin-to_fee) as volume from otc_hq_pay_third_order \n" +
            "#通兑接口--转出 \n" +
            "union all \n" +
            "select create_time as create_date,'通兑转入' as type,coin_type as coin_symbol,(0-amount_coin-from_fee) as volume from otc_hq_pay_third_order \n" +
            "union all \n" +
            "select create_date as create_date,'OTC转入' as type,symbol as coin_symbol,(volume) as volume from otc_volume_change_request where \n" +
            "`status`='1' and type='6'\n" +
            "#通兑接口--转出 \n" +
            ") t\n" +
            "group by t.coin_symbol\n" +
            ") t where coin_symbol is not null\n" +
            "group by coin_symbol;\n")
    long insertAllVolumeSnapshot(@Param("date") String date);

    @Insert("INSERT INTO `js_plat_trader_volume_snapshot` (`id`, `snap_date`, `user_id`, `user_tag`, `coin_id`, `coin_symbol`, `trade_volume`, `offline_volume`, `lock_volume`, `total_volume`, `bill_sum_volume`, `balance`, `bobi_volume`, `remark`, `create_date`, `create_by`)\n" +
            "select REPLACE(UUID(),'-','') as id, #{date} as snap_date, '' as user_id, 'AUTO-TRADER' as user_tag, '' as coin_id, coin_symbol, sum(trade_volume) as trade_volume, sum(offline_volume) as offline_volume, sum(lock_volume) as lock_volume,sum(trade_volume)+sum(offline_volume)+sum(lock_volume) as total_volume,sum(bill_sum_volume) as bill_sum_volume,sum(trade_volume)+sum(offline_volume)+sum(lock_volume)-sum(bill_sum_volume) as balance,sum(bobi_volume) as bobi_volume, '' as remark, now(), 'task'\n" +
            "from (\n" +
            "select coin_symbol,0 as trade_volume,0 as offline_volume,0 as lock_volume,0  as bill_sum_volume,sum(volume) as bobi_volume from js_plat_user_coin_volume_history where \n" +
            "user_id in (select id from js_plat_user where source='AUTO-TRADER') \n" +
            "group by coin_symbol \n" +
            "union all \n" +
            "select coin_symbol,sum(volume+lock_volume+ifnull(out_lock_volume,0)) as trade_volume,0 as offline_volume,0 as lock_volume,0  as bill_sum_volume,0 as bobi_volume from js_plat_user_coin_volume where \n" +
            "user_id in (select id from js_plat_user where source='AUTO-TRADER') \n" +
            "group by coin_symbol \n" +
            "union all \n" +
            "select coin_symbol,0 as trade_volume,sum(volume + advert_volume + lock_volume + bail_volume) as  offline_volume,0 as lock_volume,0 as bill_sum_volume,0 as bobi_volume from js_plat_offline_coin_volume where \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER') \n" +
            "group by coin_symbol \n" +
            "union all \n" +
            "#冻结\n" +
            "select coin_symbol,0 as trade_volume,0 as offline_volume ,sum(volume) as lock_volume ,0 as bill_sum_volume ,0 as bobi_volume\n" +
            "from (\n" +
            "    SELECT coin_symbol, t.lock_volume AS volume FROM mk2_popularize_common_member t WHERE t.lock_status = '1' and \n" +
            "    t.user_id in(select id from js_plat_user where source='AUTO-TRADER') \n" +
            "    UNION ALL  \n" +
            "    SELECT coin_symbol,t.lock_volume AS volume FROM mk2_popularize_nodal_member t WHERE t.type = '2' AND t.lock_status = '1' and \n" +
            "    t.user_id in(select id from js_plat_user where source='AUTO-TRADER') \n" +
            "    UNION ALL \n" +
            "    SELECT coin_symbol,t.lock_volume AS volume FROM mk2_popularize_area_member t WHERE t.type = '1' AND t.`status` = '1'  and \n" +
            "    t.user_id in(select id from js_plat_user where source='AUTO-TRADER') \n" +
            ") f \n" +
            "group by coin_symbol \n" +
            "union all \n" +
            "#############个人帐户bill_sum_volume##################\n" +
            "select  coin_symbol,0 as trade_volume,0 as offline_volume ,0 as lock_volume ,sum(volume) as bill_sum_volume,0 as bobi_volume from (\n" +
            "#查充币\n" +
            "select create_date,'充币' as type ,coin_symbol,volume from js_plat_user_deposit_log where status = 1 and \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER') \n" +
            "#查提币\n" +
            "union all \n" +
            "select create_date,'提币' as type,coin_symbol,0-volume as volume from js_plat_user_withdraw_log where status = 3 and  \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER') \n" +
            "#查赠币\n" +
            "union all \n" +
            "select create_date,'赠币' as type,coin_symbol,volume from mk2_popularize_register_coin where status = 2 and   \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER')  \n" +
            "#查手工转币\n" +
            "union all \n" +
            "select create_date,'手工转币' as type,coin_symbol,volume from js_plat_user_coin_volume_history where   type != 'manual_scene' and type != 'income_scene' and \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER') \n" +
            "#查转入C2C\n" +
            "#select create_date,'转入C2C' as type,coin_symbol,0-volume as volume from js_plat_offline_transfer_log where type =0 and \n" +
            "#user_id in(select id from js_plat_user where source='AUTO-TRADER')  \n" +
            "#union all \n" +
            "#查C2C转出\n" +
            "#select create_date,'C2C转出' as type,coin_symbol,volume as volume from js_plat_offline_transfer_log where type =1 and \n" +
            "#user_id in(select id from js_plat_user where source='AUTO-TRADER')  \n" +
            "#union all \n" +
            "#查买入交易记录\n" +
            "union all \n" +
            "SELECT create_date,'买入支出' as type,coin_main as coin_symbol,0-spent_volume as volume from js_plat_ex_order where ex_type = 0 and `status` in(1,2,3) and \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER') \n" +
            "union all \n" +
            "SELECT create_date,'买入收入' as type,coin_other as coin_symbol,to_coin_volume as volume from js_plat_ex_order where ex_type = 0 and `status` in(1,2,3) and \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER') \n" +
            "#查卖出交易记录\n" +
            "union all \n" +
            "SELECT create_date,'卖出支出' as type,coin_other as coin_symbol,0-spent_volume as volume from js_plat_ex_order where ex_type = 1 and `status` in(1,2,3) and \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER')\n" +
            "union all  \n" +
            "SELECT create_date,'卖出收入' as type,coin_main as coin_symbol,to_coin_volume as volume from js_plat_ex_order where ex_type = 1 and `status` in(1,2,3) and \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER')\n" +
            "#冻结\n" +
            "#SELECT create_date,'个人冻结' as type,coin_symbol, t.lock_volume AS volume FROM mk2_popularize_common_member t WHERE t.lock_status = '1' and \n" +
            "#t.user_id in(select id from js_plat_user where source='AUTO-TRADER') \n" +
            "#UNION ALL  \n" +
            "#SELECT create_date,'节点人冻结' as type, coin_symbol,t.lock_volume AS volume FROM mk2_popularize_nodal_member t WHERE t.type = '2' AND t.lock_status = '1' and \n" +
            "#t.user_id in(select id from js_plat_user where source='AUTO-TRADER')\n" +
            "#UNION ALL \n" +
            "#SELECT update_date as create_date,'区域合伙人冻结' as type, coin_symbol,t.lock_volume AS volume FROM mk2_popularize_area_member t WHERE t.type = '1' AND t.`status` = '1'  and \n" +
            "#t.user_id in(select id from js_plat_user where source='AUTO-TRADER') \n" +
            "#UNION ALL \n" +
            "#释放\n" +
            "#SELECT t.release_cycle_date as create_date,'释放' as type,coin_symbol,t.release_volume AS volume FROM     mk2_popularize_release_log t WHERE     t.release_status = '1' AND \n" +
            "#t.user_id in(select id from js_plat_user where source='AUTO-TRADER') \n" +
            "#union all \n" +
            "#查买入交易记录\n" +
            "UNION ALL \n" +
            "SELECT create_date,'C2C买入收入' as type,symbol as coin_symbol, volume from js_plat_offline_order_detail where  `status` = 2 and remarks='buy'  and \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER') \n" +
            "#查卖出交易记录\n" +
            "union all \n" +
            "SELECT create_date,'C2C卖出支出' as type,symbol as coin_symbol,0-volume-fee_volume as volume from js_plat_offline_order_detail where  `status` = 2 and remarks='sell'  and \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER') \n" +
            "#个人挖矿\n" +
            "union all \n" +
            "select create_date,'持币挖矿' as type ,coin_symbol,volume  from mk2_popularize_mining_give_coin_log where type = 1 and \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER') \n" +
            "#多元挖矿\n" +
            "union all \n" +
            "select create_date,'多元挖矿' as type ,coin_symbol,volume  from mk2_popularize_mining_give_coin_log where type = 2 and \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER') \n" +
            "#查OTC交易记录\n" +
            "union all \n" +
            "SELECT create_date,'otc买入收入' as type,symbol as coin_symbol, volume from otc_offline_order_detail where  `status` = 2 and remarks='buy'  and \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER') \n" +
            "union all \n" +
            "SELECT create_date,'otc卖出支出' as type,symbol as coin_symbol,0-volume as volume from otc_offline_order_detail where  `status` = 2 and remarks='sell'  and \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER') \n" +
            "#查转帐转入记录\n" +
            "union all \n" +
            "select create_date,'转帐转入' as type,coin_symbol ,(volume+fee) as volume from js_plat_offline_change_log where type = 0 and \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER')\n" +
            "union all \n" +
            "select create_date,'转帐转出' as type,coin_symbol ,0-(volume+fee) as volume from js_plat_offline_change_log where type = 1 and \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER')\n" +
            "union all \n" +
            "select create_date,'红包支出' as type,coin_symbol ,0-(volume+fee) as volume from js_plat_offline_change_log where type = '2' and \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER')\n" +
            "union all \n" +
            "select create_date,'红包收入' as type,coin_symbol ,(volume+fee) as volume from js_plat_offline_change_log where type = '3' and \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER')\n" +
            "union all \n" +
            "select create_date,'红包退回' as type,coin_symbol ,(volume+fee) as volume from js_plat_offline_change_log where type = '4' and \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER')\n" +
            "#查广告手续费\n" +
            "union all \n" +
            "select create_date,'广告手续费' as type,symbol as coin_symbol,0-fee_volume as volume from js_plat_offline_order where fee_volume >0 and \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER')\n" +
            "#注册红包\n" +
            "union all \n" +
            "select create_date,'注册红包' as type,coin_symbol,real_volume as volume  from mk_user_register_lottery_log  where \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER') \n" +
            "#接力撞奖 \n" +
            "union all \n" +
            "select create_date,'接力撞奖' as type,coin_symbol, volume  from mk_relay_remit_log  where \n" +
            "user_id in(select id from js_plat_user where source='AUTO-TRADER') \n" +
            "#通兑接口--转入 \n" +
            "union all \n" +
            "select create_time as create_date,'通兑转入' as type,coin_type as coin_symbol,(amount_coin-to_fee) as volume from otc_hq_pay_third_order where \n" +
            "to_user_id in(select id from js_plat_user where source='AUTO-TRADER') \n" +
            "#通兑接口--转出 \n" +
            "union all \n" +
            "select create_time as create_date,'通兑转入' as type,coin_type as coin_symbol,(0-amount_coin-from_fee) as volume from otc_hq_pay_third_order where \n" +
            "from_user_id in(select id from js_plat_user where source='AUTO-TRADER') \n" +
            "union all \n" +
            "select create_date as create_date,'OTC转入' as type,symbol as coin_symbol,(volume) as volume from otc_volume_change_request where \n" +
            "buy_user_id in(select id from js_plat_user where source='AUTO-TRADER')  and `status`='1' and type='6'\n" +
            "#通兑接口--转出 \n" +
            ") t\n" +
            "group by t.coin_symbol\n" +
            ") t \n" +
            "group by coin_symbol")
    long insertAutoTraderVolumeSnapshot(@Param("date") String date);


    @Select("select count(*) from js_plat_trader_volume_snapshot where snap_date = #{date};")
    long findTraderVolumeSnapshot(@Param("date") String date);

}
