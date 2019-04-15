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
public class UserTradeQuery extends RequestQuery {

    private String userId;

    private String coinMain;

    private String coinOther;

    /**
     * 用户挂单状态 传 0 表示当前挂单， 1 历史.
     */
    private Integer status;
}
