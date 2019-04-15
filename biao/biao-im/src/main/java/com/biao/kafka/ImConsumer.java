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

package com.biao.kafka;

import com.biao.constant.ImKafkaConstants;
import com.biao.current.threadpool.BbexThreadFactory;
import com.biao.current.threadpool.BbexThreadPool;
import com.biao.kafka.interceptor.ImKafkaDTO;
import com.biao.kafka.interceptor.SampleMessage;
import com.biao.util.SnowFlake;
import org.apache.commons.lang3.StringUtils;
import org.jim.common.Const;
import org.jim.common.ImAio;
import org.jim.common.ImConfig;
import org.jim.common.ImPacket;
import org.jim.common.message.IMesssageHelper;
import org.jim.common.packets.ChatBody;
import org.jim.common.packets.ChatType;
import org.jim.common.packets.Command;
import org.jim.common.packets.RespBody;
import org.jim.common.utils.ChatKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * im kafka Consumer.
 *
 *  ""
 */
@Component
public class ImConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImConsumer.class);

    private static BlockingQueue<ChatBody> QUEUE = new LinkedBlockingQueue<>(1024);

    private static final int MAX_THREAD = Runtime.getRuntime().availableProcessors() << 1;

    private static ImConfig imConfig;

    private static IMesssageHelper messsageHelper;


    /**
     * Sets im config.
     *
     * @param imConfig the im config
     */
    public static void setImConfig(final ImConfig imConfig) {
        ImConsumer.imConfig = imConfig;
    }

    /**
     * Sets messsage helper.
     *
     * @param messsageHelper the messsage helper
     */
    public static void setMesssageHelper(final IMesssageHelper messsageHelper) {
        ImConsumer.messsageHelper = messsageHelper;
    }

    /**
     * 推送订单状态给用户.
     *
     * @param sampleMessages message
     */
    @KafkaListener(topics = ImKafkaConstants.IM_ORDER)
    public void sendOrderStatus(final SampleMessage sampleMessages) {
        try {
            final ImKafkaDTO message = sampleMessages.getMessage(ImKafkaDTO.class);
            if (Objects.nonNull(message) && StringUtils.isNoneBlank(message.getMessage())) {
                final ChatBody chatBody = buildChatBody(message);
                //存储
                submit(chatBody);

                ImPacket chatPacket = new ImPacket(Command.COMMAND_CHAT_REQ,
                        new RespBody(Command.COMMAND_CHAT_REQ, buildChatBody(message)).toByte());

                chatPacket.setSynSeq(new Random(100).nextInt());

                if (message.getTwoSides()) {
                    final String from = message.getFrom();
                    boolean isOnline = ChatKit.isOnline(from, imConfig);
                    if (isOnline) {
                        ImAio.sendToUser(from, chatPacket);
                    }
                }
                final String to = message.getTo();
                boolean isOnline = ChatKit.isOnline(to, imConfig);
                if (isOnline) {
                    ImAio.sendToUser(to, chatPacket);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        synchronized (LOGGER) {
            final ExecutorService executorService =
                    BbexThreadPool.newCustomFixedThreadPool(MAX_THREAD,
                            BbexThreadFactory.create("kline-pull-data",
                                    false));
            for (int i = 0; i < MAX_THREAD; i++) {
                executorService.execute(new Worker());
            }
        }
    }

    private ChatBody buildChatBody(final ImKafkaDTO message) {
        return ChatBody.newBuilder()
                .setId(SnowFlake.createSnowFlake().nextIdString())
                .setCreateTime(System.currentTimeMillis())
                .setFrom(message.getFrom())
                .setTo(message.getTo())
                .setMsgType(5)
                .setChatType(ChatType.CHAT_TYPE_PRIVATE.getNumber())
                .setContent(message.getMessage()).build();

    }

    private static void submit(final ChatBody chatBody) {
        try {
            QUEUE.put(chatBody);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * The type Worker.
     */
    class Worker implements Runnable {

        @Override
        public void run() {
            execute();
        }

        /**
         * 执行..
         */
        private void execute() {
            while (true) {
                try {
                    final ChatBody chatBody = QUEUE.take();
                    //开启持久化
                    if ("on".equals(imConfig.getIsStore())) {
                        //存储群聊消息;
                        String from = chatBody.getFrom();
                        String to = chatBody.getTo();
                        String sessionId = ChatKit.sessionId(from, to);
                        messsageHelper.writeMessage(Const.STORE, Const.USER + ":" + sessionId, chatBody);
                        boolean isOnline = ChatKit.isOnline(to, imConfig);
                        if (!isOnline) {
                            messsageHelper.writeMessage(Const.PUSH, Const.USER + ":" + to + ":" + from, chatBody);
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error(" failure ," + e.getMessage());
                }
            }
        }
    }


}
