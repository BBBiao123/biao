/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.SnowFlake;
import com.thinkgem.jeesite.common.utils.TimeUtils;
import com.thinkgem.jeesite.modules.plat.dao.OfflineAppealDao;
import com.thinkgem.jeesite.modules.plat.dao.OfflineCoinVolumeDao;
import com.thinkgem.jeesite.modules.plat.dao.OfflineOrderDao;
import com.thinkgem.jeesite.modules.plat.dao.OfflineOrderDetailDao;
import com.thinkgem.jeesite.modules.plat.entity.OfflineAppeal;
import com.thinkgem.jeesite.modules.plat.entity.OfflineCoinVolume;
import com.thinkgem.jeesite.modules.plat.entity.OfflineOrder;
import com.thinkgem.jeesite.modules.plat.entity.OfflineOrderDetail;
import com.thinkgem.jeesite.modules.plat.enums.OfflineAppealStatusEnum;
import com.thinkgem.jeesite.modules.plat.enums.OfflineOrderDetailStatusEnum;
import com.thinkgem.jeesite.modules.plat.enums.OfflineOrderStatusEnum;
import com.thinkgem.jeesite.modules.plat.utils.OkHttpTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 申诉Service
 *
 * @author dongfeng
 * @version 2018-06-29
 */
@Service
@Transactional(readOnly = true)
@SuppressWarnings("all")
public class OfflineAppealService extends CrudService<OfflineAppealDao, OfflineAppeal> {

    @Autowired
    private OfflineOrderDao offlineOrderDao;

    @Autowired
    private OfflineOrderDetailDao offlineOrderDetailDao;

    @Autowired
    private OfflineCoinVolumeDao offlineCoinVolumeDao;

    @Value("${platUrl}")
    private String platUrl;

    public OfflineAppeal get(String id) {
        return super.get(id);
    }

    public List<OfflineAppeal> findList(OfflineAppeal offlineAppeal) {
        return super.findList(offlineAppeal);
    }

    public Page<OfflineAppeal> findPage(Page<OfflineAppeal> page, OfflineAppeal offlineAppeal) {
        return super.findPage(page, offlineAppeal);
    }

    @Transactional(readOnly = false)
    public void save(OfflineAppeal offlineAppeal) {
        super.save(offlineAppeal);
    }

    @Transactional(readOnly = false)
    public void delete(OfflineAppeal offlineAppeal) {
        super.delete(offlineAppeal);
    }

    @Transactional(readOnly = false)
    public void examineBuyer(String appealId, String userId, String examineResultReason) {
        OfflineAppeal appeal = dao.get(appealId);// 申诉单
        OfflineOrderDetail offlineOrderDetail = offlineOrderDetailDao.findUserIdAndSubOrderId(appeal.getBuyUserId(), appeal.getSubOrderId());// 买家订单
        OfflineOrder offlineOrder = offlineOrderDao.get(offlineOrderDetail.getOrderId());// 广告

        if (!"1".equals(offlineOrderDetail.getStatus())) {
            throw new ServiceException("买家未点击已付款，不能判给买家，若判给买家，需要求买家在交易平台C2C未完成订单里点击【我已付款给卖家】按钮！");
        }

        OfflineCoinVolume sellCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(appeal.getSellUserId(), offlineOrder.getCoinId()); // C2C卖家账户资金

        BigDecimal exVolume = new BigDecimal(offlineOrderDetail.getVolume());
        BigDecimal lockVolume = new BigDecimal(sellCoinVolume.getLockVolume()).subtract(exVolume);
        if (lockVolume.compareTo(BigDecimal.ZERO) == -1) {
            throw new ServiceException("卖家C2C账户冻结资金小于本订单金额");
        }
        // 1、更新卖家C2C账户的交易冻结资金
        long count = offlineCoinVolumeDao.updateLockVolume(sellCoinVolume.getUserId(), offlineOrder.getCoinId(), lockVolume, new Timestamp(sellCoinVolume.getUpdateDate().getTime()), sellCoinVolume.getVersion());
        if (count != 1) {
            throw new ServiceException("更新失败！");
        }
        OfflineCoinVolume buyCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(appeal.getBuyUserId(), offlineOrder.getCoinId()); // C2C买家账户资金
        // 2、更新买家C2C账户可用资金
        if (null == buyCoinVolume) {
            buyCoinVolume = new OfflineCoinVolume();
            String id = SnowFlake.createSnowFlake().nextIdString();
            buyCoinVolume.setId(id);
            buyCoinVolume.setCoinId(offlineOrder.getCoinId());
            buyCoinVolume.setCoinSymbol(offlineOrder.getSymbol());
            buyCoinVolume.setUserId(appeal.getBuyUserId());
            buyCoinVolume.setVolume(exVolume.toString());
            buyCoinVolume.setAdvertVolume(BigDecimal.ZERO.toString());
            buyCoinVolume.setLockVolume(BigDecimal.ZERO.toString());
            buyCoinVolume.setCreateDate(new Date());
            buyCoinVolume.setUpdateDate(new Date());
            offlineCoinVolumeDao.insert(buyCoinVolume);// 添加买家C2C账户可用资金
        } else {
            BigDecimal volume = new BigDecimal(buyCoinVolume.getVolume()).add(exVolume);
            if (volume.compareTo(BigDecimal.ZERO) == -1) {
                throw new ServiceException("操作非法");
            }
            count = offlineCoinVolumeDao.updateVolume(buyCoinVolume.getUserId(), buyCoinVolume.getCoinId(), volume, new Timestamp(buyCoinVolume.getUpdateDate().getTime()), buyCoinVolume.getVersion());// 更新买家C2C账户可用资金
            if (count != 1) {
                throw new ServiceException("更新失败！");
            }
        }
        // 3、更新买家/卖家订单状态为已成交
        count = offlineOrderDetailDao.updateStatusReceiptBySubOrderId(2, appeal.getSubOrderId(), TimeUtils.curTimeLocal());
        if (count != 2) {
            throw new ServiceException("更新失败！");
        }

        BigDecimal successVolume = new BigDecimal(offlineOrder.getSuccessVolume()).add(new BigDecimal(offlineOrderDetail.getVolume()));
        lockVolume = new BigDecimal(offlineOrder.getLockVolume()).subtract(new BigDecimal(offlineOrderDetail.getVolume()));
        if (lockVolume.compareTo(BigDecimal.ZERO) == -1) {
            throw new ServiceException("操作非法");
        }
        Integer orderStatus = Integer.parseInt(OfflineOrderStatusEnum.PART_COMPLATE.getCode());
        if (offlineOrder.getStatus().equals(Integer.parseInt(OfflineOrderStatusEnum.CANCEL.getCode()))) {
            if (successVolume.compareTo(new BigDecimal(offlineOrder.getVolume())) == -1) {
                orderStatus = 3;
            }
        }
        if (successVolume.compareTo(new BigDecimal(offlineOrder.getVolume())) == 0) {
            orderStatus = Integer.parseInt(OfflineOrderStatusEnum.COMPLATE.getCode());
        } else if (successVolume.compareTo(new BigDecimal(offlineOrder.getVolume())) == 1) {
            throw new ServiceException("操作非法");
        }
        //修改c2c广告 订单为 2个用户下买单 后 ，其中一个先付款，广告进行撤销，另外一个再付款，卖家进行确认收款2次的订单问题
        if (offlineOrder.getStatus().toString().equals(OfflineOrderStatusEnum.PART_CANCEL.getCode())) {
            orderStatus = Integer.parseInt(OfflineOrderStatusEnum.PART_CANCEL.getCode());
        }
        // 4、更新广告状态和币个数
        count = offlineOrderDao.updateSuccessVolumeAndLockVolumeAndStatusById(offlineOrder.getId(), successVolume, lockVolume, orderStatus, new Timestamp(offlineOrder.getUpdateDate().getTime()));
        if (count != 1) {
            throw new ServiceException("更新失败！");
        }
        // 5、更新申诉单为已处理
        count = dao.updateAppealDone(appealId, userId, TimeUtils.curTimeLocal(), appeal.getBuyUserId(), examineResultReason, appeal.getBuyRealName());
        if (count != 1) {
            throw new ServiceException("更新申诉单状态失败");
        }
        try {
            final String url = buildBuyParam("buy", offlineOrderDetail.getUserId(), offlineOrderDetail.getAskUserId(), offlineOrderDetail.getSubOrderId());
            final String success = OkHttpTools.getInstance().get(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String buildBuyParam(String type, String toUserId, String fromUserId, String orderId) {
        return platUrl + "/bbex/im/" + type + "?toUserId=" + toUserId + "&fromUserId=" + fromUserId + "&orderId=" + orderId;
    }

    @Transactional(readOnly = false)
    public void examineSeller(String appealId, String userId, String examineResultReason) {
        OfflineAppeal appeal = dao.get(appealId);// 申诉单
        if (!OfflineAppealStatusEnum.ING.getCode().equals(appeal.getStatus())) {
            throw new ServiceException("该申诉单已经被处理过");
        }
        OfflineOrderDetail offlineOrderDetail = offlineOrderDetailDao.findUserIdAndSubOrderId(appeal.getSellUserId(), appeal.getSubOrderId());// 订单
        OfflineOrder offlineOrder = offlineOrderDao.get(offlineOrderDetail.getOrderId());// 广告
        if (null == offlineOrder) {
            throw new ServiceException("参数错误");
        }
        //如果是发布广告的去确认收钱 则发布的是卖单
        //判断广告是买单还是卖单  卖出的广告只有买入方可以撤销  总之只有付钱的一方才可以撤销
        if ("1".equals(offlineOrder.getExType())) {// 卖单
            cancelSell2Seller(appeal, offlineOrder, offlineOrderDetail);
        } else if ("0".equals(offlineOrder.getExType())) {
            cancelBuy2Seller(appeal, offlineOrder, offlineOrderDetail);
        }
        long count = dao.updateAppealDone(appealId, userId, TimeUtils.curTimeLocal(), appeal.getSellUserId(), examineResultReason, appeal.getSellRealName());
        if (count != 1) {
            throw new ServiceException("更新申诉单状态失败");
        }
        final String url = buildBuyParam("sell", offlineOrderDetail.getUserId(), offlineOrderDetail.getAskUserId(), offlineOrderDetail.getSubOrderId());
        try {
            final String success = OkHttpTools.getInstance().get(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cancelSell2Seller(OfflineAppeal appeal, OfflineOrder offlineOrder, OfflineOrderDetail offlineOrderDetail) {
        //将订单详情状态改为取消
        String sellUserId = appeal.getSellUserId();
        String subOrderId = appeal.getSubOrderId();
        String coinId = offlineOrder.getCoinId();

        //给卖家恢复资产 lockVolume -> advertVolume
        //查询该笔交易涉及的订单
        if (offlineOrderDetail.getStatus().toString().equals("9")) {
            throw new ServiceException("该订单已取消");
        }
        // 1、更新买家/卖家订单状态
        Integer status = Integer.parseInt(OfflineOrderDetailStatusEnum.CANCEL.getCode());
        long count = offlineOrderDetailDao.updateStatusBySubOrderId(status, subOrderId);
        if (count != 2) {
            throw new ServiceException("更新失败！");
        }

        BigDecimal exVolume = new BigDecimal(offlineOrderDetail.getVolume());
        BigDecimal lockVolume = new BigDecimal(offlineOrder.getLockVolume()).subtract(exVolume);
        if (lockVolume.compareTo(BigDecimal.ZERO) == -1) {
            throw new ServiceException("锁定个数有误");
        }
        // 2、更新卖家广告锁定个数
        count = offlineOrderDao.updateLockVolumeById(offlineOrder.getId(), lockVolume, new Timestamp(offlineOrder.getUpdateDate().getTime()));
        if (count != 1) {
            throw new ServiceException("更新失败！");
        }
        OfflineCoinVolume sellCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(appeal.getSellUserId(), offlineOrder.getCoinId()); // C2C卖家账户资金
        BigDecimal advertVolume = new BigDecimal(sellCoinVolume.getAdvertVolume()).add(exVolume);// 广告冻结
        lockVolume = new BigDecimal(sellCoinVolume.getLockVolume()).subtract(exVolume);// 交易冻结
        if (lockVolume.compareTo(BigDecimal.ZERO) == -1) {
            throw new ServiceException("资金账户交易锁个数有误");
        }
        if (offlineOrder.getStatus().equals(OfflineOrderStatusEnum.CANCEL.getCode()) || offlineOrder.getStatus().toString().equals(OfflineOrderStatusEnum.PART_CANCEL.getCode())) {//更新卖家C2C资金账户交易锁定和广告锁定
            BigDecimal volume = new BigDecimal(sellCoinVolume.getVolume()).add(exVolume);
            count = offlineCoinVolumeDao.updateVolumeAndLockVolume(sellCoinVolume.getUserId(), coinId, volume, lockVolume, new Timestamp(sellCoinVolume.getUpdateDate().getTime()), sellCoinVolume.getVersion());
            if (count != 1) {
                throw new ServiceException("更新失败！");
            }
        } else {
            // 3、更新卖家C2C资金账户交易锁定和广告锁定
            count = offlineCoinVolumeDao.updateAdvertVolumeAndLockVolume(sellCoinVolume.getUserId(), coinId, advertVolume, lockVolume, new Timestamp(sellCoinVolume.getUpdateDate().getTime()), sellCoinVolume.getVersion());
            if (count != 1) {
                throw new ServiceException("更新失败！");
            }
        }
    }

    private void cancelBuy2Seller(OfflineAppeal appeal, OfflineOrder offlineOrder, OfflineOrderDetail offlineOrderDetail) {
        //将订单详情状态改为取消
        String buyUserId = appeal.getBuyUserId();
        String subOrderId = appeal.getSubOrderId();
        String coinId = offlineOrder.getCoinId();

        //给卖家恢复资产 lockVolume -
        //查询该笔交易涉及的订单
        if (offlineOrderDetail.getStatus().toString().equals("9")) {
            throw new ServiceException("订单状态有误");
        }
        // 1、更新买家卖家订单状态 防止重复取消
        Integer status = Integer.parseInt(OfflineOrderDetailStatusEnum.CANCEL.getCode());
        long count = offlineOrderDetailDao.updateStatusBySubOrderId(status, subOrderId);
        if (count != 2) {
            throw new ServiceException("更新失败！");
        }

        //还原广告的lockVolume
        BigDecimal resetVolume = new BigDecimal(offlineOrder.getLockVolume()).subtract(new BigDecimal(offlineOrderDetail.getVolume()));
        if (resetVolume.compareTo(BigDecimal.ZERO) == -1) {
            throw new ServiceException("广告数量有误");
        }
        // 2、更新买家广告的lock数量
        count = offlineOrderDao.updateLockVolumeById(offlineOrder.getId(), resetVolume, new Timestamp(offlineOrder.getUpdateDate().getTime()));
        if (count != 1) {
            throw new ServiceException("更新失败！");
        }

        OfflineCoinVolume sellCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(appeal.getSellUserId(), offlineOrder.getCoinId()); // C2C卖家账户资金
        //  此处无需判断为空 卖出方一定已经有记录了
        BigDecimal exVolume = new BigDecimal(offlineOrderDetail.getVolume());
        BigDecimal lockVolume = new BigDecimal(sellCoinVolume.getLockVolume()).subtract(exVolume);
        if (lockVolume.compareTo(BigDecimal.ZERO) == -1) {
            throw new ServiceException("卖家C2C资金账户锁定数量有误");
        }
        BigDecimal volume = new BigDecimal(sellCoinVolume.getVolume()).add(exVolume);

        String sellUserId = appeal.getSellUserId();
        // 3、更新卖家的C2C账户的交易锁定数量
        count = offlineCoinVolumeDao.updateVolumeAndLockVolume(sellCoinVolume.getUserId(), coinId, volume, lockVolume, new Timestamp(sellCoinVolume.getUpdateDate().getTime()), sellCoinVolume.getVersion());
        if (count != 1) {
            throw new ServiceException("更新失败！");
        }
    }
}