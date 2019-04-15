package com.biao.exception;

public class PlatException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 异常信息
     */
    private String msg;
    /**
     * 具体异常码
     */
    private int code;

    public PlatException(int code) {
        super();
        this.code = code;
    }

    public PlatException(int code, String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
