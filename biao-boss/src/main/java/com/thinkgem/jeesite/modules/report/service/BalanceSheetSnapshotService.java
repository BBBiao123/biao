/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.report.entity.BalanceSheetSnapshot;
import com.thinkgem.jeesite.modules.report.dao.BalanceSheetSnapshotDao;

/**
 * 资产负债表Service
 * @author zzj
 * @version 2019-01-08
 */
@Service
@Transactional(readOnly = true)
public class BalanceSheetSnapshotService extends CrudService<BalanceSheetSnapshotDao, BalanceSheetSnapshot> {

	public BalanceSheetSnapshot get(String id) {
		return super.get(id);
	}
	
	public List<BalanceSheetSnapshot> findList(BalanceSheetSnapshot balanceSheetSnapshot) {
		return super.findList(balanceSheetSnapshot);
	}
	
	public Page<BalanceSheetSnapshot> findPage(Page<BalanceSheetSnapshot> page, BalanceSheetSnapshot balanceSheetSnapshot) {
		return super.findPage(page, balanceSheetSnapshot);
	}
	
	@Transactional(readOnly = false)
	public void save(BalanceSheetSnapshot balanceSheetSnapshot) {
		super.save(balanceSheetSnapshot);
	}
	
	@Transactional(readOnly = false)
	public void delete(BalanceSheetSnapshot balanceSheetSnapshot) {
		super.delete(balanceSheetSnapshot);
	}
	
}