package com.biao.vo.otc;

import com.biao.config.CustomLocalDateTimeDeserializer;
import com.biao.config.CustomLocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class OtcVolumeChangeResultVO implements Serializable {

    private String type;
    private String batchNo;
    private String coinId;
    private String symbol;
//    private String sellUserId;
//    private String buyUserId;
    private String orderId;
    private String adType;
    private String subOrderId;
    private BigDecimal volume;
    private BigDecimal feeVolume;
    private String status;
    private String result;
    private String remarks;
    private String publishSource; // 来源
    private String key; // 加密校验
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateDate;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createDate;

}
