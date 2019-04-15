package com.biao.mapper.register;

import com.biao.entity.register.UserRegisterLotteryRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *  ""
 */
@Mapper
public interface UserRegisterLotteryRuleDao {


    @Select("select id,lottery_id,name,min_count,max_count,ratio " +
            " from mk_user_register_lottery_rule ")
    List<UserRegisterLotteryRule> findByLotteryId(String LotteryId);

}
