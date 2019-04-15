package com.biao.web.controller;

import com.biao.entity.PlatLink;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.PlatLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 首页平台链接
 *
 *  ""
 */
@RestController
@RequestMapping("/biao")
public class PlatLinkController {

    @Autowired
    private PlatLinkService platLinkService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final int LINKS_TIME_OUT = 600; // 缓存10分钟

    private static final String PLAT_LINKS_KEY = "index:plat:links";

    /**
     * 首页获取交易对数据.
     *
     * @return GlobalMessageResponseVo
     */
    @GetMapping("/index/platLinks")
    public Mono<GlobalMessageResponseVo> platLink() {
        List<PlatLink> links = (List<PlatLink>) redisTemplate.opsForValue().get(PLAT_LINKS_KEY);
        if (Objects.isNull(links)) {
            links = platLinkService.findAll();
            redisTemplate.opsForValue().set(PLAT_LINKS_KEY, links, LINKS_TIME_OUT, TimeUnit.SECONDS);
        }
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(links));
    }

}
