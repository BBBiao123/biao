/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.Mk2PopularizeRegisterCoin;
import com.thinkgem.jeesite.modules.plat.dao.Mk2PopularizeRegisterCoinDao;

/**
 * 注册用户送币Service
 * @author dongfeng
 * @version 2018-07-20
 */
@Service
@Transactional(readOnly = true)
public class Mk2PopularizeRegisterCoinService extends CrudService<Mk2PopularizeRegisterCoinDao, Mk2PopularizeRegisterCoin> {

	public Mk2PopularizeRegisterCoin get(String id) {
		return super.get(id);
	}
	
	public List<Mk2PopularizeRegisterCoin> findList(Mk2PopularizeRegisterCoin mk2PopularizeRegisterCoin) {
		return super.findList(mk2PopularizeRegisterCoin);
	}
	
	public Page<Mk2PopularizeRegisterCoin> findPage(Page<Mk2PopularizeRegisterCoin> page, Mk2PopularizeRegisterCoin mk2PopularizeRegisterCoin) {
		return super.findPage(page, mk2PopularizeRegisterCoin);
	}
	
	@Transactional(readOnly = false)
	public void save(Mk2PopularizeRegisterCoin mk2PopularizeRegisterCoin) {
		super.save(mk2PopularizeRegisterCoin);
	}
	
	@Transactional(readOnly = false)
	public void delete(Mk2PopularizeRegisterCoin mk2PopularizeRegisterCoin) {
		super.delete(mk2PopularizeRegisterCoin);
	}
	
}