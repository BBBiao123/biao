/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import com.thinkgem.jeesite.modules.sys.entity.Area;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 区域销售Entity
 * @author dong
 * @version 2018-07-06
 */
public class AreaSell extends DataEntity<AreaSell> {
	
	private static final long serialVersionUID = 1L;
	private Area area;		// 区域码
	private String areaName;		// 区域名称
	private String areaParaentId;		// 上级区域码
	private String areaParaentName;		// 上级区域名称
	private String sellPrice;		// 卖价
	private String sold;		// 售出
	
	public AreaSell() {
		super();
	}

	public AreaSell(String id){
		super(id);
	}

	@NotNull(message="区域码不能为空")
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
	@Length(min=1, max=64, message="区域名称长度必须介于 1 和 64 之间")
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
	@Length(min=1, max=64, message="上级区域码长度必须介于 1 和 64 之间")
	public String getAreaParaentId() {
		return areaParaentId;
	}

	public void setAreaParaentId(String areaParaentId) {
		this.areaParaentId = areaParaentId;
	}
	
	@Length(min=0, max=64, message="上级区域名称长度必须介于 0 和 64 之间")
	public String getAreaParaentName() {
		return areaParaentName;
	}

	public void setAreaParaentName(String areaParaentName) {
		this.areaParaentName = areaParaentName;
	}
	
	public String getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(String sellPrice) {
		this.sellPrice = sellPrice;
	}
	
	@Length(min=1, max=1, message="售出长度必须介于 1 和 1 之间")
	public String getSold() {
		return sold;
	}

	public void setSold(String sold) {
		this.sold = sold;
	}
	
}