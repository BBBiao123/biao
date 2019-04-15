package com.biao.query;

import com.biao.pojo.RequestQuery;
import lombok.Data;

@Data
public class UserLoginLogQuery extends RequestQuery {

    private String userId;

    private String source;
}
