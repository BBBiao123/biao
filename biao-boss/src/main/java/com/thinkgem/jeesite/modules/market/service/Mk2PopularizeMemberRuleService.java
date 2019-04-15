/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeMemberRule;
import com.thinkgem.jeesite.modules.market.dao.Mk2PopularizeMemberRuleDao;

/**
 * 会员规则设置Service
 * @author dongfeng
 * @version 2019-02-28
 */
@Service
@Transactional(readOnly = true)
public class Mk2PopularizeMemberRuleService extends CrudService<Mk2PopularizeMemberRuleDao, Mk2PopularizeMemberRule> {

	public Mk2PopularizeMemberRule get(String id) {
		return super.get(id);
	}
	
	public List<Mk2PopularizeMemberRule> findList(Mk2PopularizeMemberRule mk2PopularizeMemberRule) {
		return super.findList(mk2PopularizeMemberRule);
	}
	
	public Page<Mk2PopularizeMemberRule> findPage(Page<Mk2PopularizeMemberRule> page, Mk2PopularizeMemberRule mk2PopularizeMemberRule) {
		return super.findPage(page, mk2PopularizeMemberRule);
	}
	
	@Transactional(readOnly = false)
	public void save(Mk2PopularizeMemberRule mk2PopularizeMemberRule) {
		super.save(mk2PopularizeMemberRule);
	}
	
	@Transactional(readOnly = false)
	public void delete(Mk2PopularizeMemberRule mk2PopularizeMemberRule) {
		super.delete(mk2PopularizeMemberRule);
	}
	
}