package com.biao.mapper;

import com.biao.entity.UserCoinVolume;
import com.biao.sql.build.UserCoinVolumeSqlBuild;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 用户资产的操作信息；
 *
 *
 */
@Mapper
public interface UserCoinVolumeDao {


    /**
     * 根据id查询数据；
     *
     * @param id id;
     * @return 数据 ；
     */
    @SelectProvider(type = UserCoinVolumeSqlBuild.class, method = "findById")
    UserCoinVolume findById(String id);

    /**
     * 根据id修改;
     *
     * @param userCoinVolume 资产信息；
     * @return 返回数据 ；
     */
    @UpdateProvider(type = UserCoinVolumeSqlBuild.class, method = "updateById")
    long updateById(UserCoinVolume userCoinVolume);

    /**
     * Find by user id list.
     *
     * @param userId the user id
     * @return the list
     */
    @Select("select " + UserCoinVolumeSqlBuild.columns + " from js_plat_user_coin_volume where user_id = #{userId}")
    List<UserCoinVolume> findByUserId(String userId);

    /**
     * 根据用户的user_id 与交易对币总查询资产信息；
     *
     * @param userId 用户ID;
     * @param symbol 币总;
     * @return 用户资产信息 ；
     */
    @Select("select " + UserCoinVolumeSqlBuild.columns + " from js_plat_user_coin_volume where user_id = #{userId} and coin_symbol = #{symbol}")
    UserCoinVolume findByUserIdAndSymbol(@Param("userId") String userId, @Param("symbol") String symbol);


    /**
     * user与symbol，与 updateDate 修改冻结资产。
     *
     * @param userId     用户ID;
     * @param symbol     标识;
     * @param volume     所有资产
     * @param lockVolume 冻结资产
     * @return the long
     */
    @Update("update js_plat_user_coin_volume set volume=#{volume},lock_volume=#{lock_volume} where user_id = #{userId} and coin_symbol = #{symbol}")
    long updateVolumeAndLockVolume(@Param("userId") String userId,
                                   @Param("symbol") String symbol,
                                   @Param("volume") BigDecimal volume,
                                   @Param("lock_volume") BigDecimal lockVolume);

    /**
     * Update flag long.
     *
     * @param flag     the flag
     * @param flagMark the flag mark
     * @param userId   the user id
     * @return the long
     */
    @Update("update js_plat_user_coin_volume set flag=#{flag},flag_mark=#{flagMark} where user_id = #{userId} ")
    long updateFlag(@Param("flag") Integer flag, @Param("flagMark") String flagMark,
                    @Param("userId") String userId);


    /**
     * 查询语解码器;
     *
     * @param userId the user id
     * @param coinId the coin id
     * @return user coin volume
     */
    @Select("select " + UserCoinVolumeSqlBuild.columns + " from js_plat_user_coin_volume where user_id = #{userId} and coin_id = #{coinId}")
    UserCoinVolume findByUserIdAndCoinId(@Param("userId") String userId, @Param("coinId") String coinId);


    /**
     * Update volume long.
     *
     * @param userId     the user id
     * @param coinId     the coin id
     * @param subVolume  the sub volume
     * @param updateDate the update date
     * @return long
     */
    @Update("update js_plat_user_coin_volume set volume=#{volume} where user_id=#{userId} and coin_id =#{coinId} and update_date<=#{update_date}")
    long updateVolume(@Param("userId") String userId, @Param("coinId") String coinId, @Param("volume") BigDecimal subVolume, @Param("update_date") Timestamp updateDate);

    /**
     * Update volume and lock volume by coin id long.
     *
     * @param userId     the user id
     * @param coinId     the coin id
     * @param volume     the volume
     * @param lockVolume the lock volume
     * @param updateDate the update date
     * @return the long
     */
    @Update("update js_plat_user_coin_volume set volume=#{volume} ,lock_volume =#{lockVolume} where user_id=#{userId} and coin_id =#{coinId} and update_date<=#{update_date}")
    long updateVolumeAndLockVolumeByCoinId(@Param("userId") String userId, @Param("coinId") String coinId, @Param("volume") BigDecimal volume, @Param("lockVolume") BigDecimal lockVolume, @Param("update_date") Timestamp updateDate);

    /**
     * Insert long.
     *
     * @param userCoinVolume the user coin volume
     * @return the long
     */
    @InsertProvider(type = UserCoinVolumeSqlBuild.class, method = "insert")
    long insert(UserCoinVolume userCoinVolume);

    /**
     * Find by user id and coin symbol user coin volume.
     *
     * @param userId     the user id
     * @param coinSymbol the coin symbol
     * @return the user coin volume
     */
    @Select("select " + UserCoinVolumeSqlBuild.columns + " from js_plat_user_coin_volume where user_id = #{userId} and coin_symbol = #{coinSymbol}")
    UserCoinVolume findByUserIdAndCoinSymbol(@Param("userId") String userId, @Param("coinSymbol") String coinSymbol);

    /**
     * Update only lock volume by symbol long.
     *
     * @param userId      the user id
     * @param coin_symbol the coin symbol
     * @param lockVolume  the lock volume
     * @param updateDate  the update date
     * @return the long
     */
    @Update("update js_plat_user_coin_volume set lock_volume=#{lockVolume} where user_id=#{userId} and coin_symbol =#{coinSymbol} and update_date<=#{updateDate}")
    long updateOnlyLockVolumeBySymbol(@Param("userId") String userId, @Param("coinSymbol") String coin_symbol, @Param("lockVolume") BigDecimal lockVolume, @Param("updateDate") Timestamp updateDate);

    /**
     * Update volume and out lock volume by coin id long.
     *
     * @param userId        the user id
     * @param coinId        the coin id
     * @param volume        the volume
     * @param outLockVolume the out lock volume
     * @param updateDate    the update date
     * @return the long
     */
    @Update("update js_plat_user_coin_volume set volume=#{volume} ,out_lock_volume =#{outLockVolume} where user_id=#{userId} and coin_id =#{coinId} and update_date<=#{update_date}")
    long updateVolumeAndOutLockVolumeByCoinId(@Param("userId") String userId, @Param("coinId") String coinId, @Param("volume") BigDecimal volume, @Param("outLockVolume") BigDecimal outLockVolume, @Param("update_date") Timestamp updateDate);

    /**
     * Update only out lock volume by symbol long.
     *
     * @param userId        the user id
     * @param coin_symbol   the coin symbol
     * @param outLockVolume the out lock volume
     * @param updateDate    the update date
     * @return the long
     */
    @Update("update js_plat_user_coin_volume set out_lock_volume=#{outLockVolume} where user_id=#{userId} and coin_symbol =#{coinSymbol} and update_date<=#{updateDate}")
    long updateOnlyOutLockVolumeBySymbol(@Param("userId") String userId, @Param("coinSymbol") String coin_symbol, @Param("outLockVolume") BigDecimal outLockVolume, @Param("updateDate") Timestamp updateDate);

    @Update("update js_plat_user_coin_volume set out_lock_volume=#{outLockVolume},version= version+1 where user_id=#{userId} and coin_id =#{coinId} and version<=#{version}")
    long updateOutLockVolumeByUserIdAndCoinId(@Param("userId") String userId,@Param("coinId") String coinId,@Param("outLockVolume") BigDecimal outLockVolume, @Param("version") Long version);


}
