package com.biao.service;

import com.biao.exception.PlatException;

public interface SmsMessageService {

    public void sendSms(String mobile, String templateCode, String outId) throws PlatException;

    public void sendSms(String countryCode, String mobile, String templateCode, String outId) throws PlatException;

    public boolean validSmsCode(String mobile, String templateCode, String code) throws PlatException;
}
