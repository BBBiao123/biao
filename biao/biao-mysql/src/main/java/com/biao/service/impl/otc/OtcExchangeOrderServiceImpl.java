package com.biao.service.impl.otc;

import com.biao.constant.Constants;
import com.biao.entity.PlatUser;
import com.biao.entity.otc.OtcAccountSecret;
import com.biao.entity.otc.OtcExchangeCoinFee;
import com.biao.entity.otc.OtcExchangeOrder;
import com.biao.enums.UserTagEnum;
import com.biao.exception.PlatException;
import com.biao.mapper.PlatUserDao;
import com.biao.mapper.otc.OtcExchangeCoinFeeDao;
import com.biao.mapper.otc.OtcExchangeOrderDao;
import com.biao.service.OfflineCoinVolumeService;
import com.biao.service.otc.OtcAccountSecretService;
import com.biao.service.otc.OtcExchangeOrderService;
import com.biao.util.SnowFlake;
import com.biao.vo.otc.OtcExchangeOrderVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class OtcExchangeOrderServiceImpl implements OtcExchangeOrderService {

    private Logger logger = LoggerFactory.getLogger(OtcExchangeOrderServiceImpl.class);

    @Autowired
    private OtcExchangeOrderDao otcExchangeOrderDao;

    @Autowired
    private PlatUserDao platUserDao;

    @Autowired
    private OtcExchangeCoinFeeDao otcExchangeCoinFeeDao;

    @Autowired
    private OfflineCoinVolumeService offlineCoinVolumeService;

    @Autowired
    private OtcExchangeOrderService otcExchangeOrderService;

    @Autowired
    private OtcAccountSecretService otcAccountSecretService;


    @Override
    public OtcExchangeOrder findByBatchNo(String batchNo) {
        return otcExchangeOrderDao.findByBatchNo(batchNo);
    }

    public String getBatchNo() {
        return SnowFlake.createSnowFlake().nextIdString();
    }

//    @Override
//    @Transactional
//    public OtcExchangeOrder otcPay(OtcExchangeOrder otcExchangeOrder, Map<String, String> paramMap, OtcExchangeOrderVO otcExchangeOrderVO) {
//        otcAccountSecretService.checkAccountSecret(otcExchangeOrderVO, paramMap); // 来源安全校验
//        return this.userPay(otcExchangeOrder);
//    }

//    @Override
//    @Transactional
//    public OtcExchangeOrder shPay(OtcExchangeOrder otcExchangeOrder, Map<String, String> paramMap, OtcExchangeOrderVO otcExchangeOrderVO) {
//
//        OtcShAccountSecret otcShAccountSecret = otcShAccountSecretDao.findByPayCode(otcExchangeOrderVO.getPayCode());
//        checkShPayParam(otcShAccountSecret, paramMap, otcExchangeOrderVO); // 校验支付参数
//        otcExchangeOrder.setUserId(otcShAccountSecret.getUserId());
//        return this.userPay(otcExchangeOrder);
//    }

//    private void checkShPayParam(OtcShAccountSecret otcShAccountSecret, Map<String, String> paramMap, OtcExchangeOrderVO otcExchangeOrderVO) {
//        // 1、商家支付码判断
//        if (Objects.isNull(otcShAccountSecret)) {
//            throw new PlatException(Constants.PARAM_ERROR, "商户支付码错误");
//        }
//        if (!"1".equals(otcShAccountSecret.getStatus())) {
//            throw new PlatException(Constants.PARAM_ERROR, "商户已被冻结");
//        }
//        // 2、商家支付IP判断
//        if (StringUtils.isNotBlank(otcShAccountSecret.getAccessIp())) { // 商家支付IP限制判断
//            boolean access = false;
//            String[] accessIps = otcShAccountSecret.getAccessIp().split(",");
//            for (String accessIp : accessIps) {
//                if (otcExchangeOrderVO.getLoginIp().contains(accessIp)) {
//                    access = true;
//                    break;
//                }
//            }
//            logger.info("商户支付IP：", otcExchangeOrderVO.getLoginIp());
//            if (!access) {
//                throw new PlatException(Constants.PARAM_ERROR, "商户支付IP错误");
//            }
//        }
//        // 3、商家支付秘钥判断
//        String secretKey = MD5Util.getSign(paramMap, otcShAccountSecret.getSecret());
//        if (StringUtils.isBlank(otcExchangeOrderVO.getKey()) || StringUtils.isBlank(secretKey) || !otcExchangeOrderVO.getKey().equals(secretKey)) {
//            throw new PlatException(Constants.PARAM_ERROR, "秘钥加密错误");
//        }
//    }

    /**
     * 通兑业务入口
     *
     * @param otcExchangeOrder
     * @return
     */
//    @Override
////    @Transactional
////    public OtcExchangeOrder userPay(OtcExchangeOrder otcExchangeOrder) {
////
////        preCheckOtcExchangeOrder(otcExchangeOrder); // 检查批次号是否存在
////        // 处理通兑业务
////        try {
////            otcExchangeOrder.setId(SnowFlake.createSnowFlake().nextIdString());
////            otcExchangeOrder.setCreateDate(LocalDateTime.now());
////            otcExchangeOrder.setUpdateDate(LocalDateTime.now());
////
////            PlatUser outUser = platUserDao.findById(otcExchangeOrder.getUserId());
////            PlatUser inUser = platUserDao.findById(otcExchangeOrder.getAskUserId());
////            checkUser(outUser, inUser); // 检查至少有一方是商家
////
////            // 出款方信息
////            otcExchangeOrder.setRealName(outUser.getRealName());
////            otcExchangeOrder.setMobile(outUser.getMobile());
////            otcExchangeOrder.setMail(outUser.getMail());
////            // 收款方信息
////            otcExchangeOrder.setAskRealName(inUser.getRealName());
////            otcExchangeOrder.setAskUserMobile(inUser.getMobile());
////            otcExchangeOrder.setAskUserMail(inUser.getMail());
////
////            BigDecimal feeVolume = calcFee(otcExchangeOrder);
//////            BigDecimal feeVolume = otcExchangeOrder.getFeeVolume(); // 手续费由otc传过来
////            if (StringUtils.isNotBlank(outUser.getTag()) && outUser.getTag().contains(UserTagEnum.OTC_BUSINESS.getCode())) {
////                otcExchangeOrder.setFeeVolume(feeVolume);
////            } else {
////                otcExchangeOrder.setFeeVolume(BigDecimal.ZERO);
////            }
////            if (StringUtils.isNotBlank(inUser.getTag()) && inUser.getTag().contains(UserTagEnum.OTC_BUSINESS.getCode())) {
////                otcExchangeOrder.setAskFeeVolume(feeVolume);
////            } else {
////                otcExchangeOrder.setAskFeeVolume(BigDecimal.ZERO);
////            }
////            otcExchangeOrder.setRealVolume(otcExchangeOrder.getVolume().subtract(otcExchangeOrder.getAskFeeVolume())); // 实际到账
////            lastCheckOtcExchangeOrder(otcExchangeOrder);// 检查数量，手续费参数是否正确
////            otcExchangeOrder.setStatus("1"); // 通兑成功
////            otcExchangeOrder.setResult("通兑转入成功");
////            // == 资产划转
////            otcExchangeOrderService.exchangeVolumeInOut(otcExchangeOrder);
////
////        } catch (PlatException e) {
////            logger.error("通兑失败流水号{}", otcExchangeOrder.getBatchNo());
////            logger.error("通兑失败", e);
////            String msg = Optional.ofNullable(e.getMsg()).orElse("");
////            msg = msg.length() > 450 ? msg.substring(0, 450) : msg;
////            otcExchangeOrder.setResult(msg);
////            otcExchangeOrder.setStatus("0"); // 通兑失败
////            otcExchangeOrderDao.insert(otcExchangeOrder);
////        } catch (Exception e) {
////            logger.error("通兑失败流水号{}", otcExchangeOrder.getBatchNo());
////            logger.error("通兑失败", e);
////            String msg = Optional.ofNullable(e.toString()).orElse("");
////            msg = msg.length() > 450 ? msg.substring(0, 450) : msg;
////            otcExchangeOrder.setResult(msg);
////            otcExchangeOrder.setStatus("0"); // 通兑失败
////            otcExchangeOrderDao.insert(otcExchangeOrder);
////        }
////
////        return otcExchangeOrder;
////    }

    /**
     * 通兑业务入口
     * @param otcExchangeOrder
     * @return
     */
    @Override
    @Transactional
    public OtcExchangeOrder custUserPay(OtcExchangeOrder otcExchangeOrder) {

        preCheckOtcExchangeOrder(otcExchangeOrder); // 检查批次号是否存在
        // 处理通兑业务
        try {
            otcExchangeOrder.setId(SnowFlake.createSnowFlake().nextIdString());
            otcExchangeOrder.setCreateDate(LocalDateTime.now());
            otcExchangeOrder.setUpdateDate(LocalDateTime.now());

            PlatUser outUser = platUserDao.findById(otcExchangeOrder.getUserId());
            PlatUser inUser = platUserDao.findById(otcExchangeOrder.getAskUserId());
            checkUser(outUser, inUser); // 检查至少有一方是商家

            // 出款方信息
            otcExchangeOrder.setRealName(outUser.getRealName());
            otcExchangeOrder.setMobile(outUser.getMobile());
            otcExchangeOrder.setMail(outUser.getMail());
            // 收款方信息
            otcExchangeOrder.setAskRealName(inUser.getRealName());
            otcExchangeOrder.setAskUserMobile(inUser.getMobile());
            otcExchangeOrder.setAskUserMail(inUser.getMail());

            BigDecimal feeVolume = otcExchangeOrder.getVolume().subtract(otcExchangeOrder.getRealVolume());// 手续费由otc传过来  = 付款数量 -  收款数量
            otcExchangeOrder.setFeeVolume(feeVolume);
            otcExchangeOrder.setAskFeeVolume(BigDecimal.ZERO);
            lastCheckOtcExchangeOrder(otcExchangeOrder);// 检查数量，手续费参数是否正确
            otcExchangeOrder.setStatus("1"); // 通兑成功
            otcExchangeOrder.setResult("通兑转入成功");
            // == 资产划转
            otcExchangeOrderService.exchangeCustVolumeInOut(otcExchangeOrder);

        } catch (PlatException e) {
            logger.error("通兑失败流水号{}", otcExchangeOrder.getBatchNo());
            logger.error("通兑失败", e);
            String msg = Optional.ofNullable(e.getMsg()).orElse("");
            msg = msg.length() > 450 ? msg.substring(0, 450) : msg;
            otcExchangeOrder.setResult(msg);
            otcExchangeOrder.setStatus("0"); // 通兑失败
            otcExchangeOrderDao.insert(otcExchangeOrder);
        } catch (Exception e) {
            logger.error("通兑失败流水号{}", otcExchangeOrder.getBatchNo());
            logger.error("通兑失败", e);
            String msg = Optional.ofNullable(e.toString()).orElse("");
            msg = msg.length() > 450 ? msg.substring(0, 450) : msg;
            otcExchangeOrder.setResult(msg);
            otcExchangeOrder.setStatus("0"); // 通兑失败
            otcExchangeOrderDao.insert(otcExchangeOrder);
        }

        return otcExchangeOrder;
    }

    @Override
    @Transactional
    public OtcExchangeOrder custOtcPay(OtcExchangeOrder otcExchangeOrder, Map<String, String> paramMap, OtcExchangeOrderVO otcExchangeOrderVO) {
        otcAccountSecretService.checkAccountSecret(otcExchangeOrderVO, paramMap); // 来源安全校验
        return this.custUserPay(otcExchangeOrder);
    }
    /**
     * 通兑业务资产划转
     *
     * @param otcExchangeOrder
     */
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void exchangeVolumeInOut(OtcExchangeOrder otcExchangeOrder) {
//
//        otcExchangeOrderDao.insert(otcExchangeOrder);// 记录请求成功日志
//
//        // 转币方转出可用资金
//        BigDecimal outVolume = otcExchangeOrder.getVolume().add(otcExchangeOrder.getFeeVolume());
//        offlineCoinVolumeService.coinVolumeSubtract(otcExchangeOrder.getUserId(), otcExchangeOrder.getCoinId(), outVolume, otcExchangeOrder.getBatchNo());
//
//        // 收币方转入可用资金
//        BigDecimal inVolume = otcExchangeOrder.getVolume().subtract(otcExchangeOrder.getAskFeeVolume());
//        offlineCoinVolumeService.coinVolumeAdd(otcExchangeOrder.getAskUserId(), otcExchangeOrder.getCoinId(), otcExchangeOrder.getSymbol(), inVolume, otcExchangeOrder.getBatchNo());
//
//        // ==feeVolume
//    }

    /**
     * 通兑业务资产划转
     * @param otcExchangeOrder
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void exchangeCustVolumeInOut(OtcExchangeOrder otcExchangeOrder) {

        otcExchangeOrderDao.insert(otcExchangeOrder);// 记录请求成功日志

        // 转币方转出可用资金
        BigDecimal outVolume = otcExchangeOrder.getVolume();
        offlineCoinVolumeService.coinVolumeSubtract(otcExchangeOrder.getUserId(), otcExchangeOrder.getCoinId(), outVolume, otcExchangeOrder.getBatchNo());

        // 收币方转入可用资金
        BigDecimal inVolume = otcExchangeOrder.getRealVolume();
        offlineCoinVolumeService.coinVolumeAdd(otcExchangeOrder.getAskUserId(), otcExchangeOrder.getCoinId(), otcExchangeOrder.getSymbol(), inVolume, otcExchangeOrder.getBatchNo());

        // ==feeVolume   otc总账户收取手续费
        BigDecimal feeVolume = otcExchangeOrder.getFeeVolume();
        if (feeVolume.compareTo(BigDecimal.ZERO) == 1) {
            OtcAccountSecret secret = otcAccountSecretService.findByPublishSource("otc");
            if (Objects.isNull(secret)) {
                throw new PlatException(Constants.PARAM_ERROR, "otc总账户未配置");
            }
            offlineCoinVolumeService.coinVolumeAdd(secret.getUserId(), otcExchangeOrder.getCoinId(), otcExchangeOrder.getSymbol(), feeVolume, otcExchangeOrder.getBatchNo());
        }
    }

    private void checkUser(PlatUser payUser, PlatUser receiptUser) {
        if (Objects.isNull(payUser)) {
            throw new PlatException(Constants.PARAM_ERROR, "付款方信息输入错误");
        }
        if (Objects.isNull(receiptUser)) {
            throw new PlatException(Constants.PARAM_ERROR, "收款方信息输入错误");
        }
        if (payUser.getId().equals(receiptUser.getId())) {
            throw new PlatException(Constants.PARAM_ERROR, "自己不能给自己通兑转账");
        }
        int shTag = 0;
        if (StringUtils.isNotBlank(payUser.getTag()) && payUser.getTag().contains(UserTagEnum.OTC_BUSINESS.getCode())) {
            shTag++;
        }
        if (StringUtils.isNotBlank(receiptUser.getTag()) && receiptUser.getTag().contains(UserTagEnum.OTC_BUSINESS.getCode())) {
            shTag++;
        }
        if (shTag == 0) {
            throw new PlatException(Constants.PARAM_ERROR, "付款方或收款方至少一方是商家");
        }
    }

    private void preCheckOtcExchangeOrder(OtcExchangeOrder otcExchangeOrder) {
        if (StringUtils.isBlank(otcExchangeOrder.getBatchNo())) {
            throw new PlatException(Constants.PARAM_ERROR, "流水号为空，请重新输入");
        }
        OtcExchangeOrder dbExchangeOrder = otcExchangeOrderDao.findByBatchNo(otcExchangeOrder.getBatchNo());
        if (Objects.nonNull(dbExchangeOrder)) {
            throw new PlatException(Constants.PARAM_ERROR, "该流水号已存在");
        }
        OtcExchangeCoinFee coinFee = otcExchangeCoinFeeDao.findBySymbol(otcExchangeOrder.getSymbol());
        if (Objects.isNull(coinFee)) {
            throw new PlatException(Constants.OFFLINE_COIN_NOT_EXSIT_ERROR, "通兑币种不存在");
        }
        otcExchangeOrder.setCoinId(coinFee.getCoinId());
    }

    private void lastCheckOtcExchangeOrder(OtcExchangeOrder otcExchangeOrder) {
        if (otcExchangeOrder.getVolume().compareTo(BigDecimal.ZERO) < 1 || otcExchangeOrder.getRealVolume().compareTo(BigDecimal.ZERO) < 1) {
            throw new PlatException(Constants.PARAM_ERROR, "通兑币种数量输入错误");
        }
        if (otcExchangeOrder.getFeeVolume().compareTo(BigDecimal.ZERO) < 0 || otcExchangeOrder.getAskFeeVolume().compareTo(BigDecimal.ZERO) < 0) {
            throw new PlatException(Constants.PARAM_ERROR, "手续费数据错误");
        }
    }


    /**
     * 计算手续费
     *
     * @param otcExchangeOrder
     * @return
     */
//    private BigDecimal calcFee(OtcExchangeOrder otcExchangeOrder) {
//        OtcExchangeCoinFee coinFee = otcExchangeCoinFeeDao.findBySymbol(otcExchangeOrder.getSymbol());
//        if (Objects.isNull(coinFee)) {
//            throw new PlatException(Constants.OFFLINE_COIN_NOT_EXSIT_ERROR, "通兑币种不存在");
//        }
//        otcExchangeOrder.setCoinId(coinFee.getCoinId());// 设置通兑币种ID
//        String feeType = coinFee.getFeeType();
//        BigDecimal feeVolume = BigDecimal.ZERO; // 手续费
//        if (feeType.equals(OfflineFeeTypeEnum.STATIC.getCode())) { // 固定
//            String sellFeeStep = coinFee.getExFeeStep();
//            feeVolume = FeeUtils.getFeeByStep(otcExchangeOrder.getVolume(), sellFeeStep);
//        } else if (feeType.equals(OfflineFeeTypeEnum.SCALE.getCode())) { // 比例
//            feeVolume = otcExchangeOrder.getVolume().multiply(coinFee.getExFee());
//        }
//        return feeVolume;
//    }
}
