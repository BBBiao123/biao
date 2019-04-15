package com.biao.vo.otc;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.biao.pojo.RequestQuery;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
public class OtcAgentVO extends RequestQuery {

    private String id;
    private String userId;
    private String username;
    private String mobile;
    private String mail;
    private Integer sex;
    private Integer age;
    private String realName;
    private String nickName;
    private String idCard;
    private String countryCode;
    private String inviteCode;
    private String tag;
    private BigDecimal volume;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime updateDate;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createDate;

    private long countDayDetail;// 今日订单总条数
    private BigDecimal sumDayDetailVolume; // 今日订单总数量volume
    private long countDetailNoComplete; // 未完成订单总条数

    private String coinSymbol;//

    private Integer cardLevel;
    private String key;

}
