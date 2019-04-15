package com.biao.service.impl.otc;

import com.biao.constant.Constants;
import com.biao.entity.PlatUser;
import com.biao.entity.otc.OtcOfflineAppeal;
import com.biao.entity.otc.OtcOfflineOrder;
import com.biao.entity.otc.OtcOfflineOrderDetail;
import com.biao.enums.OfflineAppealEnum;
import com.biao.enums.OfflineOrderDetailStatusEnum;
import com.biao.enums.TradeEnum;
import com.biao.exception.PlatException;
import com.biao.mapper.PlatUserDao;
import com.biao.mapper.otc.OtcOfflineAppealDao;
import com.biao.mapper.otc.OtcOfflineOrderDao;
import com.biao.mapper.otc.OtcOfflineOrderDetailDao;
import com.biao.service.otc.OtcOfflineAppealService;
import com.biao.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OtcOfflineAppealServiceImpl implements OtcOfflineAppealService {
    @Autowired
    private OtcOfflineAppealDao offlineAppealDao;

    @Autowired
    private OtcOfflineOrderDetailDao offlineOrderDetailDao;

    @Autowired
    private OtcOfflineOrderDao offlineOrderDao;

    @Autowired
    private PlatUserDao platUserDao;

    /**
     * 根据OTC订单号查询关联的申诉单
     *
     * @param subOrderId
     * @return
     */
    public List<OtcOfflineAppeal> findBySubOrderId(String subOrderId) {
        return offlineAppealDao.findBySubOrderId(subOrderId);
    }

    /**
     * 保存申诉单
     *
     * @param offlineAppeal
     * @return
     */
    public String save(OtcOfflineAppeal offlineAppeal) {
        offlineAppeal.setId(SnowFlake.createSnowFlake().nextIdString());
        OtcOfflineOrderDetail orderDetail = offlineOrderDetailDao.findByUserIdSubOrderId(offlineAppeal.getAppealUserId(), offlineAppeal.getSubOrderId(), offlineAppeal.getPublishSource());
        if (orderDetail == null || !orderDetail.getUserId().equals(offlineAppeal.getAppealUserId())) {// 订单为空，或者订单和当前申诉人不是同一人则抛错
            throw new PlatException(6000, "订单号错误!");
        }
        if (!offlineAppeal.getPublishSource().equals(orderDetail.getPublishSource())) {
            throw new PlatException(6000, "申诉渠道错误!");
        }
        OtcOfflineOrder order = offlineOrderDao.findById(orderDetail.getOrderId());// 广告
        // 买家申诉条件：订单状态必须是已付款。卖家申诉条件：订单状态是未付款或已付款
        if (OfflineOrderDetailStatusEnum.NOMAL.getCode().equals(String.valueOf(orderDetail.getStatus()))) {// 未付款时，买家卖家都不允许申诉。订单会在30分钟内自动取消
            throw new PlatException(60001, "买家确认已付款后才能申诉");
        } else if (!OfflineOrderDetailStatusEnum.CONFIRM_PAY.getCode().equals(String.valueOf(orderDetail.getStatus()))) {// 除已付款状态外，其他状态都不允许申诉
            throw new PlatException(60001, "该订单状态下不能申诉");
        }
        // 设置买家卖家用户ID
        if (String.valueOf(TradeEnum.BUY.ordinal()).equals(order.getExType())) {// 广告是买入，设置申诉单的买家卖家userid
            offlineAppeal.setBuyUserId(order.getUserId());
            if (!order.getUserId().equals(orderDetail.getUserId())) {
                offlineAppeal.setSellUserId(orderDetail.getUserId());
            } else if (!order.getUserId().equals(orderDetail.getAskUserId())) {
                offlineAppeal.setSellUserId(orderDetail.getAskUserId());
            } else {
                throw new PlatException(60001, "数据异常");
            }
        }
        if (String.valueOf(TradeEnum.SELL.ordinal()).equals(order.getExType())) {// 广告是卖出，设置申诉单的买家卖家userid
            offlineAppeal.setSellUserId(order.getUserId());
            if (!order.getUserId().equals(orderDetail.getUserId())) {
                offlineAppeal.setBuyUserId(orderDetail.getUserId());
            } else if (!order.getUserId().equals(orderDetail.getAskUserId())) {
                offlineAppeal.setBuyUserId(orderDetail.getAskUserId());
            } else {
                throw new PlatException(60001, "数据异常");
            }
        }
        fillSellBuyUserInfo(offlineAppeal);// 冗余买家卖家用户详细信息
        offlineAppealDao.insert(offlineAppeal);
        return offlineAppeal.getId();
    }

    /**
     * 冗余买家卖家用户详细信息
     *
     * @param offlineAppeal
     */
    private void fillSellBuyUserInfo(OtcOfflineAppeal offlineAppeal) {
        PlatUser sellUser = platUserDao.findById(offlineAppeal.getSellUserId());
        offlineAppeal.setSellMail(sellUser.getMail());
        offlineAppeal.setSellMobile(sellUser.getMobile());
        offlineAppeal.setSellRealName(sellUser.getRealName());
        offlineAppeal.setSellIdCard(sellUser.getIdCard());
        PlatUser buyUser = platUserDao.findById(offlineAppeal.getBuyUserId());
        offlineAppeal.setBuyMail(buyUser.getMail());
        offlineAppeal.setBuyMobile(buyUser.getMobile());
        offlineAppeal.setBuyRealName(buyUser.getRealName());
        offlineAppeal.setBuyIdCard(buyUser.getIdCard());

        if (offlineAppeal.getAppealUserId().equals(offlineAppeal.getSellUserId())) {
            offlineAppeal.setAppealIdCard(sellUser.getIdCard());
        }
        if (offlineAppeal.getAppealUserId().equals(offlineAppeal.getBuyUserId())) {
            offlineAppeal.setAppealIdCard(buyUser.getIdCard());
        }
    }

    /**
     * 根据用户ID查询用户下所有的申诉单
     *
     * @param userId
     * @return
     */
    public List<OtcOfflineAppeal> findAllAppeal(String userId, String loginSource) {
        return offlineAppealDao.findAllAppeal(userId, loginSource);
    }

    /**
     * 根据用户ID和申诉单ID撤销申诉单
     *
     * @param userId
     * @param appealId
     */
    public void cancelAppeal(String userId, String appealId) {
        OtcOfflineAppeal appeal = offlineAppealDao.findByIdAndUserId(userId, appealId);
        if (Objects.isNull(appeal)) {
            throw new PlatException(Constants.PARAM_ERROR, "申诉的参数错误");
        }
        if (OfflineAppealEnum.DONE.getCode().equals(appeal.getStatus())) {
            throw new PlatException(60003, "该申诉单客服已处理完成，若有疑问请联系客服，谢谢！");
        }
        if (OfflineAppealEnum.CANCEL.getCode().equals(appeal.getStatus())) {
            throw new PlatException(60004, "该申诉单已撤销！");
        }
        int record = offlineAppealDao.cancelAppeal(userId, appealId);
        if (record < 1) {
            throw new PlatException(60005, "申诉单状态有误，无法取消");
        }
    }

}
