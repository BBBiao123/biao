/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 会员推广明细Entity
 * @author zhangzijun
 * @version 2018-07-20
 */
public class MkPromoteDetail extends DataEntity<MkPromoteDetail> {
	
	private static final long serialVersionUID = 1L;
	private String promoteId;		// 分红规则ID
	private String volume;		// 奖励数量
	private String level;		// 层级
	
	public MkPromoteDetail() {
		super();
	}

	public MkPromoteDetail(String id){
		super(id);
	}

	@Length(min=0, max=64, message="分红规则ID长度必须介于 0 和 64 之间")
	public String getPromoteId() {
		return promoteId;
	}

	public void setPromoteId(String promoteId) {
		this.promoteId = promoteId;
	}
	
	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}
	
	@Length(min=1, max=2, message="层级长度必须介于 1 和 2 之间")
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
}