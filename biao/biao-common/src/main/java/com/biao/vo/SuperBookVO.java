package com.biao.vo;

import com.biao.pojo.RequestQuery;
import lombok.Data;

@Data
public class SuperBookVO extends RequestQuery {
    private static final long serialVersionUID = 1L;

    private String address; // 超级账本地址
}
