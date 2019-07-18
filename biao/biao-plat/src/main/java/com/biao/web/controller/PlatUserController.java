package com.biao.web.controller;

import com.biao.config.AliYunAuthenticateSigConfig;
import com.biao.config.UserConfig;
import com.biao.config.sercurity.RedisSessionUser;
import com.biao.constant.Constants;
import com.biao.constant.SercurityConstant;
import com.biao.disruptor.DisruptorData;
import com.biao.disruptor.DisruptorManager;
import com.biao.entity.PlatUser;
import com.biao.entity.PlatUserOplog;
import com.biao.enums.*;
import com.biao.exception.PlatException;
import com.biao.google.GoogleAuthenticator;
import com.biao.neo4j.service.Neo4jPlatUserService;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.pojo.ResponsePage;
import com.biao.query.UserLoginLogQuery;
import com.biao.reactive.data.mongo.domain.UserLoginLog;
import com.biao.reactive.data.mongo.enums.SecurityLogEnums;
import com.biao.reactive.data.mongo.service.UserLoginLogService;
import com.biao.service.MessageSendService;
import com.biao.service.MkDistributeLogService;
import com.biao.service.PlatUserService;
import com.biao.service.SmsMessageService;
import com.biao.util.*;
import com.biao.utils.AliYunAuthenticateSigUtils;
import com.biao.vo.*;
import com.biao.web.valid.ValidateFiled;
import com.biao.web.valid.ValidateGroup;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/biao")
public class PlatUserController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private static Logger logger = LoggerFactory.getLogger(PlatUserController.class);

    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;

    @Autowired
    private PlatUserService platUserService;
    @Autowired
    private MessageSendService messageSendService;
    /*@Autowired
    private BaiduApiService baiduApiService;
    @Autowired
    private CardRepository cardRepository;*/
    @Autowired
    private UserLoginLogService userLoginLogService;
    @Autowired
    private SmsMessageService smsMessageService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MkDistributeLogService mkDistributeLogService;

    @Value("${password.resetUrl}")
    private String resetUrl = "https://www.biao.one/biao/resetPassword";

    @Value("${register.switch}")
    private String registerSwitch = "1";

    @Autowired
    private UserConfig userConfig;

    //谷歌验证提示
    @Value("${google.domain}")
    private String domainVaule = "biao";
    @Autowired
    private AliYunAuthenticateSigConfig aliYunAuthenticateSigConfig;

    @Autowired
    private Neo4jPlatUserService neo4jPlatUserService;

    /**
     * 密码前端加密后长度变了，不能使用正则
     *
     * @param platUserVO
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "registerType", isEnums = true, enums = "1,2", notNull = true, errMsg = "注册类型不正确"),
            @ValidateFiled(index = 0, filedName = "password", notNull = true, errMsg = "请输入格式正确的密码"),
            @ValidateFiled(index = 0, filedName = "mail", regStr = Constants.EMAIL_PATTERN, notNull = true, when = "{\"filedName\":\"registerType\",\"value\":\"2\"}", errMsg = "请输入正确的邮箱"),
            @ValidateFiled(index = 0, filedName = "mobile", regStr = "1[3|4|5|6|7|8|9][0-9]\\d{4,8}", notNull = true, when = "{\"filedName\":\"registerType\",\"value\":\"1\"}", errMsg = "请输入正确的手机号"),
            @ValidateFiled(index = 0, filedName = "code", notNull = false, errMsg = "验证码不正确"),
            @ValidateFiled(index = 0, filedName = "countryId", errMsg = "国籍不正确"/*, when = "{\"filedName\":\"registerType\",\"value\":\"1\"}"*/),
            @ValidateFiled(index = 0, filedName = "countryCode", errMsg = "区号不正确"/*, when = "{\"filedName\":\"registerType\",\"value\":\"1\"}"*/)
    })
    @PostMapping("/user/register")
    public Mono<GlobalMessageResponseVo> register(PlatUserVO platUserVO) {

        if ("0" .equals(registerSwitch)) {
            return Mono.just(GlobalMessageResponseVo.newErrorInstance("暂停注册！"));
        }

        Integer registerType = platUserVO.getRegisterType();
        String validCode = platUserVO.getCode();
        PlatUser platUser = new PlatUser();
        platUser.setIsAward("0");  //默认未奖励送币
        //如果没有邀请码，不让注册
        if(StringUtils.isEmpty(platUserVO.getInviteCode())){
            return Mono.just(GlobalMessageResponseVo.newErrorInstance("邀请码为空！"));
        }
        if (registerType == 1) {
            //手机注册,验证验证码
            platUser.setUsername(platUserVO.getMobile());
            platUser.setMobile(platUserVO.getMobile());
            platUser.setMobileAuditDate(LocalDateTime.now());
            //验证手机验证码
            //todo TEST  为了方便测试，输入验证码 123456 则为通过。
            if (!validCode.equals("123456")) {
                smsMessageService.validSmsCode(platUserVO.getMobile(), MessageTemplateCode.MOBILE_REGISTER_TEMPLATE.getCode(), validCode);
            }
        } else {
            //邮箱注册,验证验证码
            messageSendService.mailValid(MessageTemplateCode.REGISTER_TEMPLATE.getCode(), VerificationCodeType.REGISTER_CODE, platUserVO.getMail(), validCode);
            platUser.setUsername(platUserVO.getMail());
            platUser.setMail(platUserVO.getMail());
        }
        //解密密码
        String password = RsaUtils.decryptByPrivateKey(platUserVO.getPassword(), RsaUtils.DEFAULT_PRIVATE_KEY);
        platUser.setPassword(password);
        platUser.setInviteCode(platUserVO.getInviteCode());
        platUser.setCountryCode(platUserVO.getCountryCode());
        platUser.setCountryId(platUserVO.getCountryId());
        try {
            platUserService.registerPlatUser(platUser, true);
            neo4jPlatUserService.save(platUser); // 保存用户节点和关系到NEO4J库
        } catch (PlatException platException) {
            if (platException.getCode() == Constants.INVOTE_CODE_SYNC_ERROR) {
                //邀请码冲突，重试
                platUserService.registerPlatUser(platUser, true);
                neo4jPlatUserService.save(platUser); // 保存用户节点和关系到NEO4J库
            } else {
                throw platException;
            }
        }
        //清楚验证码
        messageSendService.clearValid(MessageTemplateCode.REGISTER_TEMPLATE.getCode(), VerificationCodeType.REGISTER_CODE.getCode(), platUserVO.getMail());
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance("注册成功"));
    }


    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, notNull = true, errMsg = "用户名不正确"),
    })
    @RequestMapping("/user/findUser")
    public Mono<GlobalMessageResponseVo> findExistUser(String username) {
        PlatUser platUser = platUserService.findByLoginName(username);
        if (platUser != null) {
            return Mono.just(GlobalMessageResponseVo.newErrorInstance("用户已经注册了"));
        }
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance("用户可以注册"));
    }

    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "mail", regStr = Constants.EMAIL_PATTERN, notNull = true, errMsg = "邮箱不正确"),
            @ValidateFiled(index = 0, filedName = "type", isEnums = true, enums = "login,register,message,reset,withdraw,binder", notNull = true, errMsg = "业务类型不能为空"),
            @ValidateFiled(index = 0, filedName = "code", errMsg = "图形验证码不能为空"),
            @ValidateFiled(index = 0, filedName = "source", errMsg = "客户端来源"),
            @ValidateFiled(index = 0, filedName = "appKey", errMsg = "请升级接口"),
            @ValidateFiled(index = 0, filedName = "sessionId", maxLen = 800, errMsg = "请升级接口"),
            @ValidateFiled(index = 0, filedName = "sig", maxLen = 664, errMsg = "sig,请升级接口"),
            @ValidateFiled(index = 0, filedName = "vtoken", errMsg = "vtoken,请升级接口"),
            @ValidateFiled(index = 0, filedName = "scene", errMsg = "请升级接口")
    })
    @PostMapping("/mail/sendCode")
    public Mono<GlobalMessageResponseVo> sendMessageCode(MessageVO messageVO) {
        VerificationCodeType typeEnums = VerificationCodeType.valueToEnums(messageVO.getType());
        //阿里滑块验证
        AuthenticateSigVO authenticateSigVO = new AuthenticateSigVO();
        String otcTag = messageVO.getAuthTag();
        authenticateSigVO.setAccessKeyId(aliYunAuthenticateSigConfig.getAccessKeyId());
        authenticateSigVO.setAccessKeySecret(aliYunAuthenticateSigConfig.getAccessKeySecret());
        if (StringUtils.isNotBlank(otcTag) && "otc" .equalsIgnoreCase(otcTag)) {
            authenticateSigVO.setAccessKeyId(aliYunAuthenticateSigConfig.getOtcAccessKeyId());
            authenticateSigVO.setAccessKeySecret(aliYunAuthenticateSigConfig.getOtcAccessKeySecret());
        }
        authenticateSigVO.setAppKey(messageVO.getAppKey());
        authenticateSigVO.setScene(messageVO.getScene());
        authenticateSigVO.setSessionId(messageVO.getSessionId());
        authenticateSigVO.setSig(messageVO.getSig());
        authenticateSigVO.setToken(messageVO.getVtoken());
        boolean isValid = AliYunAuthenticateSigUtils.isValid(authenticateSigVO);
        if (!isValid) {
            throw new PlatException(Constants.COMMON_ERROR_CODE, "验证码验证失败");
        }
        Map<String, Object> params = new HashMap<>();
        if (typeEnums == VerificationCodeType.REGISTER_CODE) {
            //邮箱注册邮件
            params.put(MessageSendService.PARAM_CODE, NumberUtils.getRandomNumber(6));
            messageSendService.mailSend(MessageTemplateCode.REGISTER_TEMPLATE.getCode(), typeEnums, messageVO.getMail(), params);
            return Mono.just(GlobalMessageResponseVo.newSuccessInstance("验证码发送成功"));
        } else if (typeEnums == VerificationCodeType.RESET_CODE) {
            //重置密码邮件
            String ptoken = UUID.randomUUID().toString().replace("-", "");
            String resetRequest = new StringBuilder(resetUrl).append("?ptoken=").append(ptoken).toString();
            params.put(MessageSendService.PARAM_PTOKEN_CODE, ptoken);
            params.put(MessageSendService.PARAM_CODE, resetRequest);
            messageSendService.mailSend(MessageTemplateCode.RESET_TEMPLATE.getCode(), typeEnums, messageVO.getMail(), params);
            return Mono.just(GlobalMessageResponseVo.newSuccessInstance("验证码发送成功"));
        } else if (typeEnums == VerificationCodeType.WITHDRAW_CODE) {
            params.put(MessageSendService.PARAM_CODE, NumberUtils.getRandomNumber(6));
            messageSendService.mailSend(MessageTemplateCode.WITHDRAW_TEMPLATE.getCode(), typeEnums, messageVO.getMail(), params);
            return Mono.just(GlobalMessageResponseVo.newSuccessInstance("验证码发送成功"));
        } else if (typeEnums == VerificationCodeType.BINDER_CODE) {
            params.put(MessageSendService.PARAM_CODE, NumberUtils.getRandomNumber(6));
            messageSendService.mailSend(MessageTemplateCode.EMAIL_BINDER_TEMPLATE.getCode(), typeEnums, messageVO.getMail(), params);
            return Mono.just(GlobalMessageResponseVo.newSuccessInstance("验证码发送成功"));
        }
        return Mono.just(GlobalMessageResponseVo.newErrorInstance("不支持的业务类型"));
    }

    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "mobile", regStr = "1[3|4|5|6|7|8|9][0-9]\\d{4,8}", notNull = true, errMsg = "手机号不正确"),
            @ValidateFiled(index = 0, filedName = "type", isEnums = true, enums = "login,register,message,reset,withdraw", notNull = true, errMsg = "请输入正确的业务类型"),
            @ValidateFiled(index = 0, filedName = "code", errMsg = "图形验证码不能为空"),
            @ValidateFiled(index = 0, filedName = "source", errMsg = "客户端来源"),
            @ValidateFiled(index = 0, filedName = "appKey", errMsg = "请升级接口"),
            @ValidateFiled(index = 0, filedName = "sessionId", maxLen = 800, errMsg = "请升级接口"),
            @ValidateFiled(index = 0, filedName = "sig", maxLen = 664, errMsg = "sig,请升级接口"),
            @ValidateFiled(index = 0, filedName = "vtoken", errMsg = "vtoken,请升级接口"),
            @ValidateFiled(index = 0, filedName = "scene", errMsg = "请升级接口")
    })
    @RequestMapping("/mobile/sendCode")
    public Mono<GlobalMessageResponseVo> sendSmsCode(MessageVO messageVO) {
        VerificationCodeType typeEnums = VerificationCodeType.valueToEnums(messageVO.getType());
        //阿里滑块验证
        AuthenticateSigVO authenticateSigVO = new AuthenticateSigVO();
        String otcTag = messageVO.getAuthTag();
        authenticateSigVO.setAccessKeyId(aliYunAuthenticateSigConfig.getAccessKeyId());
        authenticateSigVO.setAccessKeySecret(aliYunAuthenticateSigConfig.getAccessKeySecret());
        if (StringUtils.isNotBlank(otcTag) && "otc" .equalsIgnoreCase(otcTag)) {
            authenticateSigVO.setAccessKeyId(aliYunAuthenticateSigConfig.getOtcAccessKeyId());
            authenticateSigVO.setAccessKeySecret(aliYunAuthenticateSigConfig.getOtcAccessKeySecret());
        }
        authenticateSigVO.setAppKey(messageVO.getAppKey());
        authenticateSigVO.setScene(messageVO.getScene());
        authenticateSigVO.setSessionId(messageVO.getSessionId());
        authenticateSigVO.setSig(messageVO.getSig());
        authenticateSigVO.setToken(messageVO.getVtoken());
        boolean isValid = AliYunAuthenticateSigUtils.isValid(authenticateSigVO);
        if (!isValid) {
            return Mono.just(GlobalMessageResponseVo.newErrorInstance("验证码验证失败"));
        }
        if (typeEnums == VerificationCodeType.REGISTER_CODE) {
            //手机注册
            DisruptorData.saveSecurityLog(DisruptorData.buildSecurityLog(messageVO.getMobile(), MessageTemplateCode.MOBILE_REGISTER_TEMPLATE.getCode(), ""));
            //smsMessageService.sendSms(messageVO.getMobile(), MessageTemplateCode.MOBILE_REGISTER_TEMPLATE.getCode(), "");
            return Mono.just(GlobalMessageResponseVo.newSuccessInstance("验证码发送成功"));
        }
        if (typeEnums == VerificationCodeType.RESET_CODE) {
            //手机找回密码
            DisruptorData.saveSecurityLog(DisruptorData.buildSecurityLog(messageVO.getMobile(), MessageTemplateCode.MOBILE_RESET_TEMPLATE.getCode(), ""));
            //smsMessageService.sendSms(messageVO.getMobile(), MessageTemplateCode.MOBILE_RESET_TEMPLATE.getCode(), "");
            return Mono.just(GlobalMessageResponseVo.newSuccessInstance("验证码发送成功"));
        }
        return Mono.just(GlobalMessageResponseVo.newErrorInstance("不支持的业务类型"));
    }

    @RequestMapping("/user/createGoogleSecret")
    public Mono<GlobalMessageResponseVo> createGoogleSecret() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal).cast(RedisSessionUser.class)
                .flatMap(user -> {
                    Map<String, Object> data = new HashMap<>();
                    String gooleSecret = GoogleAuthenticator.generateSecretKey();
                    String googleKey = StringUtils.isNotBlank(user.getMail()) ? user.getMail() : user.getMobile();
                    String qrcodeContent = GoogleAuthenticator.getQRBarcode(googleKey, gooleSecret, domainVaule);
                    data.put("secret", gooleSecret);
                    data.put("qrcodeContent", qrcodeContent);
                    //1:手机  2:邮箱
                    Integer type = StringUtils.isNotBlank(user.getMobile()) ? 1 : 2;
                    data.put("type", type);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(data));
                }).subscribeOn(Schedulers.parallel());
    }

    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "mobile", regStr = "1[3|4|5|6|7|8|9][0-9]\\d{4,8}", notNull = true, errMsg = "请输入正确的手机号")
    })
    @PostMapping("/user/sendMobileSms")
    public Mono<GlobalMessageResponseVo> sendMobileSms(PlatUserVO platUserVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    DisruptorData.saveSecurityLog(DisruptorData.buildSecurityLog(platUserVO.getMobile(), MessageTemplateCode.MOBILE_BINDER_TEMPLATE.getCode(), ""));
                    //smsMessageService.sendSms(platUserVO.getMobile(), MessageTemplateCode.MOBILE_BINDER_TEMPLATE.getCode(), "");
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("发送短信成功"));
                });
    }

    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "smsType", isEnums = true, enums = "ex_password,bander_google,ex_mobile,exchange_pass", notNull = true, errMsg = "请输入正确的业务类型")
    })
    @PostMapping("/user/mobile/sendMobileSms")
    public Mono<GlobalMessageResponseVo> sendUserMobileSms(PlatUserVO platUserVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    if (StringUtils.isBlank(user.getMobile())) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("请先绑定手机号"));
                    }
                    VerificationCodeType typeEnums = VerificationCodeType.valueToEnums(platUserVO.getSmsType());
                    if (typeEnums == VerificationCodeType.EX_TRADE_PASS) {
                        //设置交易密码
                        DisruptorData.saveSecurityLog(DisruptorData.buildSecurityLog(user.getMobile(), MessageTemplateCode.MOBILE_TRADE_PASSWORD_TEMPLATE.getCode(), ""));
                        //smsMessageService.sendSms(user.getMobile(), MessageTemplateCode.MOBILE_TRADE_PASSWORD_TEMPLATE.getCode(), "");
                        return Mono.just(GlobalMessageResponseVo.newSuccessInstance("验证码发送成功"));
                    }
                    if (typeEnums == VerificationCodeType.BINDER_GOOGLE_CODE) {
                        //设置交易密码
                        DisruptorData.saveSecurityLog(DisruptorData.buildSecurityLog(user.getMobile(), MessageTemplateCode.MOBILE_BINDER_GOOGLE_TEMPLATE.getCode(), ""));
                        //smsMessageService.sendSms(user.getMobile(), MessageTemplateCode.MOBILE_BINDER_GOOGLE_TEMPLATE.getCode(), "");
                        return Mono.just(GlobalMessageResponseVo.newSuccessInstance("验证码发送成功"));
                    }
                    if (typeEnums == VerificationCodeType.EX_TRADE_MOBILE) {
                        //交易验证类型
                        DisruptorData.saveSecurityLog(DisruptorData.buildSecurityLog(user.getMobile(), MessageTemplateCode.MOBILE_EX_TRADE_TEMPLATE.getCode(), ""));
                        //smsMessageService.sendSms(user.getMobile(), MessageTemplateCode.MOBILE_EX_TRADE_TEMPLATE.getCode(), "");
                        return Mono.just(GlobalMessageResponseVo.newSuccessInstance("验证码发送成功"));
                    }
                    if (typeEnums == VerificationCodeType.EX_EXCHANGE_PASS) {
                        //设置交易密码
                        DisruptorData.saveSecurityLog(DisruptorData.buildSecurityLog(user.getMobile(), MessageTemplateCode.MOBILE_EXCHANGE_PASS_TEMPLATE.getCode(), ""));
                        //smsMessageService.sendSms(user.getMobile(), MessageTemplateCode.MOBILE_EXCHANGE_PASS_TEMPLATE.getCode(), "");
                        return Mono.just(GlobalMessageResponseVo.newSuccessInstance("验证码发送成功"));
                    }
                    return Mono.just(GlobalMessageResponseVo.newErrorInstance("不支持的业务类型"));
                });
    }

    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "smsType", isEnums = true, enums = "ex_password_mail,ex_mail,exchange_pass,bander_google", notNull = true, errMsg = "请输入正确的业务类型")
    })
    @PostMapping("/user/mail/sendMail")
    public Mono<GlobalMessageResponseVo> sendUserMail(PlatUserVO platUserVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    if (StringUtils.isBlank(user.getMail())) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("请先绑定邮箱"));
                    }
                    if (platUserVO.getSmsType().equals("bander_google")) {
                        platUserVO.setSmsType(VerificationCodeType.BINDER_GOOGLE_CODE_MAIL.getCode());
                    }
                    VerificationCodeType typeEnums = VerificationCodeType.valueToEnums(platUserVO.getSmsType());
                    Map<String, Object> params = new HashMap<>();
                    if (typeEnums == VerificationCodeType.EX_TRADE_MAIL) {
                        params.put(MessageSendService.PARAM_CODE, NumberUtils.getRandomNumber(6));
                        messageSendService.mailSend(MessageTemplateCode.EMAIL_EX_TRADE_TEMPLATE.getCode(), typeEnums, user.getMail(), params);
                        return Mono.just(GlobalMessageResponseVo.newSuccessInstance("验证码发送成功"));
                    }
                    if (typeEnums == VerificationCodeType.EX_EXCHANGE_PASS) {
                        params.put(MessageSendService.PARAM_CODE, NumberUtils.getRandomNumber(6));
                        messageSendService.mailSend(MessageTemplateCode.EMAIL_EXCHANGE_PASS_TEMPLATE.getCode(), typeEnums, user.getMail(), params);
                        return Mono.just(GlobalMessageResponseVo.newSuccessInstance("验证码发送成功"));
                    }
                    if (typeEnums == VerificationCodeType.EX_PASS_MAIL) {
                        params.put(MessageSendService.PARAM_CODE, NumberUtils.getRandomNumber(6));
                        messageSendService.mailSend(MessageTemplateCode.EMAIL_TRADE_PASSWORD_TEMPLATE.getCode(), typeEnums, user.getMail(), params);
                        return Mono.just(GlobalMessageResponseVo.newSuccessInstance("验证码发送成功"));
                    }
                    if (typeEnums == VerificationCodeType.BINDER_GOOGLE_CODE_MAIL) {
                        params.put(MessageSendService.PARAM_CODE, NumberUtils.getRandomNumber(6));
                        messageSendService.mailSend(MessageTemplateCode.EMAIL_BINDER_GOOGLE_TEMPLATE.getCode(), typeEnums, user.getMail(), params);
                        return Mono.just(GlobalMessageResponseVo.newSuccessInstance("验证码发送成功"));
                    }
                    return Mono.just(GlobalMessageResponseVo.newErrorInstance("不支持的业务类型"));
                });
    }

    /**
     * 修改手机号
     *
     * @param platUserVO
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "mobile", regStr = "1[3|4|5|6|7|8|9][0-9]\\d{4,8}", errMsg = "请输入正确的手机号"),
    })
    @PostMapping("/user/updateMobileSms")
    public Mono<GlobalMessageResponseVo> sendUpdateMobileSms(PlatUserVO platUserVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    String mobile = platUserVO.getMobile();
                    if (StringUtils.isBlank(mobile)) {
                        mobile = user.getMobile();
                    }
                    //smsMessageService.sendSms(mobile, MessageTemplateCode.MOBILE_BINDER_UPDATE.getCode(), "");
                    DisruptorData.saveSecurityLog(DisruptorData.buildSecurityLog(mobile, MessageTemplateCode.MOBILE_BINDER_UPDATE.getCode(), ""));
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("发送短信成功"));
                });
    }

    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, regStr = "1[3|4|5|6|7|8|9][0-9]\\d{4,8}", filedName = "mobile", notNull = true, errMsg = "请输入正确的手机号"),
            @ValidateFiled(index = 0, regStr = "\\d{4,6}", filedName = "code", notNull = true, errMsg = "请输入正确的手机号验证码"),
            @ValidateFiled(index = 0, filedName = "googleCode", regStr = "\\d{4,6}", errMsg = "谷歌验证码不正确")
    })
    @PostMapping("/user/mobileBinder")
    public Mono<GlobalMessageResponseVo> binderMobile(SmsMessageVO messageVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    if (smsMessageService.validSmsCode(messageVO.getMobile(), MessageTemplateCode.MOBILE_BINDER_TEMPLATE.getCode(), messageVO.getCode())) {
                        PlatUser exist = platUserService.findByLoginName(messageVO.getMobile());
                        if (exist != null) {
                            com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                                    com.biao.reactive.data.mongo.disruptor.DisruptorData.
                                            buildSecurityLog(SecurityLogEnums.SECURITY_BINDER_MOBILE, 1, "该手机号已经绑定了", user.getId(), messageVO.getMobile(), user.getMail()));
                            return Mono.just(GlobalMessageResponseVo.newErrorInstance("该手机号已经绑定了"));
                        }
                        PlatUser platUser = new PlatUser();
                        platUser.setMobile(messageVO.getMobile());
                        platUser.setId(user.getId());
                        platUser.setMobileAuditDate(LocalDateTime.now()); //手机认证时间
                        platUserService.updateById(platUser);
                        //更新redis数据
                        user.setMobile(messageVO.getMobile());
                        stringRedisTemplate.opsForHash().put(SercurityConstant.SESSION_TOKEN_REDIS_NAMESAPCE + user.getToken(), SercurityConstant.SESSION_TOKEN_REDIS_USER, JsonUtils.toJson(user));
                        //异步解析用户归属地
                        DisruptorManager.instance().runConfig();
                        DisruptorData data = new DisruptorData();
                        data.setType(3);
                        platUser.setReferId(user.getReferId());
                        data.setPlatUser(platUser);
                        DisruptorManager.instance().publishData(data);
                        com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                                com.biao.reactive.data.mongo.disruptor.DisruptorData.
                                        buildSecurityLog(SecurityLogEnums.SECURITY_BINDER_MOBILE, 0, "绑定手机号", user.getId(), messageVO.getMobile(), user.getMail()));
                        return Mono.just(GlobalMessageResponseVo.newSuccessInstance("绑定手机号成功"));
                    }
                    return Mono.just(GlobalMessageResponseVo.newErrorInstance("绑定手机号失败"));
                });
    }

    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "mail", regStr = Constants.EMAIL_PATTERN, notNull = true, errMsg = "请输入正确的邮箱"),
            @ValidateFiled(index = 0, regStr = "\\d{4,6}", filedName = "code", notNull = true, errMsg = "请输入正确的邮箱验证码")
    })
    @PostMapping("/user/binderEmail")
    public Mono<GlobalMessageResponseVo> binderEmail(SmsMessageVO messageVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    messageSendService.mailValid(MessageTemplateCode.EMAIL_BINDER_TEMPLATE.getCode(), VerificationCodeType.BINDER_CODE, messageVO.getMail(), messageVO.getCode());
                    PlatUser exist = platUserService.findByLoginName(messageVO.getMail());
                    if (exist != null) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("该邮箱已经绑定了"));
                    }
                    PlatUser platUser = new PlatUser();
                    platUser.setMail(messageVO.getMail());
                    platUser.setId(user.getId());
                    platUserService.updateById(platUser);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("绑定邮箱成功"));
                });
    }

    @RequestMapping("/user/invotes")
    public Mono<GlobalMessageResponseVo> userInvites(UserLoginLogQuery query) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    query.setUserId(user.getId());
                    ResponsePage<PlatUser> responsePage = platUserService.findInvitesById(query);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(responsePage));
                });
    }

    @RequestMapping("/user/findCardStatus")
    public Mono<GlobalMessageResponseVo> findCardStatus() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    Map<String, Object> map = new HashMap<>();
                    PlatUser platUser = platUserService.findById(user.getId());
                    map.put("cardStatus", platUser.getCardStatus());
                    map.put("reason", platUser.getCardStatusReason());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(map));
                });
    }

    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "secret", notNull = true, errMsg = "谷歌秘钥不正确"),
            @ValidateFiled(index = 0, filedName = "code", notNull = true, errMsg = "谷歌验证码不正确", maxLen = 6, minLen = 4),
            @ValidateFiled(index = 0, filedName = "smsCode", notNull = true, errMsg = "短信验证码不正确", maxLen = 6, minLen = 4)
    })
    @PostMapping("/user/googleBinder")
    public Mono<GlobalMessageResponseVo> googleBinder(PlatUserVO platUserVO) {
        GoogleAuthenticator ga = new GoogleAuthenticator();
        ga.setWindowSize(5);
        boolean result = ga.checkCode(platUserVO.getSecret(), Long.parseLong(platUserVO.getCode()), System.currentTimeMillis());
        if (result) {
            logger.info("绑定谷歌,谷歌验证成功，smsCode:{}", platUserVO.getSmsCode());
            //更新数据库
            return ReactiveSecurityContextHolder.getContext()
                    .filter(c -> c.getAuthentication() != null)
                    .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> this.updateGoogleBinder(user, platUserVO.getSecret(), platUserVO.getSmsCode()));
        }
        return Mono.just(GlobalMessageResponseVo.newErrorInstance("验证失败"));
    }

    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "nickName", notNull = true, errMsg = "昵称不正确"),
    })
    @PostMapping("/user/updateNickName")
    public Mono<GlobalMessageResponseVo> updateNickName(PlatUserVO platUserVO) {
        //更新数据库
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    PlatUser platUser = new PlatUser();
                    platUser.setNickName(platUserVO.getNickName());
                    ;
                    platUser.setId(user.getId());
                    platUserService.updateNickNameById(platUser);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("更新昵称成功"));
                });
    }

    private Mono<GlobalMessageResponseVo> updateGoogleBinder(RedisSessionUser user, String secret, String smsCode) {
        //验证短信验证码
        Integer mobileOrMail = StringUtils.isNotBlank(user.getMobile()) ? 1 : 2;
        if (mobileOrMail == 1) {
            //手机验证
            if (!smsMessageService.validSmsCode(user.getMobile(), MessageTemplateCode.MOBILE_BINDER_GOOGLE_TEMPLATE.getCode(), smsCode)) {
                com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                        com.biao.reactive.data.mongo.disruptor.DisruptorData.
                                buildSecurityLog(SecurityLogEnums.SECURITY_BINDER_GOOGLE, 1, "手机验证码验证失败",
                                        user.getId(), user.getMobile(), user.getMail()));
                return Mono.just(GlobalMessageResponseVo.newErrorInstance("手机验证码验证失败"));
            }
        } else {
            //邮件验证
            logger.info("绑定谷歌mail:{},验证code:{}", mobileOrMail, smsCode);
            messageSendService.mailValid(MessageTemplateCode.EMAIL_BINDER_GOOGLE_TEMPLATE.getCode(), VerificationCodeType.BINDER_GOOGLE_CODE_MAIL, user.getMail(), smsCode);
        }
        PlatUser platUser = new PlatUser();
        platUser.setGoogleAuth(secret);
        platUser.setId(user.getId());
        platUserService.updateGoogleAuthById(platUser);
        user.setGoogleAuth(secret);
        logger.info("用户userId:{},设置谷歌成功", user.getId());
        //更新redis数据
        stringRedisTemplate.opsForHash().put(SercurityConstant.SESSION_TOKEN_REDIS_NAMESAPCE + user.getToken(), SercurityConstant.SESSION_TOKEN_REDIS_USER, JsonUtils.toJson(user));
        com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                com.biao.reactive.data.mongo.disruptor.DisruptorData.
                        buildSecurityLog(SecurityLogEnums.SECURITY_BINDER_GOOGLE, 0, SecurityLogEnums.SECURITY_BINDER_GOOGLE.getMessage(),
                                user.getId(), user.getMobile(), user.getMail()));
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance("验证成功"));
    }


    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "cardUpId", notNull = true, errMsg = "身份证正面不正确"),
            @ValidateFiled(index = 0, filedName = "cardDownId", notNull = true, errMsg = "身份证反面不正确"),
            @ValidateFiled(index = 0, filedName = "cardFaceId", notNull = true, errMsg = "手持身份证不正确"),
            @ValidateFiled(index = 0, filedName = "idCard", notNull = true, errMsg = "请输入正确的身份证"),
            @ValidateFiled(index = 0, filedName = "age", notNull = true, regStr = "\\d{2}", errMsg = "请输入正确的年龄"),
            @ValidateFiled(index = 0, filedName = "sex", notNull = true, isEnums = true, enums = "1,2", errMsg = "性别不正确"),
            @ValidateFiled(index = 0, filedName = "realName", notNull = true, errMsg = "真实名称不正确"),
            @ValidateFiled(index = 0, filedName = "address", notNull = true, errMsg = "身份证地址不正确"),
            @ValidateFiled(index = 0, filedName = "countryCode", notNull = true, errMsg = "国家码不正确")
    })
    @PostMapping("/user/updateUser")
    public Mono<GlobalMessageResponseVo> updateUser(PlatUserVO platUserVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    if (user.getCardLevel() != null && (user.getCardLevel() == CardStatusEnum.CARD_STATUS_ONE.getCode() || user.getCardLevel() == CardStatusEnum.CARD_STATUS_TWO.getCode())) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("身份资料已经提交"));
                    }
                    if (UserCardStatusEnum.APPLY.getCode().equals(String.valueOf(user.getCardStatus())) || UserCardStatusEnum.TWO_APPLY.getCode().equals(String.valueOf(user.getCardStatus()))) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("身份资料已经提交"));
                    }
                    PlatUser platUser = new PlatUser();
                    platUser.setId(user.getId());
                    platUser.setIdCard(platUserVO.getIdCard());
                    platUser.setCardDownId(platUserVO.getCardDownId());
                    platUser.setAge(platUserVO.getAge());
                    platUser.setCardFaceId(platUserVO.getCardFaceId());
                    platUser.setCardUpId(platUserVO.getCardUpId());
                    platUser.setRealName(platUserVO.getRealName().trim());
                    platUser.setCountryCode(platUserVO.getCountryCode());
                    //男 1 女2
                    platUser.setSex(platUserVO.getSex());
                    platUser.setRemarks(platUserVO.getAddress().trim());
                    if (platUserVO.getCountryCode().equalsIgnoreCase("00")) {
                        //境内用户
                        platUser.setCardLevel(CardStatusEnum.CARD_STATUS_ZERO.getCode());
                        platUser.setCardStatus(Integer.parseInt(UserCardStatusEnum.APPLY.getCode()));
                        user.setCardStatus(Integer.parseInt(UserCardStatusEnum.APPLY.getCode()));
                    } else {
                        platUser.setCardStatus(Integer.parseInt(UserCardStatusEnum.TWO_APPLY.getCode()));
                        user.setCardStatus(Integer.parseInt(UserCardStatusEnum.TWO_APPLY.getCode()));
                    }
                    platUser.setCardStatusReason(" ");
                    platUserService.updateCardById(platUser);
                    user.setCardStatus(Integer.parseInt(UserCardStatusEnum.APPLY.getCode()));
                    stringRedisTemplate.opsForHash().put(SercurityConstant.SESSION_TOKEN_REDIS_NAMESAPCE + user.getToken(), SercurityConstant.SESSION_TOKEN_REDIS_USER, JsonUtils.toJson(user));
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("提交资料成功"));
                });
    }

    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "idCard", notNull = true, errMsg = "请输入正确的身份证"),
            @ValidateFiled(index = 0, filedName = "age", notNull = true, regStr = "\\d{2}", errMsg = "请输入正确的年龄"),
            @ValidateFiled(index = 0, filedName = "sex", notNull = true, isEnums = true, enums = "1,2", errMsg = "性别不正确"),
            @ValidateFiled(index = 0, filedName = "realName", notNull = true, errMsg = "真实名称不正确"),
            @ValidateFiled(index = 0, filedName = "address", notNull = true, errMsg = "身份证地址不正确"),
            @ValidateFiled(index = 0, filedName = "countryCode", notNull = true, errMsg = "国家码不正确")
    })
    @PostMapping("/user/updateUserCardOne")
    public Mono<GlobalMessageResponseVo> updateUserCardOne(PlatUserVO platUserVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    if (user.getCardLevel() != null && user.getCardLevel() != CardStatusEnum.CARD_STATUS_ZERO.getCode()) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("请进行身份认证"));
                    }
                    if (user.getCardStatus() != null && user.getCardStatus() == Integer.parseInt(UserCardStatusEnum.APPLY.getCode())) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("身份资料已经提交"));
                    }
                    PlatUser platUser = new PlatUser();
                    platUser.setCountryCode(platUserVO.getCountryCode());
                    platUser.setId(user.getId());
                    platUser.setIdCard(platUserVO.getIdCard());
                    platUser.setAge(platUserVO.getAge());
                    platUser.setRealName(platUserVO.getRealName().trim());
                    platUser.setCountryCode(platUserVO.getCountryCode());
                    //男 1 女2
                    platUser.setSex(platUserVO.getSex());
                    platUser.setRemarks(platUserVO.getAddress().trim());
                    platUser.setCardStatus(Integer.parseInt(UserCardStatusEnum.APPLY.getCode()));
                    platUser.setCardLevel(CardStatusEnum.CARD_STATUS_ZERO.getCode());
                    platUser.setCardStatusReason(" ");
                    platUserService.updateCardById(platUser);
                    user.setCardStatus(Integer.parseInt(UserCardStatusEnum.APPLY.getCode()));
                    stringRedisTemplate.opsForHash().put(SercurityConstant.SESSION_TOKEN_REDIS_NAMESAPCE + user.getToken(), SercurityConstant.SESSION_TOKEN_REDIS_USER, JsonUtils.toJson(user));
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("提交资料成功"));
                });
    }

    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "cardUpId", notNull = true, errMsg = "身份证正面不正确"),
            @ValidateFiled(index = 0, filedName = "cardDownId", notNull = true, errMsg = "身份证反面不正确"),
            @ValidateFiled(index = 0, filedName = "cardFaceId", errMsg = "手持身份证不正确")
    })
    @PostMapping("/user/updateUserCardTwo")
    public Mono<GlobalMessageResponseVo> updateUserCardTwo(PlatUserVO platUserVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    if (user.getCardLevel() == null || (user.getCardLevel() == CardStatusEnum.CARD_STATUS_ZERO.getCode() || user.getCardLevel() == CardStatusEnum.CARD_STATUS_TWO.getCode())) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("请先进行v1认证"));
                    }
                    PlatUser platUser = new PlatUser();
                    platUser.setId(user.getId());
                    platUser.setCardDownId(platUserVO.getCardDownId());
                    platUser.setCardFaceId(platUserVO.getCardFaceId());
                    platUser.setCardUpId(platUserVO.getCardUpId());
                    //男 1 女2
                    platUser.setCardStatus(Integer.parseInt(UserCardStatusEnum.TWO_APPLY.getCode()));
                    platUser.setCardStatusReason(" ");
                    platUserService.updateCardById(platUser);
                    user.setCardStatus(Integer.parseInt(UserCardStatusEnum.TWO_APPLY.getCode()));
                    stringRedisTemplate.opsForHash().put(SercurityConstant.SESSION_TOKEN_REDIS_NAMESAPCE + user.getToken(), SercurityConstant.SESSION_TOKEN_REDIS_USER, JsonUtils.toJson(user));
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("提交资料成功"));
                });
    }


    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "tempToken", notNull = true, errMsg = "token不正确"),
            @ValidateFiled(index = 0, filedName = "tokenType", isEnums = true, enums = "1,0", notNull = true, errMsg = "验证类型不正确"),
            @ValidateFiled(index = 0, filedName = "code", regStr = "\\d{4,6}", notNull = true, errMsg = "验证码不正确"),
    })
    @PostMapping("user/loginValid")
    public Mono<GlobalMessageResponseVo> loginValid(LoginTempValidVO tempValidVO) {
        Integer type = tempValidVO.getTokenType();
        String redisKey = "temp:" + tempValidVO.getTokenType() + "-" + tempValidVO.getTempToken();
        if (!valOpsStr.getOperations().hasKey(redisKey)) {
            return Mono.just(GlobalMessageResponseVo.newErrorInstance("token已经失效，请重新登录"));
        }
        String loginUser = valOpsStr.get(redisKey);
        if (StringUtils.isBlank(loginUser)) {
            return Mono.just(GlobalMessageResponseVo.newErrorInstance("token已经失效，请重新登录"));
        }
        valOpsStr.getOperations().delete(redisKey);
        boolean tag = false;
        RedisSessionUser user = JsonUtils.fromJson(loginUser, RedisSessionUser.class);
        if (type == 0) {
            //发送用户短信
            tag = smsMessageService.validSmsCode(user.getMobile(), MessageTemplateCode.MOBILE_LOGIN_TEMPLATE.getCode(), tempValidVO.getCode());
        } else if (type == 1) {
            //发送用户邮件
            messageSendService.mailValid(MessageTemplateCode.LOGIN_TEMPLATE.getCode(), VerificationCodeType.LOGIN_MAIL_CODE, user.getMail(), tempValidVO.getCode());
            tag = true;
        }
        if (!tag) {
            return Mono.just(GlobalMessageResponseVo.newErrorInstance("验证码验证失败"));
        }
        String authorizationToken = user.getToken();
        String userRedisKey = SercurityConstant.SESSION_TOKEN_REDIS_NAMESAPCE + authorizationToken;
        //使用hash结构,存取用户
        stringRedisTemplate.opsForHash().put(userRedisKey, SercurityConstant.SESSION_TOKEN_REDIS_USER, JsonUtils.toJson(user));
        stringRedisTemplate.expire(userRedisKey, SercurityConstant.REDIS_EXPIRE_TIME_SESSION, TimeUnit.SECONDS);
        user.setPassword(null);
        if (StringUtils.isNotEmpty(user.getGoogleAuth())) {
            user.setGoogleAuth("true");
        }
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(user));
    }


    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, regStr = "\\d{4,6}", filedName = "oldCode", notNull = true, errMsg = "请输入正确的手机号验证码"),
            @ValidateFiled(index = 0, regStr = "1[3|4|5|6|7|8|9][0-9]\\d{4,8}", filedName = "newMobile", notNull = true, errMsg = "请输入正确的手机号"),
            @ValidateFiled(index = 0, regStr = "\\d{4,6}", filedName = "newCode", notNull = true, errMsg = "请输入正确的手机号验证码")
    })
    @PostMapping("/user/updateUserMobile")
    public Mono<GlobalMessageResponseVo> updateUserMobile(MobileVO mobileVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    //验证
                    if (user.getMobile().equalsIgnoreCase(mobileVO.getNewMobile())) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("手机号输入错误"));
                    }
                    if (!smsMessageService.validSmsCode(user.getMobile(), MessageTemplateCode.MOBILE_BINDER_UPDATE.getCode(), mobileVO.getOldCode())) {
                        com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                                com.biao.reactive.data.mongo.disruptor.DisruptorData.
                                        buildSecurityLog(SecurityLogEnums.SECURITY_UPDATE_MOBILE, 1, "手机验证码验证失败",
                                                user.getId(), mobileVO.getNewMobile(), user.getMail()));
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("手机验证码验证失败"));
                    }
                    if (!smsMessageService.validSmsCode(mobileVO.getNewMobile(), MessageTemplateCode.MOBILE_BINDER_UPDATE.getCode(), mobileVO.getNewCode())) {
                        com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                                com.biao.reactive.data.mongo.disruptor.DisruptorData.
                                        buildSecurityLog(SecurityLogEnums.SECURITY_UPDATE_MOBILE, 1, "新手机验证码验证失败",
                                                user.getId(), mobileVO.getNewMobile(), user.getMail()));
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("新手机验证码验证失败"));
                    }
                    PlatUser platUser = platUserService.findByLoginName(mobileVO.getNewMobile());
                    if (platUser != null) {
                        com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                                com.biao.reactive.data.mongo.disruptor.DisruptorData.
                                        buildSecurityLog(SecurityLogEnums.SECURITY_UPDATE_MOBILE, 1, "该手机号已经绑定了",
                                                user.getId(), mobileVO.getNewMobile(), user.getMail()));
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("该手机号已经绑定了"));
                    }
                    String oldMoblile = user.getMobile();
                    LocalDateTime lockDate = userConfig.plusHours();
                    PlatUser updatePlatUser = new PlatUser();
                    updatePlatUser.setId(user.getId());
                    updatePlatUser.setMobile(mobileVO.getNewMobile());
                    updatePlatUser.setLockDate(lockDate);
                    platUserService.updateById(updatePlatUser);
                    user.setMobile(mobileVO.getNewMobile());
                    user.setLockDate(lockDate);
                    //更新redis数据
                    stringRedisTemplate.opsForHash().put(SercurityConstant.SESSION_TOKEN_REDIS_NAMESAPCE + user.getToken(), SercurityConstant.SESSION_TOKEN_REDIS_USER, JsonUtils.toJson(user));
                    com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                            com.biao.reactive.data.mongo.disruptor.DisruptorData.
                                    buildSecurityLog(SecurityLogEnums.SECURITY_UPDATE_MOBILE, 0, SecurityLogEnums.SECURITY_UPDATE_MOBILE.getMessage(),
                                            user.getId(), mobileVO.getNewMobile(), user.getMail()));

                    //异步解析用户归属地
                    DisruptorManager.instance().runConfig();
                    DisruptorData data = new DisruptorData();
                    data.setType(9);
                    PlatUserOplog platUserOplog = new PlatUserOplog();
                    platUserOplog.setContent("手机号:" + oldMoblile + "->" + mobileVO.getNewMobile());
                    platUserOplog.setCreateBy(user.getId());
                    platUserOplog.setCreateByName("plat");
                    platUserOplog.setCreateDate(LocalDateTime.now());
                    platUserOplog.setMail(user.getMail());
                    platUserOplog.setMobile(user.getMobile());
                    platUserOplog.setRealName(user.getRealName());
                    platUserOplog.setReason("用户修改手机号,锁定用户");
                    platUserOplog.setId(SnowFlake.createSnowFlake().nextIdString());
                    platUserOplog.setType(PlatUserOplogTypeEnum.LOCK_DATE.getCode());
                    platUserOplog.setUserId(user.getId());
                    platUserOplog.setUpdateDate(LocalDateTime.now());
                    platUserOplog.setUpdateByName("plat");
                    platUserOplog.setUpdateBy(user.getId());
                    data.setPlatUserOplog(platUserOplog);
                    DisruptorManager.instance().publishData(data);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("更新资料成功"));
                });
    }

    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "code", notNull = true, errMsg = "谷歌验证码不正确", maxLen = 6, minLen = 4)
    })
    @PostMapping("/user/googleValid")
    public Mono<GlobalMessageResponseVo> googleValid(PlatUserVO platUserVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> this.googleValid(user, platUserVO.getCode()));
    }

    private Mono<GlobalMessageResponseVo> googleValid(RedisSessionUser user, String code) {
        Integer exValidType = user.getExValidType();
        if (exValidType == null) {
            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.USER_TRADE_VALID_TYPE_CODE, "请绑定交易验证方式"));
        }
        if (exValidType == 1 && StringUtils.isBlank(user.getGoogleAuth())) {
            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.USER_TRADE_VALID_TYPE_CODE, "请绑定谷歌"));
        }
        if (exValidType == 3 && StringUtils.isBlank(user.getMobile())) {
            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.USER_TRADE_VALID_TYPE_CODE, "请绑定手机"));
        }
        if (exValidType == 4 && StringUtils.isBlank(user.getMail())) {
            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.USER_TRADE_VALID_TYPE_CODE, "请绑定邮箱"));
        }
        boolean validResult = false;
        if (exValidType == 1) {
            PlatUser platUser = this.platUserService.findById(user.getId());
            if (StringUtils.isBlank(platUser.getGoogleAuth())) {
                return Mono.just(GlobalMessageResponseVo.newInstance(Constants.USER_TRADE_VALID_TYPE_CODE, "请绑定谷歌"));
            }
            GoogleAuthenticator ga = new GoogleAuthenticator();
            ga.setWindowSize(5);
            validResult = ga.checkCode(platUser.getGoogleAuth(), Integer.parseInt(code), System.currentTimeMillis());
        }
        if (exValidType == 3) {
            //短信验证
            validResult = smsMessageService.validSmsCode(user.getMobile(), MessageTemplateCode.MOBILE_EX_TRADE_TEMPLATE.getCode(), code);
        }
        if (exValidType == 4) {
            //邮箱验证
            try {
                messageSendService.mailValid(MessageTemplateCode.EMAIL_EX_TRADE_TEMPLATE.getCode(), VerificationCodeType.EX_TRADE_MAIL, user.getMail(), code);
                validResult = true;
            } catch (Exception e) {
                validResult = false;
            }
        }
        if (validResult) {
            //设置谷歌验证码2小时失效
            String authKey = SercurityConstant.AUTH_USERNAME_REDIS_NAMESAPCE + user.getUsername();
            valOpsStr.set(authKey, SercurityConstant.AuthType.PASS_AUTH.getCode(), SercurityConstant.REDIS_EXPIRE_TIME_ONE_HOUR * 2, TimeUnit.SECONDS);
            return Mono.just(GlobalMessageResponseVo.newSuccessInstance("交易验证成功"));
        }
        return Mono.just(GlobalMessageResponseVo.newErrorInstance("交易验证失败"));
    }

    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "password", notNull = true, errMsg = "请输入格式正确的密码"),
            @ValidateFiled(index = 0, filedName = "oldPassword", notNull = true, errMsg = "请输入格式正确的原始密码"),
    })
    @PostMapping("/user/updatePassword")
    public Mono<GlobalMessageResponseVo> findPassword(PlatUserVO platUserVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> this.updatePassword(user, platUserVO));
    }

    private Mono<GlobalMessageResponseVo> updatePassword(RedisSessionUser user, PlatUserVO platUserVO) {
        PlatUser platUser = new PlatUser();
        platUser.setId(user.getId());
        String decryPassword = RsaUtils.decryptByPrivateKey(platUserVO.getPassword(), RsaUtils.DEFAULT_PRIVATE_KEY);
        String decryOldPassword = RsaUtils.decryptByPrivateKey(platUserVO.getOldPassword(), RsaUtils.DEFAULT_PRIVATE_KEY);
        platUser.setPassword(decryPassword);
        platUser.setOldPassword(decryOldPassword);
        try {
            platUserService.updatePassword(platUser);
            com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                    com.biao.reactive.data.mongo.disruptor.DisruptorData.
                            buildSecurityLog(SecurityLogEnums.SECURITY_UPDATE_PASSWORD, 0, SecurityLogEnums.SECURITY_UPDATE_PASSWORD.getMessage(),
                                    user.getId(), user.getMobile(), user.getMail()));
        } catch (Exception e) {
            com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                    com.biao.reactive.data.mongo.disruptor.DisruptorData.
                            buildSecurityLog(SecurityLogEnums.SECURITY_UPDATE_PASSWORD, 1, "更新密码失败," + e.getMessage(),
                                    user.getId(), user.getMobile(), user.getMail()));
            throw e;
        }
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance("更新密码成功"));
    }

    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "password", notNull = true, errMsg = "请输入格式正确的密码"),
            @ValidateFiled(index = 0, filedName = "ptoken", notNull = true, errMsg = "重置密码token不能为空"),
    })
    @PostMapping("/user/mail/resetpwd")
    public Mono<GlobalMessageResponseVo> resetPassword(PlatUserVO platUserVO) {
        PlatUser platUser = new PlatUser();
        String decryPassword = RsaUtils.decryptByPrivateKey(platUserVO.getPassword(), RsaUtils.DEFAULT_PRIVATE_KEY);
        platUser.setPassword(decryPassword);
        platUser.setPtoken(platUserVO.getPtoken());
        PlatUser updatePlat = platUserService.resetPassword(platUser);
        com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                com.biao.reactive.data.mongo.disruptor.DisruptorData.
                        buildSecurityLog(SecurityLogEnums.SECURITY_RESET_PASSWORD, 0, "邮箱重置密码",
                                updatePlat.getId(), "", ""));
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance("重置密码成功"));
    }

    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "mobile", regStr = "1[3|4|5|6|7|8|9][0-9]\\d{4,8}", notNull = true, errMsg = "请输入格式正确的密码"),
            @ValidateFiled(index = 0, filedName = "password", notNull = true, errMsg = "请输入格式正确的密码"),
            @ValidateFiled(index = 0, filedName = "code", regStr = "\\d{4,6}", notNull = true, errMsg = "请输入正确的手机验证码"),
    })
    @PostMapping("/user/mobile/resetpwd")
    public Mono<GlobalMessageResponseVo> resetMobilePassword(PlatUserVO platUserVO) {
        //验证验证码
        if (smsMessageService.validSmsCode(platUserVO.getMobile(), MessageTemplateCode.MOBILE_RESET_TEMPLATE.getCode(), platUserVO.getCode())) {
            //验证短信验证码
            PlatUser platUser = new PlatUser();
            String decryPassword = RsaUtils.decryptByPrivateKey(platUserVO.getPassword(), RsaUtils.DEFAULT_PRIVATE_KEY);
            platUser.setPassword(decryPassword);
            platUser.setMobile(platUserVO.getMobile());
            try {
                PlatUser updatePlat = platUserService.resetPasswordByMobile(platUser);
                com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                        com.biao.reactive.data.mongo.disruptor.DisruptorData.
                                buildSecurityLog(SecurityLogEnums.SECURITY_RESET_PASSWORD, 0, "手机重置密码",
                                        updatePlat.getId(), platUserVO.getMobile(), ""));
            } catch (Exception e) {
                com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                        com.biao.reactive.data.mongo.disruptor.DisruptorData.
                                buildSecurityLog(SecurityLogEnums.SECURITY_RESET_PASSWORD, 1, "手机重置密码失败,e:" + e.getMessage(),
                                        "", platUserVO.getMobile(), ""));
                throw e;
            }
            return Mono.just(GlobalMessageResponseVo.newSuccessInstance("重置密码成功"));
        }
        return Mono.just(GlobalMessageResponseVo.newErrorInstance("重置密码失败"));
    }

    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "exPassword", notNull = true, errMsg = "请输入格式正确的交易密码"),
            @ValidateFiled(index = 0, filedName = "password", notNull = true, errMsg = "请输入格式正确的登录密码"),
            @ValidateFiled(index = 0, filedName = "code", notNull = true, errMsg = "请输入格式正确的短信验证码"),
    })
    @PostMapping("/user/updateExPassword")
    public Mono<GlobalMessageResponseVo> updateExPassword(PlatUserVO platUserVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> this.updateExPassword(user, platUserVO));
    }

    private Mono<GlobalMessageResponseVo> updateExPassword(RedisSessionUser user, PlatUserVO platUserVO) {
        if (!smsMessageService.validSmsCode(user.getMobile(), MessageTemplateCode.MOBILE_TRADE_PASSWORD_TEMPLATE.getCode(), platUserVO.getCode())) {
            com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                    com.biao.reactive.data.mongo.disruptor.DisruptorData.
                            buildSecurityLog(SecurityLogEnums.SECURITY_UPDATE_EX_PASS, 1, "手机验证码验证失败",
                                    user.getId(), user.getMobile(), user.getMail()));
            return Mono.just(GlobalMessageResponseVo.newErrorInstance("手机验证码验证失败"));
        }
        String oldExpassword = user.getExPassword();
//        String decryPassword = RsaUtils.decryptByPrivateKey(platUserVO.getPassword(), RsaUtils.DEFAULT_PRIVATE_KEY);
        String exDecryPassword = RsaUtils.decryptByPrivateKey(platUserVO.getExPassword(), RsaUtils.DEFAULT_PRIVATE_KEY);
//        if (decryPassword.equals(exDecryPassword)) {
//            com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
//                    com.biao.reactive.data.mongo.disruptor.DisruptorData.
//                            buildSecurityLog(SecurityLogEnums.SECURITY_UPDATE_EX_PASS, 1, "交易密码不能和登录密码一致",
//                                    user.getId(), user.getMobile(), user.getMail()));
//            throw new PlatException(Constants.GLOBAL_ERROR_CODE, "交易密码不能和登录密码一致");
//        }
        LocalDateTime lockDate = userConfig.plusHours();
        String decodeExPassword = passwordEncoder.encode(exDecryPassword);
        PlatUser platUser = new PlatUser();
//        platUser.setPassword(decryPassword);
        platUser.setExPassword(decodeExPassword);
        platUser.setId(user.getId());
        platUser.setLockDate(lockDate);
        platUserService.updateExpasswordById(platUser);
        //刷选设置用户的交易密码
        user.setExPassword(decodeExPassword);
        user.setLockDate(lockDate);
        //更新redis数据
        stringRedisTemplate.opsForHash().put(SercurityConstant.SESSION_TOKEN_REDIS_NAMESAPCE + user.getToken(), SercurityConstant.SESSION_TOKEN_REDIS_USER, JsonUtils.toJson(user));
        String message = SecurityLogEnums.SECURITY_UPDATE_EX_PASS.getMessage();
        SecurityLogEnums type = SecurityLogEnums.SECURITY_UPDATE_EX_PASS;
        if (StringUtils.isBlank(oldExpassword)) {
            message = SecurityLogEnums.SECURITY_BINDER_EX_PASS.getMessage();
            type = SecurityLogEnums.SECURITY_BINDER_EX_PASS;
        }
        com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                com.biao.reactive.data.mongo.disruptor.DisruptorData.
                        buildSecurityLog(type, 0, message,
                                user.getId(), user.getMobile(), user.getMail()));
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance("设置交易密码成功"));
    }

    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "exValidType", notNull = true, isEnums = true, enums = "1,3,4", errMsg = "请输入格式正确的交易验证码方式")
    })
    @PostMapping("/user/updateExValidType")
    public Mono<GlobalMessageResponseVo> updateExValidType(PlatUserVO platUserVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    //判断是否谷歌验证、交易密码
                    if (user.getExValidType() != null) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("你已经绑定了交易验证类型,请修改"));
                    }
                    Integer exValidType = platUserVO.getExValidType();
                    if (exValidType == 1 && StringUtils.isBlank(user.getGoogleAuth())) {
                        //谷歌验证
                        com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                                com.biao.reactive.data.mongo.disruptor.DisruptorData.
                                        buildSecurityLog(SecurityLogEnums.SECURITY_SET_EX_TYPE, 1, "没有绑定谷歌认证",
                                                user.getId(), user.getMobile(), user.getMail()));
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("请先绑定谷歌认证"));
                    }
                    if (exValidType == 2 && StringUtils.isBlank(user.getExPassword())) {
                        //交易密码验证
                        com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                                com.biao.reactive.data.mongo.disruptor.DisruptorData.
                                        buildSecurityLog(SecurityLogEnums.SECURITY_SET_EX_TYPE, 1, "没有设置交易密码",
                                                user.getId(), user.getMobile(), user.getMail()));
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("请先设置交易密码"));
                    }
                    if (exValidType == 3 && StringUtils.isBlank(user.getMobile())) {
                        //交易密码验证
                        com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                                com.biao.reactive.data.mongo.disruptor.DisruptorData.
                                        buildSecurityLog(SecurityLogEnums.SECURITY_SET_EX_TYPE, 1, "没有绑定手机",
                                                user.getId(), user.getMobile(), user.getMail()));
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("请先绑定手机"));
                    }
                    if (exValidType == 4 && StringUtils.isBlank(user.getMail())) {
                        //交易密码验证
                        com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                                com.biao.reactive.data.mongo.disruptor.DisruptorData.
                                        buildSecurityLog(SecurityLogEnums.SECURITY_SET_EX_TYPE, 1, "没有绑定邮箱",
                                                user.getId(), user.getMobile(), user.getMail()));
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("请先绑定邮箱"));
                    }
                    PlatUser platUser = new PlatUser();
                    platUser.setId(user.getId());
                    platUser.setExValidType(exValidType);
                    platUserService.updateById(platUser);
                    //更新redis数据
                    user.setExValidType(exValidType);
                    stringRedisTemplate.opsForHash().put(SercurityConstant.SESSION_TOKEN_REDIS_NAMESAPCE + user.getToken(), SercurityConstant.SESSION_TOKEN_REDIS_USER, JsonUtils.toJson(user));
                    com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                            com.biao.reactive.data.mongo.disruptor.DisruptorData.
                                    buildSecurityLog(SecurityLogEnums.SECURITY_SET_EX_TYPE, 0, SecurityLogEnums.SECURITY_SET_EX_TYPE.getMessage(),
                                            user.getId(), user.getMobile(), user.getMail()));
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("更新交易验证码方式成功"));
                });
    }

    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "code", notNull = true, errMsg = "请输入正确的验证码"),
            @ValidateFiled(index = 0, filedName = "exValidType", notNull = true, isEnums = true, enums = "1,3,4", errMsg = "请输入格式正确的交易验证码方式")
    })
    @PostMapping("/user/changeExValidType")
    public Mono<GlobalMessageResponseVo> changeExValidType(PlatUserVO platUserVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    //判断是否谷歌验证、交易密码
                    Integer exValidType = user.getExValidType();
                    if (exValidType == null) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("交易验证码方式"));
                    }
                    String validTimeKey = new StringBuilder("plat:user:exType:")
                            .append(DateUtil.formatDate()).append(":").append(user.getId()).toString();
                    Long errorTime = valOpsStr.increment(validTimeKey, 1);
                    valOpsStr.getOperations().expire(validTimeKey, 3600 * 24, TimeUnit.SECONDS);
                    if (errorTime > 10) {
                        //一天登录大于10次不能登录了
                        logger.error("userId:{},交易密码修改超过限制,禁止当天修改c2c交易类型", user.getId());
                        com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                                com.biao.reactive.data.mongo.disruptor.DisruptorData.
                                        buildSecurityLog(SecurityLogEnums.SECURITY_UPDATE_EX_TYPE, 1, "交易验证类型修改超过限制,禁止当天修改c2c交易验证类型",
                                                user.getId(), user.getMobile(), user.getMail()));
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("交易验证类型修改超过限制,禁止当天修改c2c交易验证类型"));
                    }
                    if (platUserVO.getExValidType() == 1 && StringUtils.isBlank(user.getGoogleAuth())) {
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.USER_TRADE_VALID_TYPE_CODE, "请绑定谷歌"));
                    }
                    if (platUserVO.getExValidType() == 3 && StringUtils.isBlank(user.getMobile())) {
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.USER_TRADE_VALID_TYPE_CODE, "请绑定手机"));
                    }
                    if (platUserVO.getExValidType() == 4 && StringUtils.isBlank(user.getMail())) {
                        return Mono.just(GlobalMessageResponseVo.newInstance(Constants.USER_TRADE_VALID_TYPE_CODE, "请绑定邮箱"));
                    }
                    String decryCode = platUserVO.getCode();
                    if (exValidType == 1) {
                        //谷歌验证
                        PlatUser platUser = this.platUserService.findById(user.getId());
                        if (StringUtils.isBlank(platUser.getGoogleAuth())) {
                            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.USER_TRADE_VALID_TYPE_CODE, "请绑定谷歌"));
                        }
                        GoogleAuthenticator ga = new GoogleAuthenticator();
                        ga.setWindowSize(5);
                        boolean result = ga.checkCode(platUser.getGoogleAuth(), Long.parseLong(decryCode), System.currentTimeMillis());
                        if (!result) {
                            logger.warn("====用户id:{},修改c2c交易验证类型失败", user.getId());
                            com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                                    com.biao.reactive.data.mongo.disruptor.DisruptorData.
                                            buildSecurityLog(SecurityLogEnums.SECURITY_UPDATE_EX_TYPE, 1, "谷歌认证失败",
                                                    user.getId(), user.getMobile(), user.getMail()));
                            return Mono.just(GlobalMessageResponseVo.newErrorInstance("谷歌认证失败"));
                        }
                    }
                    if (exValidType == 2) {
                        //交易密码验证
                        boolean result = passwordEncoder.matches(decryCode, user.getExPassword());
                        if (!result) {
                            logger.warn("====用户id:{},修改c2c交易验证类型失败", user.getId());
                            com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                                    com.biao.reactive.data.mongo.disruptor.DisruptorData.
                                            buildSecurityLog(SecurityLogEnums.SECURITY_UPDATE_EX_TYPE, 1, "请输入正确的交易密码",
                                                    user.getId(), user.getMobile(), user.getMail()));
                            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.TRADE_C2C_NEED_VALID_ERROR, "请输入正确的交易密码"));
                        }
                    }
                    if (exValidType == 3) {
                        //短信验证
                        boolean result = smsMessageService.validSmsCode(user.getMobile(), MessageTemplateCode.MOBILE_EXCHANGE_PASS_TEMPLATE.getCode(), decryCode);
                        if (!result) {
                            logger.warn("====用户id:{},修改c2c交易验证类型失败", user.getId());
                            com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                                    com.biao.reactive.data.mongo.disruptor.DisruptorData.
                                            buildSecurityLog(SecurityLogEnums.SECURITY_UPDATE_EX_TYPE, 1, "请输入正确的短信验证码",
                                                    user.getId(), user.getMobile(), user.getMail()));
                            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.TRADE_C2C_NEED_VALID_ERROR, "请输入正确的短信验证码"));
                        }

                    }
                    if (exValidType == 4) {
                        //邮箱验证
                        try {
                            messageSendService.mailValid(MessageTemplateCode.EMAIL_EXCHANGE_PASS_TEMPLATE.getCode(), VerificationCodeType.EX_EXCHANGE_PASS, user.getMail(), decryCode);
                        } catch (Exception e) {
                            logger.warn("====用户id:{},修改c2c交易验证类型失败", user.getId());
                            com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                                    com.biao.reactive.data.mongo.disruptor.DisruptorData.
                                            buildSecurityLog(SecurityLogEnums.SECURITY_UPDATE_EX_TYPE, 1, "请输入正确的邮箱验证码",
                                                    user.getId(), user.getMobile(), user.getMail()));
                            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.TRADE_C2C_NEED_VALID_ERROR, "请输入正确的邮箱验证码"));
                        }
                    }

                    PlatUser platUser = new PlatUser();
                    platUser.setId(user.getId());
                    //修改新的交易验证类型
                    LocalDateTime lockDate = userConfig.plusHours();
                    platUser.setExValidType(platUserVO.getExValidType());
                    platUser.setLockDate(lockDate);
                    platUserService.updateById(platUser);
                    //更新redis数据
                    user.setExValidType(platUserVO.getExValidType());
                    user.setLockDate(lockDate);
                    stringRedisTemplate.opsForHash().put(SercurityConstant.SESSION_TOKEN_REDIS_NAMESAPCE + user.getToken(), SercurityConstant.SESSION_TOKEN_REDIS_USER, JsonUtils.toJson(user));
                    com.biao.reactive.data.mongo.disruptor.DisruptorData.saveSecurityLog(
                            com.biao.reactive.data.mongo.disruptor.DisruptorData.
                                    buildSecurityLog(SecurityLogEnums.SECURITY_UPDATE_EX_TYPE, 0, SecurityLogEnums.SECURITY_UPDATE_EX_TYPE.getMessage(),
                                            user.getId(), user.getMobile(), user.getMail()));

                    DisruptorManager.instance().runConfig();
                    DisruptorData data = new DisruptorData();
                    data.setType(9);
                    PlatUserOplog platUserOplog = new PlatUserOplog();
                    platUserOplog.setContent("验证方式:" + exValidType + "->" + platUserVO.getExValidType());
                    platUserOplog.setCreateBy(user.getId());
                    platUserOplog.setCreateByName("plat");
                    platUserOplog.setCreateDate(LocalDateTime.now());
                    platUserOplog.setMail(user.getMail());
                    platUserOplog.setMobile(user.getMobile());
                    platUserOplog.setRealName(user.getRealName());
                    platUserOplog.setReason("用户修改交易验证方式,锁定用户");
                    platUserOplog.setType(PlatUserOplogTypeEnum.LOCK_DATE.getCode());
                    platUserOplog.setUserId(user.getId());
                    platUserOplog.setId(SnowFlake.createSnowFlake().nextIdString());
                    platUserOplog.setUpdateDate(LocalDateTime.now());
                    platUserOplog.setUpdateByName("plat");
                    platUserOplog.setUpdateBy(user.getId());
                    data.setPlatUserOplog(platUserOplog);
                    DisruptorManager.instance().publishData(data);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("更新交易验证码方式成功"));
                });
    }


    @GetMapping("/user/queryLog")
    public Mono<GlobalMessageResponseVo> findLoginPageLog(UserLoginLogQuery userLoginLogQuery) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    userLoginLogQuery.setUserId(user.getId());
                    ResponsePage<UserLoginLog> pageUserLogs = userLoginLogService.findPage(userLoginLogQuery);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(pageUserLogs));
                });

    }

    @PostMapping("/user/distributeLog/list")
    public Mono<GlobalMessageResponseVo> distributeLogList(MkDistributeLogListVO mkDistributeLogListVO) {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    String userId = e.getId();
                    mkDistributeLogListVO.setUserId(userId);
                    return GlobalMessageResponseVo.newSuccessInstance(mkDistributeLogService.findPage(mkDistributeLogListVO));
                });
    }

}
