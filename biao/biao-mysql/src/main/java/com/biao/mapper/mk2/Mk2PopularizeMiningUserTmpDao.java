package com.biao.mapper.mk2;

import com.biao.entity.mk2.Mk2PopularizeMiningUserTmp;
import com.biao.entity.mk2.Mk2PopularizeMiningUserTmpBak;
import com.biao.sql.build.mk2.Mk2PopularizeMiningUserTmpBuild;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface Mk2PopularizeMiningUserTmpDao {

    @InsertProvider(type = Mk2PopularizeMiningUserTmpBuild.class, method = "insert")
    void insert(Mk2PopularizeMiningUserTmp userTmp);

    @InsertProvider(type = Mk2PopularizeMiningUserTmpBuild.class, method = "batchInsert")
    void insertBatch(@Param("listValues") List<Mk2PopularizeMiningUserTmp> mk2PopularizeMiningUserTmps);

    ////////////////////////////////个人持币挖矿 Begin////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Select("SELECT UUID() AS 'id', '1' AS 'type', t.user_id, t.coin_id, t.coin_symbol, t.volume, t.count_date, t.source_volume, " +
            " (@rowNum :=@rowNum + 1) AS 'order_no' FROM mk2_popularize_mining_user_tmp_bak t, (SELECT(@rowNum := 0)) b " +
            " WHERE t.type = '9' AND t.source_volume >= #{greaterVolume} ORDER BY t.volume ASC")
    List<Mk2PopularizeMiningUserTmp> findUserTmpBySort(@Param("greaterVolume") BigDecimal greaterVolume);

    @Select("SELECT SUM(t.order_no) FROM mk2_popularize_mining_user_tmp t WHERE t.type = #{type} AND t.count_date = #{countDate} AND t.coin_id = #{coinId} ")
    BigDecimal countOrderNo(@Param("type") String type, @Param("countDate") LocalDateTime countDate, @Param("coinId") String coinId);

    @Select("SELECT " + Mk2PopularizeMiningUserTmpBuild.columns + " FROM mk2_popularize_mining_user_tmp t WHERE t.type = #{type} AND t.count_date = #{countDate} AND t.coin_id = #{coinId} ")
    List<Mk2PopularizeMiningUserTmp> findByCondition(@Param("type") String type, @Param("countDate") LocalDateTime countDate, @Param("coinId") String coinId);
    ////////////////////////////////个人持币挖矿 end////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////团队持币挖矿=> Begin//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Select("SELECT r.parent_id FROM " +
            " (SELECT DISTINCT(t.refer_id) AS parent_id from js_plat_user t where t.refer_id is not null) r, " +
            "  mk2_popularize_mining_user_tmp_bak c " +
            " WHERE r.parent_id = c.user_id AND c.type = '9' AND c.source_volume >= #{minVolume}")
    List<String> findAllTeamUserIds(@Param("minVolume") BigDecimal minVolume);

    ////////////////////////////////团队持币挖矿=> end////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////团队挖矿中间表，临时表数据清理 begin/////////////////////////////////////////////////////////////////////////////////////
    @Insert("INSERT INTO mk2_popularize_mining_user_coin_bak(id, user_id, coin_id, coin_symbol, volume, lock_volume) " +
            "SELECT t.id, t.user_id, t.coin_id, t.coin_symbol, t.volume, t.lock_volume FROM js_plat_user_coin_volume t WHERE t.coin_symbol = #{coinSymbol} ")
    long snapshotUserCoinHold(@Param("coinSymbol") String coinSymbol);

    @Insert("INSERT INTO mk2_popularize_mining_super_coin_bak(id, user_id, coin_id, coin_symbol, volume, deposit_begin, deposit_end, create_date, update_date, version) " +
            "SELECT t.id, t.user_id, t.coin_id, t.coin_symbol, t.volume, t.deposit_begin, t.deposit_end, t.create_date, t.update_date, t.version FROM js_plat_super_coin_volume t WHERE t.coin_symbol = #{coinSymbol} ")
    long snapshotSuperCoinHold(@Param("coinSymbol") String coinSymbol);

    @Select("SELECT UUID() AS 'id', '9' AS 'type', t.user_id AS 'user_id', t.coin_id AS 'coin_id', t.coin_symbol AS 'coin_symbol', t.volume AS 'volume', #{countDate} AS 'count_date', t.volume AS 'source_volume' " +
            " FROM mk2_popularize_mining_user_coin_bak t WHERE t.coin_symbol = #{coinSymbol} ")
    List<Mk2PopularizeMiningUserTmpBak> findUserCoin(@Param("countDate") LocalDateTime countDate, @Param("coinSymbol") String coinSymbol);

    @Select("SELECT t.user_id AS 'user_id', t.coin_symbol AS 'coin_symbol', t.volume AS 'volume' FROM mk2_popularize_mining_super_coin_bak t WHERE t.coin_symbol = #{coinSymbol} ")
    List<Mk2PopularizeMiningUserTmpBak> findSuperUserCoin(@Param("coinSymbol") String coinSymbol);

    @Select("SELECT m.user_id, (sum(m.lock_volume) - sum(m.release_volume)) AS volume from mk2_popularize_common_member m WHERE m.coin_symbol = #{coinSymbol} GROUP BY m.user_id ")
    List<Mk2PopularizeMiningUserTmpBak> findLockUserCoin(@Param("coinSymbol") String coinSymbol);

//    @Insert("INSERT INTO mk2_popularize_mining_user_tmp_bak (id, type, user_id, coin_id, coin_symbol, volume, count_date, source_volume) " +
//            " SELECT UUID(), '9', t.user_id, t.coin_id, t.coin_symbol, " +
//            " t.volume + (IFNULL(s.volume,0) * #{multiple}) + IFNULL(cm.lockVolume,0) * #{memberLockMultiple}, #{countDate}, t.volume + IFNULL(s.volume,0) + IFNULL(cm.lockVolume,0)" +
//            " FROM mk2_popularize_mining_user_coin_bak t LEFT JOIN mk2_popularize_mining_super_coin_bak s ON t.user_id = s.user_id AND t.coin_symbol = s.coin_symbol " +
//            " LEFT JOIN (SELECT m.user_id, (sum(m.lock_volume) - sum(m.release_volume)) AS lockVolume from mk2_popularize_common_member m WHERE m.coin_symbol = #{coinSymbol} GROUP BY m.user_id) cm ON cm.user_id = t.user_id " +
//            " WHERE t.coin_symbol = #{coinSymbol} ")
//    long insertUserVolumeTemp(@Param("countDate") LocalDateTime countDate, @Param("coinSymbol") String coinSymbol, @Param("multiple") BigDecimal multiple, @Param("memberLockMultiple") BigDecimal memberLockMultiple);

    @Delete("DELETE FROM mk2_popularize_mining_user_coin_bak ")
    long deleteHistorySnapshot();

    @Delete("DELETE FROM mk2_popularize_mining_super_coin_bak ")
    long deleteHistorySuperSnapshot();

    @Delete("DELETE FROM mk2_popularize_mining_user_tmp_bak ")
    long deleteHistoryUserTmpBak();

    @Delete("DELETE FROM mk2_popularize_mining_user_tmp ")
    long deleteHistoryUserTmp();

    //////////////////////////////团队挖矿中间表，临时表数据清理 end/////////////////////////////////////////////////////////////////////////////////////
}
