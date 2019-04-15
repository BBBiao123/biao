package com.biao.web.controller;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.pojo.OfflineChangeVO;
import com.biao.service.OfflineChangeLogService;
import com.biao.vo.OfflineChangeListVO;
import com.biao.web.valid.ValidateFiled;
import com.biao.web.valid.ValidateGroup;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * c2c用户转账
 *
 *  ""zj
 */
@RestController
@RequestMapping("/biao")
public class OfflineChangeController {

    @Autowired
    private OfflineChangeLogService offlineChangeLogService;

    @PostMapping("/offline/change/myList")
    public Mono<GlobalMessageResponseVo> myList(OfflineChangeListVO offlineChangeListVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    offlineChangeListVO.setUserId(user.getId());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(offlineChangeLogService.findPage(offlineChangeListVO)));
                });

    }

    @GetMapping("/offline/change/detail/{changeLogId}")
    public Mono<GlobalMessageResponseVo> detail(@PathVariable("changeLogId") String changeLogId) {
        if (StringUtils.isEmpty(changeLogId)) {
            return Mono.just(GlobalMessageResponseVo.newErrorInstance("参数非法！"));
        }
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(offlineChangeLogService.findById(changeLogId)));
                });
    }

    /**
     * 前置检查
     *
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "coinId", notNull = true, errMsg = "币种id不能为空"),
    })
    @PostMapping("/offline/change/preCheck")
    public Mono<GlobalMessageResponseVo> preCheck(OfflineChangeVO offlineChangeVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    final boolean lockTrade = user.isLockTrade();
                    if (lockTrade) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("用户被锁定不能转账"));
                    }
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(offlineChangeLogService.preCheck(offlineChangeVO, user.getId())));
                });
    }


    /**
     * 转账确认信息
     *
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "coinId", notNull = true, errMsg = "币种id不能为空"),
            @ValidateFiled(index = 0, filedName = "toAccount", notNull = true, errMsg = "账户不能为空"),
            @ValidateFiled(index = 0, filedName = "volume", notNull = true, errMsg = "转账数量不能为空"),
    })
    @PostMapping("/offline/change/preConfirm")
    public Mono<GlobalMessageResponseVo> preConfirm(OfflineChangeVO offlineChangeVO) {
        if (offlineChangeVO.getVolume().compareTo(BigDecimal.ZERO) == -1) {
            return Mono.just(GlobalMessageResponseVo.newErrorInstance("转账数量非法"));
        }
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    final boolean lockTrade = user.isLockTrade();
                    if (lockTrade) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("用户被锁定不能转账"));
                    }
                    OfflineChangeVO changeVO = offlineChangeLogService.preConfirm(offlineChangeVO, user.getId());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(changeVO));
                });
    }

    /**
     * 确认转账
     *
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "changeNo", notNull = true, errMsg = "转账流水不能为空"),
            @ValidateFiled(index = 0, filedName = "coinId", notNull = true, errMsg = "币种id不能为空"),
            @ValidateFiled(index = 0, filedName = "volume", notNull = true, errMsg = "转账数量不能为空"),
            @ValidateFiled(index = 0, filedName = "toAccount", notNull = true, errMsg = "账户不能为空"),
            @ValidateFiled(index = 0, filedName = "realName", notNull = true, errMsg = "真实姓名不能为空"),
            @ValidateFiled(index = 0, filedName = "fee", notNull = true, errMsg = "费用不能为空"),
    })
    @PostMapping("/offline/change/confirm")
    public Mono<GlobalMessageResponseVo> confirm(OfflineChangeVO offlineChangeVO) {
        if (offlineChangeVO.getVolume().compareTo(BigDecimal.ZERO) == -1) {
            return Mono.just(GlobalMessageResponseVo.newErrorInstance("转入数量非法"));
        }
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    final boolean lockTrade = user.isLockTrade();
                    if (lockTrade) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("用户被锁定不能转账"));
                    }
                    offlineChangeLogService.confirm(offlineChangeVO, user.getId());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("转账成功！"));
                });
    }

}

