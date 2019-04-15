package com.thinkgem.jeesite.modules.sys.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.CookieRememberMeManager;

import com.thinkgem.jeesite.common.utils.JedisUtils;

public class CustomerRememberMeManager extends CookieRememberMeManager{

	@Override
	public void onSuccessfulLogin(Subject subject, AuthenticationToken token, AuthenticationInfo info) {
		//清空登录次数限制
		UsernamePasswordToken authcToken = (UsernamePasswordToken) token;
		String redisLoginIncreKey = SystemAuthorizingRealm.buildLoginKey(authcToken.getUsername());
		JedisUtils.del(redisLoginIncreKey);
		super.onSuccessfulLogin(subject, token, info);
	}
    	
}
