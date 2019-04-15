/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.modules.market.dao.MkLuckyDrawConfigDao;
import com.thinkgem.jeesite.modules.market.entity.MkLuckyDrawConfig;
import com.thinkgem.jeesite.modules.market.entity.MkLuckyDrawPlayer;
import com.thinkgem.jeesite.modules.market.entity.MkLuckyDrawRecord;
import com.thinkgem.jeesite.modules.plat.dao.OfflineCoinVolumeDao;
import com.thinkgem.jeesite.modules.plat.entity.OfflineCoinVolume;
import com.thinkgem.jeesite.modules.plat.service.UserCoinVolumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 抽奖活动规则Service
 * @author zzj
 * @version 2018-11-01
 */
@Service
@Transactional(readOnly = true)
public class MkLuckyDrawConfigService extends CrudService<MkLuckyDrawConfigDao, MkLuckyDrawConfig> {

	@Autowired
	private MkLuckyDrawPlayerService mkLuckyDrawPlayerService;

	@Autowired
	private MkLuckyDrawRecordService mkLuckyDrawRecordService;

	@Autowired
	private OfflineCoinVolumeDao offlineCoinVolumeDao;

	public MkLuckyDrawConfig get(String id) {
		return super.get(id);
	}
	
	public List<MkLuckyDrawConfig> findList(MkLuckyDrawConfig mkLuckyDrawConfig) {
		return super.findList(mkLuckyDrawConfig);
	}
	
	public Page<MkLuckyDrawConfig> findPage(Page<MkLuckyDrawConfig> page, MkLuckyDrawConfig mkLuckyDrawConfig) {
		return super.findPage(page, mkLuckyDrawConfig);
	}
	
	@Transactional(readOnly = false)
	public void save(MkLuckyDrawConfig mkLuckyDrawConfig) {
		super.save(mkLuckyDrawConfig);
	}
	
	@Transactional(readOnly = false)
	public void delete(MkLuckyDrawConfig mkLuckyDrawConfig) {
		super.delete(mkLuckyDrawConfig);
	}

	@Transactional(readOnly = false)
	public void draw(MkLuckyDrawConfig mkLuckyDrawConfig) {
		MkLuckyDrawPlayer paramPlayer = new MkLuckyDrawPlayer();
		paramPlayer.setPeriods(mkLuckyDrawConfig.getPeriods());
		List<MkLuckyDrawPlayer> mkLuckyDrawPlayerList = mkLuckyDrawPlayerService.findList(paramPlayer);

		int luckyNumber = 0;
		int playerNumber = 0;
		MkLuckyDrawPlayer luckyPlayer = null;
		Double luckyVolume = 0.00;
		if(CollectionUtils.isEmpty(mkLuckyDrawPlayerList)){
			mkLuckyDrawConfig.setPoolVolume(Double.valueOf("0"));
		}else{
			playerNumber = mkLuckyDrawPlayerList.size();
			luckyNumber = 1 + (int)(Math.random() * playerNumber);
			luckyPlayer = mkLuckyDrawPlayerList.get(luckyNumber - 1);
			luckyVolume = mkLuckyDrawConfig.getPoolVolume();

			luckyPlayer.setDrawDate(new Date());
			luckyPlayer.setStatus("1");
			luckyPlayer.setLuckyVolume(luckyVolume);
			//更新中奖者
			mkLuckyDrawPlayerService.save(luckyPlayer);
			//更新未中奖者
			mkLuckyDrawPlayerService.updateUnLucky(luckyPlayer);
			//更新配置
			if(ObjectUtils.isEmpty(mkLuckyDrawConfig.getGrantVolume())){
				mkLuckyDrawConfig.setGrantVolume(Double.valueOf("0"));
			}
			mkLuckyDrawConfig.setGrantVolume(mkLuckyDrawConfig.getGrantVolume() + luckyVolume);
			mkLuckyDrawConfig.setPoolVolume(Double.valueOf("0"));

			OfflineCoinVolume playerCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(luckyPlayer.getUserId(), mkLuckyDrawConfig.getCoinId()); // C2C卖家账户资金
			BigDecimal volume = new BigDecimal(playerCoinVolume.getVolume());
			volume = volume.add(BigDecimal.valueOf(luckyVolume));
			long count = offlineCoinVolumeDao.updateVolume(luckyPlayer.getUserId(), mkLuckyDrawConfig.getCoinId(), volume, new Timestamp(playerCoinVolume.getUpdateDate().getTime()), playerCoinVolume.getVersion());

			if (count != 1) {
				throw new ServiceException("更新失败！");
			}
		}

		super.save(mkLuckyDrawConfig);

		MkLuckyDrawRecord mkLuckyDrawRecord = new MkLuckyDrawRecord();
		mkLuckyDrawRecord.setCoinId(mkLuckyDrawConfig.getCoinId());
		mkLuckyDrawRecord.setCoinSymbol(mkLuckyDrawConfig.getCoinSymbol());
		mkLuckyDrawRecord.setDeductFee(mkLuckyDrawConfig.getFee());
		if(!ObjectUtils.isEmpty(luckyPlayer)){
			mkLuckyDrawRecord.setUserId(luckyPlayer.getUserId());
			mkLuckyDrawRecord.setMail(luckyPlayer.getMail());
			mkLuckyDrawRecord.setMobile(luckyPlayer.getMobile());
			mkLuckyDrawRecord.setRealName(luckyPlayer.getRealName());
		}
		mkLuckyDrawRecord.setStatus("1");
		mkLuckyDrawRecord.setGrantVolume(mkLuckyDrawConfig.getGrantVolume());
		mkLuckyDrawRecord.setLuckyVolume(luckyVolume);
		mkLuckyDrawRecord.setVolume(mkLuckyDrawConfig.getVolume());
		mkLuckyDrawRecord.setPeriods(mkLuckyDrawConfig.getPeriods());
		mkLuckyDrawRecord.setPlayerNumber(playerNumber);
		mkLuckyDrawRecord.setLuckyNumber(luckyNumber);
		mkLuckyDrawRecord.setPoolVolume(luckyVolume);

		mkLuckyDrawRecordService.save(mkLuckyDrawRecord);

	}

	
}