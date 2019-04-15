/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import com.thinkgem.jeesite.common.utils.JedisUtils;
import com.thinkgem.jeesite.modules.market.entity.MkAutoTradeSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkAutoTradeMonitor;
import com.thinkgem.jeesite.modules.market.dao.MkAutoTradeMonitorDao;

/**
 * 自动交易监控Service
 * @author zhangzijun
 * @version 2018-08-13
 */
@Service
@Transactional(readOnly = true)
public class MkAutoTradeMonitorService extends CrudService<MkAutoTradeMonitorDao, MkAutoTradeMonitor> {

	public static final  String MK_AUTO_TRADE_MONITOR = "market:trade:monitor";

	@Autowired
	private MkAutoTradeSettingService mkAutoTradeSettingService;

	public MkAutoTradeMonitor get(String id) {
		return super.get(id);
	}
	
	public List<MkAutoTradeMonitor> findList(MkAutoTradeMonitor mkAutoTradeMonitor) {
		return super.findList(mkAutoTradeMonitor);
	}
	
	public Page<MkAutoTradeMonitor> findPage(Page<MkAutoTradeMonitor> page, MkAutoTradeMonitor mkAutoTradeMonitor) {
		return super.findPage(page, mkAutoTradeMonitor);
	}
	
	@Transactional(readOnly = false)
	public void save(MkAutoTradeMonitor mkAutoTradeMonitor) {

		if("4".equals(mkAutoTradeMonitor.getStatus())){
			MkAutoTradeSetting mkAutoTradeSetting = mkAutoTradeSettingService.get(mkAutoTradeMonitor.getSettingId());
			if(null != mkAutoTradeSetting){
				mkAutoTradeSetting.setStatus("0");
				mkAutoTradeSettingService.save(mkAutoTradeSetting);
			}
		}
		super.save(mkAutoTradeMonitor);
		JedisUtils.del(MK_AUTO_TRADE_MONITOR);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkAutoTradeMonitor mkAutoTradeMonitor) {
		super.delete(mkAutoTradeMonitor);
	}

	public MkAutoTradeMonitor findActiveBySetting(String settingId){
		JedisUtils.del(MK_AUTO_TRADE_MONITOR);
		return dao.findAcitveBySetting(settingId);
	}
	
}