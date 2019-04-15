package com.biao.config.sercurity;

public class CustomerAuthTradeException extends CustomerAuthException {

    private static final long serialVersionUID = 1L;

    private int code;

    private String msg;

    public CustomerAuthTradeException(int code) {
        super(code);
        this.code = code;
    }

    public CustomerAuthTradeException(int code, String msg) {
        super(code, msg);
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
