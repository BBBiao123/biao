/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.disruptor.DisruptorData;
import com.thinkgem.jeesite.modules.plat.dao.JsPlatUserAirdropDao;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatUserAirdrop;

/**
 * 用户空头币种Service
 * @author ruoyu
 * @version 2018-11-15
 */
@Service
@Transactional(readOnly = true)
public class JsPlatUserAirdropService extends CrudService<JsPlatUserAirdropDao, JsPlatUserAirdrop> {

	public JsPlatUserAirdrop get(String id) {
		return super.get(id);
	}
	
	public List<JsPlatUserAirdrop> findList(JsPlatUserAirdrop jsPlatUserAirdrop) {
		return super.findList(jsPlatUserAirdrop);
	}
	
	public Page<JsPlatUserAirdrop> findPage(Page<JsPlatUserAirdrop> page, JsPlatUserAirdrop jsPlatUserAirdrop) {
		return super.findPage(page, jsPlatUserAirdrop);
	}
	
	@Transactional(readOnly = false)
	public void update(JsPlatUserAirdrop jsPlatUserAirdrop) {
		super.save(jsPlatUserAirdrop);
	}
	
	@Transactional(readOnly = false)
	public void save(JsPlatUserAirdrop jsPlatUserAirdrop) {
		super.save(jsPlatUserAirdrop);
		//开始业务空头
		DisruptorData.savePublish(jsPlatUserAirdrop, 1);
	}
	
	@Transactional(readOnly = false)
	public void delete(JsPlatUserAirdrop jsPlatUserAirdrop) {
		super.delete(jsPlatUserAirdrop);
	}
	
}