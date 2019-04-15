/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.PlatLink;
import com.thinkgem.jeesite.modules.plat.dao.PlatLinkDao;

/**
 * 平台链接Service
 * @author dongfeng
 * @version 2018-07-03
 */
@Service
@Transactional(readOnly = true)
public class PlatLinkService extends CrudService<PlatLinkDao, PlatLink> {

	public PlatLink get(String id) {
		return super.get(id);
	}
	
	public List<PlatLink> findList(PlatLink platLink) {
		return super.findList(platLink);
	}
	
	public Page<PlatLink> findPage(Page<PlatLink> page, PlatLink platLink) {
		return super.findPage(page, platLink);
	}
	
	@Transactional(readOnly = false)
	public void save(PlatLink platLink) {
		super.save(platLink);
	}
	
	@Transactional(readOnly = false)
	public void delete(PlatLink platLink) {
		super.delete(platLink);
	}
	
}