/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 接力撞奖名单Entity
 * @author zzj
 * @version 2018-09-05
 */
public class MkRelayPrizeCandidate extends DataEntity<MkRelayPrizeCandidate> {
	
	private static final long serialVersionUID = 1L;
	private String status;		// 状态
	private String userId;		// user_id
	private String mail;		// 邮箱
	private String mobile;		// 手机
	private String realName;		// 真实姓名
	private String referId;		// refer_id
	private String coinId;		// 币种id
	private String coinSymbol;		// 币种代号
	private String volume;		// 已发放数量
	private Date achieveDate;		// 参与资格时间
	private String isPrize;		// 是否中奖
	private String prizeVolume;		// 奖金
	private Date lostTime;		// 失效时间
	private String remark;		// 说明
	private Date beginAchieveDate;		// 开始 参与资格时间
	private Date endAchieveDate;		// 结束 参与资格时间
	private String targetMobile; 	//目标手机号
	
	public MkRelayPrizeCandidate() {
		super();
	}

	public MkRelayPrizeCandidate(String id){
		super(id);
	}

	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=64, message="user_id长度必须介于 0 和 64 之间")
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
	
	@Length(min=0, max=64, message="手机长度必须介于 0 和 64 之间")
	@ExcelField(title="原手机号", align=2, sort=1)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@Length(min=0, max=64, message="真实姓名长度必须介于 0 和 64 之间")
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	@Length(min=0, max=64, message="refer_id长度必须介于 0 和 64 之间")
	public String getReferId() {
		return referId;
	}

	public void setReferId(String referId) {
		this.referId = referId;
	}
	
	@Length(min=0, max=64, message="币种id长度必须介于 0 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=0, max=45, message="币种代号长度必须介于 0 和 45 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAchieveDate() {
		return achieveDate;
	}

	public void setAchieveDate(Date achieveDate) {
		this.achieveDate = achieveDate;
	}
	
	@Length(min=0, max=1, message="是否中奖长度必须介于 0 和 1 之间")
	public String getIsPrize() {
		return isPrize;
	}

	public void setIsPrize(String isPrize) {
		this.isPrize = isPrize;
	}
	
	public String getPrizeVolume() {
		return prizeVolume;
	}

	public void setPrizeVolume(String prizeVolume) {
		this.prizeVolume = prizeVolume;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getLostTime() {
		return lostTime;
	}

	public void setLostTime(Date lostTime) {
		this.lostTime = lostTime;
	}
	
	@Length(min=0, max=500, message="说明长度必须介于 0 和 500 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Date getBeginAchieveDate() {
		return beginAchieveDate;
	}

	public void setBeginAchieveDate(Date beginAchieveDate) {
		this.beginAchieveDate = beginAchieveDate;
	}
	
	public Date getEndAchieveDate() {
		return endAchieveDate;
	}

	public void setEndAchieveDate(Date endAchieveDate) {
		this.endAchieveDate = endAchieveDate;
	}

	@ExcelField(title="目标手机号", align=2, sort=10)
	public String getTargetMobile() {
		return targetMobile;
	}

	public void setTargetMobile(String targetMobile) {
		this.targetMobile = targetMobile;
	}
}