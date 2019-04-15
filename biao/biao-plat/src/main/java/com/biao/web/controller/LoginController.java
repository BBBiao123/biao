package com.biao.web.controller;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.constant.SercurityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

@RestController
public class LoginController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public Mono<Void> login(ServerWebExchange exchange) {
        ServerHttpResponse result = exchange.getResponse();
        result.setStatusCode(HttpStatus.OK);
        result.getHeaders().setContentType(MediaType.TEXT_HTML);
        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        Mono<DataBuffer> token = Mono.just(bufferFactory.wrap(createPage(false, false, "")));
        return result.writeWith(token);
        // return Mono.just("login");
    }

    private static byte[] createPage(boolean isError, boolean isLogoutSuccess, String csrfTokenHtmlInput) {
        String page = "<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "  <head>\n"
                + "    <meta charset=\"utf-8\">\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n"
                + "    <meta name=\"description\" content=\"\">\n"
                + "    <meta name=\"author\" content=\"\">\n"
                + "    <title>Please sign in</title>\n"
                + "    <link href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M\" crossorigin=\"anonymous\">\n"
                + "    <link href=\"http://getbootstrap.com/docs/4.0/examples/signin/signin.css\" rel=\"stylesheet\" crossorigin=\"anonymous\"/>\n"
                + "  </head>\n"
                + "  <body>\n"
                + "     <div class=\"container\">\n"
                + "      <form class=\"form-signin\" method=\"post\" action=\"/login\">\n"
                + "        <h2 class=\"form-signin-heading\">Please sign in</h2>\n"
                + createError(isError)
                + createLogoutSuccess(isLogoutSuccess)
                + "        <p>\n"
                + "          <label for=\"username\" class=\"sr-only\">Username</label>\n"
                + "          <input type=\"text\" id=\"username\" name=\"username\" class=\"form-control\" placeholder=\"Username\" required autofocus>\n"
                + "        </p>\n"
                + "        <p>\n"
                + "          <label for=\"password\" class=\"sr-only\">Password</label>\n"
                + "          <input type=\"password\" id=\"password\" name=\"password\" class=\"form-control\" placeholder=\"Password\" required>\n"
                + "        </p>\n"
                + csrfTokenHtmlInput
                + "        <button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">Sign in</button>\n"
                + "      </form>\n"
                + "    </div>\n"
                + "  </body>\n"
                + "</html>";

        return page.getBytes(Charset.defaultCharset());
    }

    private static String createError(boolean isError) {
        return isError ? "<div class=\"alert alert-danger\" role=\"alert\">Invalid credentials</div>" : "";
    }

    private static String createLogoutSuccess(boolean isLogoutSuccess) {
        return isLogoutSuccess ? "<div class=\"alert alert-success\" role=\"alert\">You have been signed out</div>" : "";
    }

    @RequestMapping(value = "/log")
    public Mono<String> log() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .map(SecurityContext::getAuthentication).map(Authentication::getPrincipal).cast(RedisSessionUser.class).flatMap(user -> this.log(user));
    }

    public Mono<String> log(RedisSessionUser user) {
        System.out.println("user:" + user);
        valOpsStr.set(SercurityConstant.AUTH_USERNAME_REDIS_NAMESAPCE + user.getUsername(), SercurityConstant.AuthType.PASS_AUTH.getCode(), SercurityConstant.REDIS_EXPIRE_TIME_ONE_HOUR * 2, TimeUnit.SECONDS);
        return Mono.just("log");
    }

    @RequestMapping(value = "/trade/{id}", method = RequestMethod.GET)
    public Mono<String> trade() {
        return Mono.just("trade");
    }

}
