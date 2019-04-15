/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.OfflineCoinVolume;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * c2c资产DAO接口
 * @author dazi
 * @version 2018-04-27
 */
@MyBatisDao
public interface OfflineCoinVolumeDao extends CrudDao<OfflineCoinVolume> {

    public static final String columns = " id,user_id,coin_id,coin_symbol,volume,advert_volume,lock_volume,version,create_by,update_by,create_date,update_date " ;

    @Select("select "+ columns +" from js_plat_offline_coin_volume where user_id = #{userId} and coin_id = #{coinId} limit 1")
    OfflineCoinVolume findByUserIdAndCoinId(@Param("userId") String userId,@Param("coinId") String coinId);

    @Update("update js_plat_offline_coin_volume set lock_volume = #{lockVolume},version = version + 1 where user_id = #{userId} and coin_id = #{coinId} and update_date = #{updateDate} and version = #{version}")
    long updateLockVolume(@Param("userId") String userId, @Param("coinId") String coinId, @Param("lockVolume") BigDecimal lockVolume, @Param("updateDate") Timestamp updateDate, @Param("version") Long version);

    @Update("update js_plat_offline_coin_volume set volume = #{volume},version = version + 1 where user_id = #{userId} and coin_id = #{coinId} and update_date = #{updateDate} and version = #{version}")
    long updateVolume(@Param("userId") String userId,@Param("coinId") String coinId,@Param("volume")  BigDecimal addVolume,@Param("updateDate") Timestamp updateDate, @Param("version") Long version);

    @Update("update js_plat_offline_coin_volume set volume = #{volume},lock_volume = #{lockVolume},version = version + 1 where user_id = #{userId} and coin_id = #{coinId} and update_date = #{updateDate} and version = #{version}")
    long updateVolumeAndLockVolume(@Param("userId") String userId,@Param("coinId") String coinId,@Param("volume") BigDecimal volume,@Param("lockVolume") BigDecimal lockVolume,@Param("updateDate") Timestamp updateDate, @Param("version") Long version);

    @Update("update js_plat_offline_coin_volume set advert_volume = #{advertVolume},lock_volume = #{lockVolume},version = version + 1 where user_id = #{userId} and coin_id = #{coinId} and update_date = #{updateDate} and version = #{version}")
    long updateAdvertVolumeAndLockVolume(@Param("userId") String userId,@Param("coinId") String coinId,@Param("advertVolume") BigDecimal advertVolume,@Param("lockVolume") BigDecimal lockVolume,@Param("updateDate") Timestamp timestamp, @Param("version") Long version);

    @Update("UPDATE js_plat_offline_coin_volume SET advert_volume = #{advertVolume}, volume = #{volume}, version = version + 1 WHERE user_id = #{userId} and coin_id = #{coinId} AND version = #{version} AND update_date = #{updateDate} ")
    long updateVolumeAndAdvert(OfflineCoinVolume offlineCoinVolume);
}