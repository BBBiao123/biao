package com.biao.mapper.register;

import com.biao.entity.register.UserRegisterLotteryLimit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * The interface User register lottery limit dao.
 *
 *  ""
 */
@Mapper
public interface UserRegisterLotteryLimitDao {


    /**
     * Find by lottery id list.
     *
     * @param LotteryId the lottery id
     * @return the list
     */
    @Select("select id,lottery_id,start_count,end_count,ratio " +
            " from mk_user_register_lottery_limit ")
    List<UserRegisterLotteryLimit> findByLotteryId(String LotteryId);

}
