package com.biao.mail;

import com.biao.BaseTest;
import com.biao.constant.SercurityConstant;
import com.biao.entity.EmailSendLog;
import com.biao.service.MessageSendService;
import com.biao.util.NumberUtils;
import com.biao.util.SnowFlake;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class MailTest extends BaseTest {

    @Autowired
    private MessageSendService messageSendService;

    @Test
    public void sendMail() {
        String mail = "460497231@qq.com";
        Map<String, Object> params = new HashMap<>();
        params.put(MessageSendService.PARAM_CODE, NumberUtils.getRandomNumber(6));
        EmailSendLog emailSendLog = new EmailSendLog();
        String id = SnowFlake.createSnowFlake().nextIdString();
        emailSendLog.setId(id);
        emailSendLog.setDelFlag("0");
        emailSendLog.setStatus("0");
        emailSendLog.setBusinessType("test");
        emailSendLog.setTemplateId("test");
        emailSendLog.setContent("==========test===============");
        emailSendLog.setSubject("测试邮件");
        emailSendLog.setEmail(mail);
        emailSendLog.setCreateDate(LocalDateTime.now());
        long expireTime = SercurityConstant.REDIS_EXPIRE_TIME_HALF_HOUR;
        emailSendLog.setExpireTime(LocalDateTime.now().plusSeconds(expireTime));
        messageSendService.sendMail(emailSendLog);
    }
}
