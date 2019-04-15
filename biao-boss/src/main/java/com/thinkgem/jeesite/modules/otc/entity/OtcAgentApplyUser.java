/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.otc.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 拨币申请会员列表Entity
 * @author zzj
 * @version 2018-09-19
 */
public class OtcAgentApplyUser extends DataEntity<OtcAgentApplyUser> {
	
	private static final long serialVersionUID = 1L;
	private String agentId;		// 银商ID
	private String agentName;		// 银商名称
	private String applyId;		// 拨币申请ID
	private String status;		// 拨币状态
	private String percentage;		// 拨币占比
	private String coinId;		// 币种ID
	private String coinSymbol;		// 币种符号
	private Double volume;		// 拨币数量
	private String userId;		// 会员ID
	private String mail;		// 邮箱
	private String mobile;		// 手机
	private String realName;		// 真实姓名
	private String remark;		// 说明
	private Date beginCreateDate;		// 开始 创建日期
	private Date endCreateDate;		// 结束 创建日期
	
	public OtcAgentApplyUser() {
		super();
	}

	public OtcAgentApplyUser(String id){
		super(id);
	}

	@Length(min=0, max=64, message="银商ID长度必须介于 0 和 64 之间")
	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	
	@Length(min=0, max=64, message="银商名称长度必须介于 0 和 64 之间")
	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
	@Length(min=0, max=64, message="拨币申请ID长度必须介于 0 和 64 之间")
	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
	
	@Length(min=0, max=1, message="拨币状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	
	@Length(min=0, max=64, message="币种ID长度必须介于 0 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=0, max=64, message="币种符号长度必须介于 0 和 64 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}
	
	@Length(min=0, max=64, message="会员ID长度必须介于 0 和 64 之间")
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
	
	@Length(min=0, max=500, message="说明长度必须介于 0 和 500 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
		
}