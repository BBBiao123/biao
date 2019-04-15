/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.biao.util.http;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * OkHttpTools.
 *
 *  ""
 */
public final class OkHttpTools {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final OkHttpTools OK_HTTP_TOOLS = new OkHttpTools();

    private static final Gson GOSN = new Gson();

    private OkHttpTools() {

    }

    public static OkHttpTools getInstance() {
        return OK_HTTP_TOOLS;
    }

    public Request buildPost(String url, Map<String, String> params) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (params != null) {
            for (String key : params.keySet()) {
                formBuilder.add(key, params.get(key));
            }
        }
        return new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .post(formBuilder.build())
                .build();
    }

    public String post(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        assert response.body() != null;
        return response.body().string();

    }

    public String get(String url) throws IOException {
        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
        OkHttpClient client = mBuilder
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        assert response.body() != null;
        return response.body().string();
    }

    public String proxyGet(String url) throws IOException {
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 1080));//设置socks代理服务器ip端口
        java.net.Authenticator.setDefault(new java.net.Authenticator()//由于okhttp好像没有提供socks设置Authenticator用户名密码接口，因此设置一个全局的Authenticator
        {
            private PasswordAuthentication authentication = new PasswordAuthentication("username", "password".toCharArray());

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return authentication;
            }
        });

        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
        OkHttpClient client = mBuilder
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .proxy(proxy)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        assert response.body() != null;
        return response.body().string();
    }

    public <T> T get(String url, Map<String, String> params, Class<T> classOfT) throws IOException {
        return execute(buildPost(url, params), classOfT);
    }

    public <T> T get(String url, Type type) throws IOException {
        return execute(buildPost(url, null), type);
    }


    private <T> T execute(Request request, Class<T> classOfT) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return GOSN.fromJson(response.body().string(), classOfT);
    }

    private String execute(Request request) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public <T> T execute(Request request, Type type) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return GOSN.fromJson(response.body().string(), type);
    }

}
