/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import com.thinkgem.jeesite.common.utils.JedisUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatMainCnb;
import com.thinkgem.jeesite.modules.plat.dao.JsPlatMainCnbDao;

/**
 * 主区币兑人民币汇率Service
 * @author dongfeng
 * @version 2018-08-21
 */
@Service
@Transactional(readOnly = true)
public class JsPlatMainCnbService extends CrudService<JsPlatMainCnbDao, JsPlatMainCnb> {

	private static final String REDIS_MAIN_CNB_KEY = "coin:ex:cnb";

	public JsPlatMainCnb get(String id) {
		return super.get(id);
	}
	
	public List<JsPlatMainCnb> findList(JsPlatMainCnb jsPlatMainCnb) {
		return super.findList(jsPlatMainCnb);
	}
	
	public Page<JsPlatMainCnb> findPage(Page<JsPlatMainCnb> page, JsPlatMainCnb jsPlatMainCnb) {
		return super.findPage(page, jsPlatMainCnb);
	}
	
	@Transactional(readOnly = false)
	public void save(JsPlatMainCnb jsPlatMainCnb) {
		super.save(jsPlatMainCnb);
		deleteRedisMainCnb();
	}
	
	@Transactional(readOnly = false)
	public void delete(JsPlatMainCnb jsPlatMainCnb) {
		super.delete(jsPlatMainCnb);
		deleteRedisMainCnb();
	}

	private void deleteRedisMainCnb() {
		JedisUtils.delObject(REDIS_MAIN_CNB_KEY);
	}
	
}