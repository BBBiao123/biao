package com.biao.vo.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * project :biao
 *
 *  ""
 * @version 1.0
 * @date 2018/6/18 下午4:07
 * @since JDK 1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisUserCoinVolume implements Serializable {

    private String id;

    private String userId;

    private BigDecimal volume;

    private BigDecimal lockVolume;

    private String coinId;

    private String coinSymbol;

    private Short flag;

    private String flagMark;

    @Override
    public String toString() {
        return "RedisUserCoinVolume{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", volume=" + volume +
                ", lockVolume=" + lockVolume +
                ", coinId='" + coinId + '\'' +
                ", coinSymbol='" + coinSymbol + '\'' +
                ", flag=" + flag +
                ", flagMark='" + flagMark + '\'' +
                '}';
    }
}
