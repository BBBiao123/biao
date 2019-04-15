package com.biao.reactive.data.mongo.domain;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class UserLoginLog {

    @Id  // 主键
    private String id;

    private String loginName;

    private String ip;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime loginTime;

    private String source;

    private String mail;

    private String mobile;

    private String address;

    private String remark;

    //登录状态 0:成功 1:失败
    private Integer status;

    private String userId;

}
