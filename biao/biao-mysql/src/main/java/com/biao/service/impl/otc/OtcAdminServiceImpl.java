package com.biao.service.impl.otc;

import com.biao.constant.Constants;
import com.biao.entity.Coin;
import com.biao.entity.PlatUser;
import com.biao.entity.otc.*;
import com.biao.enums.TradeEnum;
import com.biao.enums.UserTagEnum;
import com.biao.exception.PlatException;
import com.biao.mapper.CoinDao;
import com.biao.mapper.PlatUserDao;
import com.biao.mapper.otc.*;
import com.biao.pojo.ResponsePage;
import com.biao.service.OfflineCoinVolumeService;
import com.biao.service.otc.OtcAccountSecretService;
import com.biao.service.otc.OtcAdminService;
import com.biao.service.otc.OtcOfflineOrderDetailService;
import com.biao.util.DateUtils;
import com.biao.util.SnowFlake;
import com.biao.util.otc.MD5Util;
import com.biao.vo.KlineVO;
import com.biao.vo.otc.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OtcAdminServiceImpl implements OtcAdminService {

    private Logger logger = LoggerFactory.getLogger(OtcAdminServiceImpl.class);

    @Autowired
    private CoinDao coinDao;

    @Autowired
    private OtcOfflineOrderDetailService otcOfflineOrderDetailService;

    @Autowired
    private OtcOfflineOrderDetailDao otcOfflineOrderDetailDao;

    @Autowired
    private OtcOfflineAppealRequestDao otcOfflineAppealRequestDao;

    @Autowired
    private OtcAppropriationRequestDao otcAppropriationRequestDao;

    @Autowired
    private OtcOfflineCoinDao otcOfflineCoinDao;

    @Autowired
    private OtcAccountSecretDao otcAccountSecretDao;

    @Autowired
    private OtcAdminService otcAdminService;

    @Autowired
    private PlatUserDao platUserDao;

    @Autowired
    private OtcAppropriationRequestDetailDao otcAppropriationRequestDetailDao;

    @Autowired
    private OfflineCoinVolumeService offlineCoinVolumeService;

    @Autowired
    private OtcAccountSecretService otcAccountSecretService;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final int APPROPRIATION_MAX_NUMBER = 10; // 批量划拨最大支持数量

    @Override
    public OtcCoinVO findCoin(OtcCoinVO otcCoinVO, Map<String, String> paramMap) {
        checkSecretKey(otcCoinVO.getKey(), paramMap);
        Coin coin = coinDao.findByName(otcCoinVO.getName());
        if (Objects.nonNull(coin)) {
            BeanUtils.copyProperties(coin, otcCoinVO);
        }
        return otcCoinVO;
    }

    private void checkSecretKey(String key, Map<String, String> paramMap) {
        OtcAccountSecret accountSecret = otcAccountSecretDao.findByPublishSource("otc");
        if (Objects.isNull(accountSecret)) {
            throw new PlatException(Constants.PARAM_ERROR, "OTC信息未设置");
        }

        String mykey = MD5Util.getSign(paramMap, accountSecret.getSecret());
        if (StringUtils.isBlank(key) || StringUtils.isBlank(mykey) || !key.equals(mykey)) {
            throw new PlatException(Constants.PARAM_ERROR, "秘钥加密错误");
        }
    }

    @Override
    public String getBatchNo() {
        return SnowFlake.createSnowFlake().nextIdString();
    }

    public OtcOfflineAppealRequest findAppeal(OtcOfflineAppealRequestVO otcOfflineAppealRequestVO) {
        return otcOfflineAppealRequestDao.findByBatchNo(otcOfflineAppealRequestVO.getBatchNo());
    }

//    @Override
//    @Transactional
//    public OtcOfflineAppealRequest doAppeal(OtcOfflineAppealRequestVO otcOfflineAppealRequestVO, Map<String, String> paramMap) {
//
//        checkAppealParam(otcOfflineAppealRequestVO);
//        OtcOfflineAppealRequest appealRequest = new OtcOfflineAppealRequest();
//        BeanUtils.copyProperties(otcOfflineAppealRequestVO, appealRequest);
//        appealRequest.setId(SnowFlake.createSnowFlake().nextIdString());
//        appealRequest.setCreateDate(LocalDateTime.now());
//        appealRequest.setUpdateDate(LocalDateTime.now());
//        try {
//            appealRequest.setStatus("1");
//            appealRequest.setResult("处理成功");
//            otcAdminService.doAppealActtion(otcOfflineAppealRequestVO, paramMap, appealRequest); // 处理申诉结果
//        } catch (PlatException e) {
//            logger.error("申诉批次号{}", appealRequest.getBatchNo());
//            logger.error("申诉失败", e);
//            String msg = Optional.ofNullable(e.getMsg()).orElse("");
//            msg = msg.length() > 450 ? msg.substring(0, 450) : msg;
//            appealRequest.setResult(msg);
//            appealRequest.setStatus("0"); // 申诉失败
//            otcOfflineAppealRequestDao.insert(appealRequest);
//        } catch (Exception e) {
//            logger.error("通兑失败流水号{}", appealRequest.getBatchNo());
//            logger.error("通兑失败", e);
//            String msg = Optional.ofNullable(e.toString()).orElse("");
//            msg = msg.length() > 450 ? msg.substring(0, 450) : msg;
//            appealRequest.setResult(msg);
//            appealRequest.setStatus("0"); // 申诉失败
//            otcOfflineAppealRequestDao.insert(appealRequest);
//        }
//
//        return appealRequest;
//    }

//    private void checkAppealParam(OtcOfflineAppealRequestVO otcOfflineAppealRequestVO) {
//        if (StringUtils.isBlank(otcOfflineAppealRequestVO.getPublishSource())) {
//            throw new PlatException(Constants.PARAM_ERROR, "来源不能为空");
//        }
//        if (StringUtils.isBlank(otcOfflineAppealRequestVO.getBatchNo())) {
//            throw new PlatException(Constants.PARAM_ERROR, "批次号不能为空");
//        }
//        if (StringUtils.isBlank(otcOfflineAppealRequestVO.getSubOrderId())) {
//            throw new PlatException(Constants.PARAM_ERROR, "订单号不能为空");
//        }
//        if (StringUtils.isBlank(otcOfflineAppealRequestVO.getResultUserId())) {
//            throw new PlatException(Constants.PARAM_ERROR, "胜方ID不能为空");
//        }
//        if (StringUtils.isBlank(otcOfflineAppealRequestVO.getResultExType())) {
//            throw new PlatException(Constants.PARAM_ERROR, "买卖方向不能为空");
//        }
//    }

//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void doAppealActtion(OtcOfflineAppealRequestVO otcOfflineAppealRequestVO, Map<String, String> paramMap, OtcOfflineAppealRequest appealRequest) {
//
//        otcOfflineAppealRequestDao.insert(appealRequest); // 记录请求成功日志
//
//        otcAccountSecretService.checkAccountSecret(otcOfflineAppealRequestVO, paramMap); // 来源和秘钥校验
//
//        OtcOfflineOrderDetail orderDetail = otcOfflineOrderDetailDao.findByUserIdSubOrderId(otcOfflineAppealRequestVO.getResultUserId(), otcOfflineAppealRequestVO.getSubOrderId(), otcOfflineAppealRequestVO.getPublishSource());
//        if (Objects.isNull(orderDetail)) {
//            throw new PlatException(Constants.PARAM_ERROR, "输入参数错误");
//        }
//        OtcOfflineOrderDetail askOrderDetail = otcOfflineOrderDetailDao.findByUserIdSubOrderId(orderDetail.getAskUserId(), orderDetail.getSubOrderId(), orderDetail.getPublishSource());
//        OtcOfflineOrderDetailVO offlineConfirmVO = new OtcOfflineOrderDetailVO();
//        offlineConfirmVO.setUserId(askOrderDetail.getUserId());
//        offlineConfirmVO.setSubOrderId(askOrderDetail.getSubOrderId());
//        offlineConfirmVO.setLoginSource(askOrderDetail.getPublishSource());
//        offlineConfirmVO.setUpdateBy("admin");
//        if (String.valueOf(TradeEnum.BUY.ordinal()).equals(askOrderDetail.getExType())) {
//            otcOfflineOrderDetailService.doCancelOrderDetailNoAppeal(offlineConfirmVO, otcOfflineAppealRequestVO.getBatchNo());
//        } else if (String.valueOf(TradeEnum.SELL.ordinal()).equals(askOrderDetail.getExType())) {
//            otcOfflineOrderDetailService.doConfirmReceiptNoAppeal(offlineConfirmVO, otcOfflineAppealRequestVO.getBatchNo());
//        } else {
//            throw new PlatException(Constants.PARAM_ERROR, "订单参数错误");
//        }
//    }

    public OtcAppropriationRequest findAppropriation(OtcAppropriationRequestVO otcOfflineAppealRequestVO) {
        return otcAppropriationRequestDao.findByBatchNo(otcOfflineAppealRequestVO.getBatchNo());
    }

    @Override
    @Transactional
    public OtcAppropriationRequest doAppropriation(OtcAppropriationRequestVO otcAppropriationRequestVO, Map<String, String> paramMap) {

        checkAppropriationParam(otcAppropriationRequestVO); // 验证传参是否为空
        OtcAppropriationRequest otcAppropriationRequest = new OtcAppropriationRequest();
        BeanUtils.copyProperties(otcAppropriationRequestVO, otcAppropriationRequest);
        otcAppropriationRequest.setId(SnowFlake.createSnowFlake().nextIdString());
        otcAppropriationRequest.setCreateDate(LocalDateTime.now());
        otcAppropriationRequest.setUpdateDate(LocalDateTime.now());

        try {
            otcAppropriationRequest.setStatus("1");
            otcAppropriationRequest.setResult("处理成功");
            otcAdminService.doAppropriationAction(otcAppropriationRequestVO, paramMap, otcAppropriationRequest); // 处理拨币结果
        } catch (PlatException e) {
            logger.error("拨币批次号{}", otcAppropriationRequest.getBatchNo());
            logger.error("拨币失败", e);
            String msg = Optional.ofNullable(e.getMsg()).orElse("");
            msg = msg.length() > 450 ? msg.substring(0, 450) : msg;
            otcAppropriationRequest.setResult(msg);
            otcAppropriationRequest.setStatus("0"); // 拨币失败
            otcAppropriationRequestDao.insert(otcAppropriationRequest);
        } catch (Exception e) {
            logger.error("拨币批次号{}", otcAppropriationRequest.getBatchNo());
            logger.error("拨币失败", e);
            String msg = Optional.ofNullable(e.toString()).orElse("");
            msg = msg.length() > 450 ? msg.substring(0, 450) : msg;
            otcAppropriationRequest.setResult(msg);
            otcAppropriationRequest.setStatus("0"); // 拨币失败
            otcAppropriationRequestDao.insert(otcAppropriationRequest);
        }
        return otcAppropriationRequest;
    }

    private void checkAppropriationParam(OtcAppropriationRequestVO otcAppropriationRequestVO) {
        if (StringUtils.isBlank(otcAppropriationRequestVO.getPublishSource())) {
            throw new PlatException(Constants.PARAM_ERROR, "来源不能为空");
        }
        if (StringUtils.isBlank(otcAppropriationRequestVO.getBatchNo())) {
            throw new PlatException(Constants.PARAM_ERROR, "批次号不能为空");
        }
        if (StringUtils.isBlank(otcAppropriationRequestVO.getSymbol())) {
            throw new PlatException(Constants.PARAM_ERROR, "币种代码不能为空");
        }
        if (StringUtils.isBlank(otcAppropriationRequestVO.getUserIdStr())) {
            throw new PlatException(Constants.PARAM_ERROR, "用户ID不能为空");
        }
        if (StringUtils.isBlank(otcAppropriationRequestVO.getVolumeStr())) {
            throw new PlatException(Constants.PARAM_ERROR, "拨币数量不能为空");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doAppropriationAction(OtcAppropriationRequestVO otcAppropriationRequestVO, Map<String, String> paramMap, OtcAppropriationRequest otcAppropriationRequest) {
        OtcAccountSecret accountSecret = otcAccountSecretService.findByPublishSource(otcAppropriationRequestVO.getPublishSource());
        otcAccountSecretService.checkAccountSecret(otcAppropriationRequestVO, paramMap); // 来源和秘钥校验

//        doCheckAppropriation(otcAppropriationRequestVO);
        Coin coin = coinDao.findByName(otcAppropriationRequestVO.getSymbol());
        if (Objects.isNull(coin)) {
            throw new PlatException(Constants.PARAM_ERROR, "币种名称有误");
        }
        otcAppropriationRequestVO.setCoinId(coin.getId());
        otcAppropriationRequest.setCoinId(coin.getId());

        otcAppropriationRequestDao.insert(otcAppropriationRequest); // 记录请求成功日志

        String[] userIds = getUserIds(otcAppropriationRequestVO);
        BigDecimal[] volumes = getVolumes(otcAppropriationRequestVO);
        if (userIds.length < 1 || userIds.length != volumes.length) {
            throw new PlatException(Constants.PARAM_ERROR, "用户和数量不匹配");
        }

        BigDecimal totalVolume = BigDecimal.ZERO;
        for (BigDecimal volume : volumes) {
            totalVolume = totalVolume.add(volume);
        }
        if (totalVolume.compareTo(BigDecimal.ZERO) < 1) {
            throw new PlatException(Constants.PARAM_ERROR, "转入总数量错误");
        }

        // 转币方转出可用资金
        offlineCoinVolumeService.coinVolumeSubtract(accountSecret.getUserId(), otcAppropriationRequestVO.getCoinId(), totalVolume, otcAppropriationRequestVO.getBatchNo());

        OtcAppropriationRequestDetail detail = null;
        for (int index = 0; index < userIds.length; index++) {
            // 收币方转入可用资金
            offlineCoinVolumeService.coinVolumeAdd(userIds[index], otcAppropriationRequestVO.getCoinId(), otcAppropriationRequestVO.getSymbol(), volumes[index], otcAppropriationRequestVO.getBatchNo());
            detail = new OtcAppropriationRequestDetail();
            detail.setId(SnowFlake.createSnowFlake().nextIdString());
            detail.setBatchNo(otcAppropriationRequestVO.getBatchNo());
            detail.setUserId(userIds[index]);
            detail.setCoinId(otcAppropriationRequestVO.getCoinId());
            detail.setSymbol(otcAppropriationRequestVO.getSymbol());
            detail.setVolume(volumes[index]);
            detail.setCreateDate(LocalDateTime.now());
            detail.setStatus("1");
            detail.setType(otcAppropriationRequestVO.getType());
            otcAppropriationRequestDetailDao.insert(detail); // 划拨明细
        }

    }

    private void doCheckAppropriation(OtcAppropriationRequestVO otcAppropriationRequestVO) {
        OtcOfflineCoin coin = otcOfflineCoinDao.findBySymbol(otcAppropriationRequestVO.getSymbol());
        if (Objects.isNull(coin)) {
            throw new PlatException(Constants.PARAM_ERROR, "转币币种不存在");
        }
        otcAppropriationRequestVO.setCoinId(coin.getCoinId());  // 设置币种ID
        if (StringUtils.isBlank(otcAppropriationRequestVO.getUserIdStr())) {
            throw new PlatException(Constants.PARAM_ERROR, "转入用户ID不能为空");
        }
        if (StringUtils.isBlank(otcAppropriationRequestVO.getVolumeStr())) {
            throw new PlatException(Constants.PARAM_ERROR, "转入数量不能为空");
        }

    }

    private String[] getUserIds(OtcAppropriationRequestVO otcAppropriationRequestVO) {
        String[] userIds = otcAppropriationRequestVO.getUserIdStr().split(",");
        if (userIds.length > APPROPRIATION_MAX_NUMBER || userIds.length == 0) {
            throw new PlatException(Constants.PARAM_ERROR, "批量转入账户数量必须在[1,10]之间");
        }
        PlatUser user = null;
        for (String userId : userIds) {
            user = platUserDao.findById(userId);
            if (Objects.isNull(user) || StringUtils.isBlank(user.getTag()) || !user.getTag().contains(UserTagEnum.OTC_ADVERT.getCode())) {
                throw new PlatException(Constants.PARAM_ERROR, "用户ID不存在或标签不是广告商错误");
            }
        }
        return userIds;
    }

    private BigDecimal[] getVolumes(OtcAppropriationRequestVO otcAppropriationRequestVO) {
        String[] volumes = otcAppropriationRequestVO.getVolumeStr().split(",");
        if (volumes.length > APPROPRIATION_MAX_NUMBER) {
            throw new PlatException(Constants.PARAM_ERROR, "超出转入账户最大数量10");
        }
        List<BigDecimal> volumeList = new ArrayList<>();
        for (String volumeStr : volumes) {
            try {
                BigDecimal volume = new BigDecimal(volumeStr);
                if (volume.compareTo(BigDecimal.ZERO) < 1) {
                    throw new PlatException(Constants.PARAM_ERROR, "转入数量错误");
                }
                volumeList.add(volume);
            } catch (Exception e) {
                logger.error("数量错误", e);
                throw new PlatException(Constants.PARAM_ERROR, "转入数量错误");
            }
        }
        return volumeList.toArray(new BigDecimal[volumeList.size()]);
    }

//    public ResponsePage<OtcOfflineOrderDetail> getDetails(OtcOfflineOrderDetailVO otcOfflineOrderDetailVO, Map<String, String> paramMap) {
//        OtcAccountSecretVO otcAccountSecretVO = new OtcAccountSecretVO();
//        BeanUtils.copyProperties(otcOfflineOrderDetailVO, otcAccountSecretVO);
//        otcAccountSecretService.checkAccountSecret(otcAccountSecretVO, paramMap); // 来源和秘钥校验
//        ResponsePage<OtcOfflineOrderDetail> responsePage = new ResponsePage<>();
//        Page<OtcOfflineOrderDetail> page = PageHelper.startPage(otcOfflineOrderDetailVO.getCurrentPage(), otcOfflineOrderDetailVO.getShowCount());
//        List<OtcOfflineOrderDetail> offlineOrderDetails = otcOfflineOrderDetailDao.findDetailByCondition(otcOfflineOrderDetailVO);
//        offlineOrderDetails.forEach(detail -> {
//            detail.setUserId(null);
//            detail.setOrderUserId(null);
//            detail.setAskUserId(null);
//        });
//        responsePage.setCount(page.getTotal());
//        responsePage.setList(offlineOrderDetails);
//        return responsePage;
//    }

    public String getLastRate(String coinMain, String coinOther) {
        Map<String, KlineVO> map = redisTemplate.opsForHash().entries(buildKey(coinMain, coinOther));
       LocalDateTime lastDate = null;
       KlineVO klineVO = null;
       for(Iterator<Map.Entry<String, KlineVO>> it = map.entrySet().iterator(); it.hasNext();) {
           Map.Entry<String, KlineVO> v = it.next();
           try {
               LocalDateTime tempDate = DateUtils.parseLocalDateTime(v.getKey(), DateUtils.DATE_FORMAT_DATETIME);
               KlineVO tempKlineVO = v.getValue();
               if (Objects.isNull(lastDate) || lastDate.isBefore(tempDate)) {
                   lastDate = tempDate;
                   klineVO = tempKlineVO;
               }
           } catch (Exception e) {
               logger.error("取UES-EUS汇率时间转换错误");
           }
       }
       return Objects.nonNull(klineVO) ? klineVO.getC() : "";
    }

    private String buildKey(String coinMain, String coinOther) {
        return "kline:" + coinMain + "_" + coinOther + ":1d";
    }
}
