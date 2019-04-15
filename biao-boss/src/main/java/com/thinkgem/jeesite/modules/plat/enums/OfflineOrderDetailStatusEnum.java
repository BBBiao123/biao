package com.thinkgem.jeesite.modules.plat.enums;

public enum OfflineOrderDetailStatusEnum {

	NOMAL("0","买入下单|卖出下单"),
	CONFIRM_PAY("1","确认付款了"),
	CANCEL("9","取消"),
	CONFIRM_IN("2","确认收到款"),
	CONFIRM_NOT_IN("3","确认没收到款"),
	SHENSU("4","申诉"),
	SHENSU_OVER("5","仲裁结束")
	;
	private String code ;

	private String message ;

	OfflineOrderDetailStatusEnum(String code, String message){
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
