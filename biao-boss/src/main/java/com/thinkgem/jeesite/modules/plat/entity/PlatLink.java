/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 平台链接Entity
 * @author dongfeng
 * @version 2018-07-03
 */
public class PlatLink extends DataEntity<PlatLink> {
	
	private static final long serialVersionUID = 1L;
	private String typeid;		// 类型
	private String linkurl;		// URL
	private String linkimage;		// 图片
	private String showOrder;		// 展示顺序
	private String createby;		// 创建人
	
	public PlatLink() {
		super();
	}

	public PlatLink(String id){
		super(id);
	}

	@Length(min=1, max=100, message="类型长度必须介于 1 和 100 之间")
	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}
	
	@Length(min=1, max=1500, message="URL长度必须介于 1 和 1500 之间")
	public String getLinkurl() {
		return linkurl;
	}

	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}
	
	@Length(min=0, max=1000, message="图片长度必须介于 0 和 1000 之间")
	public String getLinkimage() {
		return linkimage;
	}

	public void setLinkimage(String linkimage) {
		this.linkimage = linkimage;
	}
	
	@Length(min=1, max=3, message="展示顺序长度必须介于 1 和 3 之间")
	public String getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(String showOrder) {
		this.showOrder = showOrder;
	}
	
	@Length(min=1, max=200, message="创建人长度必须介于 1 和 200 之间")
	public String getCreateby() {
		return createby;
	}

	public void setCreateby(String createby) {
		this.createby = createby;
	}
	
}