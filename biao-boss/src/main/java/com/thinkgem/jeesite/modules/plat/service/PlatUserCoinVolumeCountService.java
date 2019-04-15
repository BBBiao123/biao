/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.math.BigDecimal;
import java.util.*;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.modules.plat.entity.PlatUserCoinCountShow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.PlatUserCoinVolumeCount;
import com.thinkgem.jeesite.modules.plat.dao.PlatUserCoinVolumeCountDao;

/**
 * 持币数量统计Service
 * @author dongfeng
 * @version 2018-09-04
 */
@Service
@Transactional(readOnly = true)
public class PlatUserCoinVolumeCountService extends CrudService<PlatUserCoinVolumeCountDao, PlatUserCoinVolumeCount> {

	public PlatUserCoinVolumeCount get(String id) {
		return super.get(id);
	}
	
	public List<PlatUserCoinVolumeCount> findList(PlatUserCoinVolumeCount platUserCoinVolumeCount) {
		return super.findList(platUserCoinVolumeCount);
	}
	
	public List<PlatUserCoinVolumeCount> findCurrentCount(PlatUserCoinVolumeCount platUserCoinVolumeCount) {
		return dao.findList(platUserCoinVolumeCount);
	}

	public PlatUserCoinCountShow queryByDate(PlatUserCoinVolumeCount platUserCoinVolumeCount) {
		List<PlatUserCoinVolumeCount> countList = dao.queryByDate(platUserCoinVolumeCount);
		Map<String, List<Long>> personMap = Maps.newLinkedHashMap();
		Map<String, List<BigDecimal>> coinMap = Maps.newLinkedHashMap();

		List<String> typeList = new ArrayList<>();
		Map<String, String> typeMap = Maps.newHashMap();
		List<String> dateList = new ArrayList<>();
		Map<String, String> dateMap = Maps.newHashMap();

		countList.forEach(e -> {
			// 人数列表
			List<Long> personList = personMap.get(e.getTypeDesc());
			if (Objects.isNull(personList)) {
				personList = new ArrayList<Long>();
				personMap.put(e.getTypeDesc(), personList);
			}
			personList.add(e.getPersonCount());

			// 持币量列表
			List<BigDecimal> coinVolumeList = coinMap.get(e.getTypeDesc());
			if (Objects.isNull(coinVolumeList)) {
				coinVolumeList = new ArrayList<BigDecimal>();
				coinMap.put(e.getTypeDesc(), coinVolumeList);
			}
			coinVolumeList.add(e.getHoldCoinVolume());

			// 时间列表
			if ( ! dateMap.containsKey(e.getCountDateStr())) {
				dateList.add(e.getCountDateStr());
				dateMap.put(e.getCountDateStr(), e.getCountDateStr());
			}

			// 类型列表
			if ( ! typeMap.containsKey(e.getTypeDesc())) {
				typeList.add(e.getTypeDesc());
				typeMap.put(e.getTypeDesc(), e.getTypeDesc());
			}
		});

		PlatUserCoinCountShow show = new PlatUserCoinCountShow();
		show.setDateList(dateList);
		show.setTypeList(typeList);
		show.setPersonMap(personMap);
		show.setCoinMap(coinMap);
		return show;
	}

	@Transactional(readOnly = false)
	public void save(PlatUserCoinVolumeCount platUserCoinVolumeCount) {
		super.save(platUserCoinVolumeCount);
	}
	
	@Transactional(readOnly = false)
	public void delete(PlatUserCoinVolumeCount platUserCoinVolumeCount) {
		super.delete(platUserCoinVolumeCount);
	}
	
}