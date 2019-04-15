/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 币种资产风控管理Entity
 * @author zzj
 * @version 2019-01-18
 */
public class JsPlatCoinVolumeRiskMgt extends DataEntity<JsPlatCoinVolumeRiskMgt> {
	
	private static final long serialVersionUID = 1L;
	private String coinId;		// 币种id
	private String coinSymbol;		// 币种代号
	private Double volume;		// 风控阀值
	private String remark;		// 备注
	
	public JsPlatCoinVolumeRiskMgt() {
		super();
	}

	public JsPlatCoinVolumeRiskMgt(String id){
		super(id);
	}

	@Length(min=1, max=64, message="币种id长度必须介于 1 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=1, max=45, message="币种代号长度必须介于 1 和 45 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	@NotNull(message="风控阀值不能为空")
	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}
	
	@Length(min=0, max=500, message="备注长度必须介于 0 和 500 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}