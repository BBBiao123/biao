package com.biao.processor;

import org.jim.common.ImStatus;
import org.jim.common.packets.Command;
import org.jim.common.packets.LoginReqBody;
import org.jim.common.packets.LoginRespBody;
import org.jim.common.packets.User;
import org.jim.server.command.handler.processor.login.LoginProcessorIntf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;

/**
 *  ""
 */
public class LoginServiceProcessor implements LoginProcessorIntf {

    private Logger logger = LoggerFactory.getLogger(LoginServiceProcessor.class);

    /**
     * doLogin方法注意：J-IM登陆命令是根据user是否为空判断是否登陆成功.
     * <p>
     * 当登陆失败时设置user属性需要为空，相反登陆成功user属性是必须非空的;
     */
    @Override
    public LoginRespBody doLogin(LoginReqBody loginReqBody, ChannelContext channelContext) {
        String loginName = loginReqBody.getLoginname();
        User user = new User();
        user.setId(loginName);
        user.setStatus("on");
        return new LoginRespBody(Command.COMMAND_LOGIN_RESP, ImStatus.C10007, user);
    }

    @Override
    public void onSuccess(ChannelContext channelContext) {
        logger.info("登录成功回调方法");
    }

    @Override
    public boolean isProtocol(ChannelContext channelContext) {
        return true;
    }

    @Override
    public String name() {
        return "default";
    }
}
