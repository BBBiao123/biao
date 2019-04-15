/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 会员分红日志Entity
 * @author dongfeng
 * @version 2018-08-09
 */
public class Mk2PopularizeBonusMemberLog extends DataEntity<Mk2PopularizeBonusMemberLog> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 类型
	private String relationId;		// 关联外键ID,type为节点人时，为节点会员表ID；type区域合伙人时,为区域会员表ID
	private String userId;		// 用户ID
	private String mail;		// 邮箱
	private String mobile;		// mobile
	private String coinId;		// 分红币种ID
	private String coinSymbol;		// 分红币种名称
	private String totalVolume;		// 分红总量
	private String bonusRatio;		// 分红比例
	private String incomeVolume;		// 分红数量
	private String areaName;		// 城市
	private String beforIncomeVolume;		// 分红前数量
	private String joinVolume;		// 参与占比量
	private String joinTotalVolume;		// 参与占比总量
	private Date bonusDateBegin;		// 分红开始时间
	private Date bonusDateEnd;		// 分红结束时间
	private String remark;		// 描述
	private Date beginBonusDateBegin;		// 开始 分红开始时间
	private Date endBonusDateBegin;		// 结束 分红开始时间
	private Date beginBonusDateEnd;		// 开始 分红结束时间
	private Date endBonusDateEnd;		// 结束 分红结束时间
	
	public Mk2PopularizeBonusMemberLog() {
		super();
	}

	public Mk2PopularizeBonusMemberLog(String id){
		super(id);
	}

	@Length(min=1, max=1, message="类型长度必须介于 1 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=64, message="关联外键ID,type为节点人时，为节点会员表ID；type区域合伙人时,为区域会员表ID长度必须介于 0 和 64 之间")
	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	
	@Length(min=1, max=64, message="用户ID长度必须介于 1 和 64 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=0, max=64, message="邮箱长度必须介于 0 和 64 之间")
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
	@Length(min=0, max=11, message="mobile长度必须介于 0 和 11 之间")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@Length(min=1, max=64, message="分红币种ID长度必须介于 1 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=0, max=64, message="分红币种名称长度必须介于 0 和 64 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	public String getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(String totalVolume) {
		this.totalVolume = totalVolume;
	}
	
	public String getBonusRatio() {
		return bonusRatio;
	}

	public void setBonusRatio(String bonusRatio) {
		this.bonusRatio = bonusRatio;
	}
	
	public String getIncomeVolume() {
		return incomeVolume;
	}

	public void setIncomeVolume(String incomeVolume) {
		this.incomeVolume = incomeVolume;
	}
	
	@Length(min=0, max=64, message="城市长度必须介于 0 和 64 之间")
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
	public String getBeforIncomeVolume() {
		return beforIncomeVolume;
	}

	public void setBeforIncomeVolume(String beforIncomeVolume) {
		this.beforIncomeVolume = beforIncomeVolume;
	}
	
	public String getJoinVolume() {
		return joinVolume;
	}

	public void setJoinVolume(String joinVolume) {
		this.joinVolume = joinVolume;
	}
	
	public String getJoinTotalVolume() {
		return joinTotalVolume;
	}

	public void setJoinTotalVolume(String joinTotalVolume) {
		this.joinTotalVolume = joinTotalVolume;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBonusDateBegin() {
		return bonusDateBegin;
	}

	public void setBonusDateBegin(Date bonusDateBegin) {
		this.bonusDateBegin = bonusDateBegin;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBonusDateEnd() {
		return bonusDateEnd;
	}

	public void setBonusDateEnd(Date bonusDateEnd) {
		this.bonusDateEnd = bonusDateEnd;
	}
	
	@Length(min=0, max=500, message="描述长度必须介于 0 和 500 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Date getBeginBonusDateBegin() {
		return beginBonusDateBegin;
	}

	public void setBeginBonusDateBegin(Date beginBonusDateBegin) {
		this.beginBonusDateBegin = beginBonusDateBegin;
	}
	
	public Date getEndBonusDateBegin() {
		return endBonusDateBegin;
	}

	public void setEndBonusDateBegin(Date endBonusDateBegin) {
		this.endBonusDateBegin = endBonusDateBegin;
	}
		
	public Date getBeginBonusDateEnd() {
		return beginBonusDateEnd;
	}

	public void setBeginBonusDateEnd(Date beginBonusDateEnd) {
		this.beginBonusDateEnd = beginBonusDateEnd;
	}
	
	public Date getEndBonusDateEnd() {
		return endBonusDateEnd;
	}

	public void setEndBonusDateEnd(Date endBonusDateEnd) {
		this.endBonusDateEnd = endBonusDateEnd;
	}
		
}