package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.constant.SercurityConstant;
import com.biao.entity.*;
import com.biao.enums.*;
import com.biao.exception.PlatException;
import com.biao.mapper.*;
import com.biao.pojo.ResponsePage;
import com.biao.service.OfflineCoinVolumeLogService;
import com.biao.service.OfflineOrderDetailService;
import com.biao.service.push.OfflineOrderPushService;
import com.biao.util.DateTimeUtils;
import com.biao.util.FeeUtils;
import com.biao.util.RandomUtils;
import com.biao.util.SnowFlake;
import com.biao.vo.OfflineConfirmVO;
import com.biao.vo.OrderDetailVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OfflineOrderDetailServiceImpl implements OfflineOrderDetailService {

    @Autowired
    private OfflineOrderDetailDao offlineOrderDetailDao;
    @Autowired
    private OfflineOrderDao offlineOrderDao;
    @Autowired
    private OfflineCoinVolumeDao offlineCoinVolumeDao;
    @Autowired
    private OfflineCoinVolumeLogDao offlineCoinVolumeLogDao;

    @Autowired
    private OfflineAppealDao offlineAppealDao;
    @Autowired
    private PlatUserDao platUserDao;
    @Autowired
    private OfflineCoinDao offlineCoinDao;

    @Value("${remainingTime:30}")
    private Integer remainingTime;

    @Autowired
    private OfflineCoinVolumeLogService offlineCoinVolumeLogService;

    @Autowired
    private OfflineOrderPushService offlineOrderPushService;

    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;

    private static final Logger LOGGER = LoggerFactory.getLogger(OfflineOrderDetailServiceImpl.class);

    @Override
    @Transactional
    public void updateById(OfflineOrderDetail offlineOrderDetail) {
        long count = offlineOrderDetailDao.updateById(offlineOrderDetail);
        if (count == 0) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
    }

    @Override
    @Transactional
    public String sell(OfflineOrderDetail offlineOrderDetail, String sellerUserId, String sellerUserMobile, String tag, String alipayNo, String alipayQrcodeId, String wechatNo, String wechatQrcodeId, UserBank userBank) {
        //查找订单
        OfflineOrder offlineOrder = offlineOrderDao.findById(offlineOrderDetail.getOrderId());
        if (!offlineOrder.getStatus().toString().equals(OfflineOrderStatusEnum.NOMAL.getCode()) &&
                !offlineOrder.getStatus().toString().equals(OfflineOrderStatusEnum.PART_COMPLATE.getCode())) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,广告已下架");
        }
        if (offlineOrder.getExType() != TradeEnum.BUY.ordinal()) {
            throw new PlatException(Constants.OPERRATION_ERROR, "该广告为买入广告,只能卖出操作");
        }
        //自己不能卖给自己
        if (offlineOrder.getUserId().equals(sellerUserId)) {
            throw new PlatException(Constants.BUY_OWN_ERROR, "自己的广告,不能卖出");
        }
        //判断广告数量是否超出    判断卖出的人 数量必须小于等于订单的数量
        BigDecimal exTotalVolume = offlineOrderDetail.getVolume().add(offlineOrder.getLockVolume()).add(offlineOrder.getSuccessVolume());
        if (offlineOrder.getVolume().compareTo(exTotalVolume) == -1) {
            throw new PlatException(Constants.OFFLINE_VOLUME_ERROR, "卖单数量不足,请修改卖出数量");
        }

        //锁定卖出（当前登录用户）的资产 volume -> lockVolume
        OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(sellerUserId, offlineOrder.getCoinId());
        if (offlineCoinVolume == null) {
            throw new PlatException(Constants.OPERRATION_ERROR, "用户资产不足");
        }

        //最小交易量
        if (offlineOrderDetail.getVolume().compareTo(offlineOrder.getMinExVolume()) == -1) {
            throw new PlatException(Constants.OFFLINE_SELL_VOLUME_ERROR, "卖出数量至少" + offlineOrder.getMinExVolume());
        }
        //判断该币种是否收取手续费
        OfflineCoin offlineCoin = offlineCoinDao.findByCoinId(offlineOrder.getCoinId());
        if (Objects.isNull(offlineCoin)) {
            throw new PlatException(Constants.OFFLINE_COIN_NOT_EXSIT_ERROR, "C2C币种不存在");
        }
        String feeType = offlineCoin.getFeeType();
        BigDecimal feeVolume = BigDecimal.ZERO;
        if (feeType.equals(OfflineFeeTypeEnum.NONE.getCode())) {//不收取

        } else {
            //判断用户是否有保证金
            BigDecimal sellerBailVolume = offlineCoinVolume.getBailVolume();
            if (null != tag && (tag.contains(UserTagEnum.FINANCE.getCode()) || tag.contains(UserTagEnum.OTC_AGENT.getCode()))) {//财务或者银商

            } else {
                if (sellerBailVolume.compareTo(BigDecimal.ZERO) <= 0) {//无保证金
                    throw new PlatException(Constants.OFFLINE_BAIL_VOLUME_NOT_ENOUGH_ERROR, "C2C手续费预备金不足");
                } else {
                    //最低收取的保证金  因为卖出时候立马会冻结资产
                    //统计有多少个未完成的订单  未完成的卖出订单 + 本次的手续费 <= 保证金
                    String[] statusArray = {OfflineOrderDetailStatusEnum.NOMAL.getCode(), OfflineOrderDetailStatusEnum.CONFIRM_PAY.getCode()};
                    String statusIn = StringUtils.join(statusArray, ",");
                    Double sumFee = offlineOrderDetailDao.sumByStatusAndUserIdAndCoinId(statusIn, sellerUserId, offlineOrder.getCoinId());
                    if (feeType.equals(OfflineFeeTypeEnum.STATIC.getCode())) {
                        String sellFeeStep = offlineCoin.getSellFeeStep();
                        feeVolume = FeeUtils.getFeeByStep(offlineOrderDetail.getVolume(), sellFeeStep);

                    } else if (feeType.equals(OfflineFeeTypeEnum.SCALE.getCode())) {
                        feeVolume = offlineOrderDetail.getVolume().multiply(offlineCoin.getSellFee());
                    }
                    BigDecimal totalFee = BigDecimal.valueOf(sumFee == null ? 0 : sumFee).add(feeVolume);
                    if (sellerBailVolume.compareTo(totalFee) < 0) {
                        throw new PlatException(Constants.OFFLINE_BAIL_VOLUME_NOT_ENOUGH_ERROR, "C2C手续费预备金不足");
                    }
                }
            }
        }
        BigDecimal volume = offlineCoinVolume.getVolume().subtract(offlineOrderDetail.getVolume());
        if (volume.compareTo(BigDecimal.ZERO) == -1) {
            throw new PlatException(Constants.OPERRATION_ERROR, "用户资产不足");
        }
        BigDecimal lockVolume = offlineCoinVolume.getLockVolume().add(offlineOrderDetail.getVolume());
        if (lockVolume.compareTo(BigDecimal.ZERO) == -1) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,资产出现异常");
        }
        Timestamp updateDate = Timestamp.valueOf(offlineCoinVolume.getUpdateDate());
        long count = offlineCoinVolumeDao.updateVolumeAndLockVolume(offlineCoinVolume.getUserId(), offlineCoinVolume.getCoinId(), volume, lockVolume, updateDate, offlineCoinVolume.getVersion());
        if (count == 0) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        offlineCoinVolumeLogService.saveLog(offlineCoinVolume.getUserId(), offlineCoinVolume.getCoinId(), UUID.randomUUID().toString());
        //锁定广告lockVolume
        BigDecimal exAddVolume = offlineOrder.getLockVolume().add(offlineOrderDetail.getVolume());
        if (exAddVolume.compareTo(BigDecimal.ZERO) == -1) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,资产出现异常");
        }
        offlineOrderDao.updateLockVolumeById(offlineOrder.getId(), exAddVolume, Timestamp.valueOf(offlineOrder.getUpdateDate()), offlineOrder.getVersion());

        String id = SnowFlake.createSnowFlake().nextIdString();
        String subOrderId = SnowFlake.createSnowFlake().nextIdString();
        String randomNum = RandomUtils.createCode();
        offlineOrderDetail.setId(id);
        offlineOrderDetail.setRadomNum(randomNum);
        offlineOrderDetail.setUserId(sellerUserId);
        offlineOrderDetail.setAskUserId(offlineOrder.getUserId());
        offlineOrderDetail.setAskUserName(offlineOrder.getRealName());
        offlineOrderDetail.setUserMobile(sellerUserMobile);
        offlineOrderDetail.setAskUserMobile(offlineOrder.getMobile());
        offlineOrderDetail.setCoinId(offlineOrder.getCoinId());
        offlineOrderDetail.setSymbol(offlineOrder.getSymbol());
        offlineOrderDetail.setStatus(0);
        offlineOrderDetail.setPrice(offlineOrder.getPrice());
        BigDecimal totalPrice = offlineOrderDetail.getVolume().multiply(offlineOrder.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
        offlineOrderDetail.setTotalPrice(totalPrice);
        offlineOrderDetail.setRemarks("sell");
        offlineOrderDetail.setExType(TradeEnum.SELL.ordinal());
        offlineOrderDetail.setSubOrderId(subOrderId);
        offlineOrderDetail.setCreateDate(LocalDateTime.now());
        offlineOrderDetail.setUpdateDate(LocalDateTime.now());
        offlineOrderDetail.setFeeVolume(feeVolume);
        offlineOrderDetail.setSellBankNo(userBank.getCardNo());
        offlineOrderDetail.setSellBankName(userBank.getBankName());
        offlineOrderDetail.setSellBankBranchName(userBank.getBranchBankName());
        offlineOrderDetail.setAlipayNo(alipayNo);
        offlineOrderDetail.setAlipayQrcodeId(alipayQrcodeId);
        offlineOrderDetail.setWechatNo(wechatNo);
        offlineOrderDetail.setWechatQrcodeId(wechatQrcodeId);
        offlineOrderDetailDao.insert(offlineOrderDetail);
        //以上是针对买家记录  还要存一条针对卖家的记录
        String userMobile = offlineOrderDetail.getAskUserMobile();
        String askUserMobile = offlineOrderDetail.getUserMobile();
        OfflineOrderDetail buyOfflineOrderDetail = new OfflineOrderDetail();
        BeanUtils.copyProperties(offlineOrderDetail, buyOfflineOrderDetail);
        String otherId = SnowFlake.createSnowFlake().nextIdString();
        buyOfflineOrderDetail.setId(otherId);
        buyOfflineOrderDetail.setRemarks("buy");
        buyOfflineOrderDetail.setExType(TradeEnum.BUY.ordinal());
        buyOfflineOrderDetail.setUserId(offlineOrder.getUserId());
        buyOfflineOrderDetail.setUserName(offlineOrderDetail.getAskUserName());
        buyOfflineOrderDetail.setAskUserId(offlineOrderDetail.getUserId());
        buyOfflineOrderDetail.setAskUserName(offlineOrderDetail.getUserName());
        buyOfflineOrderDetail.setSubOrderId(subOrderId);
        buyOfflineOrderDetail.setCreateDate(LocalDateTime.now());
        buyOfflineOrderDetail.setUserMobile(userMobile);
        buyOfflineOrderDetail.setAskUserMobile(askUserMobile);
        buyOfflineOrderDetail.setFeeVolume(BigDecimal.ZERO);
        offlineOrderDetailDao.insert(buyOfflineOrderDetail);
        return id;
    }

    @Override
    @Transactional
    public void updateStatusBySubOrderId(OfflineConfirmVO offlineConfirmVO) {
        //判断订单状态只能是
        checkOrderDetailAppeal(offlineConfirmVO.getSubOrderId()); // 判断该单是否有申诉单，否不允许操作
        try {
            //long count = offlineOrderDetailDao.updateStatusBySubOrderIdAndStatus(offlineConfirmVO.getStatus(), offlineConfirmVO.getSubOrderId(), OfflineOrderDetailStatusEnum.NOMAL.getCode());
            LocalDateTime confirmPaymentDate = LocalDateTime.now();//确认付款时间
            long count = offlineOrderDetailDao.updateStatusAndConfirmPaymentDateBySubOrderIdAndStatus(offlineConfirmVO.getStatus(), confirmPaymentDate, offlineConfirmVO.getSubOrderId(), OfflineOrderDetailStatusEnum.NOMAL.getCode());
            if (count != 2) {
                throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
            }
        } catch (Exception e) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作非法");
        }
    }

    @Override
    @Transactional
    public void updateStatusBySubOrderIdAndUnLock(OfflineConfirmVO offlineConfirmVO, String sellTag) {
        //如果是发布广告的去确认收钱
        this.checkOrderDetailAndGetOrderId(offlineConfirmVO);
        //查询发布广告
        OfflineOrder offlineOrder = offlineOrderDao.findById(offlineConfirmVO.getOrderId());
        if (null == offlineOrder) {
            throw new PlatException(Constants.PARAM_ERROR, "参数错误");
        }
        checkOrderDetailAppeal(offlineConfirmVO.getSubOrderId()); // 判断该单是否有申诉单，否不允许操作
        //如果是发布广告的去确认收钱 则发布的是卖单
        //判断广告是买单还是卖单
        if (offlineOrder.getExType() == TradeEnum.SELL.ordinal()) {
            if (offlineOrder.getUserId().equals(offlineConfirmVO.getUserId())) {
                doSell(offlineConfirmVO, offlineOrder);
            } else {
                throw new PlatException(Constants.PARAM_ERROR, "参数错误");
            }
        } else if (offlineOrder.getExType() == TradeEnum.BUY.ordinal()) {//广告是买入 卖出的订单在确认收款时 从保证金账户扣除手续费
            if (offlineOrder.getUserId().equals(offlineConfirmVO.getUserId())) {
                throw new PlatException(Constants.PARAM_ERROR, "参数错误");
            } else {
                doBuy(offlineConfirmVO, offlineOrder, sellTag);
            }
        }
    }


    /**
     * 处理广告是买单情况
     * 涉及到手续费
     *
     * @param offlineConfirmVO
     * @param offlineOrder
     */
    private void doBuy(OfflineConfirmVO offlineConfirmVO, OfflineOrder offlineOrder, String tag) {
        //卖家确认收款
        //扣卖家款项 确认卖家资金是否足额
        String sellUserId = offlineConfirmVO.getUserId();
        String buyUserId = offlineOrder.getUserId();
        String subOrderId = offlineConfirmVO.getSubOrderId();
        String coinId = offlineOrder.getCoinId();

        //查询该笔交易涉及的订单
        OfflineOrderDetail offlineOrderDetail = offlineOrderDetailDao.findUserIdAndSubOrderIdAndCoinId(sellUserId, subOrderId, coinId);
        if (!offlineOrderDetail.getStatus().toString().equals(OfflineOrderDetailStatusEnum.CONFIRM_PAY.getCode())) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作非法");
        }
        BigDecimal exVolume = offlineOrderDetail.getVolume();
        //查询买家资产
        OfflineCoinVolume buyCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(buyUserId, coinId);
        if (null == buyCoinVolume) {
            buyCoinVolume = new OfflineCoinVolume();
            String id = SnowFlake.createSnowFlake().nextIdString();
            buyCoinVolume.setId(id);
            buyCoinVolume.setCoinId(coinId);
            buyCoinVolume.setCoinSymbol(offlineOrder.getSymbol());
            buyCoinVolume.setUserId(buyUserId);
            buyCoinVolume.setVolume(exVolume);
            buyCoinVolume.setAdvertVolume(BigDecimal.ZERO);
            buyCoinVolume.setLockVolume(BigDecimal.ZERO);
            buyCoinVolume.setBailVolume(BigDecimal.ZERO);
            buyCoinVolume.setCreateDate(LocalDateTime.now());
            buyCoinVolume.setUpdateDate(LocalDateTime.now());
            buyCoinVolume.setVersion(0L);
            offlineCoinVolumeDao.insert(buyCoinVolume);
        } else {
            Timestamp updateDate = Timestamp.valueOf(buyCoinVolume.getUpdateDate());
            BigDecimal volume = buyCoinVolume.getVolume().add(exVolume);
            if (volume.compareTo(BigDecimal.ZERO) == -1) {
                throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,资产出现异常");
            }
            //将买家资产增加
            long count = offlineCoinVolumeDao.updateVolume(buyUserId, coinId, volume, updateDate, buyCoinVolume.getVersion());
            if (count == 0) {
                throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
            }
        }
        //将当前操作用户冻结资产相应减去
        OfflineCoinVolume sellCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(sellUserId, offlineOrder.getCoinId());
        BigDecimal lockVolume = sellCoinVolume.getLockVolume().subtract(exVolume);
        if (lockVolume.compareTo(BigDecimal.ZERO) == -1) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作非法");
        }
        Timestamp updateDate = Timestamp.valueOf(sellCoinVolume.getUpdateDate());
        long count = 0l;
        if (null != tag && (tag.contains(UserTagEnum.FINANCE.getCode()) || tag.contains(UserTagEnum.OTC_AGENT.getCode()))) {//不需要扣除手续费
            count = offlineCoinVolumeDao.updateLockVolume(sellUserId, coinId, lockVolume, updateDate, sellCoinVolume.getVersion());
            if (count == 0) {
                throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
            }
        } else {//从保证金账户内扣除手续费 手续费存到卖家订单的feeVolume 字段
            BigDecimal bailVolume = sellCoinVolume.getBailVolume().subtract(offlineOrderDetail.getFeeVolume());
            count = offlineCoinVolumeDao.updateLockVolumeAndBailVolume(sellUserId, coinId, lockVolume, bailVolume, updateDate, sellCoinVolume.getVersion());
            if (count == 0) {
                throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
            }
        }


        //修改广告successVolume
        BigDecimal successVolume = offlineOrder.getSuccessVolume().add(exVolume);
        Integer orderStatus = Integer.parseInt(OfflineOrderStatusEnum.PART_COMPLATE.getCode());
        if (offlineOrder.getStatus().toString().equals(OfflineOrderStatusEnum.CANCEL.getCode())) {
            if (successVolume.compareTo(offlineOrder.getVolume()) == -1) {
                orderStatus = Integer.parseInt(OfflineOrderStatusEnum.PART_CANCEL.getCode());
            }
        }
        updateDate = Timestamp.valueOf(offlineOrder.getUpdateDate());
        if (successVolume.compareTo(offlineOrder.getVolume()) == 0) {
            orderStatus = Integer.parseInt(OfflineOrderStatusEnum.COMPLATE.getCode());
        } else if (successVolume.compareTo(offlineOrder.getVolume()) == 1) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作非法");
        }
        //剩余lockVolume
        BigDecimal residueVolume = offlineOrder.getLockVolume().subtract(offlineOrderDetail.getVolume());
        if (residueVolume.compareTo(BigDecimal.ZERO) == -1) {
            throw new PlatException(Constants.PARAM_ERROR, "数量错误,无法取消");
        }
        count = offlineOrderDao.updateSuccessVolumeAndLockVolumeAndStatusById(offlineOrder.getId(), successVolume, residueVolume, orderStatus, updateDate, offlineOrder.getVersion());
        if (count == 0) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        //修改订单详情状态
        Integer status = Integer.parseInt(OfflineOrderDetailStatusEnum.CONFIRM_IN.getCode());
        Integer preStatus = Integer.parseInt(OfflineOrderDetailStatusEnum.CONFIRM_PAY.getCode());//判断之前状态
        LocalDateTime confirmReceiptDate = LocalDateTime.now();
        count = offlineOrderDetailDao.updateStatusAndConfirmRecepitDateBySubOrderId(preStatus, status, confirmReceiptDate, subOrderId, offlineConfirmVO.getUpdateBy());
        if (count != 2) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        //扣除卖出方保证金 如果是财务或者银商则无需扣除手续费
    }

    /**
     * 处理广告是卖单情况
     *
     * @param offlineConfirmVO
     * @param offlineOrder
     */
    private void doSell(OfflineConfirmVO offlineConfirmVO, OfflineOrder offlineOrder) {
        //处理卖家资产
        //查询卖家资产

        OfflineCoinVolume sellCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(offlineOrder.getUserId(), offlineOrder.getCoinId());

        //查询该笔交易涉及的订单
        OfflineOrderDetail offlineOrderDetail = offlineOrderDetailDao.findUserIdAndSubOrderIdAndCoinId(offlineConfirmVO.getUserId(), offlineConfirmVO.getSubOrderId(), offlineOrder.getCoinId());
        if (offlineOrderDetail.getStatus().toString().equals(OfflineOrderDetailStatusEnum.CONFIRM_PAY.getCode())) {

        } else {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作非法");
        }
        BigDecimal exVolume = offlineOrderDetail.getVolume();
        Timestamp updateDate = Timestamp.valueOf(sellCoinVolume.getUpdateDate());
        BigDecimal lockVolume = sellCoinVolume.getLockVolume().subtract(exVolume);
        if (lockVolume.compareTo(BigDecimal.ZERO) == -1) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作非法");
        }
        //更新卖家资产
        long count = offlineCoinVolumeDao.updateLockVolume(offlineConfirmVO.getUserId(), offlineOrder.getCoinId(), lockVolume, updateDate, sellCoinVolume.getVersion());
        if (count == 0) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        //将我的交易锁定的资产减掉  给买家的volume可用资产增加
        //查询买家资产
        OfflineCoinVolume buyCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(offlineOrderDetail.getAskUserId(), offlineOrder.getCoinId());
        //买家是否有该资产 如果没有则插入 如果有则更新
        if (null == buyCoinVolume) {
            buyCoinVolume = new OfflineCoinVolume();
            String id = SnowFlake.createSnowFlake().nextIdString();
            buyCoinVolume.setId(id);
            buyCoinVolume.setCoinId(offlineOrder.getCoinId());
            buyCoinVolume.setCoinSymbol(offlineOrder.getSymbol());
            buyCoinVolume.setUserId(offlineOrderDetail.getAskUserId());
            buyCoinVolume.setVolume(exVolume);
            buyCoinVolume.setAdvertVolume(BigDecimal.ZERO);
            buyCoinVolume.setLockVolume(BigDecimal.ZERO);
            buyCoinVolume.setBailVolume(BigDecimal.ZERO);
            buyCoinVolume.setCreateDate(LocalDateTime.now());
            buyCoinVolume.setUpdateDate(LocalDateTime.now());
            buyCoinVolume.setVersion(0L);
            offlineCoinVolumeDao.insert(buyCoinVolume);
        } else {
            updateDate = Timestamp.valueOf(buyCoinVolume.getUpdateDate());
            BigDecimal volume = buyCoinVolume.getVolume().add(exVolume);
            if (volume.compareTo(BigDecimal.ZERO) == -1) {
                throw new PlatException(Constants.OPERRATION_ERROR, "操作非法");
            }
            count = offlineCoinVolumeDao.updateVolume(buyCoinVolume.getUserId(), buyCoinVolume.getCoinId(), volume, updateDate, buyCoinVolume.getVersion());
            if (count == 0) {
                throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
            }
        }
        //更新订单详情状态
        Integer status = Integer.parseInt(OfflineOrderDetailStatusEnum.CONFIRM_IN.getCode());
        Integer preStatus = Integer.parseInt(OfflineOrderDetailStatusEnum.CONFIRM_PAY.getCode());//判断之前状态
        LocalDateTime confirmReceiptDate = LocalDateTime.now();
        count = offlineOrderDetailDao.updateStatusAndConfirmRecepitDateBySubOrderId(preStatus, status, confirmReceiptDate, offlineConfirmVO.getSubOrderId(), offlineConfirmVO.getUpdateBy());
        if (count != 2) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        //更新广告successVolume lockVolume

        BigDecimal successVolume = offlineOrder.getSuccessVolume().add(offlineOrderDetail.getVolume());
        lockVolume = offlineOrder.getLockVolume().subtract(offlineOrderDetail.getVolume());
        if (lockVolume.compareTo(BigDecimal.ZERO) == -1) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作非法");
        }
        Integer orderStatus = Integer.parseInt(OfflineOrderStatusEnum.PART_COMPLATE.getCode());
        if (offlineOrder.getStatus().toString().equals(OfflineOrderStatusEnum.CANCEL.getCode())) {
            if (successVolume.compareTo(offlineOrder.getVolume()) == -1) {
                orderStatus = Integer.parseInt(OfflineOrderStatusEnum.PART_CANCEL.getCode());
            }
        }
        updateDate = Timestamp.valueOf(offlineOrder.getUpdateDate());

        if (successVolume.compareTo(offlineOrder.getVolume()) == 0) {
            orderStatus = Integer.parseInt(OfflineOrderStatusEnum.COMPLATE.getCode());
        } else if (successVolume.compareTo(offlineOrder.getVolume()) == 1) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作非法");
        }
        if (offlineOrder.getStatus().toString().equals(OfflineOrderStatusEnum.PART_CANCEL.getCode())) {
            orderStatus = Integer.parseInt(OfflineOrderStatusEnum.PART_CANCEL.getCode());
        }
        count = offlineOrderDao.updateSuccessVolumeAndLockVolumeAndStatusById(offlineOrder.getId(), successVolume, lockVolume, orderStatus, updateDate, offlineOrder.getVersion());
        if (count == 0) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
    }


    @Override
    public List<OfflineOrderDetail> myOrderDetail(String userId) {
        return offlineOrderDetailDao.findMyOrderDetail(userId);
    }

    @Override
    public ResponsePage<OfflineOrderDetail> myOrderDetail(OrderDetailVO orderDetailVO) {
        ResponsePage<OfflineOrderDetail> responsePage = new ResponsePage<>();
        Page<OfflineOrderDetail> page = PageHelper.startPage(orderDetailVO.getCurrentPage(), orderDetailVO.getShowCount());
        List<OfflineOrderDetail> offlineOrderDetails = offlineOrderDetailDao.findMyOrderDetailByUserIdAndStatus(orderDetailVO.getUserId(), orderDetailVO.getStatus());

        if (CollectionUtils.isNotEmpty(offlineOrderDetails)) {
            offlineOrderDetails.stream().filter(o -> o.getStatus() == 0)
                    .map(o -> {
                        final LocalDateTime endTime = o.getCreateDate().plusMinutes(remainingTime);
                        final LocalDateTime now = LocalDateTime.now();
                        if (now.isEqual(endTime) || now.isAfter(endTime)) {
                            o.setRemainingTime(0);
                        } else {
                            final int until = (int) now.until(endTime, ChronoUnit.SECONDS);
                            o.setRemainingTime(until);
                        }
                        return o;
                    }).collect(Collectors.toList());

        }
        responsePage.setList(offlineOrderDetails);
        responsePage.setCount(page.getTotal());

        return responsePage;
    }

    @Override
    public OfflineOrderDetail findById(String id) {
        OfflineOrderDetail offlineOrderDetail = offlineOrderDetailDao.findById(id);
        if (offlineOrderDetail != null && offlineOrderDetail.getStatus() == 0) {
            final LocalDateTime endTime = offlineOrderDetail.getCreateDate().plusMinutes(remainingTime);
            final LocalDateTime now = LocalDateTime.now();
            if (now.isEqual(endTime) || now.isAfter(endTime)) {
                offlineOrderDetail.setRemainingTime(0);
            } else {
                final int until = (int) now.until(endTime, ChronoUnit.SECONDS);
                offlineOrderDetail.setRemainingTime(until);
            }
        }
        return offlineOrderDetail;
    }

    @Override
    public OfflineOrderDetail findByUserIdAndOrderId(String userId, String orderId) {
        return offlineOrderDetailDao.findByUserIdAndOrderId(userId, orderId);
    }

    /**
     * 如果广告是撤销状态就不能进行买入
     *
     * @param offlineOrderDetail
     * @return
     */
    @Override
    @Transactional
    public String buy(OfflineOrderDetail offlineOrderDetail) {
        //买入数量不能为负数
        if (offlineOrderDetail.getVolume().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PlatException(Constants.PARAM_ERROR, "买入数量必须大于0");
        }
        String status = OfflineOrderDetailStatusEnum.NOMAL.getCode() + "," + OfflineOrderDetailStatusEnum.CONFIRM_PAY.getCode();
        long totalOrderDetail = offlineOrderDetailDao.countByUserIdAndExTypeAndStatusIn(offlineOrderDetail.getUserId(), TradeEnum.BUY.ordinal(), status);

        PlatUser platUser = platUserDao.findById(offlineOrderDetail.getUserId());
        if (Objects.isNull(platUser)) {
            return null;
        }
        if (StringUtils.isNotEmpty(platUser.getTag()) && (platUser.getTag().contains(UserTagEnum.FINANCE.getCode()) || platUser.getTag().contains(UserTagEnum.OTC_AGENT.getCode()))) {

            LOGGER.info("银商账号{}买入不需要限制", platUser.getMobile());
        } else {
            if (UserCardStatusEnum.authRealName(platUser.getCardStatus(),platUser.getCountryCode())) {
                if (totalOrderDetail >= 1)
                    throw new PlatException(Constants.NOT_PAY_ERROR, "买入失败,请先处理现有订单");
            } else {
                if (totalOrderDetail > 0)
                    throw new PlatException(Constants.NOT_PAY_ERROR, "有未完成的订单");
            }
        }


        //查找订单
        OfflineOrder offlineOrder = offlineOrderDao.findById(offlineOrderDetail.getOrderId());
        //如果是卖出的广告 则只能买入  如果是买入的广告则只能卖出
        if (offlineOrder.getExType() != TradeEnum.SELL.ordinal()) {
            throw new PlatException(Constants.OPERRATION_ERROR, "该广告为卖出广告,只能买入操作");
        }
        //如果广告为发布状态或部分成交才能买入
        if (!offlineOrder.getStatus().toString().equals(OfflineOrderStatusEnum.NOMAL.getCode()) &&
                !offlineOrder.getStatus().toString().equals(OfflineOrderStatusEnum.PART_COMPLATE.getCode())) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,广告已下架");
        }
        //自己不能买入自己订单
        if (offlineOrderDetail.getUserId().equals(offlineOrder.getUserId())) {
            throw new PlatException(Constants.BUY_OWN_ERROR, "自己的广告,自己不能买入");
        }
        //最小买入数量
        if (offlineOrderDetail.getVolume().compareTo(offlineOrder.getMinExVolume()) == -1) {
            throw new PlatException(Constants.OFFLINE_BUY_VOLUME_ERROR, "买入数量至少" + offlineOrder.getMinExVolume());
        }
        //广告剩余数量
        BigDecimal avilVolume = offlineOrder.getVolume().subtract(offlineOrder.getLockVolume()).subtract(offlineOrder.getSuccessVolume());
        if (avilVolume.compareTo(BigDecimal.ZERO) == -1) {
            throw new PlatException(Constants.OFFLINE_VOLUME_ERROR, "广告剩余数量非法");
        }
        if (offlineOrderDetail.getVolume().compareTo(avilVolume) > 0) {
            throw new PlatException(Constants.OFFLINE_VOLUME_ERROR, "卖单数量不足,请修改买入数量");
        }
        //查看卖家资产 一旦确定了买入订单 就冻结卖家的广告资产到交易资产
        OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(offlineOrder.getUserId(), offlineOrder.getCoinId());
        if (offlineCoinVolume != null) {
            Timestamp timestamp = Timestamp.valueOf(offlineCoinVolume.getUpdateDate());
            BigDecimal advertVolume = offlineCoinVolume.getAdvertVolume().subtract(offlineOrderDetail.getVolume());
            if (advertVolume.compareTo(BigDecimal.ZERO) == -1) {
                throw new PlatException(Constants.OFFLINE_VOLUME_ERROR, "广告数量非法");
            }
            BigDecimal lockVolume = offlineCoinVolume.getLockVolume().add(offlineOrderDetail.getVolume());
            if (lockVolume.compareTo(BigDecimal.ZERO) == -1) {
                throw new PlatException(Constants.OFFLINE_VOLUME_ERROR, "锁定数量非法");
            }
            //给卖家减少资产  从广告资产advertVolume -》  交易冻结lockVolume
            long count = offlineCoinVolumeDao.updateAdvertVolumeAndLockVolume(offlineOrder.getUserId(), offlineOrder.getCoinId(), advertVolume, lockVolume, timestamp, offlineCoinVolume.getVersion());
            if (count == 0) {
                throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
            }
        }
        //将广告的交易冻结资产增加  防止用户撤销的时候有问题 volume-(success_volume+lock_volume)=用户可撤销资产
        BigDecimal lockVolume = offlineOrder.getLockVolume().add(offlineOrderDetail.getVolume());
        if (lockVolume.compareTo(BigDecimal.ZERO) == -1) {
            throw new PlatException(Constants.OFFLINE_VOLUME_ERROR, "锁定数量非法");
        }
        Timestamp updateDate = Timestamp.valueOf(offlineOrder.getUpdateDate());
        long count = offlineOrderDao.updateLockVolumeById(offlineOrder.getId(), lockVolume, updateDate, offlineOrder.getVersion());
        if (count == 0) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        String id = SnowFlake.createSnowFlake().nextIdString();
        String subOrderId = SnowFlake.createSnowFlake().nextIdString();
        String randomNum = RandomUtils.createCode();
        offlineOrderDetail.setId(id);
        offlineOrderDetail.setRadomNum(randomNum);
        offlineOrderDetail.setAskUserId(offlineOrder.getUserId());
        offlineOrderDetail.setAskUserName(offlineOrder.getRealName());
        offlineOrderDetail.setAskUserMobile(offlineOrder.getMobile());
        offlineOrderDetail.setCoinId(offlineOrder.getCoinId());
        offlineOrderDetail.setSymbol(offlineOrder.getSymbol());
        offlineOrderDetail.setStatus(0);
        offlineOrderDetail.setPrice(offlineOrder.getPrice());
        BigDecimal totalPrice = offlineOrderDetail.getVolume().multiply(offlineOrder.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
        offlineOrderDetail.setTotalPrice(totalPrice);
        offlineOrderDetail.setCreateDate(LocalDateTime.now());
        offlineOrderDetail.setUpdateDate(LocalDateTime.now());
        offlineOrderDetail.setRemarks("buy");
        offlineOrderDetail.setExType(TradeEnum.BUY.ordinal());
        offlineOrderDetail.setSubOrderId(subOrderId);
        offlineOrderDetail.setFeeVolume(BigDecimal.ZERO);
        offlineOrderDetail.setSellBankNo(offlineOrder.getCardNo());
        offlineOrderDetail.setSellBankName(offlineOrder.getBankName());
        offlineOrderDetail.setSellBankBranchName(offlineOrder.getBankBranchName());
        offlineOrderDetail.setAlipayNo(offlineOrder.getAlipayNo());
        offlineOrderDetail.setAlipayQrcodeId(offlineOrder.getAlipayQrcodeId());
        offlineOrderDetail.setWechatNo(offlineOrder.getWechatNo());
        offlineOrderDetail.setWechatQrcodeId(offlineOrder.getWechatQrcodeId());
        offlineOrderDetailDao.insert(offlineOrderDetail);
        //以上是针对sell家记录  还要存一条针对buy家的记录

        String userMobile = offlineOrderDetail.getAskUserMobile();
        String askUserMobile = offlineOrderDetail.getUserMobile();
        OfflineOrderDetail sellOfflineOrderDetail = new OfflineOrderDetail();
        BeanUtils.copyProperties(offlineOrderDetail, sellOfflineOrderDetail);
        String otherId = SnowFlake.createSnowFlake().nextIdString();
        sellOfflineOrderDetail.setId(otherId);
        sellOfflineOrderDetail.setRemarks("sell");
        sellOfflineOrderDetail.setExType(TradeEnum.SELL.ordinal());
        sellOfflineOrderDetail.setUserId(offlineOrder.getUserId());
        sellOfflineOrderDetail.setUserName(offlineOrderDetail.getAskUserName());
        sellOfflineOrderDetail.setAskUserId(offlineOrderDetail.getUserId());
        sellOfflineOrderDetail.setAskUserName(offlineOrderDetail.getUserName());
        sellOfflineOrderDetail.setUserMobile(userMobile);
        sellOfflineOrderDetail.setAskUserMobile(askUserMobile);
        sellOfflineOrderDetail.setSubOrderId(subOrderId);
        offlineOrderDetailDao.insert(sellOfflineOrderDetail);
        return id;
    }

    @Override
    @Transactional
    public void detailCancel(OfflineConfirmVO offlineConfirmVO,String tag) {
        //如果是发布广告的去确认收钱
        this.checkOrderDetailAndGetOrderId(offlineConfirmVO);
        //查询发布广告
        OfflineOrder offlineOrder = offlineOrderDao.findById(offlineConfirmVO.getOrderId());
        if (null == offlineOrder) {
            throw new PlatException(Constants.PARAM_ERROR, "参数错误");
        }
        checkOrderDetailAppeal(offlineConfirmVO.getSubOrderId()); // 判断该单是否有申诉单，否不允许操作
        //如果是发布广告的去确认收钱 则发布的是卖单
        //判断广告是买单还是卖单  卖出的广告只有买入方可以撤销  总之只有付钱的一方才可以撤销
        if (offlineOrder.getExType() == TradeEnum.SELL.ordinal()) {
            if (!offlineOrder.getUserId().equals(offlineConfirmVO.getUserId())) {
                cancelSell(offlineConfirmVO, offlineOrder,tag);
            } else {
                throw new PlatException(Constants.SELLER_CANCLE_ORDER_ERROR, "卖家不能取消订单");
            }
        } else if (offlineOrder.getExType() == TradeEnum.BUY.ordinal()) {
            if (!offlineOrder.getUserId().equals(offlineConfirmVO.getUserId())) {
                throw new PlatException(Constants.SELLER_CANCLE_ORDER_ERROR, "卖家不能取消订单");
            } else {
                cancelBuy(offlineConfirmVO, offlineOrder);
            }
        }

        if (Objects.equals(offlineConfirmVO.getUpdateBy(), "task")) {
            offlineOrderPushService.pushData(offlineConfirmVO.getUserId(),
                    offlineConfirmVO.getSubOrderId(),
                    Integer.parseInt(OfflineOrderDetailStatusEnum.TASK_ORDER_CANCEL.getCode()));
        } else {
            offlineOrderPushService.pushData(offlineConfirmVO.getUserId(),
                    offlineConfirmVO.getSubOrderId(),
                    Integer.parseInt(OfflineOrderDetailStatusEnum.CANCEL.getCode()));
        }
    }


    /**
     * 挂单广告是买单 买家可以取消 需要给卖家恢复资产 操作人是buy即发布广告的人
     *
     * @param offlineConfirmVO
     * @param offlineOrder
     */
    private void cancelBuy(OfflineConfirmVO offlineConfirmVO, OfflineOrder offlineOrder) {
        //将订单详情状态改为取消
        String buyUserId = offlineOrder.getUserId();
        String subOrderId = offlineConfirmVO.getSubOrderId();
        String coinId = offlineOrder.getCoinId();

        //给卖家恢复资产 lockVolume -
        //查询该笔交易涉及的订单
        OfflineOrderDetail offlineOrderDetail = offlineOrderDetailDao.findUserIdAndSubOrderIdAndCoinId(buyUserId, subOrderId, coinId);
        if (!offlineOrderDetail.getStatus().toString().equals(OfflineOrderDetailStatusEnum.NOMAL.getCode()) &&
                !offlineOrderDetail.getStatus().toString().equals(OfflineOrderDetailStatusEnum.CONFIRM_PAY.getCode())) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操纵失败");
        }
        //更新订单状态 防止重复取消
        Integer status = Integer.parseInt(OfflineOrderDetailStatusEnum.CANCEL.getCode());
        Timestamp updateDate = Timestamp.valueOf(offlineOrderDetail.getUpdateDate());
        long count = offlineOrderDetailDao.updateStatusAndCancelDateBySubOrderId(status, subOrderId, LocalDateTime.now(), updateDate, offlineConfirmVO.getUpdateBy());
        if (count != 2) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        //还原广告的lockVolume
        updateDate = Timestamp.valueOf(offlineOrder.getUpdateDate());
        BigDecimal resetVolume = offlineOrder.getLockVolume().subtract(offlineOrderDetail.getVolume());
        if (resetVolume.compareTo(BigDecimal.ZERO) == -1) {
            throw new PlatException(Constants.PARAM_ERROR, "数量错误,无法取消");
        }
        count = offlineOrderDao.updateLockVolumeById(offlineOrder.getId(), resetVolume, updateDate, offlineOrder.getVersion());
        if (count == 0) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        String sellUserId = offlineOrderDetail.getAskUserId();

        OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(sellUserId, coinId);
        //  此处无需判断为空 卖出方一定已经有记录了
        BigDecimal exVolume = offlineOrderDetail.getVolume();
        BigDecimal lockVolume = offlineCoinVolume.getLockVolume().subtract(exVolume);
        if (lockVolume.compareTo(BigDecimal.ZERO) == -1) {
            throw new PlatException(Constants.PARAM_ERROR, "数量错误,无法取消");
        }
        BigDecimal volume = offlineCoinVolume.getVolume().add(exVolume);
        if (volume.compareTo(BigDecimal.ZERO) == -1) {
            throw new PlatException(Constants.OFFLINE_VOLUME_ERROR, "用户资产非法");
        }
        count = offlineCoinVolumeDao.updateVolumeAndLockVolume(sellUserId, coinId, volume, lockVolume, Timestamp.valueOf(offlineCoinVolume.getUpdateDate()), offlineCoinVolume.getVersion());
        if (count == 0) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
    }

    /**
     * 挂单广告是卖单 买家可以取消 需要给卖家恢复资产
     *
     * @param offlineConfirmVO
     * @param offlineOrder
     */
    private void cancelSell(OfflineConfirmVO offlineConfirmVO, OfflineOrder offlineOrder,String tag) {
        //将订单详情状态改为取消
        String buyUserId = offlineConfirmVO.getUserId();
        String cancelKey = buyUserId+ DateTimeUtils.format("yyyy-MM-dd");
        String sellUserId = offlineOrder.getUserId();
        String subOrderId = offlineConfirmVO.getSubOrderId();
        String coinId = offlineOrder.getCoinId();
       /* if (null != tag && (tag.contains(UserTagEnum.FINANCE.getCode()) || tag.contains(UserTagEnum.OTC_AGENT.getCode()))) {
            LOGGER.info("财务撤销{},{}",buyUserId,subOrderId);
        }else{
            String redisCancelCount = valOpsStr.get(cancelKey);

            if(StringUtils.isEmpty(redisCancelCount)){
                LOGGER.info("该用户没有撤销过当天{}",buyUserId);
            }else{
                Long cancelCount = Long.parseLong(redisCancelCount);
                if(cancelCount>3L){
                    throw new PlatException(Constants.OFFLINE_ORDER_CANCEL_MORE_ERROR, "当天撤销订单次数太多");
                }
            }
        }*/

        //给卖家恢复资产 lockVolume -> advertVolume
        //查询该笔交易涉及的订单
        OfflineOrderDetail offlineOrderDetail = offlineOrderDetailDao.findUserIdAndSubOrderIdAndCoinId(sellUserId, subOrderId, coinId);

        if (!offlineOrderDetail.getStatus().toString().equals(OfflineOrderDetailStatusEnum.NOMAL.getCode()) &&
                !offlineOrderDetail.getStatus().toString().equals(OfflineOrderDetailStatusEnum.CONFIRM_PAY.getCode())) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操纵失败");
        }
        //更新订单状态
        Integer status = Integer.parseInt(OfflineOrderDetailStatusEnum.CANCEL.getCode());
        Timestamp updateDate = Timestamp.valueOf(offlineOrderDetail.getUpdateDate());
        long count = offlineOrderDetailDao.updateStatusAndCancelDateBySubOrderId(status, subOrderId, LocalDateTime.now(), updateDate, offlineConfirmVO.getUpdateBy());
        if (count != 2) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        BigDecimal exVolume = offlineOrderDetail.getVolume();

        BigDecimal lockVolume = offlineOrder.getLockVolume().subtract(exVolume);
        if (lockVolume.compareTo(BigDecimal.ZERO) == -1) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操纵失败");
        }
        updateDate = Timestamp.valueOf(offlineOrder.getUpdateDate());
        long orderCount = offlineOrderDao.updateLockVolumeById(offlineOrder.getId(), lockVolume, updateDate, offlineOrder.getVersion());
        if (orderCount == 0) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(sellUserId, coinId);
        BigDecimal advertVolume = offlineCoinVolume.getAdvertVolume().add(exVolume);
        lockVolume = offlineCoinVolume.getLockVolume().subtract(exVolume);
        if (lockVolume.compareTo(BigDecimal.ZERO) == -1) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操纵失败");
        }

        updateDate = Timestamp.valueOf(offlineCoinVolume.getUpdateDate());
        if (offlineOrder.getStatus().toString().equals(OfflineOrderStatusEnum.CANCEL.getCode()) || offlineOrder.getStatus().toString().equals(OfflineOrderStatusEnum.PART_CANCEL.getCode())) {
            BigDecimal volume = offlineCoinVolume.getVolume().add(exVolume);
            count = offlineCoinVolumeDao.updateVolumeAndLockVolume(sellUserId, coinId, volume, lockVolume, updateDate, offlineCoinVolume.getVersion());
            if (count == 0) {
                throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
            }
        } else {
            count = offlineCoinVolumeDao.updateAdvertVolumeAndLockVolume(sellUserId, coinId, advertVolume, lockVolume, updateDate, offlineCoinVolume.getVersion());
            if (count == 0) {
                throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
            }
        }
        //将买家当天的撤销次数+1
        if (null != tag && (tag.contains(UserTagEnum.FINANCE.getCode()) || tag.contains(UserTagEnum.OTC_AGENT.getCode()))) {
            LOGGER.info("财务撤销{},{}",buyUserId,subOrderId);
        }else{
            valOpsStr.increment(cancelKey, 1L);
            valOpsStr.getOperations().expire(cancelKey, SercurityConstant.REDIS_EXPIRE_TIME_ONE_HOUR * 24, TimeUnit.SECONDS);
        }
    }

    @Override
    public void detailShensu(OfflineConfirmVO offlineConfirmVO) {

    }

    @Override
    public List<OfflineOrderDetail> findByOrderId(String userId, String orderId) {
        return offlineOrderDetailDao.findByOrderId(userId, orderId);
    }

    public void checkOrderDetailAppeal(String subOrderId) {
        List<OfflineAppeal> appealList = offlineAppealDao.findBySubOrderIdAndStatus(subOrderId, "1");
        if (CollectionUtils.isNotEmpty(appealList)) {
            throw new PlatException(Constants.SUBORDER_EXISTS_APPEAL, "该订单已被申诉，请先处理申诉单或者联系客服");
        }

        appealList = offlineAppealDao.findBySubOrderIdAndStatus(subOrderId, "2");
        if (CollectionUtils.isNotEmpty(appealList)) {
            throw new PlatException(Constants.SUBORDER_EXISTS_APPEAL, "该订单已被客服处理，请刷新页面查看订单状态。若有疑问请联系客服。");
        }
    }

    private void checkOrderDetailAndGetOrderId(OfflineConfirmVO offlineConfirmVO) {
        OfflineOrderDetail offlineOrderDetail = offlineOrderDetailDao.findByUserIdAndOrderId(offlineConfirmVO.getUserId(), offlineConfirmVO.getSubOrderId());
        if(Objects.isNull(offlineOrderDetail)){
            throw new PlatException(Constants.OPERRATION_ERROR, "操纵失败");
        }else{
            offlineConfirmVO.setOrderId(offlineOrderDetail.getOrderId());
        }
    }

}
