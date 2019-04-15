package com.biao.pojo;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充提币记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepdrawLogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String coinId;
    private String symbol;
    private String type;
    private String status;
    private BigDecimal volume;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createDate;

    private String txId;
    private String address;
    private String fee;


}
