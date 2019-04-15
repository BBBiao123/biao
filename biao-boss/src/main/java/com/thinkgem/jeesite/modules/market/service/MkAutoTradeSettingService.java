/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import com.thinkgem.jeesite.modules.plat.entity.ExPair;
import com.thinkgem.jeesite.modules.plat.service.ExPairService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkAutoTradeSetting;
import com.thinkgem.jeesite.modules.market.dao.MkAutoTradeSettingDao;

/**
 * 自动交易Service
 * @author zhangzijun
 * @version 2018-08-07
 */
@Service
@Transactional(readOnly = true)
public class MkAutoTradeSettingService extends CrudService<MkAutoTradeSettingDao, MkAutoTradeSetting> {

	@Autowired
	private ExPairService exPairService;

	public MkAutoTradeSetting get(String id) {
		return super.get(id);
	}
	
	public List<MkAutoTradeSetting> findList(MkAutoTradeSetting mkAutoTradeSetting) {
		return super.findList(mkAutoTradeSetting);
	}
	
	public Page<MkAutoTradeSetting> findPage(Page<MkAutoTradeSetting> page, MkAutoTradeSetting mkAutoTradeSetting) {
		return super.findPage(page, mkAutoTradeSetting);
	}
	
	@Transactional(readOnly = false)
	public void save(MkAutoTradeSetting mkAutoTradeSetting) {

		if(StringUtils.isNotEmpty(mkAutoTradeSetting.getExPairId())){
			ExPair exPair = exPairService.get(mkAutoTradeSetting.getExPairId());
			if(null != exPair){
				mkAutoTradeSetting.setPricePrecision(exPair.getPricePrecision());
				mkAutoTradeSetting.setVolumePrecision(exPair.getVolumePrecision());
				mkAutoTradeSetting.setExMinVolume(exPair.getMinVolume());
				mkAutoTradeSetting.setExMaxVolume(exPair.getMaxVolume());
			}
		}

		super.save(mkAutoTradeSetting);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkAutoTradeSetting mkAutoTradeSetting) {
		super.delete(mkAutoTradeSetting);
	}
	
}