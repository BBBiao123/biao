/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.*;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.OfflineOrder;

/**
 * c2c广告DAO接口
 * @author dazi
 * @version 2018-04-27
 */
@MyBatisDao
public interface OfflineOrderDao extends CrudDao<OfflineOrder> {
	
	public static final String cloumns = "id,user_id,real_name,mobile,card_no,bank_name,bank_branch_name,"
			+ "wechat_no,wechat_qrcode_id,alipay_no,alipay_qrcode_id,coin_id,symbol,volume,lock_volume,success_volume,"
			+ "fee_volume,min_ex_volume,price,total_price,status,flag,ex_type,remarks,create_date,update_date,"
			+ "cancel_date,update_by,create_by,version" ;
	
    @Update("update js_plat_offline_order set success_volume = #{successVolume}, lock_volume = #{lockVolume},status =#{orderStatus} where id = #{id} and update_date = #{updateDate}")
    long updateSuccessVolumeAndLockVolumeAndStatusById(@Param("id") String id, @Param("successVolume") BigDecimal successVolume, @Param("lockVolume") BigDecimal lockVolume, @Param("orderStatus") Integer orderStatus, @Param("updateDate") Timestamp updateDate);

    @Update("update js_plat_offline_order set lock_volume = #{lockVolume} where id = #{id} and update_date = #{updateDate}")
    long updateLockVolumeById(@Param("id") String id,@Param("lockVolume") BigDecimal lockVolume,@Param("updateDate") Timestamp updateDate);
    
    @Delete("delete from js_plat_offline_order where status in (1,9) and create_date <= #{dateTime}")
    long deleteByDate(Date dateTime);
    
    @Insert("insert into js_plat_offline_order_copy("+cloumns+") select "+cloumns+" from js_plat_offline_order where status in (1,9) and create_date <= #{dateTime}")
    long deleteByDateInsert(Date dateTime);
    
    @Delete("delete from js_plat_offline_order where id in ( SELECT a.id FROM (SELECT id FROM js_plat_offline_order WHERE id NOT IN (SELECT order_id FROM js_plat_offline_order_detail) and status = 3 and create_date <= #{dateTime}) a )")
    long deleteByDateAndStatus(Date dateTime);
    
    @Insert("insert into js_plat_offline_order_copy("+cloumns+") select "+cloumns+" from js_plat_offline_order where id in ( SELECT a.id FROM (SELECT id FROM js_plat_offline_order WHERE id NOT IN (SELECT order_id FROM js_plat_offline_order_detail) and status = 3 and create_date <= #{dateTime}) a )")
    long deleteByDateAndStatusInsert(Date dateTime);


    // ==========撤销广告==============

    @Select("SELECT " + cloumns + " FROM js_plat_offline_order t WHERE t.id = #{orderId} LIMIT 1 ")
    OfflineOrder findByOrderId(@Param("orderId") String orderId);

    @Select("SELECT " + cloumns + " FROM js_plat_offline_order t WHERE t.symbol = #{coinSymbol} AND t.ex_type = #{exType} AND t.status in ('0', '2') ")
    List<OfflineOrder> findCanCancelByCoinSymbol(@Param("coinSymbol") String coinSymbol, @Param("exType") String exType);

    @Update("UPDATE js_plat_offline_order SET status = #{status}, cancel_date = #{cancelDate}, version= version+1, remarks = #{remarks} WHERE id = #{id} and update_date = #{updateDate} and version= #{version}")
    long updateOrderCancelStatusById(OfflineOrder offlineOrder);

}