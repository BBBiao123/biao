/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.biao.kafka.interceptor;

import com.biao.util.JsonUtils;
import com.biao.util.SnowFlake;

/**
 * kafka消息处理；
 *
 *
 */
public class SampleMessage {
    /**
     * id;
     * 如果不传则会自动生成一个ID;
     */
    private String id;

    /**
     * message消息；
     */
    private Object message;

    public String getId() {
        return this.id;
    }

    public SampleMessage setId(String id) {
        this.id = id;
        return this;
    }

    public Object getMessage() {
        return message;
    }

    public <T> T getMessage(Class<T> tClass) {
        return JsonUtils.fromJson(JsonUtils.toJson(getMessage()), tClass);
    }

    public SampleMessage setMessage(Object message) {
        this.message = message;
        return this;
    }

    /**
     * 构造一个kafka消息对象；
     *
     * @param id      id;
     * @param message message 消息实体对象；
     * @param <E>     类型；
     * @return 消息；
     */
    public static <E> SampleMessage build(String id, E message) {
        return new SampleMessage().setId(id).setMessage(message);
    }

    /**
     * 构造一个kafka消息对象；
     * id:会自动生成；
     *
     * @param message message 消息实体对象；
     * @param <E>     类型；
     * @return 消息；
     */
    public static <E> SampleMessage build(E message) {
        String id = SnowFlake.createSnowFlake().nextIdString();
        return build(id, message);
    }

    @Override
    public String toString() {
        return "SampleMessage [id=" + id + "]";
    }
}
