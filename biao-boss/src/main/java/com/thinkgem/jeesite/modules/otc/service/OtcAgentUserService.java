/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.otc.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.otc.entity.OtcAgentUser;
import com.thinkgem.jeesite.modules.otc.dao.OtcAgentUserDao;

/**
 * 银商账户Service
 * @author zzj
 * @version 2018-09-17
 */
@Service
@Transactional(readOnly = true)
public class OtcAgentUserService extends CrudService<OtcAgentUserDao, OtcAgentUser> {

	public OtcAgentUser get(String id) {
		return super.get(id);
	}
	
	public List<OtcAgentUser> findList(OtcAgentUser otcAgentUser) {
		return super.findList(otcAgentUser);
	}
	
	public Page<OtcAgentUser> findPage(Page<OtcAgentUser> page, OtcAgentUser otcAgentUser) {
		return super.findPage(page, otcAgentUser);
	}
	
	@Transactional(readOnly = false)
	public void save(OtcAgentUser otcAgentUser) {
		super.save(otcAgentUser);
	}
	
	@Transactional(readOnly = false)
	public void delete(OtcAgentUser otcAgentUser) {
		super.delete(otcAgentUser);
	}

	@Transactional(readOnly = false)
	public void deleteByAgentId(String agentId) {
		dao.deleteByAgentId(agentId);
	}

	public OtcAgentUser getOneByUserId(String userId) {
		return dao.getOneByUserId(userId);
	}


}