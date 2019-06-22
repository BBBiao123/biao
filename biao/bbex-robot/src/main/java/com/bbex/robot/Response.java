package com.bbex.robot;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Objects;

/**
 * 返回一个对象；
 *
 * @author p
 */
@Data
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

}
