package com.biao.mapper;

import com.biao.entity.BalanceSheetSnapshot;
import com.biao.sql.build.BalanceSheetSnapshotSqlBuild;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BalanceSheetSnapshotDao {

    @InsertProvider(type = BalanceSheetSnapshotSqlBuild.class, method = "insert")
    void insert(BalanceSheetSnapshot balanceSheetSnapshot);

    @InsertProvider(type = BalanceSheetSnapshotSqlBuild.class, method = "batchInsert")
    void batchInsert(@Param("listValues") List<BalanceSheetSnapshot> balanceSheetSnapshots);

    @UpdateProvider(type = BalanceSheetSnapshotSqlBuild.class, method = "updateById")
    void updateById(BalanceSheetSnapshot balanceSheetSnapshot);

    @Insert("\n" +
            "INSERT INTO `js_plat_balance_sheet_snapshot` (`id`, `snap_date`, `coin_symbol`, `total_volume`, `income`, `trade_fee`, `deposit_volume`, `offline_fee`, `deduct_volume`, `withdraw_fee`, `expense`, `withdraw_volume`, `withdraw_block_fee`, `deposit_allocation_fee`, `remit_volume`, `mining_volume`, `register_volume`, `relay_volume`, `remark`, `create_date`, `create_by`)\n" +
            "select REPLACE(UUID(),'-','') as id, \n" +
            "\t\t\t #{date} as snap_date, \n" +
            "\t\t\t a.coin_symbol as coinSymbol, \n" +
            "\t\t\t 0 as totalVolume,\n" +
            "\t\t\t sum(IF(a.flag = '1',a.volume,0)) as income,  \n" +
            "\t\t\t sum(IF(a.type = 'trade_fee',a.volume,0)) as tradeFee,  \n" +
            "\t\t\t sum(IF(a.type = 'deposit',a.volume,0)) as depositVolume,  \n" +
            "\t\t\t sum(IF(a.type = 'offline_fee',a.volume,0)) as offlineFee,  \n" +
            "\t\t\t sum(IF(a.type = 'deduct',a.volume,0)) as deductVolume,  \n" +
            "\t\t\t sum(IF(a.type = 'withdraw_fee',a.volume,0)) as withdrawFee,  \n" +
            "\t\t\t sum(IF(a.flag = '2',a.volume,0)) as expense, \n" +
            "\t\t\t sum(IF(a.type = 'withdraw',a.volume,0)) as withdrawVolume,  \n" +
            "\t\t\t 0 as withdrawBlockFee,  \n" +
            "\t\t\t 0 as depositAllocationFee, \n" +
            "\t\t\t sum(IF(a.type = 'remit',a.volume,0)) as remitVolume,  \n" +
            "\t\t\t sum(IF(a.type = 'mining',a.volume,0)) as miningVolume,  \n" +
            "\t\t\t sum(IF(a.type = 'lottery' or a.type = 'register',a.volume,0)) as registerVolume,  \n" +
            "\t\t\t sum(IF(a.type = 'relay',a.volume,0)) as relayVolume,\n" +
            "\t\t\t '' as remark,\n" +
            "\t\t\t NOW() as create_date,\n" +
            "\t\t\t 'task' as create_by\n" +
            "from (\n" +
            "-- 币币交易手续费\n" +
            "select to_coin_symbol as coin_symbol, sum(ex_fee) as volume, '1' as flag, 'trade_fee' as type from js_plat_ex_order where status = '1' or status = '2' \n" +
            "\tand update_date BETWEEN #{startDate} AND #{endDate}\n" +
            "group by to_coin_symbol\n" +
            "UNION ALL\n" +
            "-- 充值\n" +
            "select coin_symbol as coin_symbol, sum(volume) as volume, '1' as flag, 'deposit' as type from js_plat_user_deposit_log where status = '1' \n" +
            "\tand create_date BETWEEN #{startDate} AND #{endDate}\n" +
            "group by coin_symbol\n" +
            "UNION ALL\n" +
            "-- c2c手续费\n" +
            "select symbol as coin_symbol, sum(fee_volume) as volume, '1' as flag, 'offline_fee' as type from js_plat_offline_order_detail where status = '2' \n" +
            "\tand confirm_payment_date BETWEEN #{startDate} AND #{endDate}\n" +
            "group by symbol\n" +
            "UNION ALL\n" +
            "-- 手动回扣币 \n" +
            "select coin_symbol as coin_symbol, ABS(sum(volume)) as volume, '1' as flag, 'deduct' as type from js_plat_user_coin_volume_history where volume < 0 \n" +
            "\tand create_date BETWEEN #{startDate} AND #{endDate}\n" +
            "group by coin_symbol\n" +
            "UNION ALL\n" +
            "-- 提现手续费\n" +
            "select coin_symbol as coin_symbol, sum(fee) as volume, '1' as flag, 'withdraw_fee' as type from js_plat_user_withdraw_log where status = '3' \n" +
            "\tand audit_date BETWEEN #{startDate} AND #{endDate}\n" +
            "group by coin_symbol\n" +
            "UNION ALL\n" +
            "-- 提币\n" +
            "select coin_symbol as coin_symbol, sum(volume) as volume, '2' as flag, 'withdraw' from js_plat_user_withdraw_log where status = '3' \n" +
            "\tand audit_date BETWEEN #{startDate} AND #{endDate}\n" +
            "group by coin_symbol\n" +
            "UNION ALL\n" +
            "-- 手动拨币\n" +
            "select coin_symbol as coin_symbol, sum(volume) as volume, '2' as flag, 'remit' from js_plat_user_coin_volume_history where volume > 0 \n" +
            "\tand create_date BETWEEN #{startDate} AND #{endDate}\n" +
            "group by coin_symbol\n" +
            "UNION ALL\n" +
            "-- 挖矿\n" +
            "select coin_symbol as coin_symbol, sum(mining_volume) as volume, '2' as flag, 'mining' from mk2_popularize_mining_task_log where status = '1' \n" +
            "\tand count_date BETWEEN #{startDate} AND #{endDate}\n" +
            "group by coin_symbol\n" +
            "UNION ALL\n" +
            "-- 注册抽奖\n" +
            "select coin_symbol as coin_symbol, sum(real_volume) as volume, '2' as flag, 'lottery' from mk_user_register_lottery_log where\n" +
            "\tcreate_date BETWEEN #{startDate} AND #{endDate}\n" +
            "group by coin_symbol\n" +
            "UNION ALL\n" +
            "-- 注册送币\n" +
            "select coin_symbol as coin_symbol, sum(volume) as volume, '2' as flag, 'register' from mk2_popularize_register_coin where status = '2' \n" +
            "\tand create_date BETWEEN #{startDate} AND #{endDate}\n" +
            "group by coin_symbol\n" +
            "UNION ALL\n" +
            "-- 接力撞奖\n" +
            "select coin_symbol as coin_symbol, sum(volume) as volume, '2' as flag, 'relay' from mk_relay_remit_log where is_remit = '1' \n" +
            "\tand create_date BETWEEN #{startDate} AND #{endDate}\n" +
            "group by coin_symbol\n" +
            ") a group by coin_symbol")
    long insertBalanceSheetSnapshot(@Param("date") String date, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDateTime);

    @Select("select count(*) from js_plat_balance_sheet_snapshot where snap_date = #{date};")
    long findBalanceSheetSnapshot(@Param("date") String date);

}
