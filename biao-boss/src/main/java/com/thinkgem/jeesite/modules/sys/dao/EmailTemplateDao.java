/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.EmailTemplate;

/**
 * 邮件模板管理DAO接口
 * @author ruoyu
 * @version 2018-07-10
 */
@MyBatisDao
public interface EmailTemplateDao extends CrudDao<EmailTemplate> {
	
}