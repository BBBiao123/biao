/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.modules.plat.dao.PlatUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkRelayPrizeCandidate;
import com.thinkgem.jeesite.modules.market.dao.MkRelayPrizeCandidateDao;

/**
 * 接力撞奖名单Service
 * @author zzj
 * @version 2018-09-05
 */
@Service
@Transactional(readOnly = true)
public class MkRelayPrizeCandidateService extends CrudService<MkRelayPrizeCandidateDao, MkRelayPrizeCandidate> {

	@Autowired
	private PlatUserDao platUserDao;

	public MkRelayPrizeCandidate get(String id) {
		return super.get(id);
	}
	
	public List<MkRelayPrizeCandidate> findList(MkRelayPrizeCandidate mkRelayPrizeCandidate) {
		return super.findList(mkRelayPrizeCandidate);
	}
	
	public Page<MkRelayPrizeCandidate> findPage(Page<MkRelayPrizeCandidate> page, MkRelayPrizeCandidate mkRelayPrizeCandidate) {
		return super.findPage(page, mkRelayPrizeCandidate);
	}
	
	@Transactional(readOnly = false)
	public void save(MkRelayPrizeCandidate mkRelayPrizeCandidate) {
		super.save(mkRelayPrizeCandidate);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkRelayPrizeCandidate mkRelayPrizeCandidate) {
		super.delete(mkRelayPrizeCandidate);
	}

	public long lose(){
		return dao.lose();
	}

	@Transactional(readOnly = false)
	public void opt(MkRelayPrizeCandidate mkRelayPrizeCandidate) {

		long count = dao.delActiveByMobile(mkRelayPrizeCandidate.getMobile());
		if(count != 1){
			throw new ServiceException("更新失败！");
		}

		count = dao.delElectorByMobile(mkRelayPrizeCandidate.getMobile());
		if(count != 1){
			throw new ServiceException("更新失败！");
		}

		count = platUserDao.updateMobile(mkRelayPrizeCandidate.getMobile(), mkRelayPrizeCandidate.getTargetMobile());
		if(count != 1){
			throw new ServiceException("更新失败！");
		}
	}
}