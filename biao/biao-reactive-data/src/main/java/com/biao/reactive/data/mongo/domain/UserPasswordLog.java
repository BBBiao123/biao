package com.biao.reactive.data.mongo.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class UserPasswordLog {

    private String userId;

    private String username;

    private String mail;

    private String mobile;

    private String source;

    private LocalDateTime createTime;

}
