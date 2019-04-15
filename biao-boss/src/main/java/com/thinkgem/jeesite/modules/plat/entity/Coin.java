
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;

/**
 * 币种资料Entity
 * @author dazi
 * @version 2018-04-25
 */
public class Coin extends DataEntity<Coin> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 交易代号
	private String fullName;		// 币种全称
	private String domain;		// 官网
	private String whitepaperUrl;		// 白皮书地址
	private String tokenVolume;		// token 总量
	private String icoPrice;		// ico 价格
	private String circulateVolume;		// 流通总量
	private String tokenStatus;		// 充值提现状态  9：不可以充值提现 1 ：可以充值 可以提现 2：仅可以充值 3：仅可以提现
	private String status;		// 状态 9 ：下架0：发布中 1：上架
	private BigDecimal exMinVolume;		// 最低挂单数量
	private BigDecimal withdrawMinVolume;		// 一次提现最低数量
	private BigDecimal withdrawMaxVolume;		// 一次提现最大数量
	private BigDecimal withdrawDayMaxVolume;		// 一天最大提现额度
	private BigDecimal withdrawFee;		// 提现手续费
	private String withdrawFeeType;		// 提现手续费
	private String coinType;		// 1:基于以太  2:基于量子 3：基于小蚂 4：基于EOS
	private String iconId;		// 币种图标
	private String contractAddress;
	private String parentId;
	
	public Coin() {
		super();
	}

	public Coin(String id){
		super(id);
	}
	
	public Coin(String id,String name){
		this.id = id;
		this.name = name ;
	}

	@Length(min=1, max=10, message="交易代号长度必须介于 1 和 10 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=24, message="币种全称长度必须介于 1 和 24 之间")
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	@Length(min=0, max=24, message="官网长度必须介于 0 和 24 之间")
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	@Length(min=0, max=64, message="白皮书地址长度必须介于 0 和 64 之间")
	public String getWhitepaperUrl() {
		return whitepaperUrl;
	}

	public void setWhitepaperUrl(String whitepaperUrl) {
		this.whitepaperUrl = whitepaperUrl;
	}
	
	@Length(min=0, max=24, message="token 总量长度必须介于 0 和 24 之间")
	public String getTokenVolume() {
		return tokenVolume;
	}

	public void setTokenVolume(String tokenVolume) {
		this.tokenVolume = tokenVolume;
	}
	
	@Length(min=0, max=45, message="ico 价格长度必须介于 0 和 45 之间")
	public String getIcoPrice() {
		return icoPrice;
	}

	public void setIcoPrice(String icoPrice) {
		this.icoPrice = icoPrice;
	}
	
	@Length(min=0, max=45, message="流通总量长度必须介于 0 和 45 之间")
	public String getCirculateVolume() {
		return circulateVolume;
	}

	public void setCirculateVolume(String circulateVolume) {
		this.circulateVolume = circulateVolume;
	}
	
	@Length(min=0, max=1, message="充值提现状态  9：不可以充值提现 1 ：可以充值 可以提现 2：仅可以充值 3：仅可以提现长度必须介于 0 和 1 之间")
	public String getTokenStatus() {
		return tokenStatus;
	}

	public void setTokenStatus(String tokenStatus) {
		this.tokenStatus = tokenStatus;
	}
	
	@Length(min=0, max=1, message="状态 9 ：下架0：发布中 1：上架长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public BigDecimal getExMinVolume() {
		return exMinVolume;
	}

	public void setExMinVolume(BigDecimal exMinVolume) {
		this.exMinVolume = exMinVolume;
	}

	public BigDecimal getWithdrawMinVolume() {
		return withdrawMinVolume;
	}

	public void setWithdrawMinVolume(BigDecimal withdrawMinVolume) {
		this.withdrawMinVolume = withdrawMinVolume;
	}

	public BigDecimal getWithdrawMaxVolume() {
		return withdrawMaxVolume;
	}

	public void setWithdrawMaxVolume(BigDecimal withdrawMaxVolume) {
		this.withdrawMaxVolume = withdrawMaxVolume;
	}

	public BigDecimal getWithdrawDayMaxVolume() {
		return withdrawDayMaxVolume;
	}

	public void setWithdrawDayMaxVolume(BigDecimal withdrawDayMaxVolume) {
		this.withdrawDayMaxVolume = withdrawDayMaxVolume;
	}

	public BigDecimal getWithdrawFee() {
		return withdrawFee;
	}

	public void setWithdrawFee(BigDecimal withdrawFee) {
		this.withdrawFee = withdrawFee;
	}

	public String getWithdrawFeeType() {
		return withdrawFeeType;
	}

	public void setWithdrawFeeType(String withdrawFeeType) {
		this.withdrawFeeType = withdrawFeeType;
	}

	@Length(min=0, max=1, message="1:基于以太  2:基于量子 3：基于小蚂 4：基于EOS长度必须介于 0 和 1 之间")
	public String getCoinType() {
		return coinType;
	}

	public void setCoinType(String coinType) {
		this.coinType = coinType;
	}
	
	@Length(min=0, max=45, message="币种图标长度必须介于 0 和 45 之间")
	public String getIconId() {
		return iconId;
	}

	public void setIconId(String iconId) {
		this.iconId = iconId;
	}


	public String getContractAddress() {
		return contractAddress;
	}

	public void setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}