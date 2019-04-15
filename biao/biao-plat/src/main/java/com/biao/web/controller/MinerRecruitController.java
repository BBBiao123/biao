package com.biao.web.controller;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.entity.MkMinerRecruit;
import com.biao.entity.PlatUser;
import com.biao.entity.UserCoinVolume;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.pojo.RequestQuery;
import com.biao.pojo.ResponsePage;
import com.biao.service.MinerRecruitService;
import com.biao.service.PlatUserService;
import com.biao.service.UserCoinVolumeExService;
import com.biao.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/biao")
public class MinerRecruitController {

    private static String MINER_RECRUIT_END_TIME = "2018-10-31 23:59:59";

    @Autowired
    private PlatUserService platUserService;

    @Autowired
    private MinerRecruitService minerRecruitService;

    @Autowired(required = false)
    private UserCoinVolumeExService userCoinVolumeService;

    private static Logger logger = LoggerFactory.getLogger(MinerRecruitController.class);

    @PostMapping("/miner/recruit/add")
    public Mono<GlobalMessageResponseVo> recruitAdd() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {

                    try {
                        LocalDateTime endDateTime = LocalDateTime.now();
                        LocalDateTime actEndDateTime = DateUtils.parseLocalDateTime(MINER_RECRUIT_END_TIME);
                        if (endDateTime.isAfter(actEndDateTime)) {
                            return Mono.just(GlobalMessageResponseVo.newErrorInstance("矿主招募活动已结束！"));
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("系统异常！"));
                    }

                    PlatUser platUser = platUserService.findById(user.getId());
                    if (ObjectUtils.isEmpty(platUser)) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("操作非法！"));
                    }

                    if (!"1".equals(String.valueOf(platUser.getCardStatus()))) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("矿主必须实名认证"));
                    }

                    if (ObjectUtils.isEmpty(platUser.getAge()) || platUser.getAge() < 18) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("矿主必须满18岁！"));
                    }

                    MkMinerRecruit mkMinerRecruit = minerRecruitService.findUserId(user.getId());
                    if (!ObjectUtils.isEmpty(mkMinerRecruit)) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("矿主只能报名一次！"));
                    }

                    UserCoinVolume volume = userCoinVolumeService.findByUserIdAndCoinSymbol(platUser.getId(), "UES");
                    if (ObjectUtils.isEmpty(volume) || StringUtils.isEmpty(volume.getUserId())) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("UES资产不能为空！"));
                    }

                    if (volume.getVolume().compareTo(new BigDecimal("5000")) < 0) {
                        return Mono.just(GlobalMessageResponseVo.newErrorInstance("UES持币必须在5000以上！"));
                    }

                    minerRecruitService.recruitAdd(platUser, volume);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("矿主招募成功"));
                });
    }

    @PostMapping("/miner/recruit/list")
    public Mono<GlobalMessageResponseVo> list(RequestQuery requestQuery) {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(minerRecruitService.findPage(requestQuery)));
    }

    @GetMapping("/miner/recruit/my")
    public Mono<GlobalMessageResponseVo> my(RequestQuery requestQuery) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {

                    MkMinerRecruit mkMinerRecruit = minerRecruitService.findUserId(user.getId());
                    ResponsePage<MkMinerRecruit> responsePage = new ResponsePage<>();

                    if (ObjectUtils.isEmpty(mkMinerRecruit)) {
                        responsePage.setCount(0L);
                        responsePage.setList(null);
                    } else {
                        if (StringUtils.isEmpty(mkMinerRecruit.getMail())) {
                            mkMinerRecruit.setMail(mkMinerRecruit.getMobile());
                        }
                        List<MkMinerRecruit> mkMinerRecruitList = new ArrayList<>();
                        mkMinerRecruitList.add(mkMinerRecruit);
                        responsePage.setList(mkMinerRecruitList);
                        responsePage.setCount(1L);
                    }
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(responsePage));
                });
    }

}
