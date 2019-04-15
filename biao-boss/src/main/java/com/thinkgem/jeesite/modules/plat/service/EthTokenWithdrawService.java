/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.EthTokenWithdraw;
import com.thinkgem.jeesite.modules.plat.dao.EthTokenWithdrawDao;

/**
 * eth token withdrawService
 * @author ruoyu
 * @version 2018-07-03
 */
@Service
@Transactional(readOnly = true)
public class EthTokenWithdrawService extends CrudService<EthTokenWithdrawDao, EthTokenWithdraw> {

	public EthTokenWithdraw get(String id) {
		return super.get(id);
	}
	
	public List<EthTokenWithdraw> findList(EthTokenWithdraw ethTokenWithdraw) {
		return super.findList(ethTokenWithdraw);
	}
	
	public Page<EthTokenWithdraw> findPage(Page<EthTokenWithdraw> page, EthTokenWithdraw ethTokenWithdraw) {
		return super.findPage(page, ethTokenWithdraw);
	}
	
	@Transactional(readOnly = false)
	public void save(EthTokenWithdraw ethTokenWithdraw) {
		super.save(ethTokenWithdraw);
	}
	
	@Transactional(readOnly = false)
	public void delete(EthTokenWithdraw ethTokenWithdraw) {
		super.delete(ethTokenWithdraw);
	}
	
}