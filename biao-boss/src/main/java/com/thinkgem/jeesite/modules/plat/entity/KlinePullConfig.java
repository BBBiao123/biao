/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 币安k线配置Entity
 * @author xiaoyu
 * @version 2018-12-25
 */
public class KlinePullConfig extends DataEntity<KlinePullConfig> {
	
	private static final long serialVersionUID = 1L;
	private String coinMain;		// 被交易的币种符号
	private String coinOther;		// 交易区符号
	private String exchangeName;		// 拉取交易所名称
	private String pullUrl;		// 交易所url
	private String proxyed;		// 是否使用代理
	private String status;		// 0 未启用 1 启用
	
	public KlinePullConfig() {
		super();
	}

	public KlinePullConfig(String id){
		super(id);
	}

	@Length(min=1, max=24, message="被交易的币种符号长度必须介于 1 和 24 之间")
	public String getCoinMain() {
		return coinMain;
	}

	public void setCoinMain(String coinMain) {
		this.coinMain = coinMain;
	}
	
	@Length(min=1, max=64, message="交易区符号长度必须介于 1 和 64 之间")
	public String getCoinOther() {
		return coinOther;
	}

	public void setCoinOther(String coinOther) {
		this.coinOther = coinOther;
	}
	
	@Length(min=0, max=100, message="拉取交易所名称长度必须介于 0 和 100 之间")
	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}
	
	@Length(min=0, max=100, message="交易所url长度必须介于 0 和 100 之间")
	public String getPullUrl() {
		return pullUrl;
	}

	public void setPullUrl(String pullUrl) {
		this.pullUrl = pullUrl;
	}
	
	@Length(min=1, max=4, message="是否使用代理长度必须介于 1 和 4 之间")
	public String getProxyed() {
		return proxyed;
	}

	public void setProxyed(String proxyed) {
		this.proxyed = proxyed;
	}
	
	@Length(min=0, max=4, message="0 未启用 1 启用长度必须介于 0 和 4 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}