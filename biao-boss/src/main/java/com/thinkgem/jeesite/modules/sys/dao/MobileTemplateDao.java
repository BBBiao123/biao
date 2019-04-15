/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import org.apache.ibatis.annotations.Select;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.MobileTemplate;

/**
 * ddDAO接口
 * @author ruoyu
 * @version 2018-06-28
 */
@MyBatisDao
public interface MobileTemplateDao extends CrudDao<MobileTemplate> {
	
	
	@Select("select id,code,access_key,access_secret,sign_name,template_param,template_code,work_sign,remark,expand_json,time_out from mobile_template where code = #{code}")
	MobileTemplate findByCode(String code);
}