
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.dao.CoinDao;

/**
 * 币种资料Service
 * @author dazi
 * @version 2018-04-25
 */
@Service
@Transactional(readOnly = true)
public class CoinService extends CrudService<CoinDao, Coin> {

	public Coin get(String id) {
		return super.get(id);
	}
	
	public List<Coin> findList(Coin coin) {
		return super.findList(coin);
	}
	
	public Page<Coin> findPage(Page<Coin> page, Coin coin) {
		return super.findPage(page, coin);
	}
	
	@Transactional(readOnly = false)
	public void save(Coin coin) {
		super.save(coin);
	}
	
	@Transactional(readOnly = false)
	public void delete(Coin coin) {
		super.delete(coin);
	}
	
}