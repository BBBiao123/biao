/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkDistributeDividend;
import com.thinkgem.jeesite.modules.market.dao.MkDistributeDividendDao;

/**
 * 分红规则Service
 * @author zhangzijun
 * @version 2018-07-05
 */
@Service
@Transactional(readOnly = true)
public class MkDistributeDividendService extends CrudService<MkDistributeDividendDao, MkDistributeDividend> {

	public MkDistributeDividend get(String id) {
		return super.get(id);
	}
	
	public List<MkDistributeDividend> findList(MkDistributeDividend mkDistributeDividend) {
		return super.findList(mkDistributeDividend);
	}
	
	public Page<MkDistributeDividend> findPage(Page<MkDistributeDividend> page, MkDistributeDividend mkDistributeDividend) {
		return super.findPage(page, mkDistributeDividend);
	}
	
	@Transactional(readOnly = false)
	public void save(MkDistributeDividend mkDistributeDividend) {
		super.save(mkDistributeDividend);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkDistributeDividend mkDistributeDividend) {
		super.delete(mkDistributeDividend);
	}


}