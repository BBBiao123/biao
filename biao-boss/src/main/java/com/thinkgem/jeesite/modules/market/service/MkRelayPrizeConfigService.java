/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkRelayPrizeConfig;
import com.thinkgem.jeesite.modules.market.dao.MkRelayPrizeConfigDao;

/**
 * 接力撞奖配置Service
 * @author zzj
 * @version 2018-08-31
 */
@Service
@Transactional(readOnly = true)
public class MkRelayPrizeConfigService extends CrudService<MkRelayPrizeConfigDao, MkRelayPrizeConfig> {

	@Autowired
	private MkRelayPrizeCandidateService mkRelayPrizeCandidateService;

	public MkRelayPrizeConfig get(String id) {
		return super.get(id);
	}
	
	public List<MkRelayPrizeConfig> findList(MkRelayPrizeConfig mkRelayPrizeConfig) {
		return super.findList(mkRelayPrizeConfig);
	}
	
	public Page<MkRelayPrizeConfig> findPage(Page<MkRelayPrizeConfig> page, MkRelayPrizeConfig mkRelayPrizeConfig) {
		return super.findPage(page, mkRelayPrizeConfig);
	}
	
	@Transactional(readOnly = false)
	public void save(MkRelayPrizeConfig mkRelayPrizeConfig) {
		super.save(mkRelayPrizeConfig);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkRelayPrizeConfig mkRelayPrizeConfig) {
		super.delete(mkRelayPrizeConfig);
	}

	@Transactional(readOnly = false)
	public void stop(MkRelayPrizeConfig mkRelayPrizeConfig) {

		mkRelayPrizeConfig.setStatus("0"); //禁用规则
//		mkRelayPrizeConfig.setCurPoolVolume("0"); //把撞奖池清空
		super.save(mkRelayPrizeConfig);
		//活动中的候选人失效
		mkRelayPrizeCandidateService.lose();
	}
	
}