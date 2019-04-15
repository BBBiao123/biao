/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 注册用户送币Entity
 * @author dongfeng
 * @version 2018-07-20
 */
public class Mk2PopularizeRegisterCoin extends DataEntity<Mk2PopularizeRegisterCoin> {
	
	private static final long serialVersionUID = 1L;
	private String registerConfId;		// 规则表ID
	private String confName;		// 活动名称
	private String userId;		// 用户ID
	private String userName;		// 用户姓名
	private Integer volume;		// 送币个数
	private String coinId;		// 送币ID
	private String coinSymbol;		// 送币名称
	private String forUserId;		// 注册用户ID
	private String status;		// 状态
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	private String mail;		// 邮箱
	private String mobile;		// 手机
	
	public Mk2PopularizeRegisterCoin() {
		super();
	}

	public Mk2PopularizeRegisterCoin(String id){
		super(id);
	}

	@Length(min=1, max=64, message="规则表ID长度必须介于 1 和 64 之间")
	public String getRegisterConfId() {
		return registerConfId;
	}

	public void setRegisterConfId(String registerConfId) {
		this.registerConfId = registerConfId;
	}
	
	@Length(min=1, max=50, message="活动名称长度必须介于 1 和 50 之间")
	public String getConfName() {
		return confName;
	}

	public void setConfName(String confName) {
		this.confName = confName;
	}
	
	@Length(min=1, max=64, message="用户ID长度必须介于 1 和 64 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=1, max=45, message="用户姓名长度必须介于 1 和 45 之间")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@NotNull(message="送币个数不能为空")
	public Integer getVolume() {
		return volume;
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	
	@Length(min=1, max=64, message="送币ID长度必须介于 1 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=1, max=64, message="送币名称长度必须介于 1 和 64 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	@Length(min=0, max=64, message="注册用户ID长度必须介于 0 和 64 之间")
	public String getForUserId() {
		return forUserId;
	}

	public void setForUserId(String forUserId) {
		this.forUserId = forUserId;
	}
	
	@Length(min=1, max=1, message="状态长度必须介于 1 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
}