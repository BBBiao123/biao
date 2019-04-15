package com.biao.mapper.register;

import com.biao.entity.register.UserRegisterLottery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * UserRegisterLotteryDao.
 *
 *  ""
 */
@Mapper
public interface UserRegisterLotteryDao {

    /**
     * Find one user register lottery.
     *
     * @return the user register lottery
     */
    @Select("select id,name,coin_symbol,status,total_prize,recommend_min_volume,recommend_ratio," +
            "recommend_day_count,recommend_total_count,recommend_count_limit,start_date " +
            " from mk_user_register_lottery where status = 1 order by update_date desc limit 1")
    UserRegisterLottery findOne();
}
