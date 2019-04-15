package com.biao.web.controller;

import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.service.CmsLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 友情链接
 */
@RestController
@RequestMapping("/biao")
public class CmsLinkController {

    @Autowired
    private CmsLinkService cmsLinkService;

    @RequestMapping("/cms/link/list")
    @ResponseBody
    public Mono<GlobalMessageResponseVo> list() {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(cmsLinkService.findAll()));
    }
}
