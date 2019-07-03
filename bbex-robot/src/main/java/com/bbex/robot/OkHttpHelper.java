package com.bbex.robot;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.bbex.robot.Constants.RESULT_FI;

/**
 * 调用okHttpHelper;
 * @author p
 */
public class OkHttpHelper {

    private OkHttpClient client;

    private final Logger logger = LoggerFactory.getLogger(OkHttpHelper.class);

    private OkHttpHelper() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        client = builder.build();
    }

    private static final MediaType FORM
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    /**
     * 静态单实例；
     */
    private static class Holder {
        /**
         * 单例
         */
        private static final OkHttpHelper instance = new OkHttpHelper();
    }

    /**
     * 获取ok对象；
     *
     * @return 对象；
     */
    public static OkHttpHelper get() {
        return Holder.instance;
    }

    /**
     * 发送一个请给求；
     */
    public Response requestForm(Map<String, String> request, Map<String, String> headers, String url) {
        Headers.Builder headersBuilder = new Headers.Builder();
        headers.forEach(headersBuilder::add);
        FormBody.Builder builder = new FormBody.Builder();
        request.forEach(builder::add);
        RequestBody body = builder.build();
        Request request1 = new Request.Builder()
                .url(url)
                .headers(headersBuilder.build())
                .post(body)
                .build();
        try {
            String json = Objects.requireNonNull(client.newCall(request1).execute().body()).string();
            return getRes(json);
        } catch (IOException e) {
            logger.error("请求{},发生了错误", url);
            throw new RuntimeException("请求" + url + "发生了错误");
        }
    }

    private Response getRes(String res) {
        if (StringUtils.isBlank(res)) {
            return new Response("返回的结果为空!", RESULT_FI);
        }
        JSONObject object = JSON.parseObject(res);
        Integer code = object.getInteger(Constants.RESULT_CODE);
        String msg = object.getString(Constants.RESULT_MSG);
        Object data = object.get(Constants.RESULT_DATA);
        Response response = new Response(msg, code);
        response.setData(data);
        return response;
    }
}
