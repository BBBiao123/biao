package com.thinkgem.jeesite.modules.plat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.thinkgem.jeesite.modules.sys.dao.MobileTemplateDao;
import com.thinkgem.jeesite.modules.sys.entity.MobileTemplate;

@Service("smsMessageService")
public class SmsMessageService{

	private static Logger logger = LoggerFactory.getLogger(SmsMessageService.class);
	
	@Autowired
    private MobileTemplateDao mobileTemplateDao ;
	
	 //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";
	
    public Boolean sendSms(String templateCode,String mobile) throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        MobileTemplate mobileTemplate = mobileTemplateDao.findByCode(templateCode);
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", mobileTemplate.getAccessKey(), mobileTemplate.getAccessSecret());
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(mobile);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(mobileTemplate.getSignName());
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(mobileTemplate.getTemplateCode());
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
        	logger.info("发送手机号mobile:{},短信成功",mobile);
        	return true;
        }else {
        	logger.info("发送手机号mobile:{},短信失败,msg:{}",mobile,sendSmsResponse.getMessage());
        	return false;
        }
        
    }

}
