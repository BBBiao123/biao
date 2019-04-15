package com.biao.reactive.data.mongo.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 安全日志
 *
 *  ""oury
 */
@Document
@Data
public class SecurityLog {

    @Id  // 主键
    private String id;

    /**
     * 安全日志类型   1:修改密码   3:重置密码  2:绑定手机号  4:修改手机号  5:绑定谷歌  6:绑定交易密码  7:修改交易密码   8:切换交易类型  9:设置交易类型
     */
    private String type;

    private String userId;

    private String mobile;

    private String mail;

    private LocalDateTime updateTime;

    private String remark;

    //登录状态 0:成功 1:失败
    private Integer status;
}
