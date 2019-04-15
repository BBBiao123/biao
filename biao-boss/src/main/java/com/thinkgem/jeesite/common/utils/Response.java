package com.thinkgem.jeesite.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Objects;

public class Response {
    /**
     * 消息体；
     */
    private String msg;
    /**
     * 错误码；
     */
    private Integer code;
    /**
     * 数据；
     */
    private Object data;

    /**
     * 消息体；
     *
     * @param msg  消息；
     * @param code 错误码；
     */
    public Response(String msg, Integer code) {
        this.msg = msg;
        this.code = code;
    }

    /**
     * 处理是否成功；
     *
     * @return true false;
     */
    public boolean sccuess() {
        return Objects.equals(code, Constants.RESULT_SC);
    }

    /**
     * 把data转换为一个obj;
     *
     * @return obj;
     */
    public JSONObject getMap() {
        return (JSONObject) data;
    }

    /**
     * 把data转换为一个array;
     *
     * @return array;
     */
    public JSONArray getArray() {
        return (JSONArray) data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
