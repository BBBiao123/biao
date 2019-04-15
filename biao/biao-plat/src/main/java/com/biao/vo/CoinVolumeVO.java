package com.biao.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户coin资产vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoinVolumeVO implements Serializable {
    private String id;
    /**
     * 币种图标
     */
    private String coinId;
    private String name;
    private BigDecimal volume;
    private BigDecimal lockVolume;
    /**
     * 状态 -1 ：下架 1：上架
     */
    private String tokenStatus;
    /**
     * 充值提现状态  -1：不可以充值提现 1 ：可以充值 可以提现 2：仅可以充值 3：仅可以提现
     */
    private String status;
    /**
     * 最低挂单数量
     */
    private BigDecimal exMinVolume;
    /**
     * 一次提现最低数量
     */
    private BigDecimal withdrawMinVolume;
    /**
     * 一次提现最大数量
     */
    private BigDecimal withdrawMaxVolume;
    /**
     * 一天最大提现额度
     */
    private BigDecimal withdrawDayMaxVolume;
    /**
     * 提现手续费
     */
    private BigDecimal withdrawFee;

    private String withdrawFeeType;
    /**
     * 1:基于以太  2:基于量子 3：基于小蚂 4：基于EOS5:cny
     **/
    private String coinType;

    private String showSuperBook = "0"; // 是否显示超级账本地址, 默认不显示
}
