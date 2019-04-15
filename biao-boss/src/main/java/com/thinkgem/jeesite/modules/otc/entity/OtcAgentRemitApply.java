/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.otc.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

/**
 * 银商拨币申请列表Entity
 * @author zzj
 * @version 2018-09-18
 */
public class OtcAgentRemitApply extends DataEntity<OtcAgentRemitApply> {
	
	private static final long serialVersionUID = 1L;
	private String agentId;		// 银商ID
	private String agentName;		// 银商名称
	private String status;		// 状态
	private String coinId;		// 币种id
	private String coinSymbol;		// 币种代号
	private Double discount;		// 折扣
	private String discountPercentage; //折扣百分比
	private String payCoinType;		// 支付币种
	private String transferTo;		// usdt转入地址
	private String transferOut;		// usdt转出地址
	private Double usdtRate;		// USDT汇率
	private Double tradeCoinRate;		// 交易币种汇率
	private Double volume;		// 数量
	private Double applyVolume;		// 申请数量
	private Double remitVolume;		// 拨币数量
	private String financeAuditComment;		// 财务审核意见
	private String marketAuditComment;		// 运营审核意见
	private String remark;		// 说明
	private String createByName;		// create_by_name
	private String updateByName;		// update_by_name
	private Date beginCreateDate;		// 开始 创建日期
	private Date endCreateDate;		// 结束 创建日期
	private String[] userIds; 	//会员ID
	private String[] percentages; //会员拨币占比
	
	public OtcAgentRemitApply() {
		super();
	}

	public OtcAgentRemitApply(String id){
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
	
	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
	
	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getDiscountPercentage() {
		return String.valueOf(this.getDiscount() * Double.valueOf("100")).concat("%");
	}

	public void setDiscountPercentage(String discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	@Length(min=0, max=1, message="支付币种长度必须介于 0 和 1 之间")
	public String getPayCoinType() {
		return payCoinType;
	}

	public void setPayCoinType(String payCoinType) {
		this.payCoinType = payCoinType;
	}
	
	@Length(min=0, max=64, message="usdt转入地址长度必须介于 0 和 64 之间")
	public String getTransferTo() {
		return transferTo;
	}

	public void setTransferTo(String transferTo) {
		this.transferTo = transferTo;
	}
	
	@Length(min=0, max=64, message="usdt转出地址长度必须介于 0 和 64 之间")
	public String getTransferOut() {
		return transferOut;
	}

	public void setTransferOut(String transferOut) {
		this.transferOut = transferOut;
	}

	@DecimalMin(value = "5.00",message ="资金比例必须在5,10之间" )
	@DecimalMax(value = "10.00",message ="资金比例必须在5,10之间")
	public Double getUsdtRate() {
		return usdtRate;
	}

	public void setUsdtRate(Double usdtRate) {
		this.usdtRate = usdtRate;
	}
	
	public Double getTradeCoinRate() {
		return tradeCoinRate;
	}

	public void setTradeCoinRate(Double tradeCoinRate) {
		this.tradeCoinRate = tradeCoinRate;
	}
	
	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}
	
	public Double getApplyVolume() {
		return applyVolume;
	}

	public void setApplyVolume(Double applyVolume) {
		this.applyVolume = applyVolume;
	}
	
	public Double getRemitVolume() {
		return remitVolume;
	}

	public void setRemitVolume(Double remitVolume) {
		this.remitVolume = remitVolume;
	}
	
	@Length(min=0, max=200, message="财务审核意见长度必须介于 0 和 200 之间")
	public String getFinanceAuditComment() {
		return financeAuditComment;
	}

	public void setFinanceAuditComment(String financeAuditComment) {
		this.financeAuditComment = financeAuditComment;
	}
	
	@Length(min=0, max=200, message="运营审核意见长度必须介于 0 和 200 之间")
	public String getMarketAuditComment() {
		return marketAuditComment;
	}

	public void setMarketAuditComment(String marketAuditComment) {
		this.marketAuditComment = marketAuditComment;
	}
	
	@Length(min=0, max=500, message="说明长度必须介于 0 和 500 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Length(min=0, max=64, message="create_by_name长度必须介于 0 和 64 之间")
	public String getCreateByName() {
		return createByName;
	}

	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}
	
	@Length(min=0, max=64, message="update_by_name长度必须介于 0 和 64 之间")
	public String getUpdateByName() {
		return updateByName;
	}

	public void setUpdateByName(String updateByName) {
		this.updateByName = updateByName;
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

	public String[] getUserIds() {
		return userIds;
	}

	public void setUserIds(String[] userIds) {
		this.userIds = userIds;
	}

	public String[] getPercentages() {
		return percentages;
	}

	public void setPercentages(String[] percentages) {
		this.percentages = percentages;
	}
}