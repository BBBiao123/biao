/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.MkUserRegisterLotteryLog;
import com.thinkgem.jeesite.modules.plat.dao.MkUserRegisterLotteryLogDao;

/**
 * 注册活动抽奖记录Service
 * @author xiaoyu
 * @version 2018-10-25
 */
@Service
@Transactional(readOnly = true)
public class MkUserRegisterLotteryLogService extends CrudService<MkUserRegisterLotteryLogDao, MkUserRegisterLotteryLog> {

	public MkUserRegisterLotteryLog get(String id) {
		return super.get(id);
	}
	
	public List<MkUserRegisterLotteryLog> findList(MkUserRegisterLotteryLog mkUserRegisterLotteryLog) {
		return super.findList(mkUserRegisterLotteryLog);
	}
	
	public Page<MkUserRegisterLotteryLog> findPage(Page<MkUserRegisterLotteryLog> page, MkUserRegisterLotteryLog mkUserRegisterLotteryLog) {
		return super.findPage(page, mkUserRegisterLotteryLog);
	}
	
	@Transactional(readOnly = false)
	public void save(MkUserRegisterLotteryLog mkUserRegisterLotteryLog) {
		super.save(mkUserRegisterLotteryLog);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkUserRegisterLotteryLog mkUserRegisterLotteryLog) {
		super.delete(mkUserRegisterLotteryLog);
	}
	
}