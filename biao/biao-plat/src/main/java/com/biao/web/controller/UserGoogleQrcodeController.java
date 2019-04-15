package com.biao.web.controller;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.google.GoogleAuthenticator;
import com.biao.google.QrCodeCreator;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.reactive.data.mongo.service.GridFsTemplateService;
import com.biao.util.ValidateCode;
import com.biao.web.valid.ValidateFiled;
import com.biao.web.valid.ValidateGroup;
import com.google.zxing.WriterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/biao")
public class UserGoogleQrcodeController {
    private final Logger logger = LoggerFactory.getLogger(UserGoogleQrcodeController.class);

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;

    @Autowired
    private GridFsTemplateService gridFsTemplateService;

    /**
     * 谷歌验证二维码生成
     *
     * @param secret
     * @param exchange
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, notNull = true, errMsg = "谷歌秘钥不能为空"),
    })
    @RequestMapping(value = "/qrcode")
    public Mono<Void> qrcode(@RequestParam("secret") String secret, ServerWebExchange exchange) {
        ServerHttpResponse result = exchange.getResponse();
        result.setStatusCode(HttpStatus.OK);
        result.getHeaders().setContentType(MediaType.IMAGE_PNG);
        DataBufferFactory bufferFactory = result.bufferFactory();
        Mono<DataBuffer> data = ReactiveSecurityContextHolder.getContext().filter(c -> c.getAuthentication() != null).map(SecurityContext::getAuthentication).map(Authentication::getPrincipal)
                .cast(RedisSessionUser.class)
                .flatMap(user -> this.getQRBarcodeContent(user, secret, bufferFactory));
        return result.writeWith(data);
    }

    private Mono<DataBuffer> getQRBarcodeContent(RedisSessionUser redisSessionUser, String secret, DataBufferFactory bufferFactory) {
        String qrcodeContent = GoogleAuthenticator.getQRBarcode(redisSessionUser.getMail(), secret);
        try {
            return Mono.just(bufferFactory.wrap(QrCodeCreator.createQrcode(QrCodeCreator.bitMatrix(qrcodeContent, 300, 300))));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return Mono.empty();
    }

    /**
     * 验证码生成
     *
     * @return
     */
    @ValidateGroup(fileds = {
            @ValidateFiled(index = 0, notNull = true, errMsg = "用户名不能为空"),
            @ValidateFiled(index = 1, notNull = true, errMsg = "验证码类型不能为空")
    })
    @RequestMapping(value = "/valid/createCode")
    @ResponseBody
    public Mono<GlobalMessageResponseVo> validCode(@RequestParam("username") String username, @RequestParam("type") String type) {
        ValidateCode validateCode = new ValidateCode(100, 30, 4, 30);
        ByteArrayInputStream inputStream = null;
        Map<String, Object> imageData = new HashMap<>();
        try {
            String imageName = "v" + UUID.randomUUID().toString().replaceAll("-", "") + ".png";
            //不用图片
            /*byte[] data = validateCode.write();
            inputStream = new ByteArrayInputStream(data);
            String imageId = gridFsTemplateService.uploadFromStream(inputStream, imageName);
            imageData.put("imageId", imageId);*/
            imageData.put("imageName", imageName);
            /*String rediKey = new StringBuilder(SercurityConstant.VALID_CODE_USERNAME_REDIS_NAMESAPCE)
                    .append(username).append(":").append(type).toString();
            valOpsStr.set(rediKey, validateCode.getCode(), SercurityConstant.REDIS_EXPIRE_TIME_HALF_HOUR, TimeUnit.SECONDS);*/
            logger.info("code:{}", validateCode.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(imageData));
    }
}
