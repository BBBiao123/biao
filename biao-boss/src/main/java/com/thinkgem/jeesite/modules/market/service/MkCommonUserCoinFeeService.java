/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkCommonUserCoinFee;
import com.thinkgem.jeesite.modules.market.dao.MkCommonUserCoinFeeDao;

/**
 * 平台收益流水Service
 * @author dongfeng
 * @version 2018-08-09
 */
@Service
@Transactional(readOnly = true)
public class MkCommonUserCoinFeeService extends CrudService<MkCommonUserCoinFeeDao, MkCommonUserCoinFee> {

	public MkCommonUserCoinFee get(String id) {
		return super.get(id);
	}
	
	public List<MkCommonUserCoinFee> findList(MkCommonUserCoinFee mkCommonUserCoinFee) {
		return super.findList(mkCommonUserCoinFee);
	}
	
	public Page<MkCommonUserCoinFee> findPage(Page<MkCommonUserCoinFee> page, MkCommonUserCoinFee mkCommonUserCoinFee) {
		return super.findPage(page, mkCommonUserCoinFee);
	}
	
	@Transactional(readOnly = false)
	public void save(MkCommonUserCoinFee mkCommonUserCoinFee) {
		super.save(mkCommonUserCoinFee);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkCommonUserCoinFee mkCommonUserCoinFee) {
		super.delete(mkCommonUserCoinFee);
	}
	
}