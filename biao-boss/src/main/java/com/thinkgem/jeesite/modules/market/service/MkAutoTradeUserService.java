/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import com.thinkgem.jeesite.common.utils.JedisUtils;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkAutoTradeUser;
import com.thinkgem.jeesite.modules.market.dao.MkAutoTradeUserDao;

/**
 * 自动交易用户Service
 * @author zhangzijun
 * @version 2018-08-17
 */
@Service
@Transactional(readOnly = true)
public class MkAutoTradeUserService extends CrudService<MkAutoTradeUserDao, MkAutoTradeUser> {

	public static final  String MK_AUTO_TRADE_USER = "market:trade:user";

	public MkAutoTradeUser get(String id) {
		return super.get(id);
	}
	
	public List<MkAutoTradeUser> findList(MkAutoTradeUser mkAutoTradeUser) {
		return super.findList(mkAutoTradeUser);
	}
	
	public Page<MkAutoTradeUser> findPage(Page<MkAutoTradeUser> page, MkAutoTradeUser mkAutoTradeUser) {
		return super.findPage(page, mkAutoTradeUser);
	}
	
	@Transactional(readOnly = false)
	public void save(MkAutoTradeUser mkAutoTradeUser) {
		super.save(mkAutoTradeUser);
		JedisUtils.del(MK_AUTO_TRADE_USER);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkAutoTradeUser mkAutoTradeUser) {
		super.delete(mkAutoTradeUser);
		JedisUtils.del(MK_AUTO_TRADE_USER);
	}

	@Transactional(readOnly = false)
	public void refresh(){
		dao.deleteAll();
		dao.refresh();
		JedisUtils.del(MK_AUTO_TRADE_USER);
	}

	public List<PlatUser> getPlatUser(MkAutoTradeUser mkAutoTradeUser){
		return dao.getPlatUser(mkAutoTradeUser);
	}
	
}