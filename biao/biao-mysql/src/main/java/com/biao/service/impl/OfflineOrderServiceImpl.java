package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.OfflineCancelLog;
import com.biao.entity.OfflineCoin;
import com.biao.entity.OfflineCoinVolume;
import com.biao.entity.OfflineOrder;
import com.biao.enums.*;
import com.biao.exception.PlatException;
import com.biao.mapper.*;
import com.biao.pojo.ResponsePage;
import com.biao.service.OfflineOrderService;
import com.biao.util.FeeUtils;
import com.biao.util.SnowFlake;
import com.biao.vo.AdvertVO;
import com.biao.vo.OfflineConfirmVO;
import com.biao.vo.OfflineOrderVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class OfflineOrderServiceImpl implements OfflineOrderService {

    @Autowired
    private OfflineOrderDao offlineOrderDao;

    @Autowired
    private OfflineCoinDao offlineCoinDao;

    @Autowired
    private OfflineCoinVolumeDao offlineCoinVolumeDao;

    @Autowired
    private OfflineCoinVolumeLogDao offlineCoinVolumeLogDao;

    @Autowired
    private OfflineCancelLogDao offlineCancelLogDao;

    @Autowired
    private OfflineOrderDetailDao offlineOrderDetailDao;

    private static final String HIDE_CHAR_DEFAULT = "**";// 银行卡，微信，支付宝，真名隐藏

    private static final Logger LOGGER = LoggerFactory.getLogger(OfflineOrderServiceImpl.class);
    @Override
    public void updateById(OfflineOrder order) {
    }

    @Override
    public List<OfflineOrder> findMyAdvertList(String userId) {
        return offlineOrderDao.findMyAdvertList(userId);
    }

    @Override
    public ResponsePage<OfflineOrder> findMyAdvertList(OfflineOrderVO offlineOrderVO) {
        ResponsePage<OfflineOrder> responsePage = new ResponsePage<>();
        Page<OfflineOrder> page = PageHelper.startPage(offlineOrderVO.getCurrentPage(), offlineOrderVO.getShowCount());
        List<OfflineOrder> offlineOrderDetails = offlineOrderDao.findMyAdvertList(offlineOrderVO.getUserId());
        responsePage.setCount(page.getTotal());
        responsePage.setList(offlineOrderDetails);
        return responsePage;
    }

    @Override
    public ResponsePage<OfflineOrder> findPage(AdvertVO advertVO) {
        ResponsePage<OfflineOrder> responsePage = new ResponsePage<>();
        Page<OfflineOrder> page = PageHelper.startPage(advertVO.getCurrentPage(), advertVO.getShowCount());
        List<OfflineOrder> data = null;
        if (advertVO.getExType() == 0) {
            //卖出 价格降序
            data = offlineOrderDao.findAdvertListByCoinIdPriceDesc(advertVO.getCoinId(), advertVO.getExType());
        } else if (advertVO.getExType() == 1) {
            //买入  价格升序
            data = offlineOrderDao.findAdvertListByCoinIdPriceAsc(advertVO.getCoinId(), advertVO.getExType());
        } else {
            data = offlineOrderDao.findAdvertListByCoinId(advertVO.getCoinId(), advertVO.getExType());
        }
        responsePage.setCount(page.getTotal());
        hideSensitiveInfo(data);// 隐藏敏感信息
        responsePage.setList(data);
        return responsePage;
    }

    /**
     * 隐藏敏感信息（微信，银行卡，真名。。。），避免返回入客户端
     */
    private void hideSensitiveInfo(List<OfflineOrder> data) {
        if (CollectionUtils.isNotEmpty(data)) {
            data.forEach(order -> {
                if (StringUtils.isNotBlank(order.getCardNo())) {
                    order.setCardNo(HIDE_CHAR_DEFAULT);
                }
                if (StringUtils.isNotBlank(order.getAlipayNo())) {
                    order.setAlipayNo(HIDE_CHAR_DEFAULT);
                }
                if (StringUtils.isNotBlank(order.getWechatNo())) {
                    order.setWechatNo(HIDE_CHAR_DEFAULT);
                }
                if (StringUtils.isNotBlank(order.getRealName())) {
                    order.setRealName(order.getRealName().substring(0, 1) + HIDE_CHAR_DEFAULT);
                }
            });
        }
    }

    /**
     * 发布的广告进行撤销
     *
     * @param offlineConfirmVO
     */
    @Override
    @Transactional
    public void advertCancel(OfflineConfirmVO offlineConfirmVO) {
        //查询发布广告
        OfflineOrder offlineOrder = offlineOrderDao.findById(offlineConfirmVO.getOrderId());
        if (null == offlineOrder) {
            throw new PlatException(Constants.PARAM_ERROR, "操作非法");
        }
        List<String> cancelStatus = new ArrayList<>();
        cancelStatus.add(OfflineOrderStatusEnum.CANCEL.getCode());
        cancelStatus.add(OfflineOrderStatusEnum.COMPLATE.getCode());
        cancelStatus.add(OfflineOrderStatusEnum.PART_CANCEL.getCode());
        Integer status = offlineOrder.getStatus();
        if (cancelStatus.contains(status + "")) {
            throw new PlatException(Constants.PARAM_ERROR, "该广告不能撤销");
        }
        // 如果 锁定数量 + 成交数量 = 发布数量，则不允许撤销广告
        Optional<BigDecimal> lockVol = Optional.ofNullable(offlineOrder.getLockVolume());
        Optional<BigDecimal> successVol = Optional.ofNullable(offlineOrder.getSuccessVolume());
        BigDecimal sumVol = lockVol.orElse(BigDecimal.ZERO).add(successVol.orElse(BigDecimal.ZERO));
        if (offlineOrder.getVolume().compareTo(sumVol) == 0) {
            throw new PlatException(Constants.PARAM_ERROR, "该广告没有可撤销的数量");
        }
        //判断广告是买单还是卖单
        if (offlineOrder.getExType() == TradeEnum.SELL.ordinal()) {
            //说明是发布广告的人进行的操作 否则非法
            if (offlineOrder.getUserId().equals(offlineConfirmVO.getUserId())) {
                orderCancelSell(offlineConfirmVO, offlineOrder);
            } else {
                throw new PlatException(Constants.PARAM_ERROR, "操作非法");
            }
        } else if (offlineOrder.getExType() == TradeEnum.BUY.ordinal()) {
            //说明是发布广告的人进行的操错 否则非法  数据权限
            if (!offlineOrder.getUserId().equals(offlineConfirmVO.getUserId())) {
                throw new PlatException(Constants.PARAM_ERROR, "操作非法");
            } else {
                orderCancelBuy(offlineOrder);
            }
        }
        //写入日志
        OfflineCancelLog offlineCancelLog = new OfflineCancelLog();
        String id = SnowFlake.createSnowFlake().nextIdString();
        offlineCancelLog.setId(id);
        offlineCancelLog.setDate(LocalDate.now());
        offlineCancelLog.setType(OfflineCancelTypeEnum.ADVERT.getCode());
        offlineCancelLog.setUserId(offlineConfirmVO.getUserId());
        offlineCancelLogDao.insert(offlineCancelLog);
    }



    @Override
    @Transactional
    public void findByStatusAndExType() {

        //部分成交的广告
        List<OfflineOrder> list2 = offlineOrderDao.findByStatusAndExType(2,TradeEnum.SELL.ordinal());
        if(CollectionUtils.isEmpty(list2)) return;
        list2.forEach(offlineOrder -> {
            //修改广告状态
            long result = offlineOrderDao.updateStatusById(3,offlineOrder.getId(),offlineOrder.getVersion());
            if(result<=0){
                LOGGER.error("修改广告状态失败1");
            }
            //归还用户资产
            OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(offlineOrder.getUserId(),offlineOrder.getCoinId());
            BigDecimal volume = offlineCoinVolume.getVolume().add(offlineOrder.getVolume()).subtract(offlineOrder.getSuccessVolume());
            BigDecimal advertVolume = offlineCoinVolume.getAdvertVolume().subtract(offlineOrder.getVolume().subtract(offlineOrder.getSuccessVolume()));
            long count = offlineCoinVolumeDao.updateVolumeAndAdvertVolumeByUserIdAndCoinId(offlineOrder.getUserId(),offlineOrder.getCoinId(),volume,advertVolume,offlineCoinVolume.getVersion());
            if(count<=0){
                LOGGER.error("取消失败用户{}:广告{}:symbol{}",offlineOrder.getUserId(),offlineOrder.getId(),offlineOrder.getSymbol());
            }

        });
        List<OfflineOrder> list = offlineOrderDao.findByStatusAndExType(0,TradeEnum.SELL.ordinal());
        if(CollectionUtils.isEmpty(list)) return;
        list.forEach(offlineOrder -> {
            //修改广告状态
            long result = offlineOrderDao.updateStatusById(9,offlineOrder.getId(),offlineOrder.getVersion());
            if(result<=0){
                LOGGER.error("修改广告状态失败");
            }
            //归还用户资产
            OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(offlineOrder.getUserId(),offlineOrder.getCoinId());
            if(null == offlineCoinVolume) {

            }
            BigDecimal volume = offlineCoinVolume.getVolume().add(offlineOrder.getVolume());
            BigDecimal advertVolume = offlineCoinVolume.getAdvertVolume().subtract(offlineOrder.getVolume());
            long count = offlineCoinVolumeDao.updateVolumeAndAdvertVolumeByUserIdAndCoinId(offlineOrder.getUserId(),offlineOrder.getCoinId(),volume,advertVolume,offlineCoinVolume.getVersion());
            if(count<=0){
                LOGGER.error("取消失败用户{}:广告{}:symbol{}",offlineOrder.getUserId(),offlineOrder.getId(),offlineOrder.getSymbol());
            }

        });
        return ;
    }

    /**
     * 发布的广告是买入
     *
     * @param
     * @param offlineOrder
     */
    private void orderCancelBuy(OfflineOrder offlineOrder) {

        //更新广告状态 满足幂等性
        if (!offlineOrder.getStatus().toString().equals(OfflineOrderStatusEnum.NOMAL.getCode()) &&
                !offlineOrder.getStatus().toString().equals(OfflineOrderStatusEnum.PART_COMPLATE.getCode())) {
            throw new PlatException(Constants.OPERRATION_ERROR);
        }
        offlineOrder.setStatus(Integer.parseInt(OfflineOrderStatusEnum.CANCEL.getCode()));
        Timestamp updateDate = Timestamp.valueOf(offlineOrder.getUpdateDate());
        //todo 要写纯正的where 判断状态
        long count = offlineOrderDao.updateOrderCancelStatusById(offlineOrder.getId(), offlineOrder.getStatus(), LocalDateTime.now(), updateDate, offlineOrder.getVersion());
        if (count == 0) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }

    }

    /**
     * 发布的广告是卖出
     *
     * @param offlineConfirmVO
     * @param offlineOrder
     */
    private void orderCancelSell(OfflineConfirmVO offlineConfirmVO, OfflineOrder offlineOrder) {
        //将广告状态改为取消
        String sellUserId = offlineConfirmVO.getUserId();
        String coinId = offlineOrder.getCoinId();
        BigDecimal exVolume = offlineOrder.getVolume().subtract(offlineOrder.getSuccessVolume()).subtract(offlineOrder.getLockVolume());
        //判断exVolume 不为负数
        if (exVolume.compareTo(BigDecimal.ZERO) == -1) {
            throw new PlatException(Constants.PARAM_ERROR);
        }
        //更新广告状态 满足幂等性 全部取消
        if (!offlineOrder.getStatus().toString().equals(OfflineOrderStatusEnum.NOMAL.getCode()) &&
                !offlineOrder.getStatus().toString().equals(OfflineOrderStatusEnum.PART_COMPLATE.getCode())) {
            throw new PlatException(Constants.OPERRATION_ERROR);
        }
        //如果是部分成功部分取消
        offlineOrder.setStatus(Integer.parseInt(OfflineOrderStatusEnum.CANCEL.getCode()));
        Timestamp updateDateOrder = Timestamp.valueOf(offlineOrder.getUpdateDate());
        long count = offlineOrderDao.updateOrderCancelStatusById(offlineOrder.getId(), offlineOrder.getStatus(), LocalDateTime.now(), updateDateOrder, offlineOrder.getVersion());
        if (count == 0) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        //更新用户资产
        OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(sellUserId, coinId);

        BigDecimal advertVolume = offlineCoinVolume.getAdvertVolume().subtract(exVolume);
        if (advertVolume.compareTo(BigDecimal.ZERO) == -1) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作非法");
        }
        BigDecimal volume = offlineCoinVolume.getVolume().add(exVolume);
        Timestamp updateDate = Timestamp.valueOf(offlineCoinVolume.getUpdateDate());
        count = offlineCoinVolumeDao.updateVolumeAndAdvertVolume(sellUserId, coinId, volume, advertVolume, updateDate, offlineCoinVolume.getVersion());
        if (count == 0) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
    }

    @Override
    public OfflineOrder findById(String id) {
        return offlineOrderDao.findById(id);
    }


    /**
     * 买入不需要判断账户资产 卖出要判断资产
     *
     * @param offlineOrder
     * @return
     */
    @Override
    @Transactional
    public String save(OfflineOrder offlineOrder, String tag) {
        //判断用户资产
        String userId = offlineOrder.getUserId();
        String coinId = offlineOrder.getCoinId();
        OfflineCoin offlineCoin = offlineCoinDao.findByCoinId(coinId);
        if (Objects.isNull(offlineCoin)) {
            throw new PlatException(Constants.OFFLINE_COIN_NOT_EXSIT_ERROR, "C2C币种不存在");
        }
        if (offlineCoin.getDisable() == 1) {
            throw new PlatException(Constants.OFFLINE_COIN_NOT_EXSIT_ERROR, "C2C币种已下架");
        }

        if (offlineOrder.getExType().equals(TradeEnum.BUY.ordinal())) {
            //要根据币种设置的最小量 和 最大量来匹配
            if (offlineOrder.getMinExVolume().compareTo(offlineCoin.getMinVolume()) == -1) {
                throw new PlatException(Constants.OFFLINE_COIN_MIN_VOLUME_ERROR, "最小交易量为" + offlineCoin.getMinVolume());
            }
            if (offlineOrder.getVolume().compareTo(offlineCoin.getMaxVolume()) == 1) {
                throw new PlatException(Constants.OFFLINE_COIN_MAX_VOLUME_ERROR, "最大买入量为" + offlineCoin.getMaxVolume());
            }

        } else if (offlineOrder.getExType().equals(TradeEnum.SELL.ordinal())) {
            if (offlineOrder.getVolume().compareTo(offlineCoin.getMaxVolume()) == 1) {
                throw new PlatException(Constants.OFFLINE_COIN_MAX_VOLUME_ERROR, "最大卖出量为" + offlineCoin.getMaxVolume());
            }
        }
        String id = SnowFlake.createSnowFlake().nextIdString();
        offlineOrder.setId(id);
        offlineOrder.setStatus(Integer.parseInt(OfflineOrderStatusEnum.NOMAL.getCode()));
        offlineOrder.setSuccessVolume(BigDecimal.ZERO);
        offlineOrder.setLockVolume(BigDecimal.ZERO);
        BigDecimal totalPrice = offlineOrder.getVolume().multiply(offlineOrder.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
        offlineOrder.setTotalPrice(totalPrice);
        offlineOrder.setVersion(0L);
        if (offlineOrder.getExType().equals(TradeEnum.BUY.ordinal())) {
            //买入不用判断用户资产
            offlineOrderDao.insert(offlineOrder);
            return id;
        } else if (offlineOrder.getExType().equals(TradeEnum.SELL.ordinal())) {

            OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(userId, coinId);
            if (Objects.isNull(offlineCoinVolume)) {
                throw new PlatException(Constants.OFFLINE_VOLUME_ERROR, "当前用户不存在该币种资产");
            }
            Optional.of(offlineCoinVolume)
                    .ifPresent(o -> {
                        if (o.getVolume().compareTo(offlineOrder.getVolume()) >= 0) {
                            BigDecimal inputVolume = offlineOrder.getVolume();//未扣除掉手续费
                            if (StringUtils.isEmpty(tag)) {//需要扣手续费
                                saveOfflineOrderFee(userId, coinId, inputVolume, offlineCoinVolume, offlineOrder);
                            } else {
                                if (null != tag && (tag.contains(UserTagEnum.FINANCE.getCode()) || tag.contains(UserTagEnum.OTC_AGENT.getCode()))) {//不需要扣除手续费
                                    //可用资产减去
                                    BigDecimal volume = o.getVolume().subtract(offlineOrder.getVolume());
                                    //广告冻结资产要加
                                    BigDecimal advertVolume = o.getAdvertVolume().add(offlineOrder.getVolume());
                                    Timestamp updateDate = Timestamp.valueOf(o.getUpdateDate());
                                    long count = offlineCoinVolumeDao.updateVolumeAndAdvertVolume(userId, coinId, volume, advertVolume, updateDate, o.getVersion());
                                    if (count == 0) {
                                        throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
                                    }
                                    //
                                    offlineOrder.setFeeVolume(BigDecimal.ZERO);
                                    offlineOrderDao.insert(offlineOrder);
                                } else {//需要扣手续费
                                    saveOfflineOrderFee(userId, coinId, inputVolume, offlineCoinVolume, offlineOrder);
                                }
                            }

                        } else {
                            throw new PlatException(Constants.OFFLINE_VOLUME_ERROR, "c2c资产不足，请先转入!");
                        }
                    });

        } else {
            throw new PlatException(Constants.PARAM_ERROR, "参数错误！");
        }
        return id;
    }

    private void saveOfflineOrderFee(String userId, String coinId, BigDecimal inputVolume, OfflineCoinVolume offlineCoinVolume, OfflineOrder offlineOrder) {
        OfflineCoin offlineCoin = offlineCoinDao.findByCoinId(coinId);
        if (Objects.isNull(offlineCoin)) {
            throw new PlatException(Constants.OFFLINE_COIN_NOT_EXSIT_ERROR, "C2C币种不存在");
        }
        String feeType = offlineCoin.getFeeType();
        BigDecimal feeVolume = BigDecimal.ZERO;
        if (feeType.equals(OfflineFeeTypeEnum.STATIC.getCode())) {
            String sellFeeStep = offlineCoin.getSellFeeStep();
            feeVolume = FeeUtils.getFeeByStep(inputVolume, sellFeeStep);

        } else if (feeType.equals(OfflineFeeTypeEnum.SCALE.getCode())) {
            feeVolume = inputVolume.multiply(offlineCoin.getSellFee());
        }

        String[] statusArray = {OfflineOrderDetailStatusEnum.NOMAL.getCode(), OfflineOrderDetailStatusEnum.CONFIRM_PAY.getCode()};
        String statusIn = StringUtils.join(statusArray, ",");
        Double sumFee = offlineOrderDetailDao.sumByStatusAndUserIdAndCoinId(statusIn, userId, coinId);
        //check保证金的数量
        BigDecimal residualVolume = feeVolume.add(BigDecimal.valueOf(sumFee == null ? 0 : sumFee));
        if (offlineCoinVolume.getBailVolume().compareTo(residualVolume) < 0) {
            throw new PlatException(Constants.OPERRATION_ERROR, "操作失败,手续费预备金不足");
        }
        //BigDecimal realVolume = inputVolume.subtract(feeVolume);
        //可用资产减去
        BigDecimal volume = offlineCoinVolume.getVolume().subtract(inputVolume);
        //广告冻结资产要加
        BigDecimal advertVolume = offlineCoinVolume.getAdvertVolume().add(inputVolume);
        //保证金账户扣掉手续费
        //判断保证金账户是否可以支付本次费用
        if (offlineCoinVolume.getBailVolume() != null && offlineCoinVolume.getBailVolume().compareTo(feeVolume) < 0) {
            throw new PlatException(Constants.OFFLINE_BAIL_VOLUME_NOT_ENOUGH_ERROR, "手续费预备金账户余额不足,请先转入");
        }

        BigDecimal bailVolume = offlineCoinVolume.getBailVolume().subtract(feeVolume);
        Timestamp updateDate = Timestamp.valueOf(offlineCoinVolume.getUpdateDate());
        long count = offlineCoinVolumeDao.updateVolumeAndAdvertVolumeAndBailVolume(userId, coinId, volume, advertVolume, bailVolume, updateDate, offlineCoinVolume.getVersion());
        if (count == 0) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        offlineOrder.setVolume(inputVolume);
        offlineOrder.setFeeVolume(feeVolume);
        offlineOrderDao.insert(offlineOrder);
    }


}
