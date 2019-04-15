package com.biao.mapper;

import com.biao.entity.SuperCoinVolume;
import com.biao.sql.build.SuperCoinVolumeSqlBuild;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SuperCoinVolumeDao {

    @InsertProvider(type = SuperCoinVolumeSqlBuild.class, method = "insert")
    void insert(SuperCoinVolume superCoinVolume);

    @SelectProvider(type = SuperCoinVolumeSqlBuild.class, method = "findById")
    SuperCoinVolume findById(String id);

    @Select("SELECT " + SuperCoinVolumeSqlBuild.columns + " FROM js_plat_super_coin_volume t WHERE t.user_id = #{userId}")
    List<SuperCoinVolume> findByUserId(@Param("userId") String userId);

    @Select("SELECT " + SuperCoinVolumeSqlBuild.columns + " from js_plat_super_coin_volume where user_id = #{userId} and coin_id = #{coinId} limit 1")
    SuperCoinVolume findByUserIdAndCoinId(@Param("userId") String userId, @Param("coinId") String coinId);

    @Select("SELECT " + SuperCoinVolumeSqlBuild.columns + " from js_plat_super_coin_volume where deposit_end <= #{expireDate}")
    List<SuperCoinVolume> findByExpireDate(@Param("expireDate") LocalDateTime expireDate);

    @InsertProvider(type = SuperCoinVolumeSqlBuild.class, method = "batchInsert")
    void batchInsert(@Param("listValues") List<SuperCoinVolume> superCoinVolumes);

    @Update("update js_plat_super_coin_volume set volume = #{volume},version= version+1 where user_id = #{userId} and coin_id = #{coinId} and update_date = #{updateDate} and version= #{version}")
    long updateVolume(@Param("userId") String userId, @Param("coinId") String coinId, @Param("volume") BigDecimal addVolume, @Param("updateDate") Timestamp updateDate, @Param("version") Long version);

    @Update("update js_plat_super_coin_volume set volume = volume + #{addVolume},version = version + 1 where user_id = #{userId} and coin_id = #{coinId} and volume >= 0")
    long updateAddVolume(@Param("userId") String userId, @Param("coinId") String coinId, @Param("addVolume") BigDecimal addVolume);

    @Update("update js_plat_super_coin_volume set volume = #{volume},version= version+1,deposit_begin = null, deposit_end = null where user_id = #{userId} and coin_id = #{coinId} and update_date = #{updateDate} and version= #{version}")
    long updateExpiredVolume(@Param("userId") String userId, @Param("coinId") String coinId, @Param("volume") BigDecimal addVolume, @Param("updateDate") Timestamp updateDate, @Param("version") Long version);

    @UpdateProvider(type = SuperCoinVolumeSqlBuild.class, method = "updateById")
    void updateById(SuperCoinVolume superCoinVolume);

}
