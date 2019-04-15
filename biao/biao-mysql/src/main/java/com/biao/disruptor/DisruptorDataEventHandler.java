package com.biao.disruptor;

import com.biao.entity.PhoneGeocoder;
import com.biao.entity.PlatUser;
import com.biao.enums.MessageTemplateCode;
import com.biao.mapper.PlatUserOplogDao;
import com.biao.phone.utils.PhoneModel;
import com.biao.phone.utils.PhoneUtil;
import com.biao.pojo.SmsMessageDTO;
import com.biao.service.MessageSendService;
import com.biao.service.PlatUserService;
import com.biao.service.SmsMessageService;
import com.biao.service.UserPhoneGeocoderService;
import com.biao.spring.SpringBeanFactoryContext;
import com.lmax.disruptor.WorkHandler;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisruptorDataEventHandler implements WorkHandler<DisruptorData> {

    private static Logger logger = LoggerFactory.getLogger(DisruptorDataEventHandler.class);

    private MessageSendService messageSendService;

    private UserPhoneGeocoderService userPhoneGeocoderService;

    private PlatUserService platUserService;

    private SmsMessageService smsMessageService;

    private PlatUserOplogDao platUserOplogDao;

    public DisruptorDataEventHandler() {
        messageSendService = SpringBeanFactoryContext.findBean(MessageSendService.class);
        userPhoneGeocoderService = SpringBeanFactoryContext.findBean(UserPhoneGeocoderService.class);
        platUserService = SpringBeanFactoryContext.findBean(PlatUserService.class);
        smsMessageService = SpringBeanFactoryContext.findBean(SmsMessageService.class);
        platUserOplogDao = SpringBeanFactoryContext.findBean(PlatUserOplogDao.class);
    }

    @Override
    public void onEvent(DisruptorData disruptorData) throws Exception {
        //获取
        if (disruptorData.getType() == null) {
            logger.error("Disruptor receive data error, not type value");
            return;
        }
        if (disruptorData.getType() == 1) {
            messageSendService.sendMail(disruptorData.getEmailSendLog());
        }
        if (disruptorData.getType() == 2) {
            messageSendService.insertLog(disruptorData.getEmailSendLog());
        }
        if (disruptorData.getType() == 4) {
            //手机记录
            messageSendService.insertMobileLog(disruptorData.getMobileSendLog());
        }

        if (disruptorData.getType() == 7) {
            //短信提示用户登录错误
            PlatUser platUser = platUserService.findByLoginName(disruptorData.getPlatUser().getUsername());
            if (platUser != null && StringUtils.isNotBlank(platUser.getMobile())) {
                //发送短信提示用户
                smsMessageService.sendSms(platUser.getMobile(), MessageTemplateCode.MOBILE_LOGIN_ERROR_TIME_TEMPLATE.getCode(), "");
            }
        }
        
        if(disruptorData.getType() == 11) {
        	SmsMessageDTO smsMessageDTO = disruptorData.getSmsMessageDTO() ;
        	smsMessageService.sendSms(smsMessageDTO.getMobile(), smsMessageDTO.getTemplateCode(), smsMessageDTO.getOutId());
        }

        if (disruptorData.getType() == 9) {
            try {
                platUserOplogDao.insert(disruptorData.getPlatUserOplog());
            } catch (Exception e) {
                logger.error("增加安全锁定记录异常，exception:{}", e);
            }
        }

        if (disruptorData.getType() == 3) {
            PhoneGeocoder phoneGeocoder = new PhoneGeocoder();
            PlatUser user = disruptorData.getPlatUser();
            phoneGeocoder.setUserId(user.getId());
            phoneGeocoder.setReferId(user.getReferId());
            //解析用户电话号码,以及归属地
            PhoneModel phoneModel = PhoneUtil.getPhoneModel(user.getMobile());
            if (phoneModel == null) {
                //采用http解析
                String mobile = user.getMobile();
                String url = "http://www.ip138.com:8080/search.asp?action=mobile&mobile=%s";
                url = String.format(url, mobile);
                Document doc = Jsoup.connect(url).get();
                Elements els = doc.getElementsByClass("tdc2");
                String phoneGeocodeName = els.get(1).text();
                String[] phoneGeocodes = phoneGeocodeName.split(" ");
                //直辖市
                if (phoneGeocodes.length == 1) {
                    phoneModel = new PhoneModel();
                    phoneModel.setCityName(PhoneUtil.getRealProvince(phoneGeocodes[0]));
                    phoneModel.setProvinceName(PhoneUtil.getRealProvince(phoneGeocodes[0]));
                }
                if (phoneGeocodes.length == 2) {
                    phoneModel = new PhoneModel();
                    phoneModel.setCityName(PhoneUtil.getRealProvince(phoneGeocodes[1]));
                    phoneModel.setProvinceName(PhoneUtil.getRealProvince(phoneGeocodes[0]));
                }
            }
            if (phoneModel != null) {
                phoneGeocoder.setCityName(phoneModel.getCityName());
                phoneGeocoder.setProvinceName(phoneModel.getProvinceName());
                phoneGeocoder.setName(phoneModel.getProvinceName() + "-" + phoneModel.getCityName());
                userPhoneGeocoderService.insert(phoneGeocoder);
            }
        }
    }

}
