/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.otc.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.otc.entity.OtcAgentUser;

/**
 * 银商账户DAO接口
 * @author zzj
 * @version 2018-09-17
 */
@MyBatisDao
public interface OtcAgentUserDao extends CrudDao<OtcAgentUser> {
    int deleteByAgentId(String agentId);
    OtcAgentUser getOneByUserId(String userId);
}