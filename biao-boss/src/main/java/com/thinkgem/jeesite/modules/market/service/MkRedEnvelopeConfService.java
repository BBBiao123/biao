/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkRedEnvelopeConf;
import com.thinkgem.jeesite.modules.market.dao.MkRedEnvelopeConfDao;

/**
 * 红包配置Service
 * @author zijun
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class MkRedEnvelopeConfService extends CrudService<MkRedEnvelopeConfDao, MkRedEnvelopeConf> {

	public MkRedEnvelopeConf get(String id) {
		return super.get(id);
	}
	
	public List<MkRedEnvelopeConf> findList(MkRedEnvelopeConf mkRedEnvelopeConf) {
		return super.findList(mkRedEnvelopeConf);
	}
	
	public Page<MkRedEnvelopeConf> findPage(Page<MkRedEnvelopeConf> page, MkRedEnvelopeConf mkRedEnvelopeConf) {
		return super.findPage(page, mkRedEnvelopeConf);
	}
	
	@Transactional(readOnly = false)
	public void save(MkRedEnvelopeConf mkRedEnvelopeConf) {
		super.save(mkRedEnvelopeConf);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkRedEnvelopeConf mkRedEnvelopeConf) {
		super.delete(mkRedEnvelopeConf);
	}
	
}