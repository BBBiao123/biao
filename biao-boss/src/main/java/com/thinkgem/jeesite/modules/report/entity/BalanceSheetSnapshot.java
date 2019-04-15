/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 资产负债表Entity
 * @author zzj
 * @version 2019-01-08
 */
public class BalanceSheetSnapshot extends DataEntity<BalanceSheetSnapshot> {
	
	private static final long serialVersionUID = 1L;
	private Date snapDate;		// 快照日期
	private String coinSymbol;		// 币种代号
	private Double totalVolume;		// 总资产
	private Double income;		// 收入累计
	private Double tradeFee;		// 币币交易手续费
	private Double depositVolume;		// 充值
	private Double offlineFee;		// c2c手续费
	private Double deductVolume;		// 手动回扣币
	private Double withdrawFee;		// 提现手续费
	private Double expense;		// 支出累计
	private Double withdrawVolume;		// 提币
	private Double withdrawBlockFee;		// 提币区块链手续费
	private Double depositAllocationFee;		// 充币归集手续费
	private Double remitVolume;		// 手动拨币
	private Double miningVolume;		// 挖矿
	private Double registerVolume;		// 注册奖励
	private Double relayVolume;		// 接力撞奖
	private String remark;		// 备注
	private Date beginSnapDate;		// 开始 快照日期
	private Date endSnapDate;		// 结束 快照日期
	
	public BalanceSheetSnapshot() {
		super();
	}

	public BalanceSheetSnapshot(String id){
		super(id);
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSnapDate() {
		return snapDate;
	}

	public void setSnapDate(Date snapDate) {
		this.snapDate = snapDate;
	}
	
	@Length(min=0, max=45, message="币种代号长度必须介于 0 和 45 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	public Double getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(Double totalVolume) {
		this.totalVolume = totalVolume;
	}
	
	public Double getIncome() {
		return income;
	}

	public void setIncome(Double income) {
		this.income = income;
	}
	
	public Double getTradeFee() {
		return tradeFee;
	}

	public void setTradeFee(Double tradeFee) {
		this.tradeFee = tradeFee;
	}
	
	public Double getDepositVolume() {
		return depositVolume;
	}

	public void setDepositVolume(Double depositVolume) {
		this.depositVolume = depositVolume;
	}
	
	public Double getOfflineFee() {
		return offlineFee;
	}

	public void setOfflineFee(Double offlineFee) {
		this.offlineFee = offlineFee;
	}
	
	public Double getDeductVolume() {
		return deductVolume;
	}

	public void setDeductVolume(Double deductVolume) {
		this.deductVolume = deductVolume;
	}
	
	public Double getWithdrawFee() {
		return withdrawFee;
	}

	public void setWithdrawFee(Double withdrawFee) {
		this.withdrawFee = withdrawFee;
	}
	
	public Double getExpense() {
		return expense;
	}

	public void setExpense(Double expense) {
		this.expense = expense;
	}
	
	public Double getWithdrawVolume() {
		return withdrawVolume;
	}

	public void setWithdrawVolume(Double withdrawVolume) {
		this.withdrawVolume = withdrawVolume;
	}
	
	public Double getWithdrawBlockFee() {
		return withdrawBlockFee;
	}

	public void setWithdrawBlockFee(Double withdrawBlockFee) {
		this.withdrawBlockFee = withdrawBlockFee;
	}
	
	public Double getDepositAllocationFee() {
		return depositAllocationFee;
	}

	public void setDepositAllocationFee(Double depositAllocationFee) {
		this.depositAllocationFee = depositAllocationFee;
	}
	
	public Double getRemitVolume() {
		return remitVolume;
	}

	public void setRemitVolume(Double remitVolume) {
		this.remitVolume = remitVolume;
	}
	
	public Double getMiningVolume() {
		return miningVolume;
	}

	public void setMiningVolume(Double miningVolume) {
		this.miningVolume = miningVolume;
	}
	
	public Double getRegisterVolume() {
		return registerVolume;
	}

	public void setRegisterVolume(Double registerVolume) {
		this.registerVolume = registerVolume;
	}
	
	public Double getRelayVolume() {
		return relayVolume;
	}

	public void setRelayVolume(Double relayVolume) {
		this.relayVolume = relayVolume;
	}
	
	@Length(min=0, max=500, message="备注长度必须介于 0 和 500 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Date getBeginSnapDate() {
		return beginSnapDate;
	}

	public void setBeginSnapDate(Date beginSnapDate) {
		this.beginSnapDate = beginSnapDate;
	}
	
	public Date getEndSnapDate() {
		return endSnapDate;
	}

	public void setEndSnapDate(Date endSnapDate) {
		this.endSnapDate = endSnapDate;
	}
		
}