package com.biao.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.biao.constant.Constants;
import com.biao.entity.PlatUser;
import com.biao.exception.PlatException;
import com.biao.mapper.PlatUserDao;
import com.biao.service.EasemobService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EasemobServiceImpl implements EasemobService {

    private String csPass = "5hMTZI9i";

    private static String ORG_NAME = "1465190103068742";
    private static String APP_NAME = "kefuchannelapp4643";
    private static String EASEMOB_REGISTER_URL = "https://a1-vip5.easemob.com/%s/%s/users";
    private static String EASEMOB_TOKEN_URL = "https://a1-vip5.easemob.com/%s/%s/token";


    @Autowired
    private PlatUserDao platUserDao;

    @Override
    public void register(String userId, String csUsername) {
        registerUser(getToken(), csUsername, csPass);
        this.updateIsRegisterCs(userId);
    }


    private void updateIsRegisterCs(String userId){
        PlatUser platUser = platUserDao.findById(userId);
        platUser.setIsRegisteredCs("1");
        platUserDao.updateById(platUser);
    }


    public static String getToken(){
        JSONObject reqObject = new JSONObject();
        reqObject.put("grant_type","client_credentials");
        reqObject.put("client_id","YXA6VhcY0A8tEem5lI33xySPsw");
        reqObject.put("client_secret","YXA6Dym4iVw_-YqYlwwForepbBqSRog");
        String url = String.format(EASEMOB_TOKEN_URL, ORG_NAME, APP_NAME);
        JSONObject resObject = doPost(url, reqObject.toJSONString(),"");
        return resObject.getString("access_token");
    }

    public static void registerUser(String token, String csUsername, String csPass){
        JSONObject reqObject = new JSONObject();
        reqObject.put("username",csUsername);
        reqObject.put("password",csPass);
        String url = String.format(EASEMOB_REGISTER_URL, ORG_NAME, APP_NAME);
        doPost(url, reqObject.toJSONString(),token);
    }

    public static JSONObject doPost(String url, String json, String token){
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        JSONObject response = null;
        try {
            StringEntity s = new StringEntity(json, "utf-8");
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");//发送json数据需要设置contentType
            if(StringUtils.isNotEmpty(token)){
                post.setHeader("Authorization", token);
            }
            post.setEntity(s);
            HttpResponse res = httpclient.execute(post);
            if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String result = EntityUtils.toString(res.getEntity());// 返回json格式：
                response = JSONObject.parseObject(result);
            }else if((res.getStatusLine().getStatusCode() == 400)){
                throw new PlatException(Constants.PARAM_ERROR, "用户已存在");
            }else{
                throw new PlatException(Constants.PARAM_ERROR, "注册失败");
            }
        } catch (PlatException pe){
            throw pe;
        }catch (Exception e) {
            throw new PlatException(Constants.PARAM_ERROR, "注册失败");
        }
        return response;
    }
}
