/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import com.thinkgem.jeesite.modules.plat.entity.PlatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkSysUserExPair;
import com.thinkgem.jeesite.modules.market.dao.MkSysUserExPairDao;

/**
 * 营销用户币币对Service
 * @author zzj
 * @version 2018-08-23
 */
@Service
@Transactional(readOnly = true)
public class MkSysUserExPairService extends CrudService<MkSysUserExPairDao, MkSysUserExPair> {

	@Autowired
	private MkAutoTradeUserService mkAutoTradeUserService;
	public MkSysUserExPair get(String id) {
		return super.get(id);
	}
	
	public List<MkSysUserExPair> findList(MkSysUserExPair mkSysUserExPair) {
		return super.findList(mkSysUserExPair);
	}

	public List<MkSysUserExPair> findAllList(MkSysUserExPair mkSysUserExPair) {
		return dao.findAllList(mkSysUserExPair);
	}


	public Page<MkSysUserExPair> findPage(Page<MkSysUserExPair> page, MkSysUserExPair mkSysUserExPair) {
		return super.findPage(page, mkSysUserExPair);
	}
	
	@Transactional(readOnly = false)
	public void save(MkSysUserExPair mkSysUserExPair) {
		super.save(mkSysUserExPair);

		mkAutoTradeUserService.refresh();
	}
	
	@Transactional(readOnly = false)
	public void delete(MkSysUserExPair mkSysUserExPair) {
		super.delete(mkSysUserExPair);
		mkAutoTradeUserService.refresh();
	}

	public MkSysUserExPair getByUserAndExPair(MkSysUserExPair mkSysUserExPair){
		return dao.getByUserAndExPair(mkSysUserExPair);
	}

	public List<PlatUser> getPlatUserBySysUser(MkSysUserExPair mkSysUserExPair){
		return dao.getPlatUserBySysUser(mkSysUserExPair);
	}

	
}