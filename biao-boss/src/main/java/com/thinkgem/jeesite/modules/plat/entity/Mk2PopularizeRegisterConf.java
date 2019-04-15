/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 注册送币规则Entity
 * @author dongfeng
 * @version 2018-07-20
 */
public class Mk2PopularizeRegisterConf extends DataEntity<Mk2PopularizeRegisterConf> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 推广名称
	private String coinId;		// 币ID
	private String coinSymbol;		// 币名称
	private String status;		// 状态
	private Integer registerVolume;		// 注册送币个数
	private Integer referVolume;		// 推荐送币个数
	private Long totalVolume;		// 总送币量
	private Long giveVolume = 0L;		// 已送币量
	private String updateby;		// 修改人
	private String remark;		// 描述
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间

	public Mk2PopularizeRegisterConf() {
		super();
	}

	public Mk2PopularizeRegisterConf(String id){
		super(id);
	}

	@Length(min=1, max=50, message="推广名称长度必须介于 1 和 50 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=64, message="币ID长度必须介于 1 和 64 之间")
	public String getCoinId() {
		return coinId;
	}

	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	@Length(min=1, max=64, message="币名称长度必须介于 1 和 64 之间")
	public String getCoinSymbol() {
		return coinSymbol;
	}

	public void setCoinSymbol(String coinSymbol) {
		this.coinSymbol = coinSymbol;
	}
	
	@Length(min=1, max=1, message="状态长度必须介于 1 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@NotNull(message="注册送币个数不能为空")
	public Integer getRegisterVolume() {
		return registerVolume;
	}

	public void setRegisterVolume(Integer registerVolume) {
		this.registerVolume = registerVolume;
	}
	
	@NotNull(message="推荐送币个数不能为空")
	public Integer getReferVolume() {
		return referVolume;
	}

	public void setReferVolume(Integer referVolume) {
		this.referVolume = referVolume;
	}
	
	@NotNull(message="总送币量不能为空")
	public Long getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(Long totalVolume) {
		this.totalVolume = totalVolume;
	}
	
	public Long getGiveVolume() {
		return giveVolume;
	}

	public void setGiveVolume(Long giveVolume) {
		this.giveVolume = giveVolume;
	}
	
	@Length(min=1, max=100, message="修改人长度必须介于 1 和 100 之间")
	public String getUpdateby() {
		return updateby;
	}

	public void setUpdateby(String updateby) {
		this.updateby = updateby;
	}
	
	@Length(min=0, max=200, message="描述长度必须介于 0 和 200 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = beginCreateDate;
	}
	
	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}

}