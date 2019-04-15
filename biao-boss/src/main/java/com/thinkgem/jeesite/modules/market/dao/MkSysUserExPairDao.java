/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.market.entity.MkSysUserExPair;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;

import java.util.List;

/**
 * 营销用户币币对DAO接口
 * @author zzj
 * @version 2018-08-23
 */
@MyBatisDao
public interface MkSysUserExPairDao extends CrudDao<MkSysUserExPair> {
    MkSysUserExPair getByUserAndExPair(MkSysUserExPair mkSysUserExPair);

    List<PlatUser> getPlatUserBySysUser(MkSysUserExPair mkSysUserExPair);
}