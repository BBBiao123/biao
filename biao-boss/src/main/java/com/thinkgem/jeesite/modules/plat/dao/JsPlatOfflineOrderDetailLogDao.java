/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Insert;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatOfflineOrderDetailLog;

/**
 * c2c流水统计表DAO接口
 * @author ruoyu
 * @version 2018-10-24
 */
@MyBatisDao
public interface JsPlatOfflineOrderDetailLogDao extends CrudDao<JsPlatOfflineOrderDetailLog> {
	
	public static final String cloumns = "id,order_id,sub_order_id,volume,fee_volume,coin_id,symbol,price,"
			+ "total_price,user_mobile,user_id,user_name,user_bank_no,sell_bank_no,sell_bank_name,sell_bank_branch_name,alipay_no,"
			+ "alipay_qrcode_id,wechat_no,wechat_qrcode_id,ask_user_mobile,ask_user_id,ask_user_name,ask_user_bank_no,remarks,status,"
			+ "radom_num,create_date,update_date,confirm_receipt_date,confirm_payment_date,sync_date,create_by,update_by,cancel_date,ex_type,advert_type" ;
	
	long batchDelete(Date countDate);
	
	@Insert("insert into js_plat_offline_order_detail_copy("+cloumns+") select "
			+ cloumns
			+ " FROM js_plat_offline_order_detail WHERE STATUS in (2,9) AND create_date <= #{countDate}")
	public int batchInsert(Date countDate);
}