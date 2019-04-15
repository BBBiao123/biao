package com.biao.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.afs.model.v20180112.AuthenticateSigRequest;
import com.aliyuncs.afs.model.v20180112.AuthenticateSigResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.biao.vo.AuthenticateSigVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

public class AliYunAuthenticateSigUtils {

    private static Logger logger = LoggerFactory.getLogger(AliYunAuthenticateSigUtils.class);

    private static String regionid = "cn-hangzhou";

    
    public static boolean isValid(AuthenticateSigVO sigVO) {
        return true ;
    }
    
    public static boolean isValidOpen(AuthenticateSigVO sigVO) {
        String accessKeyId = sigVO.getAccessKeyId();
        String accessKeySecret = sigVO.getAccessKeySecret();
        IClientProfile profile = DefaultProfile.getProfile(regionid, accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "afs", "afs.aliyuncs.com");
            AuthenticateSigRequest request = new AuthenticateSigRequest();
            request.setSessionId(sigVO.getSessionId());// 必填参数，从前端获取，不可更改
            request.setSig(sigVO.getSig());// 必填参数，从前端获取，不可更改
            request.setToken(sigVO.getToken());// 必填参数，从前端获取，不可更改
            request.setScene(sigVO.getScene());// 必填参数，从前端获取，不可更改
            request.setAppKey(sigVO.getAppKey());// 必填参数，后端填写
            request.setRemoteIp(InetAddress.getLocalHost().getHostAddress().toString());// 必填参数，后端填写
            // response的code枚举：100验签通过，900验签失败
            AuthenticateSigResponse response = client.getAcsResponse(request);
            if (response.getCode() == 100) {
                return true;
            } else {
                logger.error("滑块验证失败，code:{},error:{}", response.getCode(), response.getMsg());
            }
        } catch (ClientException e1) {
            logger.error("滑块验证异常，e:{}", e1.getErrMsg());
        } catch (Exception e1) {
            logger.error("滑块验证异常，e:{}", e1.getMessage());
        }
        return false;
    }
}
