package com.biao.vo;

import com.biao.config.CustomLocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * banner
 */
@Getter
@Setter
public class BannerVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String categoryId;
    private String title;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createDate;
    private String image;
    private String link;
}
