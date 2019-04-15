/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 接力撞奖打款日志Entity
 * @author zzj
 * @version 2018-09-05
 */
public class MkRelayRemitLog extends DataEntity<MkRelayRemitLog> {
	
	private static final long serialVersionUID = 1L;
	private String userId;		// 用户ID
	private String mail;		// 邮箱
	private String mobile;		// 手机
	private String realName;		// 真是姓名
	private String coinId;		// 币种ID
	private String coinSymbol;		// 币种
	private String volume;		// 数量
	private String userType;		// 用户类型
	private String isRemit;		// 用户类型
	private String refereeId;		// 推荐人ID
	private String referMail;
	private String referMobile;
	private String referRealName;
	private String remark;		// 备注
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	
	public MkRelayRemitLog() {
		super();
	}

	public MkRelayRemitLog(String id){
		super(id);
	}

	@Length(min=0, max=64, message="用户ID长度必须介于 0 和 64 之间")
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
	
	@Length(min=0, max=11, message="手机长度必须介于 0 和 11 之间")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@Length(min=0, max=64, message="真是姓名长度必须介于 0 和 64 之间")
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	@Length(min=0, max=64, message="币种ID长度必须介于 0 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=0, max=64, message="币种长度必须介于 0 和 64 之间")
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
	
	@Length(min=0, max=1, message="用户类型长度必须介于 0 和 1 之间")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getIsRemit() {
		return isRemit;
	}

	public void setIsRemit(String isRemit) {
		this.isRemit = isRemit;
	}

	@Length(min=0, max=64, message="推荐人ID长度必须介于 0 和 64 之间")
	public String getRefereeId() {
		return refereeId;
	}

	public void setRefereeId(String refereeId) {
		this.refereeId = refereeId;
	}

	public String getReferMail() {
		return referMail;
	}

	public void setReferMail(String referMail) {
		this.referMail = referMail;
	}

	public String getReferMobile() {
		return referMobile;
	}

	public void setReferMobile(String referMobile) {
		this.referMobile = referMobile;
	}

	public String getReferRealName() {
		return referRealName;
	}

	public void setReferRealName(String referRealName) {
		this.referRealName = referRealName;
	}

	@Length(min=0, max=500, message="备注长度必须介于 0 和 500 之间")
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