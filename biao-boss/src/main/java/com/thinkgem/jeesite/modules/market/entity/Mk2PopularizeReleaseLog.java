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
 * 冻结数量释放记录Entity
 * @author dongfeng
 * @version 2018-08-09
 */
public class Mk2PopularizeReleaseLog extends DataEntity<Mk2PopularizeReleaseLog> {
	
	private static final long serialVersionUID = 1L;
	private String relationId;		// 关联外键
	private String type;		// 类型
	private String userId;		// 用户ID
	private String mail;		// 邮箱
	private String mobile;		// mobile
	private String coinId;		// 币种ID
	private String coinSymbol;		// 冻结币种
	private Double releaseVolume;		// 释放数量
	private Date releaseCycleDate;		// 释放时间
	private String releaseStatus;		// 释放状态
	private String areaName;		// 区域名称
	private String remark;		// 结果
	private Date beginReleaseCycleDate;		// 开始 释放时间
	private Date endReleaseCycleDate;		// 结束 释放时间

	private String releaseSource; // 释放来源
	private String releaseCycleRatio; // 释放比例
	
	public Mk2PopularizeReleaseLog() {
		super();
	}

	public Mk2PopularizeReleaseLog(String id){
		super(id);
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
	
	public Double getReleaseVolume() {
		return releaseVolume;
	}

	public void setReleaseVolume(Double releaseVolume) {
		this.releaseVolume = releaseVolume;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReleaseCycleDate() {
		return releaseCycleDate;
	}

	public void setReleaseCycleDate(Date releaseCycleDate) {
		this.releaseCycleDate = releaseCycleDate;
	}
	
	public String getReleaseStatus() {
		return releaseStatus;
	}

	public void setReleaseStatus(String releaseStatus) {
		this.releaseStatus = releaseStatus;
	}
	
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Date getBeginReleaseCycleDate() {
		return beginReleaseCycleDate;
	}

	public void setBeginReleaseCycleDate(Date beginReleaseCycleDate) {
		this.beginReleaseCycleDate = beginReleaseCycleDate;
	}
	
	public Date getEndReleaseCycleDate() {
		return endReleaseCycleDate;
	}

	public void setEndReleaseCycleDate(Date endReleaseCycleDate) {
		this.endReleaseCycleDate = endReleaseCycleDate;
	}

	public String getReleaseSource() {
		return releaseSource;
	}

	public void setReleaseSource(String releaseSource) {
		this.releaseSource = releaseSource;
	}

	public String getReleaseCycleRatio() {
		return releaseCycleRatio;
	}

	public void setReleaseCycleRatio(String releaseCycleRatio) {
		this.releaseCycleRatio = releaseCycleRatio;
	}
}