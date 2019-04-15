package com.biao.pojo;

import com.biao.constant.Constants;

import java.io.Serializable;

public class GlobalMessageResponseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String msg;

    private Object data;

    private Integer code;

    /**
     * 成功返回消息提示
     *
     * @param msg
     * @return
     */
    public static GlobalMessageResponseVo newSuccessInstance(String msg) {
        return new GlobalMessageResponseVo(Constants.SUCCESS_CODE, msg, null);
    }

    /**
     * 成功返回数据
     *
     * @param data
     * @return
     */
    public static GlobalMessageResponseVo newSuccessInstance(Object data) {
        return new GlobalMessageResponseVo(Constants.SUCCESS_CODE, data);
    }

    public static GlobalMessageResponseVo newErrorInstance(String msg) {
        return new GlobalMessageResponseVo(Constants.GLOBAL_ERROR_CODE, msg, null);
    }

    public static GlobalMessageResponseVo newInstance(Integer code, String msg) {
        return new GlobalMessageResponseVo(code, msg, null);
    }

    public static GlobalMessageResponseVo newInstance(Integer code, String msg, Object data) {
        return new GlobalMessageResponseVo(code, msg, data);
    }

    public GlobalMessageResponseVo(Integer code, Object data) {
        this.code = code;
        this.data = data;
    }

    public GlobalMessageResponseVo(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "GlobalMessageResponseVo [msg=" + msg + ", data=" + data + ", code=" + code + "]";
    }

}
