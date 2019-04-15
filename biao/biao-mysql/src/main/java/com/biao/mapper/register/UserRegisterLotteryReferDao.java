package com.biao.mapper.register;

import com.biao.entity.register.UserRegisterLotteryRefer;
import com.biao.sql.build.register.UserRegisterLotteryReferSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * The interface User register lottery refer dao.
 */
@Mapper
public interface UserRegisterLotteryReferDao {

    /**
     * Find all list.
     *
     * @return the list
     */
    @Select("select " + UserRegisterLotteryReferSqlBuild.columns + " from  mk_user_register_lottery_refer")
    List<UserRegisterLotteryRefer> findAll();

    /**
     * Count int.
     *
     * @return the int
     */
    @Select("select count(1) from mk_user_register_lottery_refer")
    int count();


    /**
     * Exists user mining list.
     *
     * @return the list
     */
    @Select("select  " + UserRegisterLotteryReferSqlBuild.columns + " from  mk_user_register_lottery_refer  r where exists (select g.user_id from mk2_popularize_mining_give_coin_log g where  r.refer_id = g.user_id  )")
    List<UserRegisterLotteryRefer> existsUserMining();

    /**
     * Insert int.
     *
     * @param registerLotteryRefer the register lottery refer
     * @return the int
     */
    @InsertProvider(type = UserRegisterLotteryReferSqlBuild.class, method = "insert")
    int insert(UserRegisterLotteryRefer registerLotteryRefer);

    /**
     * Delete int.
     *
     * @param id the id
     * @return the int
     */
    @Delete("delete * from mk_user_register_lottery_refer where id = #{id}")
    int delete(@Param("id") String id);


    /**
     * Batch delete int.
     *
     * @param ids the ids
     * @return the int
     */
    @Delete({
            "<script>"
                    + "delete from mk_user_register_lottery_refer WHERE id in "
                    + "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>"
                    + "#{item}"
                    + "</foreach>"
                    + "</script>"
    })
    int batchDelete(@Param("ids") List<String> ids);
}
