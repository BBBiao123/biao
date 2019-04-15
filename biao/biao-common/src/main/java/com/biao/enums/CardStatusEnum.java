package com.biao.enums;

/**
 * 身份证状态枚举
 *  ""oury
 *
 */
public enum CardStatusEnum {

	CARD_STATUS_ZERO(0,"v0"),
	CARD_STATUS_ONE(1,"v1"),
	CARD_STATUS_TWO(2,"v2")
	;
	CardStatusEnum(int code,String msg){
		this.code = code ;
		this.msg = msg ;
	}
	
	private int code ;
	private String msg ;
	
	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
}
