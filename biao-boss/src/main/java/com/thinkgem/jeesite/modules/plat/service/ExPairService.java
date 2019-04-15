/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import com.thinkgem.jeesite.common.utils.JedisUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.ExPair;
import com.thinkgem.jeesite.modules.plat.dao.ExPairDao;

/**
 * 币币交易对Service
 * @author dazi
 * @version 2018-04-26
 */
@Service
@Transactional(readOnly = true)
public class ExPairService extends CrudService<ExPairDao, ExPair> {

	public static final  String TRADE_EX_PAIR = "trade:ex:pair";

	public ExPair get(String id) {
		return super.get(id);
	}
	
	public List<ExPair> findList(ExPair exPair) {
		return super.findList(exPair);
	}
	
	public Page<ExPair> findPage(Page<ExPair> page, ExPair exPair) {
		return super.findPage(page, exPair);
	}
	
	@Transactional(readOnly = false)
	public void save(ExPair exPair) {
		super.save(exPair);
		JedisUtils.del(TRADE_EX_PAIR);
	}
	
	@Transactional(readOnly = false)
	public void delete(ExPair exPair) {
		super.delete(exPair);
		JedisUtils.del(TRADE_EX_PAIR);
	}
	
}