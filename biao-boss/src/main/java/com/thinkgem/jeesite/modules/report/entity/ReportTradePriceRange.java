/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;

import java.util.Date;

/**
 * ddEntity
 * @author zzj
 * @version 2018-12-14
 */
public class ReportTradePriceRange extends DataEntity<ReportTradePriceRange> {

	private static final long serialVersionUID = 1L;
	private String type;		// 类型，0-买， 1-卖
	private String priceRange;		// 价格区间
	private Double volume;		// 委托数量

	public ReportTradePriceRange() {
		super();
	}

	public ReportTradePriceRange(String id){
		super(id);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPriceRange() {
		return priceRange;
	}

	public void setPriceRange(String priceRange) {
		this.priceRange = priceRange;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}
}