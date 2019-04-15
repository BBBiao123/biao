/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkAutoTradeOrder;
import com.thinkgem.jeesite.modules.market.dao.MkAutoTradeOrderDao;

/**
 * 自动交易挂单Service
 * @author zhangzijun
 * @version 2018-08-13
 */
@Service
@Transactional(readOnly = true)
public class MkAutoTradeOrderService extends CrudService<MkAutoTradeOrderDao, MkAutoTradeOrder> {

	public MkAutoTradeOrder get(String id) {
		return super.get(id);
	}
	
	public List<MkAutoTradeOrder> findList(MkAutoTradeOrder mkAutoTradeOrder) {
		return super.findList(mkAutoTradeOrder);
	}
	
	public Page<MkAutoTradeOrder> findPage(Page<MkAutoTradeOrder> page, MkAutoTradeOrder mkAutoTradeOrder) {
		return super.findPage(page, mkAutoTradeOrder);
	}
	
	@Transactional(readOnly = false)
	public void save(MkAutoTradeOrder mkAutoTradeOrder) {
		super.save(mkAutoTradeOrder);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkAutoTradeOrder mkAutoTradeOrder) {
		super.delete(mkAutoTradeOrder);
	}
	
}