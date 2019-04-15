package com.biao.web.controller;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.entity.OfflineAppeal;
import com.biao.enums.OfflineAppealEnum;
import com.biao.enums.OfflineOrderDetailStatusEnum;
import com.biao.message.MessageManager;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.pojo.RequestQuery;
import com.biao.pojo.ResponsePage;
import com.biao.service.OfflineAppealService;
import com.biao.vo.OfflineAppealVO;
import com.biao.web.valid.ValidateFiled;
import com.biao.web.valid.ValidateGroup;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * c2c申诉
 *
 *  ""ong
 */
@RestController
@RequestMapping("/biao")
@SuppressWarnings("all")
public class OfflineAppealController {

    private static final Logger logger = LoggerFactory.getLogger(OfflineAppealController.class);

    @Autowired
    private OfflineAppealService appealService;

    @Autowired
    private MessageManager messageManager;


    /**
     * 检查该订单是否能申诉。如果该订单已经有（进行中/处理完成）申诉单则不能重复申诉
     *
     * @param offlineAppealVO
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "subOrderId", notNull = true, errMsg = "申诉的C2C订单不能为空")
    })
    @GetMapping("/offline/appeal/check")
    public Mono<GlobalMessageResponseVo> checkAppeal(OfflineAppealVO offlineAppealVO) {

        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    // 根据订单ID查询（进行中/处理完成）中的申诉单
                    List<OfflineAppeal> appealList = appealService.findBySubOrderId(offlineAppealVO.getSubOrderId());
                    if (CollectionUtils.isNotEmpty(appealList)) {// 如果存在关联的申诉单则不能重复申诉
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("该订单已经被申诉"));
                    }
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("ok"));
                });
    }

    /**
     * 保存申诉单
     *
     * @param offlineAppealVO
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "subOrderId", notNull = true, errMsg = "申诉的C2C订单不能为空"),
            @ValidateFiled(index = 0, filedName = "appealType", notNull = true, errMsg = "申诉类型不能为空"),
            @ValidateFiled(index = 0, filedName = "reason", notNull = true, errMsg = "申诉理由不能为空")
    })
    @PostMapping("/offline/appeal/doappeal")
    public Mono<GlobalMessageResponseVo> doAppeal(OfflineAppealVO offlineAppealVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    // 根据订单ID查询（进行中/处理完成）中的申诉单
                    List<OfflineAppeal> appealList = appealService.findBySubOrderId(offlineAppealVO.getSubOrderId());
                    if (CollectionUtils.isNotEmpty(appealList)) {// 如果存在关联的申诉单则不能重复申诉
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("该订单已经被申诉"));
                    } else {
                        OfflineAppeal appeal = new OfflineAppeal();
                        appeal.setAppealUserId(user.getId());
                        appeal.setAppealMail(user.getMail());
                        appeal.setAppealMobile(user.getMobile());
                        appeal.setAppealRealName(user.getRealName());
//                        appeal.setAppealIdCard(user.getIdCard());
                        appeal.setSubOrderId(offlineAppealVO.getSubOrderId());
                        appeal.setStatus(OfflineAppealEnum.ING.getCode());
                        appeal.setAppealType(offlineAppealVO.getAppealType());
                        appeal.setReason(offlineAppealVO.getReason());
                        appeal.setImagePath(offlineAppealVO.getImagePath());
                        appeal.setImagePath2(offlineAppealVO.getImagePath2());
                        appeal.setImagePath3(offlineAppealVO.getImagePath3());
                        appeal.setSyncKey(offlineAppealVO.getSubOrderId());
                        appealService.save(appeal);
                        messageManager.saveAndPushMessage(user.getId(), offlineAppealVO.getSubOrderId(), Integer.parseInt(OfflineOrderDetailStatusEnum.SHENSU.getCode()));
                        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(appeal));
                    }
                });
    }

    /**
     * 查询我的所有的申诉单
     *
     * @return
     */
    @GetMapping("/offline/appeal/findall")
    public Mono<GlobalMessageResponseVo> findAllAppeal(RequestQuery requestQuery) {

        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    // 根据用户ID查询我的所有申诉单
                    ResponsePage<OfflineAppeal> appealList = appealService.findAllAppeal(requestQuery, user.getId());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(appealList));
                });
    }

    /**
     * 撤销我的申诉单
     *
     * @param offlineAppealVO
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "appealId", notNull = true, errMsg = "申诉单ID不能为空"),
    })
    @PostMapping("/offline/appeal/cancel")
    public Mono<GlobalMessageResponseVo> cAppeal(OfflineAppealVO offlineAppealVO) {

        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    // 根据用户ID和申诉单ID撤销
                    appealService.cancelAppeal(user.getId(), offlineAppealVO.getAppealId());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("ok"));
                });
    }
}
