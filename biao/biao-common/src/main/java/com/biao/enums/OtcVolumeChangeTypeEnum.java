package com.biao.enums;

public enum OtcVolumeChangeTypeEnum {

	PUBLISH_SELL_ORDER("1","发布卖出广告"),
	CANCEL_SELL_ORDER("2","撤销卖出广告"),
	CREATE_SUB_ORDER("3","创建订单"),
	CANCEL_SUB_ORDER("4","撤销订单"),
	CANCEL_SUB_ORDER_("5","撤销订单|卖出广告已撤销"),
	CONFIRM_IN("6","确认收到款"),
	APPEAL_TO_BUYER("7","申诉判给买方"),
	APPEAL_TO_SELLER("8","申诉判给卖方"),
	APPEAL_TO_SELLER_("9","申诉判给卖方|卖出广告已撤销"),
	;
	private String code ;

	private String message ;

	OtcVolumeChangeTypeEnum(String code, String message){
		this.code = code ;
		this.message = message ;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
