package com.biao.mapper.register;

import com.biao.entity.register.UserRegisterLotteryLog;
import com.biao.sql.build.register.UserRegisterLotteryLogSqlBuild;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * The interface User register lottery log dao.
 *
 *  ""
 */
@Mapper
public interface UserRegisterLotteryLogDao {

    /**
     * findByUserId
     *
     * @param userId the user id
     * @return UserRegisterLotteryLog user register lottery log
     */
    @Select("select  " + UserRegisterLotteryLogSqlBuild.columns +
            "  from mk_user_register_lottery_log where user_id =#{userId} and reason_type = 0 limit 1")
    UserRegisterLotteryLog findRegisterByUserId(String userId);


    /**
     * Sum recommend day count integer.
     *
     * @param userId    the user id
     * @param startDate the start date
     * @param endDate   the end date
     * @return the integer
     */
    @Select("select count(1) from mk_user_register_lottery_log where user_id = #{userId}  and reason_type = 1  " +
            "  and create_date BETWEEN  #{startDate}  AND #{endDate}  ")
    Integer sumRecommendDayCount(@Param("userId") String userId, @Param("startDate") LocalDateTime startDate,
                                 @Param("endDate") LocalDateTime endDate);


    /**
     * Sum recommend total count integer.
     *
     * @param userId the user id
     * @return the integer
     */
    @Select("select count(1) from mk_user_register_lottery_log where user_id = #{userId}  and reason_type = 1  ")
    Integer sumRecommendTotalCount(@Param("userId") String userId);


    /**
     * Sum total big decimal.
     *
     * @return the big decimal
     */
    @Select("select sum(real_volume) from mk_user_register_lottery_log")
    BigDecimal sumTotal();


    /**
     * Sum bb refer total big decimal.
     *
     * @param referId    the refer id
     * @param coinSymbol the coin symbol
     * @return the big decimal
     */
    @Select("select  sum(volume-lock_volume) as bbTotal from js_plat_user_coin_volume t  where " +
            "t.coin_symbol = #{coinSymbol} " +
            "and exists" +
            "   (select id from js_plat_user u where u.id= t.user_id and  u.refer_id = #{referId} ) ")
    BigDecimal sumBBReferTotal(@Param("referId") String referId, @Param("coinSymbol") String coinSymbol);


    /**
     * Sum c 2 c refer total big decimal.
     *
     * @param referId    the refer id
     * @param coinSymbol the coin symbol
     * @return the big decimal
     */
    @Select("select  sum(volume - advert_volume - lock_volume) as  c2cTotal  from js_plat_offline_coin_volume c " +
            " where   c.coin_symbol = #{coinSymbol} " +
            " and exists" +
            "   (select id from js_plat_user u where u.id= c.user_id and  u.refer_id = #{referId} )  ")
    BigDecimal sumC2CReferTotal(@Param("referId") String referId, @Param("coinSymbol") String coinSymbol);


    /**
     * Sum lottery refer total big decimal.
     *
     * @param referId    the refer id
     * @param coinSymbol the coin symbol
     * @return the big decimal
     */
    @Select("select sum(real_volume)  from mk_user_register_lottery_log g  where g.recommend_id = #{referId} and g.coin_symbol = #{coinSymbol}")
    BigDecimal sumLotteryReferTotal(@Param("referId") String referId, @Param("coinSymbol") String coinSymbol);

    /**
     * Insert int.
     *
     * @param userRegisterLotteryLog the user register lottery log
     * @return the int
     */
    @InsertProvider(type = UserRegisterLotteryLogSqlBuild.class, method = "insert")
    int insert(UserRegisterLotteryLog userRegisterLotteryLog);
}
