/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeCommonMember;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeCommonMemberCopy;

/**
 * 普通用户备份DAO接口
 * @author zzj
 * @version 2019-01-14
 */
@MyBatisDao
public interface Mk2PopularizeCommonMemberCopyDao extends CrudDao<Mk2PopularizeCommonMemberCopy> {
}