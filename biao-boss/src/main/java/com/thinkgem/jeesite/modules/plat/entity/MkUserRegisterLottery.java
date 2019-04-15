/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 注册抽奖活动Entity
 *
 * @author xiaoyu
 * @version 2018-10-25
 */
@SuppressWarnings("all")
public class MkUserRegisterLottery extends DataEntity<MkUserRegisterLottery> {

    private static final long serialVersionUID = 1L;
    private String name;        // 活动名称
    private String coinSymbol;        // 币种
    private Integer status;        // 是否开启
    private BigDecimal totalPrize;        // 奖金总量
    private BigDecimal recommendMinVolume;        // recomment_min_volume
    private Double recommendRatio;        // recommend_ratio

    private Integer recommendDayCount;

    private Integer recommendTotalCount;

    private Integer recommendCountLimit;

    public Integer getRecommendCountLimit() {
        return recommendCountLimit;
    }

    public void setRecommendCountLimit(Integer recommendCountLimit) {
        this.recommendCountLimit = recommendCountLimit;
    }

    private Date startDate;    // 更新日期

    public BigDecimal getRecommendMinVolume() {
        return recommendMinVolume;
    }

    public void setRecommendMinVolume(BigDecimal recommendMinVolume) {
        this.recommendMinVolume = recommendMinVolume;
    }

    public MkUserRegisterLottery() {
        super();
    }

    public MkUserRegisterLottery(String id) {
        super(id);
    }

    @Length(min = 1, max = 100, message = "活动名称长度必须介于 1 和 100 之间")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 0, max = 32, message = "币种长度必须介于 0 和 32 之间")
    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getTotalPrize() {
        return totalPrize;
    }

    public void setTotalPrize(BigDecimal totalPrize) {
        this.totalPrize = totalPrize;
    }

    @DecimalMin(value = "0.00", message = "推荐人获奖比例必须在0,1之间")
    @DecimalMax(value = "1.00", message = "推荐人获奖比例必须在0,1之间")
    public Double getRecommendRatio() {
        return recommendRatio;
    }

    public void setRecommendRatio(Double recommendRatio) {
        this.recommendRatio = recommendRatio;
    }


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Min(value = 1, message = "推荐人每天获取奖励个数至少为1")
    public Integer getRecommendDayCount() {
        return recommendDayCount;
    }

    public void setRecommendDayCount(Integer recommendDayCount) {
        this.recommendDayCount = recommendDayCount;
    }

    @Min(value = 1, message = "推荐人获取奖励总数至少为1")
    public Integer getRecommendTotalCount() {
        return recommendTotalCount;
    }

    public void setRecommendTotalCount(Integer recommendTotalCount) {
        this.recommendTotalCount = recommendTotalCount;
    }
}