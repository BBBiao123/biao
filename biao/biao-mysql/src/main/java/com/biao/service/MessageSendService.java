package com.biao.service;

import com.biao.entity.EmailSendLog;
import com.biao.entity.MobileSendLog;
import com.biao.enums.VerificationCodeType;
import com.biao.exception.PlatException;

import java.util.Map;

public interface MessageSendService {

    static final String PARAM_CODE = "code";

    static final String PARAM_PTOKEN_CODE = "ptoken";

    static final String REDIS_CODE_KEY = "message:valicode:";

    /**
     * 没有抛出PlatException异常,表示成功
     *
     * @param templateId 模板code
     * @param type       业务类型
     * @param toMail     发送的邮箱
     * @param params     模板参数
     * @throws PlatException
     */
    void mailSend(String templateId, VerificationCodeType type, String toMail, Map<String, Object> params) throws PlatException;

    /**
     * 发送邮件 并记录日志
     *
     * @param emailSendLog
     */
    void sendMail(EmailSendLog emailSendLog);

    /**
     * 插入日志
     *
     * @param emailSendLog
     */
    void insertLog(EmailSendLog emailSendLog);

    /**
     * 插入手机日志
     *
     * @param mobileSendLog
     */
    void insertMobileLog(MobileSendLog mobileSendLog);

    /**
     * 没有抛出PlatException异常,表示验证成功
     *
     * @param templateId 模板code
     * @param type       业务类型
     * @param toMail     发送的邮箱
     * @param validCode  验证码
     * @throws PlatException
     */
    void mailValid(String templateId, VerificationCodeType type, String toMail, String validCode) throws PlatException;

    /**
     * 通过重置密码产生的ptoken获取邮箱
     *
     * @param ptoken
     * @return
     */
    String findMailByPtoken(String ptoken);

    /**
     * 失效ptoken(重置密码后要失效)
     *
     * @param ptoken
     */
    void expirePtoken(String ptoken);

    /**
     * 清除验证码
     *
     * @param templateId
     * @param type
     * @param toMail
     */
    void clearValid(String templateId, String type, String toMail);
}
