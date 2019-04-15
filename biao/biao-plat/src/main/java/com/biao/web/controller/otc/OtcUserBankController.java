package com.biao.web.controller.otc;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.constant.Constants;
import com.biao.entity.otc.OtcUserBank;
import com.biao.enums.UserCardStatusEnum;
import com.biao.enums.UserTagEnum;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.otc.OtcUserBankService;
import com.biao.vo.otc.OtcUserBankVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * OTC用户支付设置相关操作
 */
@RestController
@RequestMapping("/biao")
public class OtcUserBankController {

    @Autowired
    private OtcUserBankService otcUserBankService;

    /**
     * 获取用户银行信息
     *
     * @param
     * @return
     */
    @GetMapping("/otc/bank/list")
    public Mono<GlobalMessageResponseVo> findList(OtcUserBankVO otcUserBankVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    OtcUserBank otcUserBank = new OtcUserBank();
                    BeanUtils.copyProperties(otcUserBankVO, otcUserBank);
                    otcUserBank.setUserId(user.getId());
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance(otcUserBankService.findByParam(otcUserBank)));
                });
    }

    /**
     * 绑定银行卡
     *
     * @param userBankVO
     * @return
     */
    @PostMapping("/otc/bank/bind")
    public Mono<GlobalMessageResponseVo> bind(OtcUserBankVO userBankVO) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    Mono<GlobalMessageResponseVo> checkResult = checkLegal(user, userBankVO);// 校验合法性
                    if (Objects.nonNull(checkResult)) {
                        return checkResult;
                    }
                    OtcUserBank userBank = new OtcUserBank();
                    BeanUtils.copyProperties(userBankVO, userBank);
                    userBank.setUserId(user.getId());
                    otcUserBankService.save(userBank);
                    return Mono.just(GlobalMessageResponseVo.newSuccessInstance("成功"));
                });
    }

    private Mono<GlobalMessageResponseVo> checkLegal(RedisSessionUser user, OtcUserBankVO otcUserBankVO) {

        if (StringUtils.isBlank(user.getTag()) || !user.getTag().contains(UserTagEnum.OTC_ADVERT.getCode())) { // 只有广告商添加银行卡才需要加如下限制，普通用户不需要
            return null;
        }
        if (!UserCardStatusEnum.authRealName(user.getCardStatus(),user.getCountryCode())) {
            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.IDENTIRY_ERROR, "请进行身份认证!"));
        }
        
        if (StringUtils.isBlank(user.getRealName())) {
            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.IDENTIRY_ERROR, "请退出重新登录"));
        }
        //增加绑定验证
        if (StringUtils.isBlank(otcUserBankVO.getRealName()) || !(otcUserBankVO.getRealName().trim().equals(user.getRealName().trim()))) {
            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.IDENTIRY_ERROR, "与身份证姓名不一致"));
        }
        return null;
    }

}
