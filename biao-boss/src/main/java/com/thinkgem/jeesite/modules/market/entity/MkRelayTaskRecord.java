/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 接力撞奖执行记录Entity
 * @author zzj
 * @version 2018-09-05
 */
public class MkRelayTaskRecord extends DataEntity<MkRelayTaskRecord> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 类型
	private String status;		// 状态
	private Date beginDate;		// 执行时间
	private Date endDate;		// 任务结束时间
	private String increaseNumber;		// 新增人数
	private String increaseVolume;		// 新增奖金数
	private String poolVolume;		// 奖池总量
	private String prizeNumber;		// 获奖人数
	private String prizeVolume;		// 获奖数量
	private String coinId;		// 币种ID
	private String coinSymbol;		// 币种
	private String remark;		// 说明
	private Date beginBeginDate;		// 开始 执行时间
	private Date endBeginDate;		// 结束 执行时间
	
	public MkRelayTaskRecord() {
		super();
	}

	public MkRelayTaskRecord(String id){
		super(id);
	}

	@Length(min=0, max=1, message="类型长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Length(min=0, max=10, message="新增人数长度必须介于 0 和 10 之间")
	public String getIncreaseNumber() {
		return increaseNumber;
	}

	public void setIncreaseNumber(String increaseNumber) {
		this.increaseNumber = increaseNumber;
	}
	
	public String getIncreaseVolume() {
		return increaseVolume;
	}

	public void setIncreaseVolume(String increaseVolume) {
		this.increaseVolume = increaseVolume;
	}
	
	public String getPoolVolume() {
		return poolVolume;
	}

	public void setPoolVolume(String poolVolume) {
		this.poolVolume = poolVolume;
	}
	
	@Length(min=0, max=10, message="获奖人数长度必须介于 0 和 10 之间")
	public String getPrizeNumber() {
		return prizeNumber;
	}

	public void setPrizeNumber(String prizeNumber) {
		this.prizeNumber = prizeNumber;
	}
	
	public String getPrizeVolume() {
		return prizeVolume;
	}

	public void setPrizeVolume(String prizeVolume) {
		this.prizeVolume = prizeVolume;
	}
	
	@Length(min=0, max=64, message="币种ID长度必须介于 0 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=1, max=64, message="币种长度必须介于 1 和 64 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	@Length(min=0, max=500, message="说明长度必须介于 0 和 500 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Date getBeginBeginDate() {
		return beginBeginDate;
	}

	public void setBeginBeginDate(Date beginBeginDate) {
		this.beginBeginDate = beginBeginDate;
	}
	
	public Date getEndBeginDate() {
		return endBeginDate;
	}

	public void setEndBeginDate(Date endBeginDate) {
		this.endBeginDate = endBeginDate;
	}
		
}