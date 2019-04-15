package com.biao.config.sercurity;


import com.biao.config.UserConfig;
import com.biao.entity.PlatUser;
import com.biao.enums.UserStatusEnum;
import com.biao.service.PlatUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    PlatUserService platUserService;
    @Autowired
    private UserConfig userConfig;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        PlatUser platUser = platUserService.findByLoginName(username);
        RedisSessionUser sessionUser = null;
        if (platUser != null) {
            sessionUser = RedisSessionUser.converRedisSessionUser(platUser, userConfig);
            //判断用户是否锁定
            if (platUser.getStatus() == null) {
                //throw new DisabledException(messages.getMessage("AccountStatusUserDetailsChecker.disabled", "UserTest is disabled"));
                throw new DisabledException("用户账号被禁用");
            }
            if (UserStatusEnum.USER_LOCK.getCode().equals(platUser.getStatus().toString())) {
                //throw new LockedException(messages.getMessage("AccountStatusUserDetailsChecker.locked", "UserTest account is locked"));
                throw new LockedException("用户账号被锁定");
            }
            if (UserStatusEnum.USER_DISABLE.getCode().equals(platUser.getStatus().toString())) {
                //throw new DisabledException(messages.getMessage("AccountStatusUserDetailsChecker.disabled", "UserTest is disabled"));
                throw new DisabledException("用户账号不可用");
            }
            //设置用户的角色权限
            List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
            //查询用户权限
            sessionUser.setAuthorities(auths);
            sessionUser.setToken(UUID.randomUUID().toString().replaceAll("-", ""));
            return Mono.just(sessionUser);
        }
        throw new UsernameNotFoundException("用户不存在");
    }

}
