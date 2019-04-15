/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkDistributePromote;
import com.thinkgem.jeesite.modules.market.dao.MkDistributePromoteDao;

/**
 * 会员推广Service
 * @author zhangzijun
 * @version 2018-07-05
 */
@Service
@Transactional(readOnly = true)
public class MkDistributePromoteService extends CrudService<MkDistributePromoteDao, MkDistributePromote> {

	public MkDistributePromote get(String id) {
		return super.get(id);
	}
	
	public List<MkDistributePromote> findList(MkDistributePromote mkDistributePromote) {
		return super.findList(mkDistributePromote);
	}
	
	public Page<MkDistributePromote> findPage(Page<MkDistributePromote> page, MkDistributePromote mkDistributePromote) {
		return super.findPage(page, mkDistributePromote);
	}
	
	@Transactional(readOnly = false)
	public void save(MkDistributePromote mkDistributePromote) {
		super.save(mkDistributePromote);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkDistributePromote mkDistributePromote) {
		super.delete(mkDistributePromote);
	}
	
}