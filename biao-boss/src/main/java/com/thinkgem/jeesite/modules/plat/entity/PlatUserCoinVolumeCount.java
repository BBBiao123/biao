/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 持币数量统计Entity
 * @author dongfeng
 * @version 2018-09-04
 */
public class PlatUserCoinVolumeCount extends DataEntity<PlatUserCoinVolumeCount> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 类型
	private String typeDesc;	// 类型描述
	private Long personCount;		// 人数
	private BigDecimal holdCoinVolume;		// 持币数量
	private Date countDate;
	private String countDateStr;
	private Date beginDate;		// 统计开始时间
	private Date endDate;		// 统计结束时间
	
	public PlatUserCoinVolumeCount() {
		super();
	}

	public PlatUserCoinVolumeCount(String id){
		super(id);
	}

	@Length(min=0, max=64, message="类型长度必须介于 0 和 64 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public Long getPersonCount() {
		return personCount;
	}

	public void setPersonCount(Long personCount) {
		this.personCount = personCount;
	}

	public BigDecimal getHoldCoinVolume() {
//		if (holdCoinVolume != null) {
//			return holdCoinVolume.setScale(8, BigDecimal.ROUND_DOWN);
//		}
		return holdCoinVolume;
	}

	public void setHoldCoinVolume(BigDecimal holdCoinVolume) {
		this.holdCoinVolume = holdCoinVolume;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getCountDate() {
		return countDate;
	}

	public void setCountDate(Date countDate) {
		this.countDate = countDate;
	}

	public String getCountDateStr() {
		return countDateStr;
	}

	public void setCountDateStr(String countDateStr) {
		this.countDateStr = countDateStr;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
}