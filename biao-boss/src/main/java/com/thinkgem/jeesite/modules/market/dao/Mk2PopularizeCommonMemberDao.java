/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeCommonMember;

import java.util.Date;

/**
 * 普通用户DAO接口
 * @author dongfeng
 * @version 2018-08-17
 */
@MyBatisDao
public interface Mk2PopularizeCommonMemberDao extends CrudDao<Mk2PopularizeCommonMember> {

    long updateReleaseInfo(Mk2PopularizeCommonMember commonMember);

    long updateReleaseVolume(Mk2PopularizeCommonMember commonMember);
}