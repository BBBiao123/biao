package com.biao.web.controller;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.pojo.OfflineChangeVO;
import com.biao.service.OfflineChangeLogService;
import com.biao.service.OfflineRedEnvelopeService;
import com.biao.vo.OfflineChangeListVO;
import com.biao.vo.RedEnvelopeListVO;
import com.biao.vo.RedEnvelopeSendVO;
import com.biao.vo.RedEnvelopeViewVO;
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
public class OfflineRedEnvelopeController {

    @Autowired
    private OfflineRedEnvelopeService offlineRedEnvelopeService;

    @PostMapping("/red/envelope/send")
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "type", notNull = true, errMsg = "红包类型不能为空"),
            @ValidateFiled(index = 0, filedName = "volume", notNull = true, errMsg = "红包总额不能为空"),
            @ValidateFiled(index = 0, filedName = "totalNumber", notNull = true, errMsg = "红包个数不能为空"),
            @ValidateFiled(index = 0, filedName = "bestWith", notNull = true, errMsg = "祝福语不能为空"),
    })
    public Mono<GlobalMessageResponseVo> send(RedEnvelopeSendVO redEnvelopeSendVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {

                    final boolean lockTrade = user.isLockTrade();
                    if (lockTrade) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("用户被锁定不能发红包"));
                    }

                    final Integer cardLevel = user.getCardLevel();
                    if(cardLevel != 2){
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("身份证通过审核才能发红包"));
                    }
                    redEnvelopeSendVO.setUserId(user.getId());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(offlineRedEnvelopeService.send(redEnvelopeSendVO)));
                });

    }

    @GetMapping("/red/envelope/view")
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "redEnvelopeId", notNull = true, errMsg = "红包id不能为空"),
    })
    public Mono<GlobalMessageResponseVo> view(RedEnvelopeViewVO redEnvelopeViewVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    redEnvelopeViewVO.setUserId(user.getId());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(offlineRedEnvelopeService.view(redEnvelopeViewVO)));
                });
    }

    /**
     * 拆红包
     *
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "redEnvelopeId", notNull = true, errMsg = "红包id不能为空"),
    })
    @PostMapping("/red/envelope/open")
    public Mono<GlobalMessageResponseVo> open(RedEnvelopeViewVO redEnvelopeViewVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    redEnvelopeViewVO.setUserId(user.getId());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(offlineRedEnvelopeService.open(redEnvelopeViewVO)));
                });
    }


    /**
     * 红包详情
     *
     * @return
     */
    @GetMapping("/red/envelope/detail/{envelopeId}")
    public Mono<GlobalMessageResponseVo> detail(@PathVariable("envelopeId") String envelopeId) {
        if (StringUtils.isEmpty(envelopeId)) {
            return Mono.just(GlobalMessageResponseVo.newErrorInstance("参数非法"));
        }
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(offlineRedEnvelopeService.detail(envelopeId, user.getId())));
                });
    }

    /**
     * 我的发送红包主体信息
     *
     * @return
     */
    @GetMapping("/red/envelope/mySendInfo")
    public Mono<GlobalMessageResponseVo> mySendInfo() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    RedEnvelopeListVO redEnvelopeListVO = new RedEnvelopeListVO();
                    redEnvelopeListVO.setUserId(user.getId());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(offlineRedEnvelopeService.findMySendInfo(redEnvelopeListVO)));
                });
    }

    /**
     * 我的发送红包列表
     *
     * @return
     */
    @GetMapping("/red/envelope/mySendList")
    public Mono<GlobalMessageResponseVo> mySendList(RedEnvelopeListVO redEnvelopeListVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    redEnvelopeListVO.setUserId(user.getId());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(offlineRedEnvelopeService.findMySendList(redEnvelopeListVO)));
                });
    }

    /**
     * 我的接收红包主体信息
     *
     * @return
     */
    @GetMapping("/red/envelope/myReceiveInfo")
    public Mono<GlobalMessageResponseVo> findMyReceiveInfo() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    RedEnvelopeListVO redEnvelopeListVO = new RedEnvelopeListVO();
                    redEnvelopeListVO.setUserId(user.getId());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(offlineRedEnvelopeService.findMyReceiveInfo(redEnvelopeListVO)));
                });
    }

    /**
     * 我的接收红包列表
     *
     * @return
     */
    @GetMapping("/red/envelope/myReceiveList")
    public Mono<GlobalMessageResponseVo> myReceiveList(RedEnvelopeListVO redEnvelopeListVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    redEnvelopeListVO.setUserId(user.getId());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(offlineRedEnvelopeService.findMyReceiveList(redEnvelopeListVO)));
                });
    }

    /**
     *
     *
     * @return
     */
    @GetMapping("/red/envelope/conf")
    public Mono<GlobalMessageResponseVo> conf() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    RedEnvelopeListVO redEnvelopeListVO = new RedEnvelopeListVO();
                    redEnvelopeListVO.setUserId(user.getId());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(offlineRedEnvelopeService.getRedEnvelopeConf()));
                });
    }
}

