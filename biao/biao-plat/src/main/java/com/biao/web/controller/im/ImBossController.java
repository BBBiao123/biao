package com.biao.web.controller.im;

import com.biao.enums.OfflineOrderDetailStatusEnum;
import com.biao.enums.OfflineOrderStatusEnum;
import com.biao.kafka.interceptor.ImParamDTO;
import com.biao.message.MessageManager;
import com.biao.service.ImOrderService;
import com.biao.service.push.OfflineOrderPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserRegisterLotteryController.
 *
 *  ""
 */
@RestController
@RequestMapping("/biao/im")
public class ImBossController {

    private final ImOrderService imOrderService;

    private final OfflineOrderPushService offlineOrderPushService;

    private final MessageManager messageManager;

    /**
     * Instantiates a new Im boss controller.
     *
     * @param imOrderService          the im order service
     * @param offlineOrderPushService the offline order push service
     */
    @Autowired
    public ImBossController(final ImOrderService imOrderService,
                            final OfflineOrderPushService offlineOrderPushService,
                            final MessageManager messageManager) {
        this.imOrderService = imOrderService;
        this.offlineOrderPushService = offlineOrderPushService;
        this.messageManager = messageManager;
    }


    /**
     * Buy string.
     *
     * @param toUserId   the to user id
     * @param fromUserId the from user id
     * @param orderId    the order id
     * @return the string
     */
    @RequestMapping("/buy")
    public String buy(@RequestParam("toUserId") String toUserId,
                      @RequestParam("fromUserId") String fromUserId,
                      @RequestParam("orderId") String orderId) {

        final int status = Integer.parseInt(OfflineOrderStatusEnum.PART_COMPLATE.getCode());
        offlineOrderPushService.pushWebsocket(toUserId, orderId, status);
        offlineOrderPushService.pushWebsocket(fromUserId, orderId, status);

        messageManager.saveAndPushMessage(toUserId, orderId,
                Integer.parseInt(OfflineOrderDetailStatusEnum.SHENSU_OVER_BUY.getCode()));

        messageManager.saveAndPushMessage(fromUserId, orderId,
                Integer.parseInt(OfflineOrderDetailStatusEnum.SHENSU_OVER_BUY.getCode()));

        ImParamDTO dto = new ImParamDTO();
        dto.setFromUserId(fromUserId);
        dto.setToUserId(toUserId);
        dto.setOrderId(orderId);
        dto.setOrderDetailStatusEnum(OfflineOrderDetailStatusEnum.SHENSU_OVER_BUY);
        dto.setTwoSides(Boolean.TRUE);
        //发送消息到im
        imOrderService.sendOfflineOrder(dto);
        return "success";
    }


    /**
     * Sell string.
     *
     * @param toUserId   the to user id
     * @param fromUserId the from user id
     * @param orderId    the order id
     * @return the string
     */
    @RequestMapping("/sell")
    public String sell(@RequestParam("toUserId") String toUserId,
                       @RequestParam("fromUserId") String fromUserId,
                       @RequestParam("orderId") String orderId) {

        final int status = Integer.parseInt(OfflineOrderStatusEnum.PART_COMPLATE.getCode());
        offlineOrderPushService.pushWebsocket(toUserId, orderId, status);
        offlineOrderPushService.pushWebsocket(fromUserId, orderId, status);

        messageManager.saveAndPushMessage(toUserId, orderId, Integer.parseInt(OfflineOrderDetailStatusEnum.SHENSU_OVER_SELL.getCode()));

        messageManager.saveAndPushMessage(fromUserId, orderId, Integer.parseInt(OfflineOrderDetailStatusEnum.SHENSU_OVER_SELL.getCode()));
        ImParamDTO dto = new ImParamDTO();
        dto.setFromUserId(fromUserId);
        dto.setToUserId(toUserId);
        dto.setOrderId(orderId);
        dto.setOrderDetailStatusEnum(OfflineOrderDetailStatusEnum.SHENSU_OVER_SELL);
        dto.setTwoSides(Boolean.TRUE);
        //发送消息到im
        imOrderService.sendOfflineOrder(dto);
        return "success";
    }


}