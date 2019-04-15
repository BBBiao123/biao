package com.biao.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {

    /**
     * 测试图片服务ok 整合webuploader
     *
     * @return
     */
    @GetMapping("/form")
    public String form() {
        return "form";
    }

}
