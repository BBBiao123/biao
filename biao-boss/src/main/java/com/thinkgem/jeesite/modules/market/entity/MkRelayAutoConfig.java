/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 接力自动撞奖配置Entity
 * @author zzj
 * @version 2018-09-26
 */
public class MkRelayAutoConfig extends DataEntity<MkRelayAutoConfig> {
	
	private static final long serialVersionUID = 1L;
	private String status;		// 状态
	private Integer startRewardNumber;		// 禁用状态下，中奖次数重启
	private String remark;		// 说明
	
	public MkRelayAutoConfig() {
		super();
	}

	public MkRelayAutoConfig(String id){
		super(id);
	}

	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getStartRewardNumber() {
		return startRewardNumber;
	}

	public void setStartRewardNumber(Integer startRewardNumber) {
		this.startRewardNumber = startRewardNumber;
	}

	@Length(min=0, max=500, message="说明长度必须介于 0 和 500 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}