/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkPromoteManage;
import com.thinkgem.jeesite.modules.market.dao.MkPromoteManageDao;

/**
 * 会员推广规则管理Service
 * @author zhangzijun
 * @version 2018-07-16
 */
@Service
@Transactional(readOnly = true)
public class MkPromoteManageService extends CrudService<MkPromoteManageDao, MkPromoteManage> {

	public MkPromoteManage get(String id) {
		return super.get(id);
	}
	
	public List<MkPromoteManage> findList(MkPromoteManage mkPromoteManage) {
		return super.findList(mkPromoteManage);
	}
	
	public Page<MkPromoteManage> findPage(Page<MkPromoteManage> page, MkPromoteManage mkPromoteManage) {
		return super.findPage(page, mkPromoteManage);
	}
	
	@Transactional(readOnly = false)
	public void save(MkPromoteManage mkPromoteManage) {
		super.save(mkPromoteManage);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkPromoteManage mkPromoteManage) {
		super.delete(mkPromoteManage);
	}
	
}