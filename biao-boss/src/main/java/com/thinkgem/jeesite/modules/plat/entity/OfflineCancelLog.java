/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * C2C取消记录Entity
 * @author zzj
 * @version 2018-11-06
 */
public class OfflineCancelLog extends DataEntity<OfflineCancelLog> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 类型
	private Date date;		// date
	private String userId;		// user_id
	
	public OfflineCancelLog() {
		super();
	}

	public OfflineCancelLog(String id){
		super(id);
	}

	@Length(min=0, max=1, message="类型长度必须介于 0 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	@Length(min=0, max=255, message="user_id长度必须介于 0 和 255 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}