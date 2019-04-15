/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.market.entity.MkLuckyDrawPlayer;
import com.thinkgem.jeesite.modules.market.dao.MkLuckyDrawPlayerDao;

/**
 * 抽奖活动参与者Service
 * @author zzj
 * @version 2018-11-01
 */
@Service
@Transactional(readOnly = true)
public class MkLuckyDrawPlayerService extends CrudService<MkLuckyDrawPlayerDao, MkLuckyDrawPlayer> {

	public MkLuckyDrawPlayer get(String id) {
		return super.get(id);
	}
	
	public List<MkLuckyDrawPlayer> findList(MkLuckyDrawPlayer mkLuckyDrawPlayer) {
		return super.findList(mkLuckyDrawPlayer);
	}
	
	public Page<MkLuckyDrawPlayer> findPage(Page<MkLuckyDrawPlayer> page, MkLuckyDrawPlayer mkLuckyDrawPlayer) {
		return super.findPage(page, mkLuckyDrawPlayer);
	}
	
	@Transactional(readOnly = false)
	public void save(MkLuckyDrawPlayer mkLuckyDrawPlayer) {
		super.save(mkLuckyDrawPlayer);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkLuckyDrawPlayer mkLuckyDrawPlayer) {
		super.delete(mkLuckyDrawPlayer);
	}

	public void updateUnLucky(MkLuckyDrawPlayer mkLuckyDrawPlayer){
		dao.updateUnLucky(mkLuckyDrawPlayer);
	}
	
}