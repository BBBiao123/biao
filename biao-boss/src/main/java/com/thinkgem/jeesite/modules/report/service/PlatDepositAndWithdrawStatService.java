/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.report.dao.PlatDepositAndWithdrawStatDao;
import com.thinkgem.jeesite.modules.report.entity.PlatDepositAndWithdrawStat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 操盘手资产快照Service
 * @author zzj
 * @version 2018-12-21
 */
@Service
@Transactional(readOnly = true)
public class PlatDepositAndWithdrawStatService extends CrudService<PlatDepositAndWithdrawStatDao, PlatDepositAndWithdrawStat> {

	public PlatDepositAndWithdrawStat get(String id) {
		return super.get(id);
	}
	
	public List<PlatDepositAndWithdrawStat> findList(PlatDepositAndWithdrawStat platDepositAndWithdrawStat) {
		return super.findList(platDepositAndWithdrawStat);
	}
	
	public Page<PlatDepositAndWithdrawStat> findPage(Page<PlatDepositAndWithdrawStat> page, PlatDepositAndWithdrawStat platDepositAndWithdrawStat) {
		return super.findPage(page, platDepositAndWithdrawStat);
	}
	
	@Transactional(readOnly = false)
	public void save(PlatDepositAndWithdrawStat platDepositAndWithdrawStat) {
		super.save(platDepositAndWithdrawStat);
	}
	
	@Transactional(readOnly = false)
	public void delete(PlatDepositAndWithdrawStat platDepositAndWithdrawStat) {
		super.delete(platDepositAndWithdrawStat);
	}
	
}