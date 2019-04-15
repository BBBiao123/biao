package com.biao.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class TsDbVo {
    /**
     * 时间
     */
    @JsonIgnore
    private LocalDateTime time;

    private String localDateTime;
    /**
     * 时分秒
     */
    @JsonFormat(pattern = "HH:mm:ss")
    private String localTime;
    /**
     * 年月日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String localDate;

    private Boolean dataNull;
    @JsonIgnore
    private Map<String, String> tags;

}
