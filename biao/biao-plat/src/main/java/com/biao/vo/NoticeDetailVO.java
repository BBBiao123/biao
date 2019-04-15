package com.biao.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * banner
 */
@Getter
@Setter
public class NoticeDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String categoryId;
    private String title;
    private String image;
    private String link;
    private String keywords;
    private String description;
    private Integer weight;
    private LocalDateTime weightDate;
    private Long hits;
    private String posid;
    private String remarks;
    private Integer delFlag;
    protected String createBy;
    protected LocalDateTime createDate;
    private String content;
    private String copyfrom;
    private String relation;
    private Integer allowComment;
}
