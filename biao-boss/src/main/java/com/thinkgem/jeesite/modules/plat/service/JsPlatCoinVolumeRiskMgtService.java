/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatCoinVolumeRiskMgt;
import com.thinkgem.jeesite.modules.plat.dao.JsPlatCoinVolumeRiskMgtDao;

/**
 * 币种资产风控管理Service
 * @author zzj
 * @version 2019-01-18
 */
@Service
@Transactional(readOnly = true)
public class JsPlatCoinVolumeRiskMgtService extends CrudService<JsPlatCoinVolumeRiskMgtDao, JsPlatCoinVolumeRiskMgt> {

	public JsPlatCoinVolumeRiskMgt get(String id) {
		return super.get(id);
	}
	
	public List<JsPlatCoinVolumeRiskMgt> findList(JsPlatCoinVolumeRiskMgt jsPlatCoinVolumeRiskMgt) {
		return super.findList(jsPlatCoinVolumeRiskMgt);
	}
	
	public Page<JsPlatCoinVolumeRiskMgt> findPage(Page<JsPlatCoinVolumeRiskMgt> page, JsPlatCoinVolumeRiskMgt jsPlatCoinVolumeRiskMgt) {
		return super.findPage(page, jsPlatCoinVolumeRiskMgt);
	}
	
	@Transactional(readOnly = false)
	public void save(JsPlatCoinVolumeRiskMgt jsPlatCoinVolumeRiskMgt) {
		super.save(jsPlatCoinVolumeRiskMgt);
	}
	
	@Transactional(readOnly = false)
	public void delete(JsPlatCoinVolumeRiskMgt jsPlatCoinVolumeRiskMgt) {
		super.delete(jsPlatCoinVolumeRiskMgt);
	}
	
}