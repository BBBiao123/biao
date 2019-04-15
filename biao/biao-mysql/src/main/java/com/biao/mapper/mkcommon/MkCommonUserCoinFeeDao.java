package com.biao.mapper.mkcommon;

import com.biao.entity.mkcommon.MkCommonUserCoinFee;
import com.biao.sql.build.mkcommon.MkCommonUserCoinFeeBuild;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MkCommonUserCoinFeeDao {

    @InsertProvider(type = MkCommonUserCoinFeeBuild.class, method = "insert")
    void insert(MkCommonUserCoinFee userCoinFee);

    @InsertProvider(type = MkCommonUserCoinFeeBuild.class, method = "batchInsert")
    void insertBatch(@Param("listValues") List<MkCommonUserCoinFee> userCoinFeeList);

    @Select("select count(1) from mk_common_user_coin_fee where begin_date >= #{lastDateTime} and end_date <= #{curDateTime}")
    long findUserCoinFeeByDate(@Param("lastDateTime") String lastDateTime, @Param("curDateTime") String curDateTime);

    @Select("SELECT SUM(t.ex_usdt_vol) FROM mk_common_user_coin_fee t WHERE t.end_date >= #{beginDate} AND t.end_date <= #{endDate}")
    BigDecimal sumDateUserTotal(@Param("beginDate") LocalDateTime beginDate, @Param("endDate") LocalDateTime endDate);


    @Select("SELECT t.parent_id AS 'user_id', SUM(t.ex_usdt_vol) AS 'ex_usdt_vol' FROM " +
            " (SELECT r.parent_id, f.ex_usdt_vol " +
            " FROM mk_common_user_relation r, mk_common_user_coin_fee f " +
            " WHERE r.user_id = f.user_id AND r.parent_id IS NOT NULL AND f.end_date >= #{beginDate} AND f.end_date <= #{endDate} " +
            " ) t " +
            " GROUP BY t.parent_id")
    List<MkCommonUserCoinFee> sumCommonMemberUsdt(@Param("beginDate") LocalDateTime beginDate, @Param("endDate") LocalDateTime endDate);

    @Select("SELECT t.parent_id AS 'user_id', IFNULL(SUM(t.ex_usdt_vol),0) AS 'ex_usdt_vol' FROM " +
            " (SELECT r.parent_id, f.ex_usdt_vol " +
            " FROM mk_common_user_relation r, mk_common_user_coin_fee f " +
            " WHERE r.user_id = f.user_id AND r.parent_id IS NOT NULL AND r.deth = #{deth} AND r.level = 1 AND f.end_date >= #{beginDate} AND f.end_date <= #{endDate} " +
            " ) t " +
            " GROUP BY t.parent_id")
    List<MkCommonUserCoinFee> sumCommonMemberUsdtByDeth(@Param("beginDate") LocalDateTime beginDate, @Param("endDate") LocalDateTime endDate, @Param("deth") int deth);

    @Select("SELECT MAX(r.deth) FROM mk_common_user_relation r ")
    int countMaxDeth();

    @Delete("DELETE FROM mk2_popularize_area_member_tmp ")
    long cleanMk2PopularizeAreaMemberTmp();

    @Delete("DELETE FROM  mk_common_user_relation_bonus ")
    long cleanmkCommonUserRelationSubTemp();

    @Insert("INSERT INTO mk2_popularize_area_member_tmp (id, user_id) " +
            " SELECT UUID(), t.user_id FROM mk2_popularize_area_member t WHERE t.`status` = '1' AND t.user_id != #{userId} " +
            " AND EXISTS (SELECT 1 FROM mk_common_user_relation r WHERE r.tree_id = CONCAT(#{userId}, ',') AND t.user_id = r.user_id) ")
    long insertSubNotCountAreaMemberUserId(@Param("userId") String userId);

    @Insert("INSERT INTO  mk_common_user_relation_bonus(id, user_id) " +
            " SELECT UUID(), t.user_id  FROM ( " +
            "   SELECT DISTINCT(r.user_id) AS user_id FROM mk_common_user_relation r, mk2_popularize_area_member_tmp m WHERE r.tree_id = CONCAT(m.user_id,',') AND r.user_id != m.user_id " +
            " ) t ")
    long insertNotCountSubUserId();

    @Select("SELECT #{userId} AS 'user_id', IFNULL(SUM(f.ex_usdt_vol),0) AS 'ex_usdt_vol' " +
            "  FROM mk_common_user_relation r, mk_common_user_coin_fee f,  mk_common_user_relation_bonus s " +
            " WHERE r.tree_id = CONCAT(#{userId},',') AND r.user_id = f.user_id AND r.user_id != #{userId} AND f.end_date >= #{beginDate} AND f.end_date <= #{endDate} " +
            " AND AND r.user_id != s.user_id ")
    MkCommonUserCoinFee sumAreaMemberReferUsdt(@Param("userId") String userId, @Param("beginDate") LocalDateTime beginDate, @Param("endDate") LocalDateTime endDate);

    @Select("SELECT t.user_id AS 'user_id', SUM(t.ex_usdt_vol) AS 'ex_usdt_vol' FROM " +
            " (SELECT m.user_id, f.ex_usdt_vol " +
            " FROM js_plat_phone_geocoder p, mk2_popularize_area_member m, mk_common_user_coin_fee f " +
            " WHERE m.`status`= '1' AND m.id = #{areaMemberId} AND m.area_id = p.city_id AND p.user_id = f.user_id AND f.end_date >= #{beginDate} AND f.end_date <= #{endDate} " +
            " ) t ")
    MkCommonUserCoinFee sumAreaMemberPhoneUsdt(@Param("areaMemberId") String areaMemberId, @Param("beginDate") LocalDateTime beginDate, @Param("endDate") LocalDateTime endDate);


}
