package com.biao.mapper;

import com.biao.entity.UserCoinVolume;
import com.biao.entity.UserCoinVolumeBill;
import com.biao.sql.build.UserCoinVolumeBillSqlBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * UserCoinVolumeBillDao.
 * <p>
 * 用户bb资产申请操作表单信息.
 * <p>
 * 19-1-2下午2:52
 *
 *  "" sixh
 */
@Mapper
public interface UserCoinVolumeBillDao {
    /**
     * 根据id查询数据；
     *
     * @param id id;
     * @return 数据 ；
     */
    @SelectProvider(type = UserCoinVolumeBillSqlBuild.class, method = "findById")
    UserCoinVolumeBill findById(String id);

    /**
     * 根据id修改;
     *
     * @param userCoinVolume 资产信息；
     * @return 返回数据 ；
     */
    @UpdateProvider(type = UserCoinVolumeBillSqlBuild.class, method = "updateById")
    long updateById(UserCoinVolume userCoinVolume);

    /**
     * Insert long.
     *
     * @param userCoinVolumeBill the user coin volume
     * @return the long
     */
    @InsertProvider(type = UserCoinVolumeBillSqlBuild.class, method = "insert")
    long insert(UserCoinVolumeBill userCoinVolumeBill);

    /**
     * Insert long.
     *
     * @param userCoinVolumeBills the user coin volume bills
     * @return the long
     */
    @InsertProvider(type = UserCoinVolumeBillSqlBuild.class, method = "batchInsert")
    long insertBatch(@Param("listValues") List<UserCoinVolumeBill> userCoinVolumeBills);

    /**
     * 查询未处理处理的任务信息.
     *
     * @param hash          the hash
     * @param defRetryCount the def retry count
     * @return list ;
     */
    @Select("select " + UserCoinVolumeBillSqlBuild.ALL_COLUMES + " from js_plat_user_coin_volume_bill where hash=#{hash} and (status=0 or status=4) and retry_count<#{retryCount}")
    List<UserCoinVolumeBill> findByStatusToUnprocessed(@Param("hash") Integer hash, @Param("retryCount") Integer defRetryCount);

    /**
     * 查询历史信息.
     *
     * @return list ;
     */
    @Select("select " + UserCoinVolumeBillSqlBuild.ALL_COLUMES + " from js_plat_user_coin_volume_bill where status=3 or status=2")
    List<UserCoinVolumeBill> findHistorys();

    /**
     * Delete history long.
     *
     * @return the long
     */
    @Delete("delete from js_plat_user_coin_volume_bill where status=3 or status=2")
    long deleteHistory();

    /**
     * Update status long.
     *
     * @param id     the id
     * @param status the status
     * @return the long
     */
    @Update("update js_plat_user_coin_volume_bill set status=#{status},retry_count=retry_count+1 where id=#{id} and (status=0 or status=4)")
    long updateStatusAndRetryCount(@Param("id") String id, @Param("status") Integer status);

    /**
     * Update status long.
     *
     * @param id     the id
     * @param status the status
     * @return the long
     */
    @Update("update js_plat_user_coin_volume_bill set status=#{status} where id=#{id}")
    long updateStatus(@Param("id") String id, @Param("status") Integer status);
}
