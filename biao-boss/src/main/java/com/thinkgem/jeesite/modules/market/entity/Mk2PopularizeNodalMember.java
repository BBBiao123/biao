/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 节点人Entity
 * @author dongfeng
 * @version 2018-07-24
 */
public class Mk2PopularizeNodalMember extends DataEntity<Mk2PopularizeNodalMember> {
	
	private static final long serialVersionUID = 1L;
	private String type; // 类型，1节点人，2节点人详细释放规则
	private String parentId; // 节点人父ID
	private String userId;		// 用户ID
	private String mail;		// 邮箱
	private String mobile;		// mobile
	private String idCard;		// 身份证号
	private String realName;	// 姓名
	private String coinId;		// 冻结币种
	private String coinSymbol;		// 币种名称
	private String lockStatus;		// 冻结状态，1成功0失败
	private BigDecimal lockVolume;		// 冻结数量
	private BigDecimal releaseVolume;		// 已释放数量
	private Date releaseBeginDate;		// 释放开始时间
	private String releaseCycle;		// 释放周期
	private BigDecimal releaseCycleRatio;		// 周期释放比例
	private String releaseOver;		// 全部释放
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	private String relationId;	// 关联bail表ref_key

	private String returnMsg; 	// 前端提示消息
	private String returnStatus; // 前端提示状态

	public Mk2PopularizeNodalMember() {
		super();
	}

	public Mk2PopularizeNodalMember(String id){
		super(id);
	}

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

	public String getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
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

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public String getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
}