package com.biao.mapper.mk2;

import com.biao.entity.mk2.Mk2UserAllLockCoin;
import com.biao.entity.mk2.Mk2UserStatisticsInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface Mk2UserStatisticsInfoDao {

    /**
     * 获取团队当前持币量UES，
     *
     * @param userId
     * @return
     */
    @Select("SELECT t.team_hold_total from mk2_popularize_mining_give_coin_log t WHERE t.user_id = #{userId} AND t.coin_symbol = #{coinSymbol} AND t.type = '2' AND t.count_date = #{lastMiningDate} limit 1 ")
    BigDecimal getTeamHoldCoinTotal(@Param("userId") String userId, @Param("coinSymbol") String coinSymbol, @Param("lastMiningDate") LocalDateTime lastMiningDate);

    /**
     * 查询用户MK2所有的冻结资产，普通用户，节点人，合伙人
     *
     * @param userId
     * @return
     */
//    @Select("SELECT '1' AS memberType, t.id AS relationId, t.user_id AS userId, t.create_date AS createDate, t.coin_id AS coinId, t.coin_symbol AS coinSymbol, t.lock_volume AS lockVolume, t.release_begin_date AS releaseBeginDate, t.release_volume AS releaseVolume " +
//            " FROM mk2_popularize_common_member t WHERE t.lock_status = '1' AND t.user_id = #{userId} " +
//            "UNION ALL " +
//            "SELECT '2' AS memberType, t.id AS relationId, t.user_id AS userId, t.create_date AS createDate, t.coin_id AS coinId, t.coin_symbol AS coinSymbol, t.lock_volume AS lockVolume, t.release_begin_date AS releaseBeginDate, t.release_volume AS releaseVolume " +
//            " FROM mk2_popularize_nodal_member t WHERE t.type = '2' AND t.lock_status = '1' AND t.user_id = #{userId} " +
//            "UNION ALL " +
//            "SELECT '3' AS memberType, t.id AS relationId, t.user_id AS userId, t.create_date AS createDate, t.coin_id AS coinId, t.coin_symbol AS coinSymbol, t.lock_volume AS lockVolume, t.release_begin_date AS releaseBeginDate, t.release_volume AS releaseVolume " +
//            " FROM mk2_popularize_area_member t WHERE t.type = '1' AND t.`status` = '1' AND t.user_id = #{userId} " +
//            "UNION ALL " +
//            "SELECT '1' AS memberType, t.id AS relationId, t.user_id AS userId, t.create_date AS createDate, t.coin_id AS coinId, t.coin_symbol AS coinSymbol, t.lock_volume AS lockVolume, NULL AS releaseBeginDate, t.release_volume AS releaseVolume " +
//            "  FROM mk_common_distribute_account t WHERE t.`status` = '1' AND t.user_id = #{userId} "
//    )
//    List<Mk2UserAllLockCoin> getUserAllLockVolume(@Param("userId") String userId);

    @Select("SELECT '1' AS memberType, t.type AS 'lockType', t.id AS relationId, t.user_id AS userId, t.create_date AS createDate, t.coin_id AS coinId, t.coin_symbol AS coinSymbol, t.lock_volume AS lockVolume, t.release_begin_date AS releaseBeginDate, t.release_volume AS releaseVolume " +
            " FROM mk2_popularize_common_member t WHERE t.lock_status = '1' AND t.user_id = #{userId} "
    )
    List<Mk2UserAllLockCoin> getUserAllLockVolume(@Param("userId") String userId);

    @Select("SELECT t.count_date FROM mk2_popularize_mining_task_log t WHERE t.status = '1' ORDER BY t.count_date DESC LIMIT 1 ")
    LocalDateTime lastMiningDateTime();

    @Select("SELECT t.join_volume FROM mk2_popularize_mining_give_coin_log t WHERE type = '1' and count_date = #{lastMiningDate} ORDER BY (volume / join_volume) DESC LIMIT 1 ")
    BigDecimal findLastBestHoldCoinVolume(@Param("lastMiningDate") LocalDateTime lastMiningDate);

    @Select("SELECT t.end_date FROM mk2_popularize_bonus_task_log t WHERE t.`status` = '1' ORDER BY t.end_date DESC LIMIT 1 ")
    LocalDateTime lastBonusDateTime();

    @Select("SELECT SUM(t.total_volume) AS miningCoinVolumeTotal, SUM(t.show_grant_volume) AS grantMiningCoinVolumeTotal FROM mk2_popularize_mining_conf t ")
    Mk2UserStatisticsInfo countMiningTotal();

    @Select("SELECT IFNULL(t.grant_volume, 0) * (SELECT c.show_multiple FROM mk2_popularize_mining_conf c WHERE c.type = '1' AND c.status = '3' ORDER BY c.create_date DESC LIMIT 1) " +
            " FROM mk2_popularize_mining_task_log t WHERE t.`status` = '1' AND t.type = '1' ORDER BY t.create_date DESC LIMIT 1 ")
    BigDecimal countLastHoldMiningTotal();

    @Select("SELECT IFNULL(t.grant_volume, 0) * (SELECT c.show_multiple FROM mk2_popularize_mining_conf c WHERE c.type = '2' AND c.status = '3' ORDER BY c.create_date DESC LIMIT 1) " +
            " FROM mk2_popularize_mining_task_log t WHERE t.`status` = '1' AND t.type = '2' ORDER BY t.create_date DESC LIMIT 1 ")
    BigDecimal countLastTeamMiningTotal();

    @Select("SELECT SUM(t.lock_volume) - IFNULL(SUM(t.release_volume), 0) from mk2_popularize_common_member t where t.lock_status = '1' AND t.coin_symbol = 'UES' AND t.type IN ('1', '2') ")
    BigDecimal countCommonLockTotal();

    @Select("SELECT SUM(t.lock_volume) - IFNULL(SUM(t.release_volume), 0) from mk2_popularize_area_member t where t.type = '1' AND t.`status` = '1' AND t.coin_symbol = 'UES' ")
    BigDecimal countAreaLockTotal();

    @Select("SELECT SUM(t.lock_volume) - IFNULL(SUM(t.release_volume), 0) FROM mk2_popularize_nodal_member t WHERE t.lock_status = '1' AND t.type = '2' AND t.coin_symbol = 'UES' ")
    BigDecimal countNodalLockTotal();

    @Select("SELECT SUM(t.income_volume) FROM mk2_popularize_bonus_member_log t WHERE t.user_id = #{userId} AND t.bonus_date_end = #{bonusDateTime} ")
    BigDecimal userLastBonusVolume(@Param("userId") String userId, @Param("bonusDateTime") LocalDateTime bonusDateTime);

    @Select("SELECT SUM(t.volume) FROM mk2_popularize_mining_give_coin_log t WHERE t.user_id = #{userId} AND t.count_date = #{countDate} ")
    BigDecimal userLastMiningVolume(@Param("userId") String userId, @Param("countDate") LocalDateTime countDate);

    @Select("SELECT t.volume FROM mk2_popularize_mining_give_coin_log t WHERE t.user_id = #{userId} AND t.count_date = #{countDate} AND t.type = '1' ")
    BigDecimal userLastMiningHoldVolume(@Param("userId") String userId, @Param("countDate") LocalDateTime countDate);

    @Select("SELECT t.volume FROM mk2_popularize_mining_give_coin_log t WHERE t.user_id = #{userId} AND t.count_date = #{countDate} AND t.type = '2' ")
    BigDecimal userLastMiningTeamVolume(@Param("userId") String userId, @Param("countDate") LocalDateTime countDate);

    @Select("SELECT SUM(t.volume) FROM mk2_popularize_mining_give_coin_log t WHERE t.user_id = #{userId} AND t.type = #{type} ")
    BigDecimal userTotalMiningVolumeByType(@Param("userId") String userId, @Param("type") String type);

    @Select("SELECT t.count_date AS 'holdMiningDate', t.join_volume AS 'holdMiningVolume', t.order_no AS 'holdMiningOrderNo', t.volume AS 'holdMiningGiveVolume' " +
            " FROM mk2_popularize_mining_give_coin_log t WHERE t.user_id = #{userId} AND t.type = '1' ORDER BY t.count_date DESC ")
    List<Mk2UserStatisticsInfo> findHoldMiningHistory(@Param("userId") String userId);

    @Select("SELECT t.count_date AS 'teamMiningDate', 0 AS 'teamMiningPersonNumber', t.team_hold_total AS 'teamMiningVolume', t.max_sub_volume AS 'teamMiningAreaVolume', t.volume AS 'teamMiningGiveVolume' " +
            " FROM mk2_popularize_mining_give_coin_log t WHERE t.user_id = #{userId} AND t.type = '2' ORDER BY t.count_date DESC ")
    List<Mk2UserStatisticsInfo> findTeamMiningHistory(@Param("userId") String userId);

    @Select("SELECT t.bonus_date_begin AS 'areaBonusDate',  SUM(t.income_volume) AS 'areaBonusFixedVolume' " +
            " FROM mk2_popularize_bonus_member_log t WHERE t.user_id = #{userId} AND t.area_bonus_type = '1' AND t.type IN ('3', '4')    GROUP BY t.bonus_date_begin ORDER BY t.bonus_date_begin DESC ")
    List<Mk2UserStatisticsInfo> findAreaFixedBonusHistory(@Param("userId") String userId);

    @Select("SELECT t.bonus_date_begin AS 'areaBonusDate',  SUM(t.income_volume) AS 'areaBonusPhoneVolume' " +
            " FROM mk2_popularize_bonus_member_log t WHERE t.user_id = #{userId} AND t.area_bonus_type = '2' AND t.type IN ('3', '4') GROUP BY t.bonus_date_begin ORDER BY t.bonus_date_begin DESC ")
    List<Mk2UserStatisticsInfo> findAreaPhoneBonusHistory(@Param("userId") String userId);

    @Select("SELECT t.bonus_date_begin AS 'areaBonusDate',  SUM(t.income_volume) AS 'areaBonusReferVolume' " +
            " FROM mk2_popularize_bonus_member_log t WHERE t.user_id = #{userId} AND t.area_bonus_type = '3' AND t.type IN ('3', '4') GROUP BY t.bonus_date_begin ORDER BY t.bonus_date_begin DESC ")
    List<Mk2UserStatisticsInfo> findAreaReferBonusHistory(@Param("userId") String userId);

    @Select("SELECT t.bonus_date_begin AS 'areaBonusDate',  SUM(t.income_volume) AS 'nodalBonusVolume' " +
            " FROM mk2_popularize_bonus_member_log t WHERE t.user_id = #{userId} AND t.type = '2' GROUP BY t.bonus_date_begin ORDER BY t.bonus_date_begin DESC ")
    List<Mk2UserStatisticsInfo> findNodalBonusHistory(@Param("userId") String userId);

    @Select("SELECT t.bonus_date_begin AS 'areaBonusDate',  SUM(t.income_volume) AS 'nodalBonusVolume' " +
            " FROM mk2_popularize_bonus_member_log t WHERE t.user_id = #{userId} AND t.type = '1' GROUP BY t.bonus_date_begin ORDER BY t.bonus_date_begin DESC ")
    List<Mk2UserStatisticsInfo> findCommonBonusHistory(@Param("userId") String userId);
}
