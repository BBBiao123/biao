package com.biao.service.impl.otc;

import com.biao.constant.Constants;
import com.biao.entity.OfflineCancelLog;
import com.biao.entity.otc.*;
import com.biao.enums.*;
import com.biao.exception.PlatException;
import com.biao.mapper.OfflineCancelLogDao;
import com.biao.mapper.OfflineCoinVolumeDao;
import com.biao.mapper.otc.OtcOfflineCoinDao;
import com.biao.mapper.otc.OtcOfflineOrderDao;
import com.biao.mapper.otc.OtcOfflineOrderPayDao;
import com.biao.mapper.otc.OtcUserBankDao;
import com.biao.pojo.ResponsePage;
import com.biao.service.OfflineCoinVolumeLogService;
import com.biao.service.OfflineCoinVolumeService;
import com.biao.service.otc.OtcOfflineOrderLogService;
import com.biao.service.otc.OtcOfflineOrderService;
import com.biao.util.FeeUtils;
import com.biao.util.SnowFlake;
import com.biao.vo.otc.OtcOfflineOrderVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OtcOfflineOrderServiceImpl implements OtcOfflineOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OtcOfflineOrderServiceImpl.class);

    @Autowired
    private OtcOfflineOrderDao otcOfflineOrderDao;

    @Autowired
    private OtcOfflineCoinDao offlineCoinDao;

    @Autowired
    private OfflineCoinVolumeDao offlineCoinVolumeDao;

    @Autowired
    private OtcUserBankDao otcUserBankDao;

    @Autowired
    private OtcOfflineOrderPayDao otcOfflineOrderPayDao;

    @Autowired
    private OfflineCoinVolumeLogService offlineCoinVolumeLogService;

    @Autowired
    private OtcOfflineOrderLogService otcOfflineOrderLogService;

    @Autowired
    private OfflineCoinVolumeService offlineCoinVolumeService;

    @Autowired
    private OfflineCancelLogDao offlineCancelLogDao;

    private static final String ALLOW_ZERO = "1";

    private static final String NO_ALLOW_ZERO = "0";

    private static final String NULL_ALLOW = "0";

    private static final String HIDE_CHAR_DEFAULT = "**";// 银行卡，微信，支付宝，真名隐藏

    @Override
    public ResponsePage<OtcOfflineOrder> findMyAdvertList(OtcOfflineOrderVO otcOfflineOrderVO) {
        ResponsePage<OtcOfflineOrder> responsePage = new ResponsePage<>();
        Page<OtcOfflineOrder> page = PageHelper.startPage(otcOfflineOrderVO.getCurrentPage(), otcOfflineOrderVO.getShowCount());
        List<OtcOfflineOrder> offlineOrders = otcOfflineOrderDao.findByUserId(otcOfflineOrderVO.getUserId(), otcOfflineOrderVO.getLoginSource(), otcOfflineOrderVO.getExType());
        responsePage.setCount(page.getTotal());
        hideSensitiveInfo(offlineOrders); // 隐藏敏感信息
        responsePage.setList(offlineOrders);
        return responsePage;
    }

    /**
     * 隐藏敏感信息（微信，银行卡，真名。。。），避免返回入客户端
     */
    private void hideSensitiveInfo(List<OtcOfflineOrder> data) {
        if (CollectionUtils.isNotEmpty(data)) {
            data.forEach(order -> {
                if (StringUtils.isNotBlank(order.getUserId())) {
                    order.setUserId(HIDE_CHAR_DEFAULT);
                }
                if (StringUtils.isNotBlank(order.getTag())) {
                    order.setTag(HIDE_CHAR_DEFAULT);
                }
                if (StringUtils.isNotBlank(order.getMobile())) {
                    order.setMobile(HIDE_CHAR_DEFAULT);
                }
                if (StringUtils.isNotBlank(order.getRealName())) {
                    order.setRealName(order.getRealName().substring(0, 1) + HIDE_CHAR_DEFAULT);
                }
            });
        }
    }

    @Override
    public ResponsePage<OtcOfflineOrder> findPage(OtcOfflineOrderVO otcOfflineOrderVO) {
        ResponsePage<OtcOfflineOrder> responsePage = new ResponsePage<>();
        Page<OtcOfflineOrder> page = PageHelper.startPage(otcOfflineOrderVO.getCurrentPage(), otcOfflineOrderVO.getShowCount());
        List<OtcOfflineOrder> data = null;
        if (String.valueOf(TradeEnum.BUY.ordinal()).equals(otcOfflineOrderVO.getExType())) {
            //卖出 价格降序
            data = otcOfflineOrderDao.findAdvertListByCoinIdPriceDesc(otcOfflineOrderVO);
        } else if (String.valueOf(TradeEnum.SELL.ordinal()).equals(otcOfflineOrderVO.getExType())) {
            //买入  价格升序
            data = otcOfflineOrderDao.findAdvertListByCoinIdPriceAsc(otcOfflineOrderVO);
        } else {
            throw new PlatException(Constants.PARAM_ERROR, "买卖方向输入错误！");
        }
        responsePage.setCount(page.getTotal());
        hideSensitiveInfo(data); // 隐藏敏感信息
        responsePage.setList(data);
        return responsePage;
    }

    @Override
    @Transactional
    public void advertCancel(OtcOfflineOrderVO otcOfflineOrderVO) {
        OtcOfflineOrder otcOfflineOrder = otcOfflineOrderDao.findById(otcOfflineOrderVO.getOrderId());
        doAdvertCancelCheck(otcOfflineOrder, otcOfflineOrderVO);// 取消广告的校验
        String batchNo = SnowFlake.createSnowFlake().nextIdString();
        otcOfflineOrderLogService.saveLog(otcOfflineOrder, batchNo); // 广告流水
        otcOfflineOrder.setStatus(OfflineOrderStatusEnum.CANCEL.getCode());
        otcOfflineOrder.setCancelDate(LocalDateTime.now());
        long count = otcOfflineOrderDao.cancelOtcOrder(otcOfflineOrder);
        if (count != 1) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        // 发布OTC广告触发C2C资产变更

        offlineCoinVolumeService.otcOrderSaveUpdateCoinVolume(otcOfflineOrder, batchNo);
        otcOfflineOrderLogService.saveLog(otcOfflineOrder.getId(), batchNo); // 广告流水

        //写入日志
        OfflineCancelLog offlineCancelLog = new OfflineCancelLog();
        String id = SnowFlake.createSnowFlake().nextIdString();
        offlineCancelLog.setId(id);
        offlineCancelLog.setDate(LocalDate.now());
        offlineCancelLog.setType(OfflineCancelTypeEnum.ADVERT.getCode());
        offlineCancelLog.setUserId(otcOfflineOrderVO.getUserId());
        offlineCancelLogDao.insert(offlineCancelLog);
    }

    /**
     * 取消广告校验
     *
     * @param otcOfflineOrder
     * @param otcOfflineOrderVO
     */
    private void doAdvertCancelCheck(OtcOfflineOrder otcOfflineOrder, OtcOfflineOrderVO otcOfflineOrderVO) {
        if (Objects.isNull(otcOfflineOrder) || !otcOfflineOrder.getUserId().equals(otcOfflineOrderVO.getUserId())) {
            throw new PlatException(Constants.PARAM_ERROR, "输入订单号错误");
        }
        if (OfflineOrderStatusEnum.CANCEL.getCode().equals(otcOfflineOrder.getStatus())) {
            throw new PlatException(Constants.PARAM_ERROR, "该广告不能重复撤销");
        }
        if (OfflineOrderStatusEnum.COMPLATE.getCode().equals(otcOfflineOrder.getStatus())) {
            throw new PlatException(Constants.PARAM_ERROR, "全部成交广告不能取消");
        }
        if (!OfflineOrderStatusEnum.NOMAL.getCode().equals(otcOfflineOrder.getStatus())) {
            throw new PlatException(Constants.PARAM_ERROR, "广告状态有误");
        }
        loginSourceCheck(otcOfflineOrderVO.getLoginSource(), otcOfflineOrder.getPublishSource()); // 校验登录渠道和发布渠道是否一致
        // 如果 锁定数量 + 成交数量 = 发布数量，则不允许撤销广告
        Optional<BigDecimal> lockVol = Optional.ofNullable(otcOfflineOrder.getLockVolume());
        Optional<BigDecimal> successVol = Optional.ofNullable(otcOfflineOrder.getSuccessVolume());
        BigDecimal sumVol = lockVol.orElse(BigDecimal.ZERO).add(successVol.orElse(BigDecimal.ZERO));
        if (otcOfflineOrder.getVolume().compareTo(sumVol) != 1) {
            throw new PlatException(Constants.PARAM_ERROR, "该广告没有可撤销的数量");
        }
    }


    @Override
    @Transactional
    public OtcOfflineOrder save(OtcOfflineOrder offlineOrder, String supportBank) {

        offlineOrder.setId(SnowFlake.createSnowFlake().nextIdString());
        offlineOrder.setStatus(OfflineOrderStatusEnum.NOMAL.getCode());
        offlineOrder.setSuccessVolume(BigDecimal.ZERO);
        offlineOrder.setLockVolume(BigDecimal.ZERO);
        BigDecimal totalPrice = offlineOrder.getVolume().multiply(offlineOrder.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
        offlineOrder.setTotalPrice(totalPrice);
        // 校验设置数据合法性
        doSaveCheckDate(offlineOrder);
        // 卖广告设置手续费
//        if (String.valueOf(TradeEnum.SELL.ordinal()).equals(offlineOrder.getExType())) {
        BigDecimal feeVolume = calcFee(offlineOrder);
        offlineOrder.setFeeVolume(feeVolume);// 设置手续费
//        }
        String supportPay = saveOrderPay(offlineOrder, supportBank); // 保存广告支付信息
        offlineOrder.setSupportPay(supportPay);
        otcOfflineOrderDao.insert(offlineOrder); // 保存广告入库

        // 发布OTC广告触发C2C资产变更
        String batchNo = SnowFlake.createSnowFlake().nextIdString();
        offlineCoinVolumeService.otcOrderSaveUpdateCoinVolume(offlineOrder, batchNo);
        otcOfflineOrderLogService.saveLog(offlineOrder.getId(), batchNo);// 广告流水
        return offlineOrder;
    }

    /**
     * 保持广告支付方式
     *
     * @param offlineOrder
     * @param supportBank
     * @return
     */
    private String saveOrderPay(OtcOfflineOrder offlineOrder, String supportBank) {
        if (StringUtils.isBlank(supportBank)) {
            throw new PlatException(Constants.OFFLINE_VOLUME_ERROR, "支付方式不能为空");
        }
        String[] bankIds = supportBank.split(",");
        if (bankIds.length < 1) {
            throw new PlatException(Constants.OFFLINE_VOLUME_ERROR, "支付方式不能为空");
        }

        List<OtcUserBank> banks = otcUserBankDao.findByUserIdAndCurrencyCode(offlineOrder.getUserId(), offlineOrder.getSupportCurrencyCode()); // 查询我的银行卡列表
        Map<String, OtcUserBank> bankMap = new HashMap<>();
        banks.forEach(bank -> {
            bankMap.put(bank.getId(), bank);
        });

        StringBuilder supportPay = new StringBuilder();
        OtcUserBank bank = null;
        List<OtcOfflineOrderPay> pays = new ArrayList<>();
        for (String bankId : bankIds) {
            bank = bankMap.get(bankId);
            if (Objects.isNull(bank) || !bank.getUserId().equals(offlineOrder.getUserId()) || !bank.getSupportCurrencyCode().contains(offlineOrder.getSupportCurrencyCode())) {
                throw new PlatException(Constants.OFFLINE_VOLUME_ERROR, "支付方式设置有误，请确认支付方式持有人或币种是否匹配");
            }
            if (!supportPay.toString().contains(bank.getType() + ",")) {
                supportPay.append(bank.getType()).append(",");
            }
            OtcOfflineOrderPay pay = new OtcOfflineOrderPay();
            BeanUtils.copyProperties(bank, pay);
            pay.setId(SnowFlake.createSnowFlake().nextIdString());
            pay.setOrderId(offlineOrder.getId());
            pay.setSupportCurrencyCode(offlineOrder.getSupportCurrencyCode());
            pay.setCreateDate(LocalDateTime.now());
            pay.setUpdateDate(LocalDateTime.now());
            pays.add(pay);
        }
        if (CollectionUtils.isNotEmpty(pays)) {
            otcOfflineOrderPayDao.insertBatch(pays);
        } else {
            throw new PlatException(Constants.PARAM_ERROR, "请绑定银行卡");
        }
        return supportPay.toString();
    }

    /**
     * 计算手续费
     *
     * @param offlineOrder
     * @return
     */
    private BigDecimal calcFee(OtcOfflineOrder offlineOrder) {

        OtcOfflineCoin offlineCoin = offlineCoinDao.findByCoinId(offlineOrder.getCoinId());
        if (Objects.isNull(offlineCoin)) {
            throw new PlatException(Constants.OFFLINE_COIN_NOT_EXSIT_ERROR, "OTC币种不存在");
        }
        String feeType = offlineCoin.getFeeType();
        BigDecimal feeVolume = BigDecimal.ZERO; // 手续费

        if (feeType.equals(OfflineFeeTypeEnum.STATIC.getCode())) {
            String sellFeeStep = offlineCoin.getSellFeeStep();
            feeVolume = FeeUtils.getFeeByStep(offlineOrder.getVolume(), sellFeeStep);
        } else if (feeType.equals(OfflineFeeTypeEnum.SCALE.getCode())) {
            if (offlineOrder.getExType().equals(String.valueOf(TradeEnum.BUY.ordinal()))) {
                feeVolume = offlineOrder.getVolume().multiply(offlineCoin.getBuyFee());
            } else {
                feeVolume = offlineOrder.getVolume().multiply(offlineCoin.getSellFee());
            }
        }
        return feeVolume;
    }

    /**
     * 发布广告校验
     *
     * @param offlineOrder
     */
    private void doSaveCheckDate(OtcOfflineOrder offlineOrder) {
        greaterZero(offlineOrder.getPrice(), NO_ALLOW_ZERO, "价格设置错误");
        greaterZero(offlineOrder.getVolume(), NO_ALLOW_ZERO, "广告数量设置错误");
        greaterZero(offlineOrder.getMinVolume(), NULL_ALLOW, "最小数量设置错误");
        greaterZero(offlineOrder.getMaxVolume(), NULL_ALLOW, "最大数量设置错误");
        if (AppSourceTypeEnum.OTC.getCode().equals(offlineOrder.getPublishSource())) {
            if (StringUtils.isBlank(offlineOrder.getTag()) || !offlineOrder.getTag().contains(UserTagEnum.OTC_ADVERT.getCode())) {
                throw new PlatException(Constants.OFFLINE_VOLUME_ERROR, "只能广告商才能发布广告");
            }
        }
        if (!offlineOrder.getExType().equals(String.valueOf(TradeEnum.BUY.ordinal())) && !offlineOrder.getExType().equals(String.valueOf(TradeEnum.SELL.ordinal()))) {
            throw new PlatException(Constants.OFFLINE_VOLUME_ERROR, "广告买卖方向设置错误");
        }
    }

    /**
     * @param equalZero 1允许，0不允许
     * @param target
     * @param errorMsg
     */
    private void greaterZero(BigDecimal target, String equalZero, String errorMsg) {
        if (NULL_ALLOW.equals(equalZero)) {
            if (Objects.nonNull(target) && target.compareTo(BigDecimal.ZERO) == -1) {
                throw new PlatException(Constants.OFFLINE_VOLUME_ERROR, errorMsg);
            }
            return;
        }
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
    public void updateOtcOrderVolume(OtcOfflineOrder otcOfflineOrder, String batchNo) {
        greaterZero(otcOfflineOrder.getVolume(), NO_ALLOW_ZERO, "广告数量设置错误");
        greaterZero(otcOfflineOrder.getLockVolume(), ALLOW_ZERO, "广告冻结设置错误");
        greaterZero(otcOfflineOrder.getSuccessVolume(), ALLOW_ZERO, "广告成交数量设置错误");
        long count = otcOfflineOrderDao.updateOtcOrder(otcOfflineOrder); // 修改广告volume, lock_volume, success_volume
        if (count != 1) {
            throw new PlatException(Constants.PARAM_ERROR, "参数错误");
        }
        otcOfflineOrderLogService.saveLog(otcOfflineOrder.getId(), batchNo);// 广告流水
    }

    /**
     * 订单变化触发广告状态变更
     *
     * @param offlineOrderDetail 订单（买/卖单对象）
     * @param otcOfflineOrder    广告
     */
    @Transactional
    public void otcDetailSaveUpdateOrder(OtcOfflineOrderDetail offlineOrderDetail, OtcOfflineOrder otcOfflineOrder, String batchNo) {
        BigDecimal lockVolume = null;
        BigDecimal successVolume = null;
        otcOfflineOrderLogService.saveLog(otcOfflineOrder, batchNo); // 广告流水
        if (offlineOrderDetail.getStatus().equals(OfflineOrderDetailStatusEnum.NOMAL.getCode())) { // 下单
            lockVolume = otcOfflineOrder.getLockVolume().add(offlineOrderDetail.getVolume());
            successVolume = otcOfflineOrder.getSuccessVolume();
        }
        if (offlineOrderDetail.getStatus().equals(OfflineOrderDetailStatusEnum.CANCEL.getCode())) { // 取消订单
            lockVolume = otcOfflineOrder.getLockVolume().subtract(offlineOrderDetail.getVolume());
            successVolume = otcOfflineOrder.getSuccessVolume();
        }
        if (offlineOrderDetail.getStatus().equals(OfflineOrderDetailStatusEnum.CONFIRM_IN.getCode())) { // 订单确认收款
            lockVolume = otcOfflineOrder.getLockVolume().subtract(offlineOrderDetail.getVolume());
            successVolume = otcOfflineOrder.getSuccessVolume().add(offlineOrderDetail.getVolume());
        }
        if (Objects.isNull(lockVolume) || Objects.isNull(successVolume)) {
            throw new PlatException(Constants.PARAM_ERROR, "参数错误");
        }
        // 如果广告初始状态，设置是否全部成交状态
        if (otcOfflineOrder.getStatus().equals(OfflineOrderStatusEnum.NOMAL.getCode())) { // 广告状态为发布，如果成交数量和发布数量相等，且锁定数量等于0则，更改广告状态为已完成
            if (otcOfflineOrder.getVolume().compareTo(successVolume) == 0 && lockVolume.compareTo(BigDecimal.ZERO) == 0) {
                otcOfflineOrder.setStatus(OfflineOrderStatusEnum.COMPLATE.getCode());
            }
            BigDecimal lastVolume = otcOfflineOrder.getVolume().subtract(lockVolume).subtract(successVolume);
            if (lastVolume.compareTo(BigDecimal.ZERO) == -1) {
                throw new PlatException(Constants.PARAM_ERROR, "广告和订单参数错误");
            }
        }
        otcOfflineOrder.setLockVolume(lockVolume);
        otcOfflineOrder.setSuccessVolume(successVolume);
        updateOtcOrderVolume(otcOfflineOrder, batchNo); // 保存广告数量
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
}
