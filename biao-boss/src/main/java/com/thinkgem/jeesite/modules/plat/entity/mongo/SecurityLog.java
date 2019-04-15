package com.thinkgem.jeesite.modules.plat.entity.mongo;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * 安全日志
 * @author zhoury
 *
 */
@Document
public class SecurityLog {

	@Id  // 主键
	private String id ;
	
	/**
	 * 安全日志类型   1:修改密码   3:重置密码  2:绑定手机号  4:修改手机号  5:绑定谷歌  6:绑定交易密码  7:修改交易密码   8:切换交易类型  9:设置交易类型  
	 */
	private String type ;
	
	private String userId ;
	
	private String mobile ;
	
	private String mail ;
	
	private LocalDateTime updateTime ;
	
	private String updateTimeStr ;
	
	private String remark ;
	
	//登录状态 0:成功 1:失败
	private Integer status ;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUpdateTimeStr() {
		return updateTimeStr;
	}

	public void setUpdateTimeStr(String updateTimeStr) {
		this.updateTimeStr = updateTimeStr;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
