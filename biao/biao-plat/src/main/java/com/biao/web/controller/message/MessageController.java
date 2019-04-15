package com.biao.web.controller.message;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.query.MessageQuery;
import com.biao.reactive.data.mongo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * MessageController.
 *
 *  ""
 */
@RestController
@RequestMapping("/biao/message")
public class MessageController {


    private final MessageService messageService;

    /**
     * Instantiates a new Message controller.
     *
     * @param messageService the message service
     */
    @Autowired
    public MessageController(final MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Find by page mono.
     *
     * @param messageQuery the message query
     * @return the mono
     */
    @PostMapping("/findByPage")
    public Mono<GlobalMessageResponseVo> findByPage(MessageQuery messageQuery) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    String userId = user.getId();
                    messageQuery.setUserId(userId);
                    return Mono.just(GlobalMessageResponseVo
                            .newSuccessInstance(messageService.findByPage(messageQuery)));
                });
    }


    /**
     * Delete mono.
     *
     * @param messageId the message id
     * @return the mono
     */
    @RequestMapping("/delete")
    public Mono<GlobalMessageResponseVo> delete(@RequestParam("messageId") String messageId) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> {
                    messageService.removeById(messageId);
                    return Mono.just(GlobalMessageResponseVo
                            .newSuccessInstance("删除成功！"));
                });
    }


    /**
     * Delete all mono.
     *
     * @return the mono
     */
    @RequestMapping("/deleteAll")
    public Mono<GlobalMessageResponseVo> deleteAll() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal)
                .cast(RedisSessionUser.class).flatMap(user -> {
                    String userId = user.getId();
                    messageService.removeByUserId(userId);
                    return Mono.just(GlobalMessageResponseVo
                            .newSuccessInstance("用户信息全部删除成功！"));
                });
    }

}