/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.report.entity.TraderVolumeSnapshot;
import com.thinkgem.jeesite.modules.report.dao.TraderVolumeSnapshotDao;

/**
 * 操盘手资产快照Service
 * @author zzj
 * @version 2018-12-21
 */
@Service
@Transactional(readOnly = true)
public class TraderVolumeSnapshotService extends CrudService<TraderVolumeSnapshotDao, TraderVolumeSnapshot> {

	public TraderVolumeSnapshot get(String id) {
		return super.get(id);
	}
	
	public List<TraderVolumeSnapshot> findList(TraderVolumeSnapshot traderVolumeSnapshot) {
		return super.findList(traderVolumeSnapshot);
	}
	
	public Page<TraderVolumeSnapshot> findPage(Page<TraderVolumeSnapshot> page, TraderVolumeSnapshot traderVolumeSnapshot) {
		return super.findPage(page, traderVolumeSnapshot);
	}
	
	@Transactional(readOnly = false)
	public void save(TraderVolumeSnapshot traderVolumeSnapshot) {
		super.save(traderVolumeSnapshot);
	}
	
	@Transactional(readOnly = false)
	public void delete(TraderVolumeSnapshot traderVolumeSnapshot) {
		super.delete(traderVolumeSnapshot);
	}
	
}