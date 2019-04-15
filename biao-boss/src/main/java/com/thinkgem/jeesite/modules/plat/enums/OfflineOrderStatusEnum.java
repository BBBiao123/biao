package com.thinkgem.jeesite.modules.plat.enums;


/**
 * @author xiaoyu
 */

public enum OfflineOrderStatusEnum {

	NOMAL("0","发布状态"),
	COMPLATE("1","已完成"),
	PART_COMPLATE("2","部分成交"),
	PART_CANCEL("3","部分成交,部分取消"),
	CANCEL("9","取消");


	OfflineOrderStatusEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}
	private final String code;

	private final String message;

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
