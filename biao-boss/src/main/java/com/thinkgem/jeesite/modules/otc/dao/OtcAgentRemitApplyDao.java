/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.otc.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.otc.entity.OtcAgentRemitApply;

/**
 * 银商拨币申请列表DAO接口
 * @author zzj
 * @version 2018-09-18
 */
@MyBatisDao
public interface OtcAgentRemitApplyDao extends CrudDao<OtcAgentRemitApply> {
	
}