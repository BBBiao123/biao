/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.otc.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.otc.entity.OtcAgentApplyUser;

/**
 * 拨币申请会员列表DAO接口
 * @author zzj
 * @version 2018-09-19
 */
@MyBatisDao
public interface OtcAgentApplyUserDao extends CrudDao<OtcAgentApplyUser> {

    OtcAgentApplyUser getByApplyIdAndUserId(OtcAgentApplyUser otcAgentApplyUser);
}