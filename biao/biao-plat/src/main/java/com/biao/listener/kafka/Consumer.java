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

package com.biao.listener.kafka;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.constant.RedisConstants;
import com.biao.kafka.interceptor.SampleMessage;
import com.biao.reactive.data.mongo.domain.UserLoginLog;
import com.biao.reactive.data.mongo.service.UserLoginLogService;
import com.biao.service.MessageSendService;
import com.biao.util.DateUtils;
import com.biao.util.JsonUtils;
import com.biao.util.http.HttpRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
class Consumer {

    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;

    @Autowired
    private MessageSendService messageSendService;
    @Autowired
    private UserLoginLogService userLoginLogService;

    @Autowired
    Producer producer;

    //自定义分区测试 指定分区消费 多个进程都可以同时消费

    /* */

    /**
     * 批量消费
     *
     * @param message
     *//*
    @KafkaListener(id = "#{tradeKafkaConsumer.groupId}",topicPartitions= {
    	@TopicPartition(topic = "#{tradeKafkaConsumer.topic}", partitions ="#{tradeKafkaConsumer.partitions}")
    },containerFactory="kafkaListenerContainerFactory",beanRef="tradeKafkaConsumer")
    public void processMessagePartitionsOne(List<SampleMessage> message) {
        System.out.println("PartitionsOne Received sample message [" + number.addAndGet(message.size()) + "]");
        //producer.send("testGroup","1","haha");
    }
    
    @Bean
	public TradeKafkaConsumer tradeKafkaConsumer() {
	    return new TradeKafkaConsumer("bar0-1","testGroup",new String[] {"0","1"});
	}
    
    @KafkaListener(id = "bar2-3",topicPartitions= {
        	@TopicPartition(topic = "testGroup", partitions = { "3", "2" })
    })
    public void processMessagePartitionsTwo(SampleMessage message) {
        System.out.println("PartitionsTwo Received sample message [" + message + "]");
        //producer.send("testGroup","1",0,message);
    }
    
    
    @KafkaListener(id = "bar4-5",topicPartitions= {
        	@TopicPartition(topic = "testGroup", partitions = { "4", "5" })
    })
    public void processMessagePartitionsThree(SampleMessage message) {
        System.out.println("PartitionsThree Received sample message [" + message + "]");
        //producer.send("testGroup","1","haha");
    }*/

    //@KafkaListener(topics = "topic.user.login.message")
    public void processLoginMessage(List<SampleMessage> messages) {
        messages.forEach(message -> {
            RedisSessionUser user = JsonUtils.fromJson(JsonUtils.toJson(message.getMessage()), RedisSessionUser.class);
            //ip变化发送邮件
            String ip = valOpsStr.get(RedisConstants.USER_LOGIN_INFO_KEY + user.getUsername());
            if (StringUtils.isBlank(ip)) {
                valOpsStr.set(RedisConstants.USER_LOGIN_INFO_KEY + user.getUsername(), user.getLoginIp());
            }
            UserLoginLog userLoginLog = new UserLoginLog();
            if (StringUtils.isNotBlank(user.getLoginIp()) && StringUtils.isNotBlank(user.getMail())) {
                //Map<String, Object> params = parseMap(user.getLoginIp(), user.getMail());
                //if (StringUtils.isNotBlank(ip) && !user.getLoginIp().equals(ip) && params != null) {
                //发送邮件采用异步
                //userLoginLog.setAddress(params.get("address").toString());
                //messageSendService.mailSend("login_change", VerificationCodeType.LOGIN_CHANGE_CODE, user.getMail(), params);
                //}
            }
            //存日志 邮箱 时间 ip 地址
            userLoginLog.setIp(user.getLoginIp());
            userLoginLog.setLoginTime(LocalDateTime.now());
            userLoginLog.setMail(user.getMail());
            userLoginLog.setMobile(user.getMobile());
            userLoginLog.setSource("web");
            userLoginLog.setUserId(user.getId());
            userLoginLog.setLoginName(user.getUsername());
            userLoginLogService.insert(userLoginLog);
        });
    }


    @SuppressWarnings("unchecked")
    private Map<String, Object> parseMap(String loginIp, String userMail) {
        String jsonDate = parseAddress(loginIp);
        Map<String, Object> data = null;
        if (StringUtils.isNotBlank(jsonDate)) {
            Map<String, Object> josnMap = JsonUtils.fromJson(jsonDate);
            if (josnMap != null && josnMap.get("code") != null && "0".equals(josnMap.get("code").toString())) {
                data = new HashMap<>();
                //获取数据
                josnMap = (Map<String, Object>) josnMap.get("data");
                StringBuilder builder = new StringBuilder();
                builder.append(josnMap.get("country")).append("|").append(josnMap.get("region")).append("|").append(josnMap.get("city"));
                data.put("address", builder.toString().replaceAll("null", ""));
                data.put("ip", loginIp);
                data.put("date", DateUtils.formaterLocalDateTime(LocalDateTime.now()));
                data.put("source", "web");
                data.put("mail", userMail);
            }
        }
        return data;
    }

    /**
     * {
     * "code": 0,
     * "data": {
     * "ip": "210.75.225.254",
     * "country": "中国",
     * "area": "华北",
     * "region": "北京市",
     * "city": "北京市",
     * "county": "",
     * "isp": "电信",
     * "country_id": "86",
     * "area_id": "100000",
     * "region_id": "110000",
     * "city_id": "110000",
     * "county_id": "-1",
     * "isp_id": "100017"
     * }
     * }
     *
     * @param ip
     * @return
     */
    private static String parseAddress(String ip) {
        //ip解析
        try {
            String httpUrl = "http://ip.taobao.com/service/getIpInfo.php";
            return HttpRequest.sendGet(httpUrl, "ip=" + ip);
        } catch (Exception e) {
            return null;
        }
    }

    //@KafkaListener(topics = "topic.user.logout.message")
    public void processLogoutMessage(List<SampleMessage> messages) {
        //System.out.println("Received sample message [" + message + "]");
    }
}
