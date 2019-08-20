/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.plat.constant.HashSelect;
import com.thinkgem.jeesite.modules.plat.constant.UserCoinVolumeBillStatusEnum;
import com.thinkgem.jeesite.modules.plat.constant.UserCoinVolumeEventEnum;
import com.thinkgem.jeesite.modules.plat.dao.UserChangeRecordDao;
import com.thinkgem.jeesite.modules.plat.dao.UserCoinVolumeDao;
import com.thinkgem.jeesite.modules.plat.entity.*;
import com.thinkgem.jeesite.modules.sys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 币币资产Service
 * @author dazi
 * @version 2018-04-27
 */
@Service
@Transactional(readOnly = true)
public class UserChangeRecordService extends CrudService<UserChangeRecordDao, UserChangeRecordVolume> {

	private HashSelect hashSelect = HashSelect.create(Integer.parseInt(Global.getConfig("plat.deploy.size")));
	

	public UserChangeRecordVolume get(String id) {
		return super.get(id);
	}

	
	public Page<UserChangeRecordVolume> findPage(Page<UserChangeRecordVolume> page, UserChangeRecordVolume userCoinVolume) {
		return super.findPage(page, userCoinVolume);
	}


	public Page<UserChangeRecordVolume> findIncomePage(Page<UserChangeRecordVolume> page, UserChangeRecordVolume userCoinVolume) {
		userCoinVolume.setPage(page);
		page.setList(dao.findIncomeList(userCoinVolume));
		return page;

	}


	public Page<UserChangeRecordVolume> findBalanceList(Page<UserChangeRecordVolume> page, UserChangeRecordVolume userCoinVolume) {
		userCoinVolume.setPage(page);
		page.setList(dao.findBalanceList(userCoinVolume));
		return page;

	}


}