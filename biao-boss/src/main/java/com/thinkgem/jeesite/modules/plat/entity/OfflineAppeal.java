/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 申诉Entity
 * @author dongfeng
 * @version 2018-06-29
 */
public class OfflineAppeal extends DataEntity<OfflineAppeal> {
	
	private static final long serialVersionUID = 1L;
	private String appealUserId;		// 申诉人
	private String appealMail;
	private String appealMobile;
	private String appealRealName;
	private String appealIdCard;
	private String subOrderId;		// 订单号
	private String sellUserId;		// 卖家
	private String sellMail;
	private String sellMobile;
	private String sellRealName;
	private String sellIdCard;
	private String sellWechat;
	private String sellAlipay;
	private String sellBankName;
	private String sellBankNo;
	private String buyUserId;		// 买家
	private String buyMail;
	private String buyMobile;
	private String buyRealName;
	private String buyIdCard;
	private String examineUserId;		// 审核人
	private String status;		// 状态
	private String appealType;		// 原因类型
	private String reason;		// 原因描述
	private String imagePath;		// 证件图片1
	private String imagePath2;		// 证件图片2
	private String imagePath3;		// 证件图片3
	private Date examineDate;		// 审核时间
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	private Date beginExamineDate;		// 开始 审核时间
	private Date endExamineDate;		// 结束 审核时间
	private String examineResultUserId;// 判诉结果用户ID
	private String examineResultUserName;// 判诉结果用户名字
	private String examineResultReason;// 判诉原因

	public OfflineAppeal() {
		super();
	}

	public OfflineAppeal(String id){
		super(id);
	}

	@Length(min=1, max=64, message="申诉人长度必须介于 1 和 64 之间")
	public String getAppealUserId() {
		return appealUserId;
	}

	public void setAppealUserId(String appealUserId) {
		this.appealUserId = appealUserId;
	}
	
	@Length(min=1, max=64, message="订单号长度必须介于 1 和 64 之间")
	public String getSubOrderId() {
		return subOrderId;
	}

	public void setSubOrderId(String subOrderId) {
		this.subOrderId = subOrderId;
	}
	
	@Length(min=1, max=64, message="卖家长度必须介于 1 和 64 之间")
	public String getSellUserId() {
		return sellUserId;
	}

	public void setSellUserId(String sellUserId) {
		this.sellUserId = sellUserId;
	}
	
	@Length(min=1, max=64, message="买家长度必须介于 1 和 64 之间")
	public String getBuyUserId() {
		return buyUserId;
	}

	public void setBuyUserId(String buyUserId) {
		this.buyUserId = buyUserId;
	}
	
	@Length(min=0, max=64, message="审核人长度必须介于 0 和 64 之间")
	public String getExamineUserId() {
		return examineUserId;
	}

	public void setExamineUserId(String examineUserId) {
		this.examineUserId = examineUserId;
	}
	
	@Length(min=1, max=1, message="状态长度必须介于 1 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=1, max=20, message="原因类型长度必须介于 1 和 20 之间")
	public String getAppealType() {
		return appealType;
	}

	public void setAppealType(String appealType) {
		this.appealType = appealType;
	}
	
	@Length(min=0, max=500, message="原因描述长度必须介于 0 和 500 之间")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@Length(min=0, max=500, message="证件图片1长度必须介于 0 和 500 之间")
	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	@Length(min=0, max=500, message="证件图片2长度必须介于 0 和 500 之间")
	public String getImagePath2() {
		return imagePath2;
	}

	public void setImagePath2(String imagePath2) {
		this.imagePath2 = imagePath2;
	}
	
	@Length(min=0, max=500, message="证件图片3长度必须介于 0 和 500 之间")
	public String getImagePath3() {
		return imagePath3;
	}

	public void setImagePath3(String imagePath3) {
		this.imagePath3 = imagePath3;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getExamineDate() {
		return examineDate;
	}

	public void setExamineDate(Date examineDate) {
		this.examineDate = examineDate;
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
		
	public Date getBeginExamineDate() {
		return beginExamineDate;
	}

	public void setBeginExamineDate(Date beginExamineDate) {
		this.beginExamineDate = beginExamineDate;
	}
	
	public Date getEndExamineDate() {
		return endExamineDate;
	}

	public void setEndExamineDate(Date endExamineDate) {
		this.endExamineDate = endExamineDate;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getAppealMail() {
		return appealMail;
	}

	public void setAppealMail(String appealMail) {
		this.appealMail = appealMail;
	}

	public String getAppealMobile() {
		return appealMobile;
	}

	public void setAppealMobile(String appealMobile) {
		this.appealMobile = appealMobile;
	}

	public String getAppealRealName() {
		return appealRealName;
	}

	public void setAppealRealName(String appealRealName) {
		this.appealRealName = appealRealName;
	}

	public String getAppealIdCard() {
		return appealIdCard;
	}

	public void setAppealIdCard(String appealIdCard) {
		this.appealIdCard = appealIdCard;
	}

	public String getSellMail() {
		return sellMail;
	}

	public void setSellMail(String sellMail) {
		this.sellMail = sellMail;
	}

	public String getSellMobile() {
		return sellMobile;
	}

	public void setSellMobile(String sellMobile) {
		this.sellMobile = sellMobile;
	}

	public String getSellRealName() {
		return sellRealName;
	}

	public void setSellRealName(String sellRealName) {
		this.sellRealName = sellRealName;
	}

	public String getSellIdCard() {
		return sellIdCard;
	}

	public void setSellIdCard(String sellIdCard) {
		this.sellIdCard = sellIdCard;
	}

	public String getBuyMail() {
		return buyMail;
	}

	public void setBuyMail(String buyMail) {
		this.buyMail = buyMail;
	}

	public String getBuyMobile() {
		return buyMobile;
	}

	public void setBuyMobile(String buyMobile) {
		this.buyMobile = buyMobile;
	}

	public String getBuyRealName() {
		return buyRealName;
	}

	public void setBuyRealName(String buyRealName) {
		this.buyRealName = buyRealName;
	}

	public String getBuyIdCard() {
		return buyIdCard;
	}

	public void setBuyIdCard(String buyIdCard) {
		this.buyIdCard = buyIdCard;
	}

	public String getExamineResultUserId() {
		return examineResultUserId;
	}

	public void setExamineResultUserId(String examineResultUserId) {
		this.examineResultUserId = examineResultUserId;
	}

	public String getExamineResultUserName() {
		return examineResultUserName;
	}

	public void setExamineResultUserName(String examineResultUserName) {
		this.examineResultUserName = examineResultUserName;
	}

	public String getExamineResultReason() {
		return examineResultReason;
	}

	public void setExamineResultReason(String examineResultReason) {
		this.examineResultReason = examineResultReason;
	}

	public String getSellWechat() {
		return sellWechat;
	}

	public void setSellWechat(String sellWechat) {
		this.sellWechat = sellWechat;
	}

	public String getSellAlipay() {
		return sellAlipay;
	}

	public void setSellAlipay(String sellAlipay) {
		this.sellAlipay = sellAlipay;
	}

	public String getSellBankName() {
		return sellBankName;
	}

	public void setSellBankName(String sellBankName) {
		this.sellBankName = sellBankName;
	}

	public String getSellBankNo() {
		return sellBankNo;
	}

	public void setSellBankNo(String sellBankNo) {
		this.sellBankNo = sellBankNo;
	}
}