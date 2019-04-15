/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatUserCoinVolumeBill;
import com.thinkgem.jeesite.modules.plat.dao.JsPlatUserCoinVolumeBillDao;

/**
 * 用户资产账单Service
 * @author ruoyu
 * @version 2019-01-02
 */
@Service
@Transactional(readOnly = true)
public class JsPlatUserCoinVolumeBillService extends CrudService<JsPlatUserCoinVolumeBillDao, JsPlatUserCoinVolumeBill> {

	public JsPlatUserCoinVolumeBill get(String id) {
		return super.get(id);
	}
	
	public List<JsPlatUserCoinVolumeBill> findList(JsPlatUserCoinVolumeBill jsPlatUserCoinVolumeBill) {
		return super.findList(jsPlatUserCoinVolumeBill);
	}
	
	public Page<JsPlatUserCoinVolumeBill> findPage(Page<JsPlatUserCoinVolumeBill> page, JsPlatUserCoinVolumeBill jsPlatUserCoinVolumeBill) {
		return super.findPage(page, jsPlatUserCoinVolumeBill);
	}
	
	@Transactional(readOnly = false)
	public void save(JsPlatUserCoinVolumeBill jsPlatUserCoinVolumeBill) {
		super.save(jsPlatUserCoinVolumeBill);
	}
	
	@Transactional(readOnly = false)
	public void delete(JsPlatUserCoinVolumeBill jsPlatUserCoinVolumeBill) {
		super.delete(jsPlatUserCoinVolumeBill);
	}
	
}