package com.biao.sql.build.balance;

import com.biao.entity.balance.BalanceUserCoinVolumeDetail;
import com.biao.entity.balance.BalanceUserVolumeIncomeDetail;
import com.biao.query.UserFinanceQuery;
import com.biao.query.UserTradeQuery;
import com.biao.sql.BaseSqlBuild;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

public class BalanceUserVolumeIncomeDetailSqlBuild extends BaseSqlBuild<BalanceUserVolumeIncomeDetail, Integer> {

    public static final String columns = "id,user_id,income_date,coin_symbol,detail_reward,reward_type,create_by,update_by,create_date,update_date,version";
    public String findByUserId(UserFinanceQuery userTradeQuery) {
        return new SQL() {{
            SELECT(columns);
            FROM("js_plat_user_income_incomedetail");
            WHERE("DATEDIFF(income_date,NOW())<=0 and DATEDIFF(income_date,NOW())>-10 ");
            if (StringUtils.isNoneBlank(userTradeQuery.getUserId())) {
                WHERE("user_id = #{userId}");
            }

            if (StringUtils.isNoneBlank(userTradeQuery.getRewardType())) {
                    WHERE("reward_type= #{rewardType}");
            }

            ORDER_BY("income_date desc,reward_type ");

        }}.toString();
    }
}
