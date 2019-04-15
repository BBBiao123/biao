package com.thinkgem.jeesite.modules.plat.enums;

public enum OfflineAppealStatusEnum {

    ING("1", "申诉中"),
    DONE("2", "客服处理完成"),
    CANCEL("3", "用户撤销")
    ;

    OfflineAppealStatusEnum (String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code ;

    private String message ;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
