package com.biao.mapper;

import com.biao.entity.MkDistributeLog;
import com.biao.sql.build.MkDistributeLogSqlBuild;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface MkDistributeRuleDao {

    @Select("select id, name, percentage, coin_id, coin_symbol,volume, grant_volume from mk_distribute_mining where status = 1 limit 1")
    Map<String, Object> findMiningRule();

    @Select("select id, type, status, remark, start_date, end_date from ${tableName} where status = 1 limit 1")
    Map<String, Object> findDistributeRuleManage(@Param("tableName") String tableName);

    @Update("UPDATE `mk_distribute_mining` SET `grant_volume`=ifnull(grant_volume,0.00) + #{grantVolume} WHERE `id`= #{id} and volume >= IFNULL(grant_volume,0.00) + #{grantVolume}")
    long updateMiningRuleById(@Param("grantVolume") BigDecimal grantVolume, @Param("id") String id);

    @Update("UPDATE `mk_distribute_dividend` SET `grant_volume`=ifnull(grant_volume,0.00) + #{grantVolume} WHERE `id`=#{id}")
    long updateDividendRuleById(@Param("grantVolume") BigDecimal grantVolume, @Param("id") String id);

    @Update("UPDATE `mk_distribute_dividend` SET ${coin} = ifnull(${coin},0.00) + #{grantVolume} WHERE `id`= #{id}")
    long updateDividendRuleByIdAndCoin(@Param("grantVolume") BigDecimal grantVolume, @Param("id") String id, @Param("coin") String coin);

    @Select("select id, type, task_date, coin_id, coin_symbol, volume, status, create_date from mk_common_task_record k where k.task_date = #{date} and k.type = #{type} and status = 1")
    Map<String, Object> findTaskRecordByDay(@Param("date") String date, @Param("type") String type);

    @Select("select id, type, task_date, DATE_FORMAT(execute_time,'%Y-%m-%d %H:%i:%s') as execute_time, coin_id, coin_symbol, volume, status, create_date from mk_common_task_record k where k.type = #{type} and status = 1 order by k.execute_time desc limit 1")
    Map<String, Object> findLastSuccessLogByType(@Param("type") String type);

    @Select("select count(*) from mk_common_volume_snapshot where snap_date = #{date};")
    long findUserVolumeSnapshot(@Param("date") String date);

    @Delete("delete from mk_common_volume_snapshot where DATEDIFF(#{date},snap_date) > 7")
    long deleteUserVolumeSnapshot(@Param("date") String date);


    @Delete("delete from mk_common_user_relation")
    long deleteUserRelation();

    @Select("select ifnull(u.refer_id,'') as refer_id, u.id as id, u.username as username from js_plat_user u where u.id  is not null")
    List<Map<String, Object>> getUserTreeId();

    @Select("select id, user_id,username, coin_id, coin_symbol, release_volume, lock_volume from mk_common_distribute_account where status = 1 and (lock_volume - release_volume) > 0")
    List<Map<String, Object>> findDistributeAccount();

    @Update("UPDATE `mk_common_distribute_account` SET `release_volume` = ifnull(release_volume,0.00) + #{releaseVolume},lock_volume = lock_volume WHERE (lock_volume - release_volume) >= #{releaseVolume} and id = #{id}")
    long updateDistributeAccount(@Param("id") String id, @Param("releaseVolume") BigDecimal releaseVolume);

    @Insert("INSERT INTO `mk_common_task_record` (`id`, `type`, `task_date`, `execute_time`, `coin_id`, `coin_symbol`, `volume`, `status`, `create_date`) VALUES (#{id}, #{type}, #{date}, #{execute_time}, #{coinId}, #{coinSymbol}, #{volume}, '0', NOW())")
    long insertTaskRecord(@Param("id") String id, @Param("type") String type, @Param("date") String date, @Param("coinId") String coinId, @Param("coinSymbol") String coinSymbol, @Param("volume") BigDecimal volume, @Param("execute_time") String execute_time);

    @Insert("INSERT INTO mk_distribute_dividend_stat (`id`, `stat_date`, `coin_id`, `coin_symbol`, `volume`, `usdt_volume`, `btc_volume`, `eth_volume`, `usdt_real_volume`, `btc_real_volume`, `eth_real_volume`, `usdt_per_volume`, `btc_per_volume`, `eth_per_volume`, `per`, `remark`) VALUES (#{id}, #{taskDate}, #{coinId}, #{coinSymbol}, #{volume},#{usdtVolume}, #{btcVolume}, #{btcVolume}, #{usdtRealVolume}, #{btcRealVolume}, #{ethRealVolume}," +
            "truncate(#{usdtRealVolume} * (#{per}/#{volume}),8), truncate(#{btcRealVolume} * (#{per}/#{volume}),8) ,truncate(#{ethRealVolume} * (#{per}/#{volume}),8), #{per}, '')")
    long insertDividendStat(@Param("id") String id, @Param("taskDate") LocalDateTime taskDate, @Param("coinId") String coinId, @Param("coinSymbol") String coinSymbol, @Param("volume") BigDecimal volume, @Param("usdtVolume") BigDecimal usdtVolume, @Param("btcVolume") BigDecimal btcVolume, @Param("ethVolume") BigDecimal ethVolume, @Param("usdtRealVolume") BigDecimal usdtRealVolume, @Param("btcRealVolume") BigDecimal btcRealVolume, @Param("ethRealVolume") BigDecimal ethRealVolume, @Param("per") BigDecimal per);

    @Insert("INSERT INTO `mk_common_distribute_log` (`id`, `type`, `user_id`, `username`, `coin_id`, `coin_symbol`, `volume`, `status`, `create_date`) VALUES (#{id}, #{type}, #{userId},'', #{coinId}, #{coinSymbol}, #{volume}, '1', NOW())")
    long insertDistributeLog(@Param("id") String id, @Param("type") String type, @Param("userId") String userId, @Param("coinId") String coinId, @Param("coinSymbol") String coinSymbol, @Param("volume") BigDecimal volume);

    @Update("UPDATE `mk_common_task_record` SET `volume`=#{volume}, `status`= #{status},remark = #{remark} WHERE `id`=#{id}")
    long updateTaskRecord(@Param("volume") BigDecimal volume, @Param("status") String status, @Param("id") String id, @Param("remark") String remark);

    @InsertProvider(type = MkDistributeLogSqlBuild.class, method = "batchInsert")
    void batchInsertDistributeLog(@Param("listValues") List<MkDistributeLog> list);

    @Insert("INSERT INTO `mk_common_volume_snapshot` (`id`, `snap_date`, `user_id`, `coin_id`, `coin_symbol`, `volume`, `lock_volume`, `create_date`) select REPLACE(UUID(),'-','') as id, #{date} as snap_date, user_id,coin_id, coin_symbol, volume, lock_volume, NOW() from js_plat_user_coin_volume where volume > 0")
    long insertUserVolumeSnapshot(@Param("date") String date);

    @Select("select id, name, percentage, user_id, username,  coin_id, coin_symbol, plat_coin_id, plat_coin_symbol, grant_volume from mk_distribute_dividend where status = 1 limit 1")
    Map<String, Object> findDividendRule();

    @Select("select id, dividend_id, percentage, user_id, username, account_type, remark from mk_distribute_dividend_detail where dividend_id = #{dividendId}")
    List<Map<String, Object>> findDividendDetailByDividendId(@Param("dividendId") String dividendId);

    @Select("select id, snap_date, user_id, coin_id, coin_symbol,volume from mk_common_volume_snapshot where coin_symbol =#{coinSymbol} and snap_date = #{date} and volume > 0")
    List<Map<String, Object>> findUserVolumeSnapshotList(@Param("date") String date, @Param("coinSymbol") String coinSymbol);

    @Select("select id, name, coin_id, coin_symbol, volume, grant_volume from mk_distribute_promote where status = 1 limit 1")
    Map<String, Object> findPromoteRule();

    @Select("select id, promote_id, volume, level from mk_distribute_promote_detail where promote_id = #{promoteId}")
    List<Map<String, Object>> findPromoteDetailByPromoteId(@Param("promoteId") String promoteId);

//	@Select("select u.id as user_id, u.username, u.mail, r.deth, 1 as level, r.tree_id from js_plat_user u, mk_common_user_relation r where u.card_status = 1 and u.audit_date >= CONCAT(#{date},' 00:00:00') and u.audit_date <= CONCAT(#{date},' 23:59:59') and r.user_id = u.id and r.`level` = 1")
//	List<Map<String, Object>> findAuditedUserByDate(@Param("date") String date);

    @Select("select u.id as user_id, u.username, u.mail, r.deth, 1 as level, r.tree_id from js_plat_user u, mk_common_user_relation r where u.card_status = 1 and u.audit_date > #{lastDateTime} and u.audit_date <= #{curDateTime} and r.user_id = u.id and r.`level` = 1")
    List<Map<String, Object>> findAuditedUserByDate(@Param("lastDateTime") String lastDateTime, @Param("curDateTime") String curDateTime);

    @Select("select r.deth as deth, r.user_id, #{size} as level,u.username from js_plat_user u, mk_common_user_relation r where #{treeId} LIKE CONCAT('%',u.id,',','%') and u.id = r.user_id and u.card_status = 1 and  r.level = 1 and (#{deth} - r.deth + 1) = #{size}")
    List<Map<String, Object>> findUserParentByUser(@Param("treeId") String treeId, @Param("deth") int deth, @Param("size") int size, @Param("userId") String userId);

    @Select("select id, username, ifnull(mail,'') as mail, ifnull(mobile,'') as mobile,audit_date from js_plat_user where id = #{userId}")
    Map<String, Object> findUserById(@Param("userId") String userId);

    @Update("UPDATE `mk_distribute_promote` SET `grant_volume`=ifnull(grant_volume,0.00) + #{grantVolume} WHERE `id`=#{id} and volume >= IFNULL(grant_volume,0.00) + #{grantVolume}")
    long updatePromoteRuleById(@Param("grantVolume") BigDecimal grantVolume, @Param("id") String id);

}
