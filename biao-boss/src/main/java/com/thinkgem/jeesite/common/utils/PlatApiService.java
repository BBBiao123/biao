package com.thinkgem.jeesite.common.utils;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PlatApiService {

    private final Logger logger = LoggerFactory.getLogger(PlatApiService.class);

    @Value("${plat.url}")
    private String platUrl;

    private OkHttpHelper oh = OkHttpHelper.get();

    /**
     * 登录处理一下；
     *
     * @param pass pass;
     * @param user user;
     * @return token;
     */
    public String login(String user, String pass) {
        String url = platUrl.concat(Constants.LOGIN);
        Map<String, String> params = Maps.newHashMap();
        params.put(Constants.LOGIN_USERNAME, user);
        params.put(Constants.LOGIN_PASSWORD, pass);
        Response response = oh.requestForm(params, new HashMap<>(), url);
        if (response.sccuess()) {
            logger.info("登录成功！", response.getMsg());
            return response.getMap().getString(Constants.LOGIN_TOKEN);
        } else {
            logger.error("登录失败！", response.getMsg());
            throw new RuntimeException("登录失败！");
        }
    }

    public Map<String, String> headers(String token) {
        Map<String, String> headers = Maps.newHashMap();
        headers.put(Constants.STOKEN, token);
        return headers;
    }
}
