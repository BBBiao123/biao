/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.EthTokenVolume;
import com.thinkgem.jeesite.modules.plat.dao.EthTokenVolumeDao;

/**
 * eth_token的volumeService
 * @author ruoyu
 * @version 2018-07-03
 */
@Service
@Transactional(readOnly = true)
public class EthTokenVolumeService extends CrudService<EthTokenVolumeDao, EthTokenVolume> {

	public EthTokenVolume get(String id) {
		return super.get(id);
	}
	
	public List<EthTokenVolume> findList(EthTokenVolume ethTokenVolume) {
		return super.findList(ethTokenVolume);
	}
	
	public Page<EthTokenVolume> findPage(Page<EthTokenVolume> page, EthTokenVolume ethTokenVolume) {
		return super.findPage(page, ethTokenVolume);
	}
	
	@Transactional(readOnly = false)
	public void save(EthTokenVolume ethTokenVolume) {
		super.save(ethTokenVolume);
	}
	
	@Transactional(readOnly = false)
	public void delete(EthTokenVolume ethTokenVolume) {
		super.delete(ethTokenVolume);
	}
	
}