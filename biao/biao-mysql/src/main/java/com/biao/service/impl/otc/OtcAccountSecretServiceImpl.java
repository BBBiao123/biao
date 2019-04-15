package com.biao.service.impl.otc;

import com.biao.constant.Constants;
import com.biao.entity.otc.OtcAccountSecret;
import com.biao.enums.AppSourceTypeEnum;
import com.biao.exception.PlatException;
import com.biao.mapper.otc.OtcAccountSecretDao;
import com.biao.service.otc.OtcAccountSecretService;
import com.biao.util.otc.MD5Util;
import com.biao.vo.otc.OtcAccountSecretVO;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class OtcAccountSecretServiceImpl implements OtcAccountSecretService {

    private Logger logger = LoggerFactory.getLogger(OtcAccountSecretServiceImpl.class);

    @Autowired
    private OtcAccountSecretDao otcAccountSecretDao;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String OTC_ACCOUNT_CACHE_KEY = "otc:account:%s";

    private static final int TIME_OUT = 120; // 缓存2分钟

    /**
     * 根据发布来源查询账户的配置信息
     *
     * @param publishSource
     * @return
     */
    public OtcAccountSecret findByPublishSource(String publishSource) {
        if (StringUtils.isBlank(publishSource)) {
            return null;
        }

        OtcAccountSecret accountSecret = otcAccountSecretDao.findByPublishSource(publishSource);

//        String cacheKey = String.format(OTC_ACCOUNT_CACHE_KEY, publishSource);
//        OtcAccountSecret accountSecret = (OtcAccountSecret)redisTemplate.opsForValue().get(cacheKey);
//        if (Objects.isNull(accountSecret)) {
//            accountSecret = otcAccountSecretDao.findByPublishSource(publishSource);
//            if (Objects.nonNull(accountSecret)) {
//                redisTemplate.opsForValue().set(cacheKey, accountSecret, TIME_OUT, TimeUnit.SECONDS);
//            }
//        }
        return accountSecret;
    }

    public void checkIsMasterAccount(String userId) {
        OtcAccountSecret account = findByPublishSource(AppSourceTypeEnum.OTC.getCode());
        if (Objects.isNull(account) || StringUtils.isBlank(account.getUserId())) {
            throw new PlatException(Constants.PARAM_ERROR, "OTC总账户未设置");
        }
        if (!account.getUserId().equals(userId)) {
            throw new PlatException(Constants.PARAM_ERROR, "当前用户和OTC总账户不一致");
        }
    }

    /**
     * 校验客户端来源和秘钥等信息是否合法
     *
     * @param otcAccountSecretVO
     * @param secretMap
     */
    @Override
    public void checkAccountSecret(OtcAccountSecretVO otcAccountSecretVO, Map<String, String> secretMap) {
        OtcAccountSecret accountSecret = findByPublishSource(otcAccountSecretVO.getPublishSource());
        // 1、OTC总账户判断
        if (Objects.isNull(accountSecret)) {
            throw new PlatException(Constants.PARAM_ERROR, "OTC总账户未设置");
        }
        if (!"1".equals(accountSecret.getStatus())) {
            throw new PlatException(Constants.PARAM_ERROR, "OTC总账户被冻结");
        }
        // 2、OTC支付IP判断
        if (StringUtils.isNotBlank(accountSecret.getAccessIp())) { // OTC后台IP限制判断
            boolean access = false;
            String[] accessIps = accountSecret.getAccessIp().split(",");
            for (String accessIp : accessIps) {
                if (otcAccountSecretVO.getLoginIp().contains(accessIp)) {
                    access = true;
                    break;
                }
            }
            logger.info("免登陆OTC客户端IP：", otcAccountSecretVO.getLoginIp());
            if (!access) {
                throw new PlatException(Constants.PARAM_ERROR, "客户端IP错误");
            }
        }
        // 3、秘钥判断
        if (MapUtils.isNotEmpty(secretMap)) {
            String secretKey = MD5Util.getSign(secretMap, accountSecret.getSecret());
            if (StringUtils.isBlank(otcAccountSecretVO.getKey()) || StringUtils.isBlank(secretKey) || !otcAccountSecretVO.getKey().equals(secretKey)) {
                throw new PlatException(Constants.PARAM_ERROR, "秘钥加密错误");
            }
        }
    }

    public void checkAccountSecret(String ip, String key, String publishSource, Map<String, String> secretMap) {
        OtcAccountSecret accountSecret = findByPublishSource(publishSource);
        // 1、OTC总账户判断
        if (Objects.isNull(accountSecret)) {
            throw new PlatException(Constants.PARAM_ERROR, "OTC总账户未设置");
        }
        if (!"1".equals(accountSecret.getStatus())) {
            throw new PlatException(Constants.PARAM_ERROR, "OTC总账户被冻结");
        }
        // 2、OTC支付IP判断
        if (StringUtils.isNotBlank(accountSecret.getAccessIp())) { // OTC后台IP限制判断
            boolean access = false;
            String[] accessIps = accountSecret.getAccessIp().split(",");
            if (StringUtils.isBlank(ip)){
                throw new PlatException(Constants.PARAM_ERROR, "客户端IP错误");
            }
            for (String accessIp : accessIps) {
                if (ip.contains(accessIp)) {
                    access = true;
                    break;
                }
            }
            logger.info("免登陆OTC客户端IP：", ip);
            if (!access) {
                throw new PlatException(Constants.PARAM_ERROR, "客户端IP错误");
            }
        }
        // 3、秘钥判断
        if (MapUtils.isNotEmpty(secretMap)) {
            String secretKey = MD5Util.getSign(secretMap, accountSecret.getSecret());
            if (StringUtils.isBlank(key) || StringUtils.isBlank(secretKey) || !key.equals(secretKey)) {
                throw new PlatException(Constants.PARAM_ERROR, "秘钥加密错误");
            }
        }
    }
}
