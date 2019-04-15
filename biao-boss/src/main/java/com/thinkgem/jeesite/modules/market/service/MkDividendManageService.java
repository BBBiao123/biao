/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkDividendManage;
import com.thinkgem.jeesite.modules.market.dao.MkDividendManageDao;

/**
 * 分红规则管理Service
 * @author zhangzijun
 * @version 2018-07-13
 */
@Service
@Transactional(readOnly = true)
public class MkDividendManageService extends CrudService<MkDividendManageDao, MkDividendManage> {

	public MkDividendManage get(String id) {
		return super.get(id);
	}
	
	public List<MkDividendManage> findList(MkDividendManage mkDividendManage) {
		return super.findList(mkDividendManage);
	}
	
	public Page<MkDividendManage> findPage(Page<MkDividendManage> page, MkDividendManage mkDividendManage) {
		return super.findPage(page, mkDividendManage);
	}
	
	@Transactional(readOnly = false)
	public void save(MkDividendManage mkDividendManage) {
		super.save(mkDividendManage);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkDividendManage mkDividendManage) {
		super.delete(mkDividendManage);
	}
	
}