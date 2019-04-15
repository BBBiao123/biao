/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 普通用户备份Entity
 * @author zzj
 * @version 2019-01-14
 */
public class Mk2PopularizeCommonMemberCopy extends DataEntity<Mk2PopularizeCommonMemberCopy> {

	private static final long serialVersionUID = 1L;
	private String type;		// 类型
	private String parentId;		// 普通会员父ID
	private String userId;		// 用户ID
	private String mail;		// 邮箱
	private String mobile;		// 手机号
	private String idCard;		// 身份证
	private String realName;		// 真实姓名
	private String coinId;		// 冻结币种
	private String coinSymbol;		// 币种名称
	private String lockStatus;		// 是否冻结
	private BigDecimal lockVolume;		// 冻结数量
	private BigDecimal releaseVolume;		// 释放数量
	private Date releaseBeginDate;		// 释放开始时间
	private String releaseCycle;		// 释放周期
	private BigDecimal releaseCycleRatio;		// 周期释放比例
	private String releaseOver;		// 全部释放
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	private String relationId;	// 关联bail表ref_key
	private String remark;

	public Mk2PopularizeCommonMemberCopy() {
		super();
	}

	public Mk2PopularizeCommonMemberCopy(String id){
		super(id);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	@Length(min=1, max=64, message="用户ID长度必须介于 1 和 64 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	public String getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
	}
	
	public BigDecimal getLockVolume() {
		return lockVolume;
	}

	public void setLockVolume(BigDecimal lockVolume) {
		this.lockVolume = lockVolume;
	}
	
	public BigDecimal getReleaseVolume() {
		return releaseVolume;
	}

	public void setReleaseVolume(BigDecimal releaseVolume) {
		this.releaseVolume = releaseVolume;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReleaseBeginDate() {
		return releaseBeginDate;
	}

	public void setReleaseBeginDate(Date releaseBeginDate) {
		this.releaseBeginDate = releaseBeginDate;
	}
	
	public String getReleaseCycle() {
		return releaseCycle;
	}

	public void setReleaseCycle(String releaseCycle) {
		this.releaseCycle = releaseCycle;
	}
	
	public BigDecimal getReleaseCycleRatio() {
		return releaseCycleRatio;
	}

	public void setReleaseCycleRatio(BigDecimal releaseCycleRatio) {
		this.releaseCycleRatio = releaseCycleRatio;
	}
	
	public String getReleaseOver() {
		return releaseOver;
	}

	public void setReleaseOver(String releaseOver) {
		this.releaseOver = releaseOver;
	}
	
	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = beginCreateDate;
	}
	
	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}