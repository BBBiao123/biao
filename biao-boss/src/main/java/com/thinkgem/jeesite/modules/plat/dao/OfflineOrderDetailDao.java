/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.OfflineOrderDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;

/**
 * c2c广告详情DAO接口
 * @author dazi
 * @version 2018-04-30
 */
@MyBatisDao
public interface OfflineOrderDetailDao extends CrudDao<OfflineOrderDetail> {

    public static final String columns = "id,user_id,ask_user_id,volume,price,order_id,coin_id,symbol,status,radom_num,total_price,remarks,sub_order_id,sync_date,create_by,update_by,create_date,update_date" ;

    @Update("update js_plat_offline_order_detail set status = #{status} where sub_order_id = #{subOrderId}")
    long updateStatusBySubOrderId(@Param("status") Integer status, @Param("subOrderId") String subOrderId);

    @Update("update js_plat_offline_order_detail set status = #{status}, confirm_receipt_date = #{receiptDate} where sub_order_id = #{subOrderId} AND status = '1'")
    long updateStatusReceiptBySubOrderId(@Param("status") Integer status, @Param("subOrderId") String subOrderId, @Param("receiptDate") Timestamp receiptDate);

    @Select("select "+ columns +" from js_plat_offline_order_detail where user_id = #{userId} and sub_order_id = #{subOrderId} ")
    OfflineOrderDetail findUserIdAndSubOrderId(@Param("userId") String userId,@Param("subOrderId")  String subOrderId);
}