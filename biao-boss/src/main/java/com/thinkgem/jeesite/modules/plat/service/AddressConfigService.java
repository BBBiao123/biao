/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.AddressConfig;
import com.thinkgem.jeesite.modules.plat.dao.AddressConfigDao;

/**
 * eth_token的volumeService
 * @author ruoyu
 * @version 2018-07-03
 */
@Service
@Transactional(readOnly = true)
public class AddressConfigService extends CrudService<AddressConfigDao, AddressConfig> {

	public AddressConfig get(String id) {
		return super.get(id);
	}
	
	public List<AddressConfig> findList(AddressConfig addressConfig) {
		return super.findList(addressConfig);
	}
	
	public Page<AddressConfig> findPage(Page<AddressConfig> page, AddressConfig addressConfig) {
		return super.findPage(page, addressConfig);
	}
	
	@Transactional(readOnly = false)
	public void save(AddressConfig addressConfig) {
		super.save(addressConfig);
	}
	
	@Transactional(readOnly = false)
	public void delete(AddressConfig addressConfig) {
		super.delete(addressConfig);
	}
	
}