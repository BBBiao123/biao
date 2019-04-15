package com.biao.sql.build;

import com.biao.entity.Order;
import com.biao.query.UserTradeQuery;
import com.biao.sql.BaseSqlBuild;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

public class OrderSqlBuild extends BaseSqlBuild<Order, Integer> {

    public static final String columns = "id,user_id,coin_id,coin_symbol,status,volume,create_by,update_by,create_date,update_date";


    public static final String USER_ORDER = "id,coin_main,ask_volume,success_volume,coin_other," +
            "to_coin_volume,ex_fee,ex_type,status,price,create_date ";

    public String findListByQuery(UserTradeQuery userTradeQuery) {
        return new SQL() {{
            SELECT(USER_ORDER);
            FROM("js_plat_ex_order");
            if (StringUtils.isNoneBlank(userTradeQuery.getUserId())) {
                WHERE("user_id = #{userId}");
            }
            if (StringUtils.isNoneBlank(userTradeQuery.getCoinMain())) {
                WHERE("coin_main = #{coinMain}");
            }

            if (StringUtils.isNoneBlank(userTradeQuery.getCoinOther())) {
                WHERE("coin_other = #{coinOther}");
            }

            if (userTradeQuery.getStatus() == 0) {
                WHERE("status in ('0','1')");
            } else {
                WHERE("`status` in ('2','3','4')");
            }

            ORDER_BY("create_date desc");

        }}.toString();
    }

}
