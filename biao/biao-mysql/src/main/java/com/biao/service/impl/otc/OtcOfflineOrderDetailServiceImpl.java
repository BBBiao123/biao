package com.biao.service.impl.otc;

import com.biao.constant.Constants;
import com.biao.entity.PlatUser;
import com.biao.entity.otc.*;
import com.biao.enums.*;
import com.biao.exception.PlatException;
import com.biao.mapper.PlatUserDao;
import com.biao.mapper.otc.*;
import com.biao.pojo.ResponsePage;
import com.biao.service.OfflineCoinVolumeService;
import com.biao.service.otc.OtcOfflineOrderDetailLogService;
import com.biao.service.otc.OtcOfflineOrderDetailService;
import com.biao.service.otc.OtcOfflineOrderService;
import com.biao.util.FeeUtils;
import com.biao.util.RandomUtils;
import com.biao.util.SnowFlake;
import com.biao.vo.otc.OtcOfflineDetailCountVO;
import com.biao.vo.otc.OtcOfflineOrderDetailVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OtcOfflineOrderDetailServiceImpl implements OtcOfflineOrderDetailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OtcOfflineOrderDetailServiceImpl.class);

    @Autowired
    private OtcOfflineOrderDetailDao otcOfflineOrderDetailDao;
    @Autowired
    private OtcOfflineOrderPayDao otcOfflineOrderPayDao;

    @Autowired
    private OtcOfflineOrderDao otcOfflineOrderDao;

    @Autowired
    private OtcUserBankDao otcUserBankDao;

    @Autowired
    private PlatUserDao platUserDao;

    @Autowired
    private OtcOfflineOrderDetailPayDao otcOfflineOrderDetailPayDao;

    @Autowired
    private OtcOfflineCoinDao offlineCoinDao;

    @Autowired
    private OtcOfflineOrderDetailLogService otcOfflineOrderDetailLogService;

    @Autowired
    private OfflineCoinVolumeService offlineCoinVolumeService;

    @Autowired
    private OtcOfflineOrderService otcOfflineOrderService;

    @Autowired
    private OtcOfflineAppealDao otcOfflineAppealDao;

    @Value("${remainingTime:30}")
    private Integer remainingTime;

    private static final String ALLOW_ZERO = "1";

    private static final String NO_ALLOW_ZERO = "0";

    private static final String HIDE_CHAR_DEFAULT = "**";// 银行卡，微信，支付宝，真名隐藏

    @Override
    public List<OtcOfflineOrderDetailPay> findPayByOfflineSubOrder(String userId, String subOrderId) {
        return otcOfflineOrderDetailPayDao.findBySubOrderId(subOrderId);
    }

    @Override
    public OtcOfflineOrderDetail findByUserIdAndDetailId(String userId, String subOrderId, String loginSource) {
        OtcOfflineOrderDetail otcOfflineOrderDetail = otcOfflineOrderDetailDao.findByUserIdSubOrderId(userId, subOrderId, loginSource);
        fillRemainingTime(otcOfflineOrderDetail);
        hideSensitiveInfo(otcOfflineOrderDetail);
        return otcOfflineOrderDetail;
    }

    public ResponsePage<OtcOfflineOrderDetail> findMySuborderByStatus(OtcOfflineOrderDetailVO otcOfflineOrderDetailVO) {
        ResponsePage<OtcOfflineOrderDetail> responsePage = new ResponsePage<>();
        Page<OtcOfflineOrderDetail> page = PageHelper.startPage(otcOfflineOrderDetailVO.getCurrentPage(), otcOfflineOrderDetailVO.getShowCount());
        List<OtcOfflineOrderDetail> offlineOrderDetails = null;
        if (StringUtils.isNotBlank(otcOfflineOrderDetailVO.getStatus())) {
            offlineOrderDetails = otcOfflineOrderDetailDao.findMySuborders(otcOfflineOrderDetailVO.getUserId(), otcOfflineOrderDetailVO.getLoginSource(), otcOfflineOrderDetailVO.getStatus());
        } else {
            offlineOrderDetails = otcOfflineOrderDetailDao.findAllSubOrders(otcOfflineOrderDetailVO.getUserId(), otcOfflineOrderDetailVO.getLoginSource());
        }
        // 设置取消倒计时
        if (CollectionUtils.isNotEmpty(offlineOrderDetails)) {
            offlineOrderDetails.stream().filter(o -> OfflineOrderDetailStatusEnum.NOMAL.getCode().equals(o.getStatus()))
                    .map(o -> {
                        fillRemainingTime(o);
                        hideSensitiveInfo(o);
                        return o;
                    }).collect(Collectors.toList());
        }
        responsePage.setCount(page.getTotal());
        responsePage.setList(offlineOrderDetails);
        return responsePage;
    }

    private void fillRemainingTime(OtcOfflineOrderDetail otcOfflineOrderDetail) {
        if (Objects.nonNull(otcOfflineOrderDetail) && OfflineOrderDetailStatusEnum.NOMAL.getCode().equals(otcOfflineOrderDetail.getStatus())) {
            final LocalDateTime endTime = otcOfflineOrderDetail.getCreateDate().plusMinutes(remainingTime);
            final LocalDateTime now = LocalDateTime.now();
            if (now.isEqual(endTime) || now.isAfter(endTime)) {
                otcOfflineOrderDetail.setRemainingTime(0);
            } else {
                final int until = (int) now.until(endTime, ChronoUnit.SECONDS);
                otcOfflineOrderDetail.setRemainingTime(until);
            }
        }
    }

    /**
     * 隐藏敏感信息（微信，银行卡，真名。。。），避免返回入客户端
     */
    private void hideSensitiveInfo(OtcOfflineOrderDetail data) {

        if (Objects.nonNull(data)) {
            if (StringUtils.isNotBlank(data.getAskUserId())) {
                data.setAskUserId(HIDE_CHAR_DEFAULT);
            }
        }
    }

    /**
     * 查询我的所有订单
     *
     * @return
     */
    @Override
    public ResponsePage<OtcOfflineOrderDetail> findAllSubOrder(OtcOfflineOrderDetailVO otcOfflineOrderDetailVO) {
        ResponsePage<OtcOfflineOrderDetail> responsePage = new ResponsePage<>();
        Page<OtcOfflineOrderDetail> page = PageHelper.startPage(otcOfflineOrderDetailVO.getCurrentPage(), otcOfflineOrderDetailVO.getShowCount());
        List<OtcOfflineOrderDetail> offlineOrderDetails = otcOfflineOrderDetailDao.findAllSubOrders(otcOfflineOrderDetailVO.getUserId(), otcOfflineOrderDetailVO.getLoginSource());
        // 设置取消倒计时
        if (CollectionUtils.isNotEmpty(offlineOrderDetails)) {
            offlineOrderDetails.stream().filter(o -> OfflineOrderDetailStatusEnum.NOMAL.getCode().equals(o.getStatus()))
                    .map(o -> {
                        fillRemainingTime(o);
                        hideSensitiveInfo(o);
                        return o;
                    }).collect(Collectors.toList());
        }
        responsePage.setCount(page.getTotal());
        responsePage.setList(offlineOrderDetails);
        return responsePage;
    }

    /**
     * 统计我的订单条数，未付款订单总条数，已付款订单总条数
     *
     * @param userId
     * @return
     */
    @Override
    public OtcOfflineDetailCountVO countOrderDetail(String userId) {
        return otcOfflineOrderDetailDao.countDetail(userId);
    }

    @Override
    @Transactional
    public void buy(OtcOfflineOrderDetail buyOrderDetail) {

        OtcOfflineOrder otcOfflineOrder = otcOfflineOrderDao.findById(buyOrderDetail.getOrderId());
        commonDetailCheck(buyOrderDetail, otcOfflineOrder); // 买单和广告数据校验
        preBuyCheck(buyOrderDetail, otcOfflineOrder); // 买单前置校验

        // 买家订单
        buyOrderDetail.setId(SnowFlake.createSnowFlake().nextIdString());
        buyOrderDetail.setRadomNum(RandomUtils.createCode());
        buyOrderDetail.setAskUserId(otcOfflineOrder.getUserId());
        buyOrderDetail.setAskUserMobile(otcOfflineOrder.getMobile());
        buyOrderDetail.setAskRealName(otcOfflineOrder.getRealName());
        buyOrderDetail.setCoinId(otcOfflineOrder.getCoinId());
        buyOrderDetail.setSymbol(otcOfflineOrder.getSymbol());
        buyOrderDetail.setStatus(OfflineOrderDetailStatusEnum.NOMAL.getCode());
        buyOrderDetail.setPrice(otcOfflineOrder.getPrice());
        BigDecimal totalPrice = buyOrderDetail.getVolume().multiply(otcOfflineOrder.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
        buyOrderDetail.setTotalPrice(totalPrice);
        buyOrderDetail.setCreateDate(LocalDateTime.now());
        buyOrderDetail.setUpdateDate(LocalDateTime.now());
        buyOrderDetail.setRemarks("buy");
        buyOrderDetail.setExType(String.valueOf(TradeEnum.BUY.ordinal()));
        buyOrderDetail.setSubOrderId(SnowFlake.createSnowFlake().nextIdString());
        buyOrderDetail.setSupportCurrencyCode(otcOfflineOrder.getSupportCurrencyCode());
        buyOrderDetail.setOrderUserId(otcOfflineOrder.getUserId());
        BigDecimal feeVolume = calcFee(buyOrderDetail, otcOfflineOrder);
        buyOrderDetail.setFeeVolume(feeVolume);
        otcOfflineOrderDetailDao.insert(buyOrderDetail); // 保存买家订单

        // 卖家订单
        OtcOfflineOrderDetail selOrderlDetail = new OtcOfflineOrderDetail();
        BeanUtils.copyProperties(buyOrderDetail, selOrderlDetail);
        selOrderlDetail.setId(SnowFlake.createSnowFlake().nextIdString());
        selOrderlDetail.setUserId(otcOfflineOrder.getUserId());
        selOrderlDetail.setUserMobile(otcOfflineOrder.getMobile());
        selOrderlDetail.setRealName(otcOfflineOrder.getRealName());
        selOrderlDetail.setAskUserId(buyOrderDetail.getUserId());
        selOrderlDetail.setAskUserMobile(buyOrderDetail.getUserMobile());
        selOrderlDetail.setAskRealName(buyOrderDetail.getRealName());
        selOrderlDetail.setRemarks("sell");
        selOrderlDetail.setExType(String.valueOf(TradeEnum.SELL.ordinal()));
        otcOfflineOrderDetailDao.insert(selOrderlDetail); // 保存卖家订单
        saveBuyDetailPay(selOrderlDetail, otcOfflineOrder); // 保存卖家支付方式

        String batchNo = SnowFlake.createSnowFlake().nextIdString();

        otcOfflineOrderService.otcDetailSaveUpdateOrder(buyOrderDetail, otcOfflineOrder, batchNo); // 订单导致广告状态变更
        offlineCoinVolumeService.otcDetailSaveUpdateCoinVolume(selOrderlDetail, otcOfflineOrder, batchNo); // 卖币订单导致卖币方C2C资产状态变更

        otcOfflineOrderDetailLogService.saveLog(buyOrderDetail.getUserId(), buyOrderDetail.getSubOrderId(), buyOrderDetail.getPublishSource(), batchNo);
        otcOfflineOrderDetailLogService.saveLog(selOrderlDetail.getUserId(), selOrderlDetail.getSubOrderId(), selOrderlDetail.getPublishSource(), batchNo);

        fillRemainingTime(buyOrderDetail); // 填充倒计时时间
    }

    private void saveBuyDetailPay(OtcOfflineOrderDetail sellDetail, OtcOfflineOrder otcOfflineOrder) {
        List<OtcOfflineOrderPay> otcOfflineOrderPays = otcOfflineOrderPayDao.findByOrderId(otcOfflineOrder.getId());
        List<OtcOfflineOrderDetailPay> otcOfflineOrderDetailPays = new ArrayList<>();
        OtcOfflineOrderDetailPay otcOfflineOrderDetailPay = null;
        for (OtcOfflineOrderPay orderPay : otcOfflineOrderPays) {
            otcOfflineOrderDetailPay = new OtcOfflineOrderDetailPay();
            BeanUtils.copyProperties(orderPay, otcOfflineOrderDetailPay);
            otcOfflineOrderDetailPay.setId(SnowFlake.createSnowFlake().nextIdString());
            otcOfflineOrderDetailPay.setSubOrderId(sellDetail.getSubOrderId());
            otcOfflineOrderDetailPay.setDetailId(sellDetail.getId());
            otcOfflineOrderDetailPay.setCreateDate(LocalDateTime.now());
            otcOfflineOrderDetailPay.setUpdateDate(LocalDateTime.now());
            otcOfflineOrderDetailPay.setSupportCurrencyCode(otcOfflineOrder.getSupportCurrencyCode());
            otcOfflineOrderDetailPays.add(otcOfflineOrderDetailPay);
        }
        otcOfflineOrderDetailPayDao.insertBatch(otcOfflineOrderDetailPays);
    }

    private void preBuyCheck(OtcOfflineOrderDetail offlineOrderDetail, OtcOfflineOrder otcOfflineOrder) {
        if (!otcOfflineOrder.getExType().equals(String.valueOf(TradeEnum.SELL.ordinal()))) {
            throw new PlatException(Constants.OPERRATION_ERROR, "该广告为卖出广告,只能买入操作");
        }
        PlatUser platUser = platUserDao.findById(offlineOrderDetail.getUserId());
        if (Objects.isNull(platUser)) {
            throw new PlatException(Constants.PARAM_ERROR, "用户ID错误");
        }
        if (StringUtils.isNotEmpty(platUser.getTag()) && (platUser.getTag().contains(UserTagEnum.FINANCE.getCode()) || platUser.getTag().contains(UserTagEnum.OTC_ADVERT.getCode()))) {
            LOGGER.info("广告商账号{}买入不需要限制", platUser.getMobile());
        } else {
            List<OtcOfflineOrderDetail> timingDetails = otcOfflineOrderDetailDao.findTiming(offlineOrderDetail.getUserId(), offlineOrderDetail.getPublishSource());
            if (UserCardStatusEnum.authRealName(platUser.getCardStatus(),platUser.getCountryCode())) {
                if (timingDetails.size() >= 5)
                    throw new PlatException(Constants.NOT_PAY_ERROR, "买入失败，每人未完成的订单不能超过5笔,请先处理现有订单");
            } else {
                if (timingDetails.size() > 0)
                    throw new PlatException(Constants.NOT_PAY_ERROR, "有未完成的订单");
            }
        }
    }

    private void commonDetailCheck(OtcOfflineOrderDetail offlineOrderDetail, OtcOfflineOrder otcOfflineOrder) {
        greaterZero(offlineOrderDetail.getVolume(), NO_ALLOW_ZERO, "订单数量错误");
        loginSourceCheck(offlineOrderDetail.getPublishSource(), offlineOrderDetail.getPublishSource()); // 校验登录渠道和发布渠道是否一致
        if (Objects.isNull(otcOfflineOrder)) {
            throw new PlatException(Constants.PARAM_ERROR, "订单参数错误");
        }
        if (!OfflineOrderStatusEnum.NOMAL.getCode().equals(otcOfflineOrder.getStatus())) {
            throw new PlatException(Constants.PARAM_ERROR, "广告状态有误");
        }
        BigDecimal exVolume = otcOfflineOrder.getVolume().subtract(otcOfflineOrder.getLockVolume()).subtract(otcOfflineOrder.getSuccessVolume());
        if (offlineOrderDetail.getVolume().compareTo(exVolume) == 1) {
            throw new PlatException(Constants.PARAM_ERROR, "广告数量不足,请修改订单数量");
        }
//        if (Objects.nonNull(otcOfflineOrder.getMinVolume()) && otcOfflineOrder.getMinVolume().compareTo(offlineOrderDetail.getVolume()) == 1) {
//            throw new PlatException(Constants.PARAM_ERROR, "订单数量错误，不能小于广告设置交易最小数量");
//        }
//        if (Objects.nonNull(otcOfflineOrder.getMaxVolume()) && offlineOrderDetail.getVolume().compareTo(otcOfflineOrder.getMaxVolume()) == 1) {
//            throw new PlatException(Constants.PARAM_ERROR, "订单数量错误，不能大于广告设置交易最小数量");
//        }
        if (offlineOrderDetail.getUserId().equals(otcOfflineOrder.getUserId())) {
            throw new PlatException(Constants.BUY_OWN_ERROR, "自己的广告,不能下单");
        }
        if (!otcOfflineOrder.getStatus().equals(OfflineOrderStatusEnum.NOMAL.getCode())) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,广告已下架");
        }
    }

    /**
     * @param equalZero 1允许，0不允许
     * @param target
     * @param errorMsg
     */
    private void greaterZero(BigDecimal target, String equalZero, String errorMsg) {
        if (Objects.isNull(target)) {
            throw new PlatException(Constants.OFFLINE_VOLUME_ERROR, errorMsg);
        }
        if (ALLOW_ZERO.equals(equalZero) && target.compareTo(BigDecimal.ZERO) == -1) {
            throw new PlatException(Constants.OFFLINE_VOLUME_ERROR, errorMsg);
        }
        if (NO_ALLOW_ZERO.equals(equalZero) && target.compareTo(BigDecimal.ZERO) != 1) {
            throw new PlatException(Constants.OFFLINE_VOLUME_ERROR, errorMsg);
        }
    }

    @Override
    @Transactional
    public void confirmPayment(OtcOfflineOrderDetailVO offlineConfirmVO) {
        OtcOfflineOrderDetail buyOrderDetail = otcOfflineOrderDetailDao.findByUserIdSubOrderId(offlineConfirmVO.getUserId(), offlineConfirmVO.getSubOrderId(), offlineConfirmVO.getLoginSource());
        // 确认付款条件：订单状态为下单状态，订单方向为买入
        if (Objects.isNull(buyOrderDetail)
                || !OfflineOrderDetailStatusEnum.NOMAL.getCode().equals(buyOrderDetail.getStatus())
                || !String.valueOf(TradeEnum.BUY.ordinal()).equals(buyOrderDetail.getExType())) {
            throw new PlatException(Constants.PARAM_ERROR, "订单参数错误");
        }
        loginSourceCheck(offlineConfirmVO.getLoginSource(), buyOrderDetail.getPublishSource()); // 校验登录渠道和发布渠道
        checkAppeal(offlineConfirmVO.getSubOrderId()); // 申诉判断

        String batchNo = SnowFlake.createSnowFlake().nextIdString();
        otcOfflineOrderDetailLogService.saveLog(offlineConfirmVO.getSubOrderId(), batchNo); // 订单流水
        // 更新买家订单
        buyOrderDetail.setConfirmPaymentDate(LocalDateTime.now());
        buyOrderDetail.setStatus(OfflineOrderDetailStatusEnum.CONFIRM_PAY.getCode());
        long count = otcOfflineOrderDetailDao.updateDetailPayment(buyOrderDetail);
        if (count != 1) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        // 更新卖家订单
        OtcOfflineOrderDetail sellOrderDetail = otcOfflineOrderDetailDao.findByUserIdSubOrderId(buyOrderDetail.getAskUserId(), offlineConfirmVO.getSubOrderId(), offlineConfirmVO.getLoginSource());
        sellOrderDetail.setConfirmPaymentDate(LocalDateTime.now());
        sellOrderDetail.setStatus(OfflineOrderDetailStatusEnum.CONFIRM_PAY.getCode());
        count = otcOfflineOrderDetailDao.updateDetailPayment(sellOrderDetail);
        if (count != 1) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }

        otcOfflineOrderDetailLogService.saveLog(offlineConfirmVO.getSubOrderId(), batchNo); // 订单流水
    }

    @Override
    @Transactional
    public void sell(OtcOfflineOrderDetail sellOrderDetail, String detailPay) {
        OtcOfflineOrder otcOfflineOrder = otcOfflineOrderDao.findById(sellOrderDetail.getOrderId());
        commonDetailCheck(sellOrderDetail, otcOfflineOrder); // 买单和广告数据校验
        preSellCheck(sellOrderDetail, otcOfflineOrder); // 卖单前置校验

        BigDecimal feeVolume = calcFee(sellOrderDetail, otcOfflineOrder);
        // 卖家订单
        sellOrderDetail.setId(SnowFlake.createSnowFlake().nextIdString());
        sellOrderDetail.setRadomNum(RandomUtils.createCode());
        sellOrderDetail.setAskUserId(otcOfflineOrder.getUserId());
        sellOrderDetail.setAskUserMobile(otcOfflineOrder.getMobile());
        sellOrderDetail.setAskRealName(otcOfflineOrder.getRealName());
        sellOrderDetail.setCoinId(otcOfflineOrder.getCoinId());
        sellOrderDetail.setSymbol(otcOfflineOrder.getSymbol());
        sellOrderDetail.setStatus(OfflineOrderDetailStatusEnum.NOMAL.getCode());
        sellOrderDetail.setPrice(otcOfflineOrder.getPrice());
        BigDecimal totalPrice = sellOrderDetail.getVolume().multiply(otcOfflineOrder.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
        sellOrderDetail.setTotalPrice(totalPrice);
        sellOrderDetail.setCreateDate(LocalDateTime.now());
        sellOrderDetail.setUpdateDate(LocalDateTime.now());
        sellOrderDetail.setRemarks("sell");
        sellOrderDetail.setExType(String.valueOf(TradeEnum.SELL.ordinal()));
        sellOrderDetail.setSubOrderId(SnowFlake.createSnowFlake().nextIdString());
        sellOrderDetail.setFeeVolume(feeVolume);
        sellOrderDetail.setSupportCurrencyCode(otcOfflineOrder.getSupportCurrencyCode());
        sellOrderDetail.setOrderUserId(otcOfflineOrder.getUserId());
        otcOfflineOrderDetailDao.insert(sellOrderDetail); // 保存卖家订单
        saveSellDetailPay(sellOrderDetail, otcOfflineOrder, detailPay);// 保存卖家支付方式

        // 买家订单
        OtcOfflineOrderDetail buyOrderDetail = new OtcOfflineOrderDetail();
        BeanUtils.copyProperties(sellOrderDetail, buyOrderDetail);
        buyOrderDetail.setId(SnowFlake.createSnowFlake().nextIdString());
        buyOrderDetail.setUserId(otcOfflineOrder.getUserId());
        buyOrderDetail.setUserMobile(otcOfflineOrder.getMobile());
        buyOrderDetail.setRealName(otcOfflineOrder.getRealName());
        buyOrderDetail.setAskUserId(sellOrderDetail.getUserId());
        buyOrderDetail.setAskUserMobile(sellOrderDetail.getUserMobile());
        buyOrderDetail.setAskRealName(sellOrderDetail.getRealName());
        buyOrderDetail.setRemarks("buy");
        buyOrderDetail.setExType(String.valueOf(TradeEnum.BUY.ordinal()));
        otcOfflineOrderDetailDao.insert(buyOrderDetail); // 保存买家订单

        String batchNo = SnowFlake.createSnowFlake().nextIdString();
        otcOfflineOrderService.otcDetailSaveUpdateOrder(sellOrderDetail, otcOfflineOrder, batchNo); // 订单导致广告状态变更
        offlineCoinVolumeService.otcDetailSaveUpdateCoinVolume(sellOrderDetail, otcOfflineOrder, batchNo); // 卖币订单导致卖币方C2C资产状态变更

        otcOfflineOrderDetailLogService.saveLog(sellOrderDetail.getUserId(), sellOrderDetail.getSubOrderId(), sellOrderDetail.getPublishSource(), batchNo);
        otcOfflineOrderDetailLogService.saveLog(buyOrderDetail.getUserId(), buyOrderDetail.getSubOrderId(), buyOrderDetail.getPublishSource(), batchNo);
    }

    private void saveSellDetailPay(OtcOfflineOrderDetail sellDetail, OtcOfflineOrder otcOfflineOrder, String detailPay) {

        List<OtcUserBank> banks = otcUserBankDao.findByUserIdAndStatus(sellDetail.getUserId(), "1"); // 查询我的银行卡列表

        if (CollectionUtils.isEmpty(banks)) {
            throw new PlatException(Constants.PARAM_ERROR, "请绑定银行卡");
        }
        List<OtcOfflineOrderDetailPay> otcOfflineOrderDetailPays = new ArrayList<>();
        OtcOfflineOrderDetailPay otcOfflineOrderDetailPay = null;
        for (OtcUserBank bank : banks) {
            if (!bank.getSupportCurrencyCode().contains(otcOfflineOrder.getSupportCurrencyCode())) {
                continue;
            }
            otcOfflineOrderDetailPay = new OtcOfflineOrderDetailPay();
            BeanUtils.copyProperties(bank, otcOfflineOrderDetailPay);
            otcOfflineOrderDetailPay.setId(SnowFlake.createSnowFlake().nextIdString());
            otcOfflineOrderDetailPay.setSubOrderId(sellDetail.getSubOrderId());
            otcOfflineOrderDetailPay.setOrderId(sellDetail.getOrderId());
            otcOfflineOrderDetailPay.setDetailId(sellDetail.getId());
            otcOfflineOrderDetailPay.setSupportCurrencyCode(otcOfflineOrder.getSupportCurrencyCode());
            otcOfflineOrderDetailPay.setCreateDate(LocalDateTime.now());
            otcOfflineOrderDetailPay.setUpdateDate(LocalDateTime.now());
            otcOfflineOrderDetailPays.add(otcOfflineOrderDetailPay);
        }
        if (CollectionUtils.isEmpty(otcOfflineOrderDetailPays)) {
            throw new PlatException(Constants.PARAM_ERROR, "已绑定的银行卡不支持广告法币币种");
        }
        otcOfflineOrderDetailPayDao.insertBatch(otcOfflineOrderDetailPays);
    }

    private void preSellCheck(OtcOfflineOrderDetail offlineOrderDetail, OtcOfflineOrder otcOfflineOrder) {
        if (!otcOfflineOrder.getExType().equals(String.valueOf(TradeEnum.BUY.ordinal()))) {
            throw new PlatException(Constants.OPERRATION_ERROR, "该广告为买入广告,只能卖出操作");
        }
    }

    private BigDecimal calcFee(OtcOfflineOrderDetail offlineOrderDetail, OtcOfflineOrder otcOfflineOrder) {
        OtcOfflineCoin offlineCoin = offlineCoinDao.findByCoinId(offlineOrderDetail.getCoinId());
        if (Objects.isNull(offlineCoin)) {
            throw new PlatException(Constants.OFFLINE_COIN_NOT_EXSIT_ERROR, "OTC币种不存在");
        }
        String feeType = offlineCoin.getFeeType();
        BigDecimal feeVolume = BigDecimal.ZERO; // 手续费

        if (feeType.equals(OfflineFeeTypeEnum.STATIC.getCode())) {
            String sellFeeStep = offlineCoin.getSellFeeStep();
            feeVolume = FeeUtils.getFeeByStep(offlineOrderDetail.getVolume(), sellFeeStep);
        } else if (feeType.equals(OfflineFeeTypeEnum.SCALE.getCode())) {
            if (otcOfflineOrder.getExType().equals(String.valueOf(TradeEnum.BUY.ordinal()))) { // 买广告
                feeVolume = offlineOrderDetail.getVolume().multiply(offlineCoin.getBuyFee());
            } else { // 卖广告
                feeVolume = offlineOrderDetail.getVolume().multiply(offlineCoin.getSellFee());
            }
        }
        return feeVolume;
    }

    @Override
    @Transactional
    public void confirmReceipt(OtcOfflineOrderDetailVO offlineConfirmVO) {
        checkAppeal(offlineConfirmVO.getSubOrderId()); // 申诉判断
        doConfirmReceiptNoAppeal(offlineConfirmVO, null);
    }

    @Transactional
    public void doConfirmReceiptNoAppeal(OtcOfflineOrderDetailVO offlineConfirmVO, String setBatchNo) {
        OtcOfflineOrderDetail sellOrderDetail = otcOfflineOrderDetailDao.findByUserIdSubOrderId(offlineConfirmVO.getUserId(), offlineConfirmVO.getSubOrderId(), offlineConfirmVO.getLoginSource());
        // 确认收款条件：订单状态已付款，订单方向为卖出
        if (Objects.isNull(sellOrderDetail)
                || !OfflineOrderDetailStatusEnum.CONFIRM_PAY.getCode().equals(sellOrderDetail.getStatus())
                || !String.valueOf(TradeEnum.SELL.ordinal()).equals(sellOrderDetail.getExType())) {
            throw new PlatException(Constants.PARAM_ERROR, "订单参数错误");
        }
        loginSourceCheck(offlineConfirmVO.getLoginSource(), sellOrderDetail.getPublishSource());// 校验登录渠道和发布渠道是否一致
        String batchNo = Optional.ofNullable(setBatchNo).orElse(SnowFlake.createSnowFlake().nextIdString());

        otcOfflineOrderDetailLogService.saveLog(offlineConfirmVO.getSubOrderId(), batchNo); // 订单流水
        // 更新卖家订单
        sellOrderDetail.setConfirmReceiptDate(LocalDateTime.now());
        sellOrderDetail.setStatus(OfflineOrderDetailStatusEnum.CONFIRM_IN.getCode());
        long count = otcOfflineOrderDetailDao.updateDetailReceipt(sellOrderDetail);
        if (count != 1) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        // 更新买家订单
        OtcOfflineOrderDetail buyOrderDetail = otcOfflineOrderDetailDao.findByUserIdSubOrderId(sellOrderDetail.getAskUserId(), offlineConfirmVO.getSubOrderId(), offlineConfirmVO.getLoginSource());
        buyOrderDetail.setConfirmReceiptDate(LocalDateTime.now());
        buyOrderDetail.setStatus(OfflineOrderDetailStatusEnum.CONFIRM_IN.getCode());
        count = otcOfflineOrderDetailDao.updateDetailReceipt(buyOrderDetail);
        if (count != 1) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        otcOfflineOrderDetailLogService.saveLog(offlineConfirmVO.getSubOrderId(), batchNo); // 订单流水

        OtcOfflineOrder offlineOrder = otcOfflineOrderDao.findById(sellOrderDetail.getOrderId());
        otcOfflineOrderService.otcDetailSaveUpdateOrder(sellOrderDetail, offlineOrder, batchNo); // 订单导致广告状态变更
        offlineCoinVolumeService.otcDetailSaveUpdateCoinVolume(sellOrderDetail, offlineOrder, batchNo); // 卖币订单导致卖币方C2C资产状态变更
    }

    @Override
    @Transactional
    public void cancelOrderDetail(OtcOfflineOrderDetailVO offlineConfirmVO) {
        checkAppeal(offlineConfirmVO.getSubOrderId()); // 申诉判断
        doCancelOrderDetailNoAppeal(offlineConfirmVO, null);
    }

    @Transactional
    public void doCancelOrderDetailNoAppeal(OtcOfflineOrderDetailVO offlineConfirmVO, String setBatchNo) {
        OtcOfflineOrderDetail buyOrderDetail = otcOfflineOrderDetailDao.findByUserIdSubOrderId(offlineConfirmVO.getUserId(), offlineConfirmVO.getSubOrderId(), offlineConfirmVO.getLoginSource());// 买订单
        if (Objects.isNull(buyOrderDetail) || !String.valueOf(TradeEnum.BUY.ordinal()).equals(buyOrderDetail.getExType())) {
            throw new PlatException(Constants.PARAM_ERROR, "订单参数错误");
        }
        // 买家只有下单状态或已付款状态，才能取消订单
        if (!OfflineOrderDetailStatusEnum.NOMAL.getCode().equals(buyOrderDetail.getStatus()) && !OfflineOrderDetailStatusEnum.CONFIRM_PAY.getCode().equals(buyOrderDetail.getStatus())) {
            throw new PlatException(Constants.PARAM_ERROR, "订单参数状态有误");
        }
        loginSourceCheck(offlineConfirmVO.getLoginSource(), buyOrderDetail.getPublishSource());// 校验登录渠道和发布渠道是否一致
        String batchNo = Optional.ofNullable(setBatchNo).orElse(SnowFlake.createSnowFlake().nextIdString());
        otcOfflineOrderDetailLogService.saveLog(offlineConfirmVO.getSubOrderId(), batchNo); // 订单流水
        // 更新买家订单
        buyOrderDetail.setStatus(OfflineOrderDetailStatusEnum.CANCEL.getCode());
        buyOrderDetail.setCancelDate(LocalDateTime.now());
        buyOrderDetail.setUpdateBy(offlineConfirmVO.getUpdateBy());
        long count = otcOfflineOrderDetailDao.updateDetailCancel(buyOrderDetail); // 更新买卖订单状态为取消
        if (count != 1) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        // 更新卖家订单状态
        OtcOfflineOrderDetail sellOrderDetail = otcOfflineOrderDetailDao.findByUserIdSubOrderId(buyOrderDetail.getAskUserId(), offlineConfirmVO.getSubOrderId(), offlineConfirmVO.getLoginSource());// 买订单
        sellOrderDetail.setStatus(OfflineOrderDetailStatusEnum.CANCEL.getCode());
        sellOrderDetail.setCancelDate(LocalDateTime.now());
        sellOrderDetail.setUpdateBy(offlineConfirmVO.getUpdateBy());
        count = otcOfflineOrderDetailDao.updateDetailCancel(sellOrderDetail); // 更新买卖订单状态为取消
        if (count != 1) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        otcOfflineOrderDetailLogService.saveLog(offlineConfirmVO.getSubOrderId(), batchNo); // 订单流水

        OtcOfflineOrder otcOfflineOrder = otcOfflineOrderDao.findById(buyOrderDetail.getOrderId());
        otcOfflineOrderService.otcDetailSaveUpdateOrder(buyOrderDetail, otcOfflineOrder, batchNo); // 订单导致广告状态变更
        offlineCoinVolumeService.otcDetailSaveUpdateCoinVolume(sellOrderDetail, otcOfflineOrder, batchNo); // 卖币订单导致卖币方C2C资产状态变更
    }

    /**
     * 判断登录渠道和广告订单发布渠道是否一致。（默认空串和null是一致）
     *
     * @param loginSource
     * @param publishSource
     */
    private void loginSourceCheck(String loginSource, String publishSource) {
        if (StringUtils.isBlank(loginSource) != StringUtils.isBlank(publishSource)) {
            throw new PlatException(Constants.PARAM_ERROR, "登录渠道和广告不匹配");
        }
        if (Objects.nonNull(loginSource) && !loginSource.equals(publishSource)) {
            throw new PlatException(Constants.PARAM_ERROR, "登录渠道和广告不匹配");
        }
        if (Objects.nonNull(publishSource) && !publishSource.equals(loginSource)) {
            throw new PlatException(Constants.PARAM_ERROR, "登录渠道和广告不匹配");
        }
    }

    public void checkAppeal(String subOrderId) {
        List<OtcOfflineAppeal> appealList = otcOfflineAppealDao.findBySubOrderIdAndStatus(subOrderId, "1");
        if (CollectionUtils.isNotEmpty(appealList)) {
            throw new PlatException(Constants.SUBORDER_EXISTS_APPEAL, "该订单已被申诉，请先处理申诉单或者联系客服");
        }

        appealList = otcOfflineAppealDao.findBySubOrderIdAndStatus(subOrderId, "2");
        if (CollectionUtils.isNotEmpty(appealList)) {
            throw new PlatException(Constants.SUBORDER_EXISTS_APPEAL, "该订单已被客服处理，请刷新页面查看订单状态。若有疑问请联系客服。");
        }
    }
}
