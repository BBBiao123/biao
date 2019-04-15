/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkMiningManage;
import com.thinkgem.jeesite.modules.market.dao.MkMiningManageDao;

/**
 * 挖矿规则管理Service
 * @author zhangzijun
 * @version 2018-07-13
 */
@Service
@Transactional(readOnly = true)
public class MkMiningManageService extends CrudService<MkMiningManageDao, MkMiningManage> {

	public MkMiningManage get(String id) {
		return super.get(id);
	}
	
	public List<MkMiningManage> findList(MkMiningManage mkMiningManage) {
		return super.findList(mkMiningManage);
	}
	
	public Page<MkMiningManage> findPage(Page<MkMiningManage> page, MkMiningManage mkMiningManage) {
		return super.findPage(page, mkMiningManage);
	}
	
	@Transactional(readOnly = false)
	public void save(MkMiningManage mkMiningManage) {
		super.save(mkMiningManage);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkMiningManage mkMiningManage) {
		super.delete(mkMiningManage);
	}
	
}