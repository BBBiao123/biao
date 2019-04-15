/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.plat.entity.Mk2PopularizeRegisterConf;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 注册送币规则DAO接口
 * @author dongfeng
 * @version 2018-07-20
 */
@MyBatisDao
public interface Mk2PopularizeRegisterConfDao extends CrudDao<Mk2PopularizeRegisterConf> {

    List<Mk2PopularizeRegisterConf> findEffective();

}