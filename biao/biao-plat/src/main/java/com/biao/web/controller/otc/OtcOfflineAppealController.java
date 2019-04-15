package com.biao.web.controller.otc;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.entity.otc.OtcOfflineAppeal;
import com.biao.enums.OfflineAppealEnum;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.otc.OtcOfflineAppealService;
import com.biao.vo.OfflineAppealVO;
import com.biao.web.controller.OfflineAppealController;
import com.biao.web.valid.ValidateFiled;
import com.biao.web.valid.ValidateGroup;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * OTC申诉
 *
 *  ""ong
 */
@RestController
@RequestMapping("/biao")
public class OtcOfflineAppealController {

    private static final Logger logger = LoggerFactory.getLogger(OfflineAppealController.class);

    @Autowired
    private OtcOfflineAppealService appealService;

    /**
     * 检查该订单是否能申诉。如果该订单已经有（进行中/处理完成）申诉单则不能重复申诉
     *
     * @param offlineAppealVO
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "subOrderId", notNull = true, errMsg = "申诉的C2C订单不能为空")
    })
//    @GetMapping("/otc/appeal/check")
    public Mono<GlobalMessageResponseVo> checkAppeal(OfflineAppealVO offlineAppealVO) {

        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    // 根据订单ID查询（进行中/处理完成）中的申诉单 
                    List<OtcOfflineAppeal> appealList = appealService.findBySubOrderId(offlineAppealVO.getSubOrderId());
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
    @PostMapping("/otc/appeal/doappeal")
    public Mono<GlobalMessageResponseVo> doAppeal(OfflineAppealVO offlineAppealVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    // 根据订单ID查询（进行中/处理完成）中的申诉单
                    List<OtcOfflineAppeal> appealList = appealService.findBySubOrderId(offlineAppealVO.getSubOrderId());
                    if (CollectionUtils.isNotEmpty(appealList)) {// 如果存在关联的申诉单则不能重复申诉
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("该订单已经被申诉"));
                    } else {
                        OtcOfflineAppeal appeal = new OtcOfflineAppeal();
                        appeal.setAppealUserId(user.getId());
                        appeal.setAppealMail(user.getMail());
                        appeal.setAppealMobile(user.getMobile());
                        appeal.setAppealRealName(user.getRealName());
//                        appeal.setAppealIdCard(user.getIdCard());
                        appeal.setSubOrderId(offlineAppealVO.getSubOrderId());
                        appeal.setStatus(OfflineAppealEnum.ING.getCode());
                        appeal.setAppealType(offlineAppealVO.getAppealType());
                        appeal.setReason(offlineAppealVO.getReason());
                        appeal.setPublishSource(user.getLoginSource());
                        appealService.save(appeal);
                        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(appeal));
                    }
                });
    }

    /**
     * 查询我的所有的申诉单
     *
     * @param offlineAppealVO
     * @return
     */
//    @GetMapping("/otc/appeal/findall")
    public Mono<GlobalMessageResponseVo> findAllAppeal(OfflineAppealVO offlineAppealVO) {

        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    // 根据用户ID查询我的所有申诉单
                    List<OtcOfflineAppeal> appealList = appealService.findAllAppeal(user.getId(), user.getLoginSource());
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
//    @PostMapping("/otc/appeal/cancel")
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
