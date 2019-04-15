package com.biao.entity.register;

import com.biao.entity.BaseEntity;
import com.biao.sql.SqlField;
import com.biao.sql.SqlTable;
import lombok.Data;

/**
 *  ""(Myth)
 */
@SqlTable("mk_user_register_lottery_limit")
@Data
public class UserRegisterLotteryLimit extends BaseEntity {

    @SqlField("lottery_id")
    private String lotteryId;

    @SqlField("start_count")
    private Integer startCount;

    @SqlField("end_count")
    private Integer endCount;

    @SqlField("ratio")
    private Double ratio;

}
