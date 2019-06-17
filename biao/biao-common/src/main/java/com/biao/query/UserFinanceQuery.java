package com.biao.query;

import com.biao.pojo.RequestQuery;
import lombok.Data;

/**
 * <p>Description: .</p>
 *
 *  ""(Myth)
 * @version 1.0
 * @date 2018/5/5 15:40
 * @since JDK 1.8
 */
@Data
public class UserFinanceQuery extends RequestQuery {


    private String userId;
    /**
     * 用户奖励类型 1表示刷单奖 ...
     */
    private String rewardType;
}
