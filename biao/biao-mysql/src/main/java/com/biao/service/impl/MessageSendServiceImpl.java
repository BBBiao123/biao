package com.biao.service.impl;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biao.config.FreemarkConfig;
import com.biao.constant.Constants;
import com.biao.constant.SercurityConstant;
import com.biao.disruptor.DisruptorData;
import com.biao.disruptor.DisruptorManager;
import com.biao.entity.EmailSendLog;
import com.biao.entity.EmailTemplate;
import com.biao.entity.MobileSendLog;
import com.biao.enums.VerificationCodeType;
import com.biao.exception.PlatException;
import com.biao.mail.CommonMailSender;
import com.biao.mapper.EmailSendLogDao;
import com.biao.mapper.EmailTemplateDao;
import com.biao.mapper.MobileSendLogDao;
import com.biao.service.MessageSendService;
import com.biao.util.SnowFlake;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;

@Service
public class MessageSendServiceImpl implements MessageSendService {

	private static Logger logger = LoggerFactory.getLogger(MessageSendServiceImpl.class);
	
    @Autowired(required = false)
    private EmailTemplateDao emailTemplateDao;
    @Autowired(required = false)
    private EmailSendLogDao emailSendLogDao;

    @Autowired(required = false)
    private MobileSendLogDao mobileSendLogDao;

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valOpsStr;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private String fromMail;

    @Override
    public void mailSend(String templateId, VerificationCodeType type, String toMail, Map<String, Object> params) throws PlatException {
        //1 通过tempalteid获取模板
        String redisTemplatKey = "redis:mail:templat:" + templateId;
        EmailTemplate emailTemplate = (EmailTemplate) redisTemplate.opsForValue().get(redisTemplatKey);
        if (emailTemplate == null) {
            emailTemplate = emailTemplateDao.findByCode(templateId);
            if (emailTemplate != null) {
                emailTemplate.setCreateDate(null);
                emailTemplate.setUpdateDate(null);
            }
            redisTemplate.opsForValue().set(redisTemplatKey, emailTemplate, 24 * 3600, TimeUnit.SECONDS);
        }
        if (emailTemplate == null) {
            throw new PlatException(Constants.MESSAGE_TEMPLATE_NULL_ERROR, "短信不支持的模板");
        }
        //2解析模板
        String content = getContent(emailTemplate.getName(), emailTemplate.getTemplateContent(), params);
        //存日志
        String id = SnowFlake.createSnowFlake().nextIdString();
        EmailSendLog emailSendLog = new EmailSendLog();
        emailSendLog.setId(id);
        emailSendLog.setDelFlag("0");
        emailSendLog.setStatus("0");
        emailSendLog.setBusinessType(type.getCode());
        emailSendLog.setTemplateId(templateId);
        emailSendLog.setContent(content);
        emailSendLog.setSubject(emailTemplate.getTemplateSubject());
        emailSendLog.setEmail(toMail);
        emailSendLog.setCreateDate(LocalDateTime.now());
        emailSendLog.setSendTimes(1);
        long expireTime = SercurityConstant.REDIS_EXPIRE_TIME_HALF_HOUR;
        if (emailTemplate.getExpireTime() != null) {
            expireTime = emailTemplate.getExpireTime();
        }
        emailSendLog.setExpireTime(LocalDateTime.now().plusSeconds(expireTime));
		/*emailSendLogDao.insert(emailSendLog);
		//改成异步发送   3   发送邮件
		SimpleMailMessage simpleMessage = new SimpleMailMessage();
		simpleMessage.setText(content);
		simpleMessage.setTo(toMail);
		simpleMessage.setFrom(fromMail);
		simpleMessage.setSubject(emailTemplate.getTemplateSubject());
		mailSender.send(simpleMessage);*/
        //记录日志和发送邮件采用异步
        DisruptorManager.instance().runConfig();
        DisruptorData data = new DisruptorData();
        //Map<String,Object> data = new HashMap<>();
        //data.put("type", 1);
        //data.put("emailSendLog", emailSendLog);
        data.setType(1);
        data.setEmailSendLog(emailSendLog);
        DisruptorManager.instance().publishData(data);
        switch (type) {
            case REGISTER_CODE:
                //注册验证码
                handlerRegisterMail(templateId, type, toMail, params, expireTime);
                break;
            case RESET_CODE:
                //重置密码
                handlerResetMail(toMail, params, expireTime);
                break;
            case LOGIN_CHANGE_CODE:
                //不处理,直接结束
                break;
            case WITHDRAW_CODE:
                handlerRegisterMail(templateId, type, toMail, params, expireTime);
                break;
            case BINDER_CODE:
                handlerRegisterMail(templateId, type, toMail, params, expireTime);
                break;
            case EX_TRADE_MAIL:
                handlerRegisterMail(templateId, type, toMail, params, expireTime);
                break;
            case EX_EXCHANGE_PASS:
                handlerRegisterMail(templateId, type, toMail, params, expireTime);
                break;
            case LOGIN_MAIL_CODE:
                handlerRegisterMail(templateId, type, toMail, params, expireTime);
                break;
            case BINDER_GOOGLE_CODE_MAIL:
                handlerRegisterMail(templateId, type, toMail, params, expireTime);
                break;
            case EX_PASS_MAIL:
                handlerRegisterMail(templateId, type, toMail, params, expireTime);
                break;
            default:
                throw new PlatException(Constants.COMMON_ERROR_CODE, "不支持的邮件发送业务类型");
        }
    }

    private void handlerResetMail(String toMail, Map<String, Object> params, long expireTime) {
        Object ptoken = params.get(PARAM_PTOKEN_CODE);
        String redisKey = new StringBuffer(REDIS_CODE_KEY).append(ptoken).toString();
        valOpsStr.set(redisKey, toMail, expireTime, TimeUnit.SECONDS);
    }

    private void handlerRegisterMail(String templateId, VerificationCodeType type, String toMail, Map<String, Object> params, long expireTime) {
        Object code = params.get(PARAM_CODE);
        //4验证码存redis
        String redisKey = new StringBuffer(REDIS_CODE_KEY).append(templateId + ":").append(type.getCode()).append(":" + toMail).toString();
        valOpsStr.set(redisKey, String.valueOf(code), expireTime, TimeUnit.SECONDS);
    }

    @Override
    public String findMailByPtoken(String ptoken) {
        String redisKey = new StringBuffer(REDIS_CODE_KEY).append(ptoken).toString();
        if (!valOpsStr.getOperations().hasKey(redisKey)) {
            throw new PlatException(Constants.RESET_PASSWORD_TOKEN_EXPIRE_ERROR, "该链接已失效");
        }
        String mail = valOpsStr.get(redisKey);
        if (StringUtils.isBlank(mail)) {
            throw new PlatException(Constants.RESET_PASSWORD_TOKEN_EXPIRE_ERROR, "该链接已失效");
        }
        return mail;
    }

    @Override
    public void expirePtoken(String ptoken) {
        String redisKey = new StringBuffer(REDIS_CODE_KEY).append(ptoken).toString();
        valOpsStr.getOperations().delete(redisKey);
    }

    @Transactional
    public void sendMail(EmailSendLog emailSendLog) {
        emailSendLogDao.insert(emailSendLog);
        SimpleMailMessage simpleMessage = new SimpleMailMessage();
        simpleMessage.setText(emailSendLog.getContent());
        simpleMessage.setTo(emailSendLog.getEmail());
        simpleMessage.setFrom(fromMail);
        simpleMessage.setSubject(emailSendLog.getSubject());
        try {
            CommonMailSender.send(simpleMessage);
        } catch (EmailException e) {
        	Integer sendTimes = (emailSendLog.getSendTimes()==0)?1:emailSendLog.getSendTimes() ;
        	if(sendTimes<3) {
        		try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
				}
        		emailSendLog.setRemarks("失败,重复发送,重试次数:"+sendTimes);
            	DisruptorManager.instance().runConfig();
                DisruptorData data = new DisruptorData();
                data.setType(1);
                data.setEmailSendLog(emailSendLog);
                emailSendLog.setSendTimes(sendTimes+1);
                DisruptorManager.instance().publishData(data);
        	}
        	logger.error("邮件mail:{},发送失败,发送次数sendTime:{}",emailSendLog.getEmail(),sendTimes);
            throw new PlatException(Constants.GLOBAL_ERROR_CODE, "邮件发送一次");
        }
    }

    @Transactional
    public void insertLog(EmailSendLog emailSendLog) {
        emailSendLogDao.insert(emailSendLog);
    }

    @Override
    public void insertMobileLog(MobileSendLog mobileSendLog) {
        mobileSendLogDao.insert(mobileSendLog);
    }

    private String getContent(String templateName, String template, Map<String, Object> params) {
        try {
            StringWriter out = new StringWriter(1024);
            FreemarkConfig.newInstance().getStringTemplateLoader().putTemplate(templateName, template);
            Template temp = FreemarkConfig.newInstance().getConfiguration().getTemplate(templateName, "utf-8");
            temp.process(params, out);
            return out.toString();
        } catch (TemplateNotFoundException e) {
            throw new PlatException(Constants.COMMON_ERROR_CODE, "freemark模板没有找到");
        } catch (MalformedTemplateNameException e) {
            throw new PlatException(Constants.COMMON_ERROR_CODE, "freemark邮件模板异常");
        } catch (ParseException e) {
            throw new PlatException(Constants.COMMON_ERROR_CODE, "freemark解析邮件模板异常");
        } catch (Exception e) {
            throw new PlatException(Constants.COMMON_ERROR_CODE, "freemark格式化邮件模板异常");
        }
    }

    @Override
    public void mailValid(String templateId, VerificationCodeType type, String toMail, String validCode) throws PlatException {
        String redisKey = new StringBuffer(REDIS_CODE_KEY).append(templateId + ":").append(type.getCode()).append(":" + toMail).toString();
        if (!valOpsStr.getOperations().hasKey(redisKey)) {
            throw new PlatException(Constants.COMMON_ERROR_CODE, "验证码不存在");
        }
        String code = valOpsStr.get(redisKey);
        logger.info("邮箱验证mai:{},模板templateId:{},redisCode:{},validCode:{}",toMail,templateId,code,validCode);
        if (StringUtils.isBlank(code)) {
            throw new PlatException(Constants.COMMON_ERROR_CODE, "验证码已失效");
        }
        if (!code.equalsIgnoreCase(validCode)) {
            throw new PlatException(Constants.COMMON_ERROR_CODE, "验证码错误");
        }
        valOpsStr.getOperations().delete(redisKey);
    }

    @Override
    public void clearValid(String templateId, String type, String toMail) {
        String redisKey = new StringBuffer(REDIS_CODE_KEY).append(templateId + ":").append(type).append(":" + toMail).toString();
        if (valOpsStr.getOperations().hasKey(redisKey)) {
            valOpsStr.getOperations().delete(redisKey);
        }
    }
}
