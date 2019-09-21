package com.biao.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.biao.cache.SmsFeieeConfig;
import com.biao.config.FreemarkConfig;
import com.biao.constant.Constants;
import com.biao.constant.SercurityConstant;
import com.biao.disruptor.DisruptorData;
import com.biao.disruptor.DisruptorManager;
import com.biao.entity.MobileSendLog;
import com.biao.entity.MobileTemplate;
import com.biao.exception.PlatException;
import com.biao.mapper.MobileTemplateDao;
import com.biao.service.SmsMessageService;
import com.biao.util.NumberUtils;
import com.biao.util.SnowFlake;
import com.biao.util.StringHelp;
import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service("smsMessageService")
public class SmsMessageServiceImpl implements SmsMessageService {

    private static Logger logger = LoggerFactory.getLogger(SmsMessageServiceImpl.class);

    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
  /*  static final String accessKeyId = "LTAIpQPdWb7bMNJg";
    static final String accessKeySecret = "4m5gdjhdH4WW6hsUFBBMJmeP8vq4NW";
    static final String signName = "ECO";
	
    @Value("${access.keyId.conf}")
    private String accessKeyIdConf = accessKeyId ;
    
    @Value("${access.keySecret.conf}")
    private String accessKeySecretConf = accessKeySecret ;
    
    @Value("${access.signName}")
    private String signNameConf = signName ;*/

    @Autowired
    private MobileTemplateDao mobileTemplateDao;

    @Autowired
    private SmsFeieeConfig smsFeieeConfig;

    public void sendSms(String mobile, String templateCode, String outId) {
        String[] arrStr=mobile.split(",");
        String countrySyscode=null;
        if(arrStr.length>1){
            countrySyscode=arrStr[0];
            mobile=arrStr[1];
        }
        //4验证码存redis
        String redisTemplatKey = "redis:sms:templat:" + templateCode;
        MobileTemplate mobileTemplate = (MobileTemplate) redisTemplate.opsForValue().get(redisTemplatKey);
        if (mobileTemplate == null) {
            mobileTemplate = mobileTemplateDao.findByCode(templateCode);
            redisTemplate.opsForValue().set(redisTemplatKey, mobileTemplate, 24 * 3600, TimeUnit.SECONDS);
        }
        if (mobileTemplate == null) {
            throw new PlatException(Constants.MESSAGE_TEMPLATE_NULL_ERROR, "短信不支持的模板");
        }
        try {
            String code = NumberUtils.getRandomNumber(6);
            //替换自动生成的参数
            if (StringUtils.isNotBlank(outId) && StringHelp.regexMatcher("\\d+", outId)) {
                code = outId;
            }
            System.out.println("----------"+code);
            MobileSendLog mobileSendLog = new MobileSendLog();
            //初始化acsClient,暂不支持region化
            if(StringUtils.isBlank(countrySyscode) || "+86".equals(countrySyscode)){
                IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", mobileTemplate.getAccessKey(), mobileTemplate.getAccessSecret());
                DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
                IAcsClient acsClient = new DefaultAcsClient(profile);
                //组装请求对象-具体描述见控制台-文档部分内容
                SendSmsRequest request = new SendSmsRequest();
                request.setConnectTimeout(10000);
                request.setReadTimeout(10000);
                //必填:待发送手机号
                request.setPhoneNumbers(mobile);
                //必填:短信签名-可在短信控制台中找到
                request.setSignName(mobileTemplate.getSignName());
                //必填:短信模板-可在短信控制台中找到
                //SMS_136700099
                request.setTemplateCode(mobileTemplate.getTemplateCode());
                //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
                Map<String, Object> params = new HashMap<>();
                params.put("code", code);
                String templateParam = getContent(mobileTemplate.getTemplateCode(), mobileTemplate.getTemplateParam(), params);
                mobileSendLog.setContent(templateParam);
                //"{\"code\":"+code+"}"
                request.setTemplateParam(templateParam);
                //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
                //request.setSmsUpExtendCode("90997");
                //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
                if (StringUtils.isNotBlank(mobileTemplate.getWorkSign())) {
                    //"yourOutId"
                    request.setOutId(mobileTemplate.getWorkSign());
                }
                //记录日志
                SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
                if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                    mobileSendLog.setStatus("0");

                    String redisKey = new StringBuffer("redis-sms:").append(mobileTemplate.getTemplateCode()).append(":" + mobile).toString();
                    valOpsStr.set(redisKey, code, mobileTemplate.getTimeOut(), TimeUnit.SECONDS);
                } else {
                    mobileSendLog.setStatus("1");
                    logger.error("发送短信失败， mobile = {}", mobile);
                    throw new PlatException(Constants.GLOBAL_ERROR_CODE, sendSmsResponse.getMessage());
                }
            }else{
                CloseableHttpClient client = null;
                CloseableHttpResponse response = null;
                try {
                    List<BasicNameValuePair> formparams = new ArrayList<>();
                    String contMobile=countrySyscode.replace("+","")+mobile;
                    formparams.add(new BasicNameValuePair("Account",smsFeieeConfig.getAccount()));
                    formparams.add(new BasicNameValuePair("Pwd",smsFeieeConfig.getPwd()));
                    formparams.add(new BasicNameValuePair("Content",code));
                    formparams.add(new BasicNameValuePair("Mobile",contMobile));
                    formparams.add(new BasicNameValuePair("SignId",smsFeieeConfig.getSignId()));//
                    formparams.add(new BasicNameValuePair("TemplateId",smsFeieeConfig.getTemplateId()));//

                    HttpPost httpPost = new HttpPost(smsFeieeConfig.getUrl());
                    httpPost.setEntity(new UrlEncodedFormEntity(formparams,"UTF-8"));
                    client = HttpClients.createDefault();
                    response = client.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    String result = EntityUtils.toString(entity);
                    System.out.println(result);
                    if (result != null && result.indexOf("OK") != -1) {
                        mobileSendLog.setStatus("0");
                        String redisKey = new StringBuffer("redis-sms:").append(mobileTemplate.getTemplateCode()).append(":" + mobile).toString();
                        valOpsStr.set(redisKey, code, mobileTemplate.getTimeOut(), TimeUnit.SECONDS);
                    } else {
                        mobileSendLog.setStatus("1");
                        logger.error("发送短信失败， mobile = {}", mobile);
                        throw new PlatException(Constants.GLOBAL_ERROR_CODE, result);
                    }
                } finally {
                    if (response != null) {
                        response.close();
                    }
                    if (client != null) {
                        client.close();
                    }
                }
            }

            String id = SnowFlake.createSnowFlake().nextIdString();

            mobileSendLog.setId(id);
            mobileSendLog.setDelFlag("0");
            mobileSendLog.setBusinessType(mobileTemplate.getTemplateCode());
            mobileSendLog.setTemplateId(templateCode);
            mobileSendLog.setSubject("发送手机号短信" + mobileTemplate.getTemplateCode());
            mobileSendLog.setMobile(mobile);
            mobileSendLog.setCreateDate(LocalDateTime.now());
            long expireTime = SercurityConstant.REDIS_EXPIRE_TIME_HALF_HOUR;
            if (mobileTemplate.getTimeOut() != null) {
                expireTime = mobileTemplate.getTimeOut();
            }
            mobileSendLog.setExpireTime(LocalDateTime.now().plusSeconds(expireTime));
            DisruptorManager.instance().runConfig();
            DisruptorData data = new DisruptorData();
            data.setType(4);
            data.setMobileSendLog(mobileSendLog);
            DisruptorManager.instance().publishData(data);

        } catch (Exception e) {
            logger.error("发送短信失败， mobile = {},aliyun调用失败", mobile);
            throw new PlatException(Constants.GLOBAL_ERROR_CODE, "短信发送失败");
        }
    }

    private static String getContent(String templateName, String template, Map<String, Object> params) {
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
    public void sendSms(String countryCode, String mobile, String template, String outId) throws PlatException {
        if ("+86".equalsIgnoreCase(countryCode)) {
            sendSms(mobile, template, outId);
        }
        //预留其他国家的短信接口
    }

    @Override
    public boolean validSmsCode(String mobile, String templateCode, String code) {
        String redisTemplatKey = "redis:sms:templat:" + templateCode;
        MobileTemplate mobileTemplate = (MobileTemplate) redisTemplate.opsForValue().get(redisTemplatKey);
        if (mobileTemplate == null) {
            mobileTemplate = mobileTemplateDao.findByCode(templateCode);
            redisTemplate.opsForValue().set(redisTemplatKey, mobileTemplate, 24 * 3600, TimeUnit.SECONDS);
        }
        if (mobileTemplate == null) {
            throw new PlatException(Constants.MESSAGE_TEMPLATE_NULL_ERROR, "短信不支持的模板");
        }
        String redisKey = new StringBuffer("redis-sms:").append(mobileTemplate.getTemplateCode()).append(":" + mobile).toString();
        if (!valOpsStr.getOperations().hasKey(redisKey)) {
            throw new PlatException(Constants.COMMON_ERROR_CODE, "请重新获取验证码");
        }
        String redisCode = valOpsStr.get(redisKey);
        if (StringUtils.isBlank(code)) {
            throw new PlatException(Constants.COMMON_ERROR_CODE, "验证码已失效");
        }
        if (!redisCode.equalsIgnoreCase(code)) {
            throw new PlatException(Constants.COMMON_ERROR_CODE, "验证码错误");
        }
        valOpsStr.getOperations().delete(redisKey);
        return true;
    }

    /*public static SendSmsResponse sendSms() throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers("13632638533");
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("ECO");
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_136700099");
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{\"code\":\"123\"}");

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse;
    }


    public static QuerySendDetailsResponse querySendDetails(String bizId) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber("15000000000");
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

        return querySendDetailsResponse;
    }

    public static void main(String[] args) throws ClientException, InterruptedException {

        //发短信
        SendSmsResponse response = sendSms();
        System.out.println("短信接口返回的数据----------------");
        System.out.println("Code=" + response.getCode());
        System.out.println("Message=" + response.getMessage());
        System.out.println("RequestId=" + response.getRequestId());
        System.out.println("BizId=" + response.getBizId());

        Thread.sleep(3000L);

        //查明细
        if(response.getCode() != null && response.getCode().equals("OK")) {
            QuerySendDetailsResponse querySendDetailsResponse = querySendDetails(response.getBizId());
            System.out.println("短信明细查询接口返回数据----------------");
            System.out.println("Code=" + querySendDetailsResponse.getCode());
            System.out.println("Message=" + querySendDetailsResponse.getMessage());
            int i = 0;
            for(QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse.getSmsSendDetailDTOs())
            {
                System.out.println("SmsSendDetailDTO["+i+"]:");
                System.out.println("Content=" + smsSendDetailDTO.getContent());
                System.out.println("ErrCode=" + smsSendDetailDTO.getErrCode());
                System.out.println("OutId=" + smsSendDetailDTO.getOutId());
                System.out.println("PhoneNum=" + smsSendDetailDTO.getPhoneNum());
                System.out.println("ReceiveDate=" + smsSendDetailDTO.getReceiveDate());
                System.out.println("SendDate=" + smsSendDetailDTO.getSendDate());
                System.out.println("SendStatus=" + smsSendDetailDTO.getSendStatus());
                System.out.println("Template=" + smsSendDetailDTO.getTemplateCode());
            }
            System.out.println("TotalCount=" + querySendDetailsResponse.getTotalCount());
            System.out.println("RequestId=" + querySendDetailsResponse.getRequestId());
        }

    }*/
}
