package com.biao.disruptor;

import com.biao.entity.EmailSendLog;
import com.biao.entity.MobileSendLog;
import com.biao.entity.PlatUser;
import com.biao.entity.PlatUserOplog;
import com.biao.pojo.SmsMessageDTO;
import com.lmax.disruptor.EventFactory;

public class DisruptorData {

    public static DisruptorFactory FACTORY_INSTANCE = new DisruptorFactory();
    //定义数据结构
    //数据类型
    private Integer type;

    //type=1 有值  邮箱
    private EmailSendLog emailSendLog;

    //type=4 手机
    private MobileSendLog mobileSendLog;

    //用户电话，用于解析用户归属地 type=3
    //用户短信提示 登录错误次数  type=7
    private PlatUser platUser;

    // type=9
    private PlatUserOplog platUserOplog;
    
    //type = 11
    private SmsMessageDTO smsMessageDTO ;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public EmailSendLog getEmailSendLog() {
        return emailSendLog;
    }

    public void setEmailSendLog(EmailSendLog emailSendLog) {
        this.emailSendLog = emailSendLog;
    }

    public PlatUser getPlatUser() {
        return platUser;
    }

    public void setPlatUser(PlatUser platUser) {
        this.platUser = platUser;
    }

    public MobileSendLog getMobileSendLog() {
        return mobileSendLog;
    }

    public void setMobileSendLog(MobileSendLog mobileSendLog) {
        this.mobileSendLog = mobileSendLog;
    }

    public PlatUserOplog getPlatUserOplog() {
        return platUserOplog;
    }

    public void setPlatUserOplog(PlatUserOplog platUserOplog) {
        this.platUserOplog = platUserOplog;
    }

    public SmsMessageDTO getSmsMessageDTO() {
		return smsMessageDTO;
	}

	public void setSmsMessageDTO(SmsMessageDTO smsMessageDTO) {
		this.smsMessageDTO = smsMessageDTO;
	}

	public static SmsMessageDTO buildSecurityLog(String mobile,String templateCode, String outId) {
		SmsMessageDTO smsMessageDTO = new SmsMessageDTO();
		smsMessageDTO.setMobile(mobile);
		smsMessageDTO.setOutId(outId);
		smsMessageDTO.setTemplateCode(templateCode);
        return smsMessageDTO;
    }
	
	public static void saveSecurityLog(SmsMessageDTO smsMessageDTO) {
        DisruptorManager.instance().runConfig();
        DisruptorData data = new DisruptorData();
        data.setType(11);
        data.setSmsMessageDTO(smsMessageDTO);
        DisruptorManager.instance().publishData(data);
    }
	
	

	public static class DisruptorFactory implements EventFactory<DisruptorData> {
        @Override
        public DisruptorData newInstance() {
            return new DisruptorData();
        }

    }
}

