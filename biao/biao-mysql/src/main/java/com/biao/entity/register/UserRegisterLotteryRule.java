package com.biao.entity.register;

import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import lombok.Data;

/**
 *  ""(Myth)
 */
@SqlTable("mk_user_register_lottery_rule")
@Data
public class UserRegisterLotteryRule extends BaseEntity {

    @SqlField("lottery_id")
    private String lotteryId;

    @SqlField("name")
    private String name;

    @SqlField("min_count")
    private Integer minCount;

    @SqlField("max_count")
    private Integer maxCount;

    @SqlField("ratio")
    private Double ratio;

    /**
     * 奖品中奖数字范围起点
     */
    private Integer awardStartCode;
    /**
     * 奖品中奖数字范围终点
     */
    private Integer awardEndCode;
}
