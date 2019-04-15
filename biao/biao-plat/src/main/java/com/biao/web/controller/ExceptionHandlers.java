package com.biao.web.controller;

import com.biao.constant.Constants;
import com.biao.exception.PlatException;
import com.biao.pojo.GlobalMessageResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

/**
 * ControllerMethodResolver
 * https://dzone.com/articles/global-exception-handling-with-controlleradvice
 *
 *  ""dministrator
 */
@ControllerAdvice
public class ExceptionHandlers {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlers.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    protected Mono<GlobalMessageResponseVo> serverExceptionHandler(Exception exception) {
        int code = Constants.GLOBAL_ERROR_CODE;
        String message;
        if (exception instanceof PlatException) {
            PlatException platException = (PlatException) exception;
            code = platException.getCode();
            message = platException.getMsg();
            LOGGER.error("业务异常:code:{},msg:{}",code, message);
        } else {
            LOGGER.error(exception.getMessage(), exception);
            message = "系统繁忙,请稍后重试";
        }
        return Mono.just(GlobalMessageResponseVo.newInstance(code, message));
    }

    public Mono<String> exceptionReturn() {
        return Mono.error(new RuntimeException("test error"));
    }
}
