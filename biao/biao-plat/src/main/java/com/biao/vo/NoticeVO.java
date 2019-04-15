package com.biao.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * banner
 */
@Getter
@Setter
public class NoticeVO implements Serializable {

    private static final long serialVersionUID = 1L;


    private String id;
    private String categoryId;
    private String title;


}
