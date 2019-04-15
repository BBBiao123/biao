package com.biao.mapper;

import com.biao.entity.OfflineCoinVolume;
import com.biao.entity.UserCoinVolume;
import com.biao.sql.build.OfflineCoinVolumeSqlBuild;
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
public interface OfflineCoinVolumeDao {

    @InsertProvider(type = OfflineCoinVolumeSqlBuild.class, method = "insert")
    void insert(OfflineCoinVolume offlineCoinVolume);

    @SelectProvider(type = OfflineCoinVolumeSqlBuild.class, method = "findById")
    OfflineCoinVolume findById(String id);

    @UpdateProvider(type = OfflineCoinVolumeSqlBuild.class, method = "updateById")
    long updateById(UserCoinVolume userCoinVolume);

    @Select("select " + OfflineCoinVolumeSqlBuild.columns + " from js_plat_offline_coin_volume where user_id = #{userId}")
    List<OfflineCoinVolume> findAll(String userId);

    @Select("select " + OfflineCoinVolumeSqlBuild.columns + " from js_plat_offline_coin_volume where user_id = #{userId} and coin_id = #{coinId} limit 1")
    OfflineCoinVolume findByUserIdAndCoinId(@Param("userId") String userId, @Param("coinId") String coinId);

    @Update("update js_plat_offline_coin_volume set volume = #{volume},lock_volume = #{lockVolume},version= version+1 where user_id = #{userId} and coin_id = #{coinId} and update_date = #{updateDate} and version= #{version}")
    long updateVolumeAndLockVolume(@Param("userId") String userId, @Param("coinId") String coinId, @Param("volume") BigDecimal volume, @Param("lockVolume") BigDecimal lockVolume, @Param("updateDate") Timestamp updateDate, @Param("version") Long version);

    @Update("update js_plat_offline_coin_volume set volume = #{volume},version= version+1 where user_id = #{userId} and coin_id = #{coinId} and update_date = #{updateDate} and version= #{version}")
    long updateVolume(@Param("userId") String userId, @Param("coinId") String coinId, @Param("volume") BigDecimal addVolume, @Param("updateDate") Timestamp updateDate, @Param("version") Long version);

    @Update("update js_plat_offline_coin_volume set volume = volume + #{addVolume},version = version + 1 where user_id = #{userId} and coin_id = #{coinId} and volume >= 0")
    long updateAddVolume(@Param("userId") String userId, @Param("coinId") String coinId, @Param("addVolume") BigDecimal addVolume);

    @Update("update js_plat_offline_coin_volume set volume = #{volume},advert_volume = #{advertVolume},version= version+1 where user_id = #{userId} and coin_id = #{coinId} and update_date = #{updateDate} and version= #{version}")
    long updateVolumeAndAdvertVolume(@Param("userId") String userId, @Param("coinId") String symbol, @Param("volume") BigDecimal volume, @Param("advertVolume") BigDecimal advertVolume, @Param("updateDate") Timestamp updateDate, @Param("version") Long version);

    @Update("update js_plat_offline_coin_volume set volume = #{volume},otc_advert_volume = #{advertVolume},version= version+1 where user_id = #{userId} and coin_id = #{coinId} and update_date = #{updateDate} and version= #{version}")
    long updateVolumeAndOtcAdvertVolume(@Param("userId") String userId, @Param("coinId") String symbol, @Param("volume") BigDecimal volume, @Param("advertVolume") BigDecimal advertVolume, @Param("updateDate") Timestamp updateDate, @Param("version") Long version);

    @Update("update js_plat_offline_coin_volume set volume = #{volume},advert_volume = #{advertVolume},bail_volume = #{bailVolume},version= version+1 where user_id = #{userId} and coin_id = #{coinId} and update_date = #{updateDate} and version= #{version}")
    long updateVolumeAndAdvertVolumeAndBailVolume(@Param("userId") String userId, @Param("coinId") String symbol, @Param("volume") BigDecimal volume, @Param("advertVolume") BigDecimal advertVolume, @Param("bailVolume") BigDecimal bailVolume, @Param("updateDate") Timestamp updateDate, @Param("version") Long version);

    @Update("update js_plat_offline_coin_volume set advert_volume = #{advertVolume},lock_volume = #{lockVolume},version= version+1 where user_id = #{userId} and coin_id = #{coinId} and update_date = #{updateDate} and version= #{version}")
    long updateAdvertVolumeAndLockVolume(@Param("userId") String userId, @Param("coinId") String coinId, @Param("advertVolume") BigDecimal advertVolume, @Param("lockVolume") BigDecimal lockVolume, @Param("updateDate") Timestamp timestamp, @Param("version") Long version);

    @Update("update js_plat_offline_coin_volume set lock_volume = #{lockVolume},version= version+1 where user_id = #{userId} and coin_id = #{coinId} and update_date = #{updateDate} and version= #{version}")
    long updateLockVolume(@Param("userId") String userId, @Param("coinId") String coinId, @Param("lockVolume") BigDecimal lockVolume, @Param("updateDate") Timestamp updateDate, @Param("version") Long version);

    @Update("update js_plat_offline_coin_volume set lock_volume = #{lockVolume},bail_volume= #{bailVolume},version= version+1 where user_id = #{userId} and coin_id = #{coinId} and update_date = #{updateDate} and version= #{version}")
    long updateLockVolumeAndBailVolume(@Param("userId") String userId, @Param("coinId") String coinId, @Param("lockVolume") BigDecimal lockVolume, @Param("bailVolume") BigDecimal bailVolume, @Param("updateDate") Timestamp updateDate, @Param("version") Long version);

    @Update("update js_plat_offline_coin_volume set volume = #{volume},bail_volume = #{bailVolume},version= version+1 where user_id = #{userId} and coin_id = #{coinId} and update_date = #{updateDate} and version= #{version}")
    long updateVolumeAndBailVolume(@Param("userId") String userId, @Param("coinId") String coinId, @Param("volume") BigDecimal volume, @Param("bailVolume") BigDecimal bailVolume, @Param("updateDate") Timestamp updateDate, @Param("version") Long version);

    @Update("UPDATE js_plat_offline_coin_volume SET volume = #{volume},advert_volume = #{advertVolume},lock_volume = #{lockVolume},bail_volume = #{bailVolume},otc_advert_volume = #{otcAdvertVolume},otc_lock_volume = #{otcLockVolume},version= version+1 " +
            " WHERE user_id = #{userId} and coin_id = #{coinId} and update_date = #{updateDate} and version= #{version}")
    long updateCoinVolumeInfo(OfflineCoinVolume offlineCoinVolume);

    @Update("UPDATE js_plat_offline_coin_volume SET volume = #{volume},otc_advert_volume = #{otcAdvertVolume},otc_lock_volume = #{otcLockVolume},bail_volume = #{bailVolume},version= version+1 " +
            " WHERE user_id = #{userId} and coin_id = #{coinId} and update_date = #{updateDate} and version= #{version}")
    long updateOtcCoinVolumeInfo(OfflineCoinVolume offlineCoinVolume);

    @Update("update js_plat_offline_coin_volume set bail_volume = #{bailVolume},version= version+1 where user_id = #{userId} and coin_id = #{coinId} and update_date = #{updateDate} and version= #{version}")
    long updateBailVolume(@Param("userId") String userId, @Param("coinId") String coinId, @Param("bailVolume") BigDecimal bailVolume, @Param("updateDate") Timestamp updateDate, @Param("version") Long version);

    @Update("update js_plat_offline_coin_volume set bail_volume = bail_volume + #{addBailVolume},version= version + 1 where user_id = #{userId} and coin_id = #{coinId} and bail_volume >= 0")
    long updateAddBailVolume(@Param("userId") String userId, @Param("coinId") String coinId, @Param("addBailVolume") BigDecimal addBailVolume);

    @Select("SELECT t.id, (c.volume + c.advert_volume + c.lock_volume) AS volume FROM js_plat_user t, js_plat_offline_coin_volume c WHERE t.tag = #{tag} AND t.id = c.user_id AND c.coin_symbol = 'UES' ")
    List<OfflineCoinVolume> findByUserTagUES(@Param("tag") String tag);

    @Update("update js_plat_offline_coin_volume set volume = #{volume},advert_volume = #{advertVolume},version= version+1 where user_id = #{userId} and coin_id = #{coinId} and version= #{version}")
    long updateVolumeAndAdvertVolumeByUserIdAndCoinId(@Param("userId") String userId,@Param("coinId") String coinId,@Param("volume") BigDecimal volume, @Param("advertVolume") BigDecimal advertVolume,@Param("version") Long version);
}
