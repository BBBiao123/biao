package com.biao.service.impl;

import com.biao.entity.OfflineAppeal;
import com.biao.entity.OfflineOrder;
import com.biao.entity.OfflineOrderDetail;
import com.biao.entity.PlatUser;
import com.biao.enums.MessageTemplateCode;
import com.biao.enums.OfflineAppealEnum;
import com.biao.enums.OfflineOrderDetailStatusEnum;
import com.biao.enums.TradeEnum;
import com.biao.exception.PlatException;
import com.biao.kafka.interceptor.ImParamDTO;
import com.biao.mapper.OfflineAppealDao;
import com.biao.mapper.OfflineOrderDao;
import com.biao.mapper.OfflineOrderDetailDao;
import com.biao.mapper.PlatUserDao;
import com.biao.pojo.RequestQuery;
import com.biao.pojo.ResponsePage;
import com.biao.service.ImOrderService;
import com.biao.service.OfflineAppealService;
import com.biao.service.SmsMessageService;
import com.biao.util.SnowFlake;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfflineAppealServiceImpl implements OfflineAppealService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OfflineAppealServiceImpl.class);

    @Autowired
    private OfflineAppealDao offlineAppealDao;

    @Autowired
    private OfflineOrderDetailDao offlineOrderDetailDao;

    @Autowired
    private OfflineOrderDao offlineOrderDao;

    @Autowired
    private PlatUserDao platUserDao;

    @Autowired
    private ImOrderService orderService;

    @Autowired
    private SmsMessageService smsMessageService;


    /**
     * 根据c2c订单号查询关联的申诉单
     *
     * @param subOrderId
     * @return
     */
    public List<OfflineAppeal> findBySubOrderId(String subOrderId) {
        return offlineAppealDao.findBySubOrderId(subOrderId);
    }

    /**
     * 保存申诉单
     *
     * @param offlineAppeal
     * @return
     */
    public String save(OfflineAppeal offlineAppeal) {
        offlineAppeal.setId(SnowFlake.createSnowFlake().nextIdString());
        OfflineOrderDetail orderDetail = offlineOrderDetailDao.findByUserIdAndOrderId(offlineAppeal.getAppealUserId(), offlineAppeal.getSubOrderId());
        if (orderDetail == null || !orderDetail.getUserId().equals(offlineAppeal.getAppealUserId())) {// 订单为空，或者订单和当前申诉人不是同一人则抛错
            throw new PlatException(6000, "订单号错误!");
        }
        OfflineOrder order = offlineOrderDao.findById(orderDetail.getOrderId());// 广告
        // 买家申诉条件：订单状态必须是已付款。卖家申诉条件：订单状态是未付款或已付款
        if (OfflineOrderDetailStatusEnum.NOMAL.getCode().equals(String.valueOf(orderDetail.getStatus()))) {// 未付款时，买家卖家都不允许申诉。订单会在30分钟内自动取消
            throw new PlatException(60001, "买家确认已付款后才能申诉");
        } else if (!OfflineOrderDetailStatusEnum.CONFIRM_PAY.getCode().equals(String.valueOf(orderDetail.getStatus()))) {// 除已付款状态外，其他状态都不允许申诉
            throw new PlatException(60001, "该订单状态下不能申诉");
        }
        // 设置买家卖家用户ID
        if (TradeEnum.BUY.ordinal() == order.getExType()) {// 广告是买入，设置申诉单的买家卖家userid
            offlineAppeal.setBuyUserId(order.getUserId());
            if (!order.getUserId().equals(orderDetail.getUserId())) {
                offlineAppeal.setSellUserId(orderDetail.getUserId());
            } else if (!order.getUserId().equals(orderDetail.getAskUserId())) {
                offlineAppeal.setSellUserId(orderDetail.getAskUserId());
            } else {
                throw new PlatException(60001, "数据异常");
            }
        }
        if (TradeEnum.SELL.ordinal() == order.getExType()) {// 广告是卖出，设置申诉单的买家卖家userid
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
        appealSendSms(orderDetail.getAskUserMobile());// 发送短信

        ImParamDTO dto = new ImParamDTO();
        dto.setTwoSides(true);
        dto.setOrderId(orderDetail.getSubOrderId());
        dto.setFromUserId(orderDetail.getUserId());
        dto.setToUserId(orderDetail.getAskUserId());
        dto.setOrderDetailStatusEnum(OfflineOrderDetailStatusEnum.SHENSU);
        orderService.sendOfflineOrder(dto);
        return offlineAppeal.getId();
    }

    private void appealSendSms(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return;
        }
        try {
            smsMessageService.sendSms(mobile, MessageTemplateCode.MOBILE_APPEAL_TEMPLATE.getCode(), "");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.error("申诉通知对方发短信失败：{}", mobile);
        }

    }

    /**
     * 冗余买家卖家用户详细信息
     *
     * @param offlineAppeal
     */
    private void fillSellBuyUserInfo(OfflineAppeal offlineAppeal) {
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
     * @return
     */
    public ResponsePage<OfflineAppeal> findAllAppeal(RequestQuery requestQuery, String userId) {

        ResponsePage<OfflineAppeal> responsePage = new ResponsePage<>();
        Page<OfflineAppeal> page = PageHelper.startPage(requestQuery.getCurrentPage(), requestQuery.getShowCount());
        List<OfflineAppeal> appealList = offlineAppealDao.findAllAppeal(userId);
        responsePage.setList(appealList);
        responsePage.setCount(page.getTotal());
        return responsePage;
    }

    /**
     * 根据用户ID和申诉单ID撤销申诉单
     *
     * @param userId
     * @param appealId
     */
    public void cancelAppeal(String userId, String appealId) {
        OfflineAppeal appeal = offlineAppealDao.findByIdAndUserId(userId, appealId);
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
