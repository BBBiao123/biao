/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkDividendStat;
import com.thinkgem.jeesite.modules.market.dao.MkDividendStatDao;

/**
 * 分红统计Service
 * @author zhangzijun
 * @version 2018-08-02
 */
@Service
@Transactional(readOnly = true)
public class MkDividendStatService extends CrudService<MkDividendStatDao, MkDividendStat> {

	public MkDividendStat get(String id) {
		return super.get(id);
	}
	
	public List<MkDividendStat> findList(MkDividendStat mkDividendStat) {
		return super.findList(mkDividendStat);
	}
	
	public Page<MkDividendStat> findPage(Page<MkDividendStat> page, MkDividendStat mkDividendStat) {
		return super.findPage(page, mkDividendStat);
	}
	
	@Transactional(readOnly = false)
	public void save(MkDividendStat mkDividendStat) {
		super.save(mkDividendStat);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkDividendStat mkDividendStat) {
		super.delete(mkDividendStat);
	}
	
}