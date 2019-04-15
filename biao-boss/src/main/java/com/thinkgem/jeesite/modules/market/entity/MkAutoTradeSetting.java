/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 自动交易Entity
 * @author zhangzijun
 * @version 2018-08-07
 */
public class MkAutoTradeSetting extends DataEntity<MkAutoTradeSetting> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 买卖类型
	private String status;		// 状态
	private String userId;		// 用户ID
	private String username;		// 名称
	private String mail;		// 邮箱
	private String mobile;		// 手机
	private String realName;		// 真实姓名
	private String idCard;		// 身份证
	private String password;  // 加密密码
	private String pass; 		//原生密码
	private String exPairId;		// 币币对ID
	private String exPairSymbol;		// 币币对
	private String coinMainId;		// 主区币ID
	private String coinMainSymbol;		// 主区币
	private String coinOtherId;		// 被交易币ID
	private String coinOtherSymbol;		// 被交易币
	private Date beginDate;		// 开始日期
	private Date endDate;		// 结束日期
	private String minVolume;		// 最小成交量
	private String maxVolume;		// 最大成交量
	private String minPrice;		// 最低价
	private String maxPrice;		// 最高价
	private Integer pricePrecision;
	private Integer volumePrecision;
	private String exMinVolume;		// 交易最小成交量
	private String exMaxVolume;		// 交易最大成交量
	private Integer frequency;		// 频率
	private String timeUnit;		// 时间单位
	private String remark;		// 说明
	private String createByName;		// 创建人名称
	private String updateByName;		// 创建人名称
	private String grantSysUserType; //
	private String platUserDialogUrl;
	
	public MkAutoTradeSetting() {
		super();
	}

	public MkAutoTradeSetting(String id){
		super(id);
	}

	@Length(min=0, max=1, message="买卖类型长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getExPairId() {
		return exPairId;
	}

	public void setExPairId(String exPairId) {
		this.exPairId = exPairId;
	}

	public String getExPairSymbol() {
		return exPairSymbol;
	}

	public void setExPairSymbol(String exPairSymbol) {
		this.exPairSymbol = exPairSymbol;
	}

	@Length(min=0, max=64, message="主区币ID长度必须介于 0 和 64 之间")
	public String getCoinMainId() {
		return coinMainId;
	}

	public void setCoinMainId(String coinMainId) {
		this.coinMainId = coinMainId;
	}
	
	@Length(min=0, max=64, message="主区币长度必须介于 0 和 64 之间")
	public String getCoinMainSymbol() {
		return coinMainSymbol;
	}

	public void setCoinMainSymbol(String coinMainSymbol) {
		this.coinMainSymbol = coinMainSymbol;
	}
	
	@Length(min=0, max=64, message="被交易币ID长度必须介于 0 和 64 之间")
	public String getCoinOtherId() {
		return coinOtherId;
	}

	public void setCoinOtherId(String coinOtherId) {
		this.coinOtherId = coinOtherId;
	}
	
	@Length(min=0, max=64, message="被交易币长度必须介于 0 和 64 之间")
	public String getCoinOtherSymbol() {
		return coinOtherSymbol;
	}

	public void setCoinOtherSymbol(String coinOtherSymbol) {
		this.coinOtherSymbol = coinOtherSymbol;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getMinVolume() {
		return minVolume;
	}

	public void setMinVolume(String minVolume) {
		this.minVolume = minVolume;
	}
	
	public String getMaxVolume() {
		return maxVolume;
	}

	public void setMaxVolume(String maxVolume) {
		this.maxVolume = maxVolume;
	}
	
	public String getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}
	
	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}
	
	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	
	@Length(min=0, max=2, message="时间单位长度必须介于 0 和 12 之间")
	public String getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}
	
	@Length(min=0, max=500, message="说明长度必须介于 0 和 500 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public Integer getPricePrecision() {
		return pricePrecision;
	}

	public void setPricePrecision(Integer pricePrecision) {
		this.pricePrecision = pricePrecision;
	}

	public Integer getVolumePrecision() {
		return volumePrecision;
	}

	public void setVolumePrecision(Integer volumePrecision) {
		this.volumePrecision = volumePrecision;
	}

	public String getCreateByName() {
		return createByName;
	}

	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}

	public String getUpdateByName() {
		return updateByName;
	}

	public void setUpdateByName(String updateByName) {
		this.updateByName = updateByName;
	}

	public String getGrantSysUserType() {
		return grantSysUserType;
	}

	public void setGrantSysUserType(String grantSysUserType) {
		this.grantSysUserType = grantSysUserType;
	}

	public String getPlatUserDialogUrl() {
		return platUserDialogUrl;
	}

	public void setPlatUserDialogUrl(String platUserDialogUrl) {
		this.platUserDialogUrl = platUserDialogUrl;
	}

	public String getExMinVolume() {
		return exMinVolume;
	}

	public void setExMinVolume(String exMinVolume) {
		this.exMinVolume = exMinVolume;
	}

	public String getExMaxVolume() {
		return exMaxVolume;
	}

	public void setExMaxVolume(String exMaxVolume) {
		this.exMaxVolume = exMaxVolume;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
}