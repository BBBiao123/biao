/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 分红统计Entity
 * @author zhangzijun
 * @version 2018-08-02
 */
public class MkDividendStat extends DataEntity<MkDividendStat> {
	
	private static final long serialVersionUID = 1L;
	private Date statDate;		// 任务日期
	private String coinId;		// 平台币种ID
	private String coinSymbol;		// 平台币符号
	private String volume;		// 平台币总量
	private String usdtVolume;		// USDT手续费
	private String btcVolume;		// BTC手续费
	private String ethVolume;		// ETH手续费
	private String usdtRealVolume;		// USDT实际分红
	private String btcRealVolume;		// BTC实际分红
	private String ethRealVolume;		// ETH实际分红
	private String usdtPerVolume;		// USDT/(1000平台币)
	private String btcPerVolume;		// BTC/(1000平台币)
	private String ethPerVolume;		// ETH/(1000平台币)
	private String per;		// 平台币
	private String remark;		// 备注
	private Date beginTaskDate;		// 开始 任务日期
	private Date endTaskDate;		// 结束 任务日期
	
	public MkDividendStat() {
		super();
	}

	public MkDividendStat(String id){
		super(id);
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStatDate() {
		return statDate;
	}

	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}



	
	@Length(min=0, max=64, message="平台币种ID长度必须介于 0 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=0, max=64, message="平台币符号长度必须介于 0 和 64 之间")
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
	
	public String getUsdtVolume() {
		return usdtVolume;
	}

	public void setUsdtVolume(String usdtVolume) {
		this.usdtVolume = usdtVolume;
	}
	
	public String getBtcVolume() {
		return btcVolume;
	}

	public void setBtcVolume(String btcVolume) {
		this.btcVolume = btcVolume;
	}
	
	public String getEthVolume() {
		return ethVolume;
	}

	public void setEthVolume(String ethVolume) {
		this.ethVolume = ethVolume;
	}
	
	public String getUsdtRealVolume() {
		return usdtRealVolume;
	}

	public void setUsdtRealVolume(String usdtRealVolume) {
		this.usdtRealVolume = usdtRealVolume;
	}
	
	public String getBtcRealVolume() {
		return btcRealVolume;
	}

	public void setBtcRealVolume(String btcRealVolume) {
		this.btcRealVolume = btcRealVolume;
	}
	
	public String getEthRealVolume() {
		return ethRealVolume;
	}

	public void setEthRealVolume(String ethRealVolume) {
		this.ethRealVolume = ethRealVolume;
	}
	
	public String getUsdtPerVolume() {
		return usdtPerVolume;
	}

	public void setUsdtPerVolume(String usdtPerVolume) {
		this.usdtPerVolume = usdtPerVolume;
	}
	
	public String getBtcPerVolume() {
		return btcPerVolume;
	}

	public void setBtcPerVolume(String btcPerVolume) {
		this.btcPerVolume = btcPerVolume;
	}
	
	public String getEthPerVolume() {
		return ethPerVolume;
	}

	public void setEthPerVolume(String ethPerVolume) {
		this.ethPerVolume = ethPerVolume;
	}
	
	public String getPer() {
		return per;
	}

	public void setPer(String per) {
		this.per = per;
	}
	
	@Length(min=0, max=500, message="备注长度必须介于 0 和 500 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Date getBeginTaskDate() {
		return beginTaskDate;
	}

	public void setBeginTaskDate(Date beginTaskDate) {
		this.beginTaskDate = beginTaskDate;
	}
	
	public Date getEndTaskDate() {
		return endTaskDate;
	}

	public void setEndTaskDate(Date endTaskDate) {
		this.endTaskDate = endTaskDate;
	}
		
}