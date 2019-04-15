/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 会员规则设置Entity
 * @author dongfeng
 * @version 2019-02-28
 */
public class Mk2PopularizeMemberRule extends DataEntity<Mk2PopularizeMemberRule> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 会员类型
	private String releaseOpen;		// 释放开关
	private Long releaseVersion;		// 释放版本
	private String releaseDay;		// 天周期开关
	private String releaseWeek;		// 周周期开关
	private String releaseMonth;		// 月周期开关
	private String releaseYear;		// 年周期开关
	private String releaseType;		// 释放冻结类型
	private Long totalMember;		// 会员总量
	private Long soldMember;		// 已售出会员总量
	private Double bonusRatio;		// 固定分红比例百分比
	private Double phoneBonusRatio;		// 号码归属地分红
	private Double referBonusRatio;		// 推荐分红
	
	public Mk2PopularizeMemberRule() {
		super();
	}

	public Mk2PopularizeMemberRule(String id){
		super(id);
	}

	@Length(min=1, max=1, message="会员类型长度必须介于 1 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=1, message="释放开关长度必须介于 0 和 1 之间")
	public String getReleaseOpen() {
		return releaseOpen;
	}

	public void setReleaseOpen(String releaseOpen) {
		this.releaseOpen = releaseOpen;
	}
	
	public Long getReleaseVersion() {
		return releaseVersion;
	}

	public void setReleaseVersion(Long releaseVersion) {
		this.releaseVersion = releaseVersion;
	}
	
	@Length(min=0, max=1, message="天周期开关长度必须介于 0 和 1 之间")
	public String getReleaseDay() {
		return releaseDay;
	}

	public void setReleaseDay(String releaseDay) {
		this.releaseDay = releaseDay;
	}
	
	@Length(min=0, max=1, message="周周期开关长度必须介于 0 和 1 之间")
	public String getReleaseWeek() {
		return releaseWeek;
	}

	public void setReleaseWeek(String releaseWeek) {
		this.releaseWeek = releaseWeek;
	}
	
	@Length(min=0, max=1, message="月周期开关长度必须介于 0 和 1 之间")
	public String getReleaseMonth() {
		return releaseMonth;
	}

	public void setReleaseMonth(String releaseMonth) {
		this.releaseMonth = releaseMonth;
	}
	
	@Length(min=0, max=1, message="年周期开关长度必须介于 0 和 1 之间")
	public String getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(String releaseYear) {
		this.releaseYear = releaseYear;
	}
	
	@Length(min=0, max=50, message="释放冻结类型长度必须介于 0 和 50 之间")
	public String getReleaseType() {
		return releaseType;
	}

	public void setReleaseType(String releaseType) {
		this.releaseType = releaseType;
	}
	
	public Long getTotalMember() {
		return totalMember;
	}

	public void setTotalMember(Long totalMember) {
		this.totalMember = totalMember;
	}
	
	public Long getSoldMember() {
		return soldMember;
	}

	public void setSoldMember(Long soldMember) {
		this.soldMember = soldMember;
	}
	
	public Double getBonusRatio() {
		return bonusRatio;
	}

	public void setBonusRatio(Double bonusRatio) {
		this.bonusRatio = bonusRatio;
	}
	
	public Double getPhoneBonusRatio() {
		return phoneBonusRatio;
	}

	public void setPhoneBonusRatio(Double phoneBonusRatio) {
		this.phoneBonusRatio = phoneBonusRatio;
	}
	
	public Double getReferBonusRatio() {
		return referBonusRatio;
	}

	public void setReferBonusRatio(Double referBonusRatio) {
		this.referBonusRatio = referBonusRatio;
	}
	
}