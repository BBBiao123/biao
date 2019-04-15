/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 前台用户DAO接口
 * @author dazi
 * @version 2018-04-26
 */
@MyBatisDao
public interface PlatUserDao extends CrudDao<PlatUser> {

    public static final String columns = "`id`, `username`, `password`, `user_type`, `status`, `mobile`, `mail`, `ex_password`, `google_auth`, `sex`, `age`, `invite_code`, `real_name`, `id_card`, `card_up_id`, `card_down_id`, `card_face_id`, `card_status`, `card_status_reason`, `open_discount`, `remarks`, `country_id`, `country_code`, `wechat_no`, `wechat_qrcode_id`, `alipay_no`, `alipay_qrcode_id`, `refer_id`, `create_date`, `update_date`, `create_by`, `update_by`, `audit_date`, `refer_invite_code`, `mobile_audit_date`, `is_award`, `source`, `c2c_in`, `c2c_out`, `coin_out`, `c2c_publish`, `tag`, `ex_valid_type`, `area`, `c2c_change`, `c2c_switch`, `trade_switch`" ;

    List<PlatUser> findBySource(String source);

    @Select("select "+ columns +" from js_plat_user where mobile = #{mobile} limit 1")
    PlatUser findByMobile(@Param("mobile") String mobile);

    @Select("select "+ columns +" from js_plat_user where mail = #{mail} limit 1")
    PlatUser findByMail(@Param("mail") String mail);

    List<PlatUser> findOne(PlatUser platUser);

    @Update("update js_plat_user set mobile = #{targetMobile} where mobile = #{mobile} and tag = 'RELAY'")
    long updateMobile(@Param("mobile") String mobile, @Param("targetMobile") String targetMobile);
	
}