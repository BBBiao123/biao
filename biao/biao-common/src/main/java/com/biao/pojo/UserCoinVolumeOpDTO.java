package com.biao.pojo;

import com.biao.enums.UserCoinVolumeEventEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * UserCoinVolumeOpDTO.
 * <p>
 * 用户bb资产数据变更的处理.
 * <p>
 * 19-1-2下午3:30
 *
 *  "" sixh
 */
@Data
public class UserCoinVolumeOpDTO {

    /**
     * 用户id;
     */
    private String userId;

    /**
     * 币总信息.
     */
    private String symbol;

    /**
     * 操作的volume.
     */
    private BigDecimal opVolume;

    /**
     * 操作的锁定volume.
     */
    private BigDecimal opLockVolume;

    /**
     * 操作符.
     */
    private Integer opSign;

    @Override
    public String toString() {
        return "UserCoinVolumeOpDTO{" +
                "userId='" + userId + '\'' +
                ", symbol='" + symbol + '\'' +
                ", opVolume=" + opVolume +
                ", opLockVolume=" + opLockVolume +
                ", opSign=" + UserCoinVolumeEventEnum.getEventText(opSign) +
                '}';
    }
}
