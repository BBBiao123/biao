/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 注册活动规则Entity
 *
 * @author xiaoyu
 * @version 2018-10-25
 */
public class MkUserRegisterLotteryRule extends DataEntity<MkUserRegisterLotteryRule> {

    private static final long serialVersionUID = 1L;
    private String lotteryId;        // 活动id
    private String name;        // name
    private Integer minCount;        // 最小数量
    private Integer maxCount;        // 最大数量
    private Double ratio;        // 中奖比例

    private String lotteryName;

    public String getLotteryName() {
        return lotteryName;
    }

    public void setLotteryName(String lotteryName) {
        this.lotteryName = lotteryName;
    }

    public MkUserRegisterLotteryRule() {
        super();
    }

    public MkUserRegisterLotteryRule(String id) {
        super(id);
    }

    @Length(min = 1, max = 64, message = "活动id长度必须介于 1 和 64 之间")
    public String getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    @Length(min = 1, max = 100, message = "name长度必须介于 1 和 100 之间")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMinCount() {
        return minCount;
    }

    public void setMinCount(Integer minCount) {
        this.minCount = minCount;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    @DecimalMin(value = "0.00", message = "中奖比例在0,1之间")
    @DecimalMax(value = "1.00", message = "中奖比例在0,1之间")
    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }
}