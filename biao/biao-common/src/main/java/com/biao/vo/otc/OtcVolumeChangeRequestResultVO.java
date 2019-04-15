package com.biao.vo.otc;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class OtcVolumeChangeRequestResultVO implements Serializable {

    private String batchNo; //批次号
    private String publishSource; // 来源
    private String status; // 状态
    private String key; // 密文
    private String result; //处理结果描述
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createDate; //处理时间

}
