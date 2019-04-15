package com.biao.entity;

import lombok.Data;

import java.io.Serializable;


@Data
public class CmsSite implements Serializable {

    private String id;

    private String name;

    private String title;

    private String logo;

    private String domain;

    private String description;

    private String keywords;

    private String theme;

    private String copyright;
}
