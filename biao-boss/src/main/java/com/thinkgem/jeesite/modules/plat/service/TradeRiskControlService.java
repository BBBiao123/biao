/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.TradeRiskControl;
import com.thinkgem.jeesite.modules.plat.dao.TradeRiskControlDao;

/**
 * 交易风险控制Service
 * @author xiaoyu
 * @version 2018-09-10
 */
@Service
@Transactional(readOnly = true)
public class TradeRiskControlService extends CrudService<TradeRiskControlDao, TradeRiskControl> {

	public TradeRiskControl get(String id) {
		return super.get(id);
	}
	
	public List<TradeRiskControl> findList(TradeRiskControl tradeRiskControl) {
		return super.findList(tradeRiskControl);
	}
	
	public Page<TradeRiskControl> findPage(Page<TradeRiskControl> page, TradeRiskControl tradeRiskControl) {
		return super.findPage(page, tradeRiskControl);
	}
	
	@Transactional(readOnly = false)
	public void save(TradeRiskControl tradeRiskControl) {
		super.save(tradeRiskControl);
	}
	
	@Transactional(readOnly = false)
	public void delete(TradeRiskControl tradeRiskControl) {
		super.delete(tradeRiskControl);
	}
	
}