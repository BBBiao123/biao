package com.biao.web.controller.otc;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.otc.OtcAccountSecretService;
import com.biao.service.otc.OtcAgentService;
import com.biao.vo.otc.OtcAgentVO;
import com.biao.web.valid.ValidateFiled;
import com.biao.web.valid.ValidateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * OTC银商
 *
 *  ""ong
 */
@RestController
@RequestMapping("/biao")
public class OtcAgentController {

    @Autowired
    private OtcAgentService otcAgentService;

    @Autowired
    private OtcAccountSecretService otcAccountSecretService;

    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "tag", notNull = true, errMsg = "银商组代码不能为空"),
    })
    @GetMapping("/otc/agent/grouplist")
    public Mono<GlobalMessageResponseVo> findGroupList(OtcAgentVO otcAgentVO) {

//        return ReactiveSecurityContextHolder.getContext()
//                .filter(c -> c.getAuthentication() != null)
//                .map(SecurityContext::getAuthentication)
//                .map(Authentication::getPrincipal)
//                .cast(RedisSessionUser.class).flatMap(user -> {
//                    otcAccountSecretService.checkIsMasterAccount(user.getId()); // 校验是否OTC总账户登录
                    Map<String, String> paramMap = new HashMap<>();
                    paramMap.put("tag", otcAgentVO.getTag());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcAgentService.findGroupAgent(otcAgentVO, paramMap)));
//                });
    }

    @GetMapping("/otc/agent/finduser")
    public Mono<GlobalMessageResponseVo> findUser(OtcAgentVO otcAgentVO) {

//        return ReactiveSecurityContextHolder.getContext()
//                .filter(c -> c.getAuthentication() != null)
//                .map(SecurityContext::getAuthentication)
//                .map(Authentication::getPrincipal)
//                .cast(RedisSessionUser.class).flatMap(user -> {
//                    otcAccountSecretService.checkIsMasterAccount(user.getId()); // 校验是否OTC总账户登录
                    Map<String, String> paramMap = new HashMap<>();
                    paramMap.put("mobile", otcAgentVO.getMobile());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcAgentService.findByMobileOrMail(otcAgentVO, paramMap)));
//                });
    }

    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, filedName = "tag", notNull = true, errMsg = "银商组代码不能为空"),
            @ValidateFiled(index = 0, filedName = "userId", notNull = true, errMsg = "用户ID不能为空"),
    })
    @PostMapping("/otc/agent/saveagent")
    public Mono<GlobalMessageResponseVo> saveAgent(OtcAgentVO otcAgentVO) {

//        return ReactiveSecurityContextHolder.getContext()
//                .filter(c -> c.getAuthentication() != null)
//                .map(SecurityContext::getAuthentication)
//                .map(Authentication::getPrincipal)
//                .cast(RedisSessionUser.class).flatMap(user -> {
//                    otcAccountSecretService.checkIsMasterAccount(user.getId()); // 校验是否OTC总账户登录
                    Map<String, String> paramMap = new HashMap<>();
                    paramMap.put("tag", otcAgentVO.getTag());
                    paramMap.put("userId", otcAgentVO.getUserId());
                    otcAgentService.saveAgent(otcAgentVO, paramMap);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("保存银商成功"));
//                });
    }

    /**
     * 查询我的银商组成员
     *
     * @return
     */
    @GetMapping("/otc/my/detailcount")
    public Mono<GlobalMessageResponseVo> countMyDetail() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(RedisSessionUser.class).flatMap(user -> {
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcAgentService.countAgentDetail(user.getId(), user.getLoginSource())));
                });
    }

    /**
     * 查询我的银商组成员信息和UES持币量
     *
     * @return
     */
    @GetMapping("/otc/my/groupagent")
    public Mono<GlobalMessageResponseVo> findMyGroupAgent() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(RedisSessionUser.class).flatMap(user -> {
                    OtcAgentVO otcAgentVO = new OtcAgentVO();
                    otcAgentVO.setTag(user.getTag());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcAgentService.findGroupAgentAndVolume(otcAgentVO)));
                });
    }
}
