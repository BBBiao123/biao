/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * ddEntity
 * @author ruoyu
 * @version 2018-06-26
 */
public class ReportTradeFee extends DataEntity<ReportTradeFee> {
	
	private static final long serialVersionUID = 1L;
	private String coinMain;		// 交易主币
	private String coinOther;		// 交易副币
	private String sumFee;		// 手续费总量
	private Date countTime;		// 统计时间
	private String coin;		// 手续费币种
	private Date createTime;		// 记录创建时间
	
	public ReportTradeFee() {
		super();
	}

	public ReportTradeFee(String id){
		super(id);
	}

	@Length(min=0, max=32, message="交易主币长度必须介于 0 和 32 之间")
	public String getCoinMain() {
		return coinMain;
	}

	public void setCoinMain(String coinMain) {
		this.coinMain = coinMain;
	}
	
	@Length(min=0, max=32, message="交易副币长度必须介于 0 和 32 之间")
	public String getCoinOther() {
		return coinOther;
	}

	public void setCoinOther(String coinOther) {
		this.coinOther = coinOther;
	}
	
	public String getSumFee() {
		return sumFee;
	}

	public void setSumFee(String sumFee) {
		this.sumFee = sumFee;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCountTime() {
		return countTime;
	}

	public void setCountTime(Date countTime) {
		this.countTime = countTime;
	}
	
	@Length(min=0, max=32, message="手续费币种长度必须介于 0 和 32 之间")
	public String getCoin() {
		return coin;
	}

	public void setCoin(String coin) {
		this.coin = coin;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}