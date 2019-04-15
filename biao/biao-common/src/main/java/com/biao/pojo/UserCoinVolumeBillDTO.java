package com.biao.pojo;

import com.biao.enums.UserCoinVolumeEventEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * UserCoinVolumeBillDTO.
 * <p>
 * 审请记录处理.
 * <p>
 * 19-1-2下午5:32
 *
 *  "" sixh
 */
@Data
public class UserCoinVolumeBillDTO {
    /**
     * 用户id;
     */
    private String userId;

    /**
     * 币总信息.
     */
    private String coinSymbol;

    /**
     * 优先级，5为默认. 数字越大，优先级越高.
     */
    private Integer priority;

    /**
     * 外部调用，自行传入的信息。
     */
    private String refKey;

    /**
     * 需要操作的指令.
     * {@link com.biao.enums.UserCoinVolumeEventEnum}
     */
    private UserCoinVolumeEventEnum[] opSign;

    /**
     * 需要处理的数量.
     */
    private BigDecimal opVolume;

    /**
     * 需要处理的锁定数量.
     */
    private BigDecimal opLockVolume;

    /**
     * 数据来源.
     */
    private String source;

    /**
     * 外部系统的标注.
     */
    private String mark;

    private Boolean forceLock;

    @Override
    public String toString() {
        return "UserCoinVolumeBillDTO{" +
                "userId='" + userId + '\'' +
                ", coinSymbol='" + coinSymbol + '\'' +
                ", priority=" + priority +
                ", refKey='" + refKey + '\'' +
                ", opSign=" + opSign +
                ", opVolume=" + opVolume +
                ", opLockVolume=" + opLockVolume +
                ", source='" + source + '\'' +
                ", mark='" + mark + '\'' +
                '}';
    }
}
