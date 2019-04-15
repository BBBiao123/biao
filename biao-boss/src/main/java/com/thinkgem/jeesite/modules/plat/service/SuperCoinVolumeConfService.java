/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.SuperCoinVolumeConf;
import com.thinkgem.jeesite.modules.plat.dao.SuperCoinVolumeConfDao;

/**
 * 超级钱包配置Service
 * @author zzj
 * @version 2018-12-25
 */
@Service
@Transactional(readOnly = true)
public class SuperCoinVolumeConfService extends CrudService<SuperCoinVolumeConfDao, SuperCoinVolumeConf> {

	public SuperCoinVolumeConf get(String id) {
		return super.get(id);
	}
	
	public List<SuperCoinVolumeConf> findList(SuperCoinVolumeConf superCoinVolumeConf) {
		return super.findList(superCoinVolumeConf);
	}
	
	public Page<SuperCoinVolumeConf> findPage(Page<SuperCoinVolumeConf> page, SuperCoinVolumeConf superCoinVolumeConf) {
		return super.findPage(page, superCoinVolumeConf);
	}
	
	@Transactional(readOnly = false)
	public void save(SuperCoinVolumeConf superCoinVolumeConf) {
		super.save(superCoinVolumeConf);
	}
	
	@Transactional(readOnly = false)
	public void delete(SuperCoinVolumeConf superCoinVolumeConf) {
		super.delete(superCoinVolumeConf);
	}
	
}