package com.biao.entity;

import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import com.biao.vo.UserCoinVolumeVO;
import com.biao.vo.redis.RedisUserCoinVolume;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 用户资产
 *
 *  ""
 */
@SqlTable("js_plat_user_coin_volume")
@Data
public class UserCoinVolume extends BaseEntity {

    @SqlField("user_id")
    private String userId;

    @SqlField("volume")
    private BigDecimal volume;

    @SqlField("lock_volume")
    private BigDecimal lockVolume;

    @SqlField("out_lock_volume")
    private BigDecimal outLockVolume = BigDecimal.ZERO;

    @SqlField("coin_id")
    private String coinId;

    @SqlField("coin_symbol")
    private String coinSymbol;

    @SqlField("flag")
    private Short flag;

    @SqlField("flag_mark")
    private String flagMark;

    @SqlField("version")
    private Long version = 0L;

    /**
     * 转换成VO.
     *
     * @param redisUserCoinVolume redis对象
     * @return UserCoinVolumeVO
     */
    public static UserCoinVolumeVO transform(final RedisUserCoinVolume redisUserCoinVolume) {
        if (Objects.isNull(redisUserCoinVolume)) {
            return new UserCoinVolumeVO();
        }
        UserCoinVolumeVO volumeVO = new UserCoinVolumeVO();
        volumeVO.setCoinSymbol(redisUserCoinVolume.getCoinSymbol());
        volumeVO.setUserId(redisUserCoinVolume.getUserId());
        volumeVO.setVolume(redisUserCoinVolume.getVolume());
        volumeVO.setLockVolume(redisUserCoinVolume.getLockVolume());
        return volumeVO;
    }


    /**
     * 转换成VO.
     *
     * @param redisUserCoinVolume 数据库实体
     * @return UserCoinVolume
     */
    public static UserCoinVolume transformToRedis(final RedisUserCoinVolume redisUserCoinVolume) {
        if (Objects.isNull(redisUserCoinVolume)) {
            return new UserCoinVolume();
        }
        UserCoinVolume userCoinVolume = new UserCoinVolume();
        userCoinVolume.setId(redisUserCoinVolume.getId());
        userCoinVolume.setCoinId(redisUserCoinVolume.getCoinId());
        userCoinVolume.setCoinSymbol(redisUserCoinVolume.getCoinSymbol());
        userCoinVolume.setUserId(redisUserCoinVolume.getUserId());
        userCoinVolume.setVolume(redisUserCoinVolume.getVolume());
        userCoinVolume.setLockVolume(redisUserCoinVolume.getLockVolume());
        userCoinVolume.setCreateDate(LocalDateTime.now());
        userCoinVolume.setUpdateDate(LocalDateTime.now());
        userCoinVolume.setFlag(redisUserCoinVolume.getFlag());
        userCoinVolume.setFlagMark(redisUserCoinVolume.getFlagMark());
        userCoinVolume.setCreateBy("biao");
        userCoinVolume.setUpdateBy("biao");
        return userCoinVolume;
    }


}
