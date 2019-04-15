/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

/**
 * 注册送奖活动限制Entity
 * @author xiaoyu
 * @version 2018-11-05
 */
public class MkUserRegisterLotteryLimit extends DataEntity<MkUserRegisterLotteryLimit> {
	
	private static final long serialVersionUID = 1L;
	private String lotteryId;		// 活动id
	private String startCount;		// 开始数量
	private String endCount;		// 结束数量
	private String ratio;		// 比例


	private String lotteryName;

	public String getLotteryName() {
		return lotteryName;
	}

	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}

	public MkUserRegisterLotteryLimit() {
		super();
	}

	public MkUserRegisterLotteryLimit(String id){
		super(id);
	}

	@Length(min=1, max=100, message="活动id长度必须介于 1 和 100 之间")
	public String getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}
	
	@Length(min=0, max=11, message="开始数量长度必须介于 0 和 11 之间")
	public String getStartCount() {
		return startCount;
	}

	public void setStartCount(String startCount) {
		this.startCount = startCount;
	}
	
	@Length(min=0, max=11, message="结束数量长度必须介于 0 和 11 之间")
	public String getEndCount() {
		return endCount;
	}

	public void setEndCount(String endCount) {
		this.endCount = endCount;
	}

	@DecimalMin(value = "0.00", message = "比例在0,1之间")
	@DecimalMax(value = "1.00", message = "比例在0,1之间")
	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	
}