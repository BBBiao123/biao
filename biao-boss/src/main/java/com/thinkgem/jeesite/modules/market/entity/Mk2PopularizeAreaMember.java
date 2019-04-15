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
 * 区域合伙人售卖规则Entity
 * @author dongfeng
 * @version 2018-07-24
 */
public class Mk2PopularizeAreaMember extends DataEntity<Mk2PopularizeAreaMember> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 类型，1区域合伙人2股东
	private String parentId;	// 父区域合伙人ID
	private BigDecimal ratio;	// 股份比例
	private String coinId;		// 冻结币种
	private String coinSymbol;		// 币种名称
	private String areaId;		// sys_area.id
	private String areaName;		// 城市
	private String areaParaentId;		// sys_area.parent_id
	private String areaParaentName;		// 省份
	private String status;		// 售出
	private String userId;		// 用户ID
	private String mail;		// 邮箱
	private String mobile;		// mobile
	private String idCard;		// 身份证
	private String realName;	// 姓名
	private BigDecimal lockVolume;		// 冻结数量
	private BigDecimal releaseVolume;		// 已释放数量
	private Date releaseBeginDate;		// 释放开始时间
	private String releaseCycle;		// 释放周期
	private BigDecimal releaseCycleRatio;		// 周期释放数量
	private String releaseOver;		// 全部释放
	private String[] shareholderId;  // 股东ID
	private String[] shareholderUserId; // 股东IDs
	private BigDecimal[] shareholderRatio;// 股份比例 和 股东IDs 一一对应
	private String relationId;	// 关联bail表ref_key
	
	public Mk2PopularizeAreaMember() {
		super();
	}

	public Mk2PopularizeAreaMember(String id){
		super(id);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
	
	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
	public String getAreaParaentId() {
		return areaParaentId;
	}

	public void setAreaParaentId(String areaParaentId) {
		this.areaParaentId = areaParaentId;
	}
	
	public String getAreaParaentName() {
		return areaParaentName;
	}

	public void setAreaParaentName(String areaParaentName) {
		this.areaParaentName = areaParaentName;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String[] getShareholderUserId() {
		return shareholderUserId;
	}

	public void setShareholderUserId(String[] shareholderUserId) {
		this.shareholderUserId = shareholderUserId;
	}

	public BigDecimal[] getShareholderRatio() {
		return shareholderRatio;
	}

	public void setShareholderRatio(BigDecimal[] shareholderRatio) {
		this.shareholderRatio = shareholderRatio;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public BigDecimal getRatio() {
		return ratio;
	}

	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
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

	public String[] getShareholderId() {
		return shareholderId;
	}

	public void setShareholderId(String[] shareholderId) {
		this.shareholderId = shareholderId;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
}