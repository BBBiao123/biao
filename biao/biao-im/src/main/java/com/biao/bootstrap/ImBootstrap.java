package com.biao.bootstrap;

import com.biao.kafka.ImConsumer;
import com.biao.listener.ImGroupListener;
import com.biao.processor.HandshakeProcessor;
import com.biao.processor.LoginServiceProcessor;
import org.jim.common.ImConfig;
import org.jim.common.config.PropertyImConfigBuilder;
import org.jim.common.packets.Command;
import org.jim.server.ImServerStarter;
import org.jim.server.command.CommandManager;
import org.jim.server.command.handler.HandshakeReqHandler;
import org.jim.server.command.handler.LoginReqHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * ImBootstrap.
 *
 *  ""(Myth)
 */
@Component
public class ImBootstrap implements CommandLineRunner {

    @Value("${jimFile:jim.properties}")
    private String jimFile;

    @Override
    public void run(String... args) {
        ImConfig imConfig = new PropertyImConfigBuilder(jimFile).build();
        //设置群组监听器，非必须，根据需要自己选择性实现;
        imConfig.setImGroupListener(new ImGroupListener());
        ImServerStarter imServerStarter = new ImServerStarter(imConfig);
        HandshakeReqHandler handshakeReqHandler = CommandManager.getCommand(Command.COMMAND_HANDSHAKE_REQ, HandshakeReqHandler.class);
        //添加自定义握手处理器;
        handshakeReqHandler.addProcessor(new HandshakeProcessor());
        LoginReqHandler loginReqHandler = CommandManager.getCommand(Command.COMMAND_LOGIN_REQ, LoginReqHandler.class);
        //添加登录业务处理器;
        loginReqHandler.addProcessor(new LoginServiceProcessor());
        ImConsumer.setImConfig(CommandManager.getImConfig());
        ImConsumer.setMesssageHelper(CommandManager.getImConfig().getMessageHelper());
        try {
            imServerStarter.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
