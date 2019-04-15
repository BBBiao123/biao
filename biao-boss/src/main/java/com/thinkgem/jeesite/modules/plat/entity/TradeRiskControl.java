/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

/**
 * 交易风险控制Entity
 *
 * @author xiaoyu
 * @version 2018-09-10
 */
public class TradeRiskControl extends DataEntity<TradeRiskControl> {

    private static final long serialVersionUID = 1L;
    private String coinMain;        // 主区
    private String coinOther;        // 被交易币种
    private String exPairId;        // 交易对id
    private String userIds;        // 所对应的用户id集合（逗号隔开）
    private Double riskRatio;        // 风险比例
    private BigDecimal fixedVolume;
    private String sourceUser;

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    @DecimalMin(value = "0.00",message ="资金比例必须在0,1之间" )
    @DecimalMax(value = "1.00",message ="资金比例必须在0,1之间")
    public Double getRiskRatio() {
        return riskRatio;
    }

    public void setRiskRatio(Double riskRatio) {
        this.riskRatio = riskRatio;
    }

    public BigDecimal getFixedVolume() {
        return fixedVolume;
    }

    public void setFixedVolume(BigDecimal fixedVolume) {
        this.fixedVolume = fixedVolume;
    }

    @NotBlank(message = "用户来源不能为空")
    public String getSourceUser() {
        return sourceUser;
    }

    public void setSourceUser(String sourceUser) {
        this.sourceUser = sourceUser;
    }

    public TradeRiskControl() {
        super();
    }

    public TradeRiskControl(String id) {
        super(id);
    }

    @Length(min = 1, max = 32, message = "主区长度必须介于 1 和 32 之间")
    public String getCoinMain() {
        return coinMain;
    }

    public void setCoinMain(String coinMain) {
        this.coinMain = coinMain;
    }

    @Length(min = 1, max = 32, message = "被交易币种长度必须介于 1 和 32 之间")
    public String getCoinOther() {
        return coinOther;
    }

    public void setCoinOther(String coinOther) {
        this.coinOther = coinOther;
    }

    @Length(min = 1, max = 128, message = "交易对id长度必须介于 1 和 128 之间")
    public String getExPairId() {
        return exPairId;
    }

    public void setExPairId(String exPairId) {
        this.exPairId = exPairId;
    }


}