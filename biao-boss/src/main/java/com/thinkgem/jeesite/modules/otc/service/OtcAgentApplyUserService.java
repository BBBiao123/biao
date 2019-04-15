/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.otc.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.otc.entity.OtcAgentApplyUser;
import com.thinkgem.jeesite.modules.otc.dao.OtcAgentApplyUserDao;

/**
 * 拨币申请会员列表Service
 * @author zzj
 * @version 2018-09-19
 */
@Service
@Transactional(readOnly = true)
public class OtcAgentApplyUserService extends CrudService<OtcAgentApplyUserDao, OtcAgentApplyUser> {

	public OtcAgentApplyUser get(String id) {
		return super.get(id);
	}
	
	public List<OtcAgentApplyUser> findList(OtcAgentApplyUser otcAgentApplyUser) {
		return super.findList(otcAgentApplyUser);
	}
	
	public Page<OtcAgentApplyUser> findPage(Page<OtcAgentApplyUser> page, OtcAgentApplyUser otcAgentApplyUser) {
		return super.findPage(page, otcAgentApplyUser);
	}
	
	@Transactional(readOnly = false)
	public void save(OtcAgentApplyUser otcAgentApplyUser) {
		super.save(otcAgentApplyUser);
	}
	
	@Transactional(readOnly = false)
	public void delete(OtcAgentApplyUser otcAgentApplyUser) {
		super.delete(otcAgentApplyUser);
	}

	public OtcAgentApplyUser getByApplyIdAndUserId(OtcAgentApplyUser otcAgentApplyUser) {
		return dao.getByApplyIdAndUserId(otcAgentApplyUser);
	}


}