package com.biao.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 心跳检查
 */

@RestController
@RequestMapping("/biao")
public class HeartbeatController {

    @GetMapping("/ping")
    public String ping() {
        return "success";
    }

}
