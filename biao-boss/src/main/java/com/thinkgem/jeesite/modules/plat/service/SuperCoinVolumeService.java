/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.SuperCoinVolume;
import com.thinkgem.jeesite.modules.plat.dao.SuperCoinVolumeDao;

/**
 * 超级钱包资产Service
 * @author zzj
 * @version 2018-12-25
 */
@Service
@Transactional(readOnly = true)
public class SuperCoinVolumeService extends CrudService<SuperCoinVolumeDao, SuperCoinVolume> {

	public SuperCoinVolume get(String id) {
		return super.get(id);
	}
	
	public List<SuperCoinVolume> findList(SuperCoinVolume superCoinVolume) {
		return super.findList(superCoinVolume);
	}
	
	public Page<SuperCoinVolume> findPage(Page<SuperCoinVolume> page, SuperCoinVolume superCoinVolume) {
		return super.findPage(page, superCoinVolume);
	}
	
	@Transactional(readOnly = false)
	public void save(SuperCoinVolume superCoinVolume) {
		super.save(superCoinVolume);
	}
	
	@Transactional(readOnly = false)
	public void delete(SuperCoinVolume superCoinVolume) {
		super.delete(superCoinVolume);
	}
	
}