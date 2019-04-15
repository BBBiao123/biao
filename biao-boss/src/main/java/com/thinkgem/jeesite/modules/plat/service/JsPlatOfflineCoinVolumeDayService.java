/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatOfflineCoinVolumeDay;
import com.thinkgem.jeesite.modules.plat.dao.JsPlatOfflineCoinVolumeDayDao;

/**
 * c2c银商和广告商对账Service
 * @author ruoyu
 * @version 2018-10-31
 */
@Service
@Transactional(readOnly = true)
public class JsPlatOfflineCoinVolumeDayService extends CrudService<JsPlatOfflineCoinVolumeDayDao, JsPlatOfflineCoinVolumeDay> {

	public JsPlatOfflineCoinVolumeDay get(String id) {
		return super.get(id);
	}
	
	public List<JsPlatOfflineCoinVolumeDay> findList(JsPlatOfflineCoinVolumeDay jsPlatOfflineCoinVolumeDay) {
		return super.findList(jsPlatOfflineCoinVolumeDay);
	}
	
	public Page<JsPlatOfflineCoinVolumeDay> findPage(Page<JsPlatOfflineCoinVolumeDay> page, JsPlatOfflineCoinVolumeDay jsPlatOfflineCoinVolumeDay) {
		return super.findPage(page, jsPlatOfflineCoinVolumeDay);
	}
	
	@Transactional(readOnly = false)
	public void save(JsPlatOfflineCoinVolumeDay jsPlatOfflineCoinVolumeDay) {
		super.save(jsPlatOfflineCoinVolumeDay);
	}
	
	@Transactional(readOnly = false)
	public void delete(JsPlatOfflineCoinVolumeDay jsPlatOfflineCoinVolumeDay) {
		super.delete(jsPlatOfflineCoinVolumeDay);
	}
	
}