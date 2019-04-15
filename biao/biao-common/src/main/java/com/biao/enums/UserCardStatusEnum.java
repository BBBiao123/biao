package com.biao.enums;

public enum UserCardStatusEnum {

    NO_APPLY("0", "未认证"),
    APPLY("2", "v1审核中"),
    TWO_APPLY("12", "v2审核中"),
    NO_PASS("9", "v1审核不通过"),
    TWO_NO_PASS("19", "v2审核不通过"),
    TWO_PASS("11", "v2审核通过"),
    PASS("1", "v1审核通过");
	
    private String code;

    private String message;

    UserCardStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    
    public static boolean authRealName(String cardStatus,String countryCode) {
    	if(cardStatus==null) {
    		return false ;
    	}
    	if(cardStatus.equals(UserCardStatusEnum.PASS.getCode())) {
    		return true ;
    	}
    	if(cardStatus.equals(UserCardStatusEnum.TWO_APPLY.getCode())) {
    		return true ;
    	}
    	if(cardStatus.equals(UserCardStatusEnum.TWO_NO_PASS.getCode())) {
    		return true ;
    	}
    	if(cardStatus.equals(UserCardStatusEnum.TWO_PASS.getCode())) {
    		return true ;
    	}
    	return false ;
    }
    
    public static boolean authRealName(Integer cardStatus,String countryCode) {
    	if(cardStatus==null) {
    		return false ;
    	}
    	return authRealName(cardStatus.toString(),countryCode);
    }
}
