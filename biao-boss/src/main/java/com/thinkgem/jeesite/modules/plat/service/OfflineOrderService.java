/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.SnowFlake;
import com.thinkgem.jeesite.modules.plat.dao.JsPlatOfflineTransferLogDao;
import com.thinkgem.jeesite.modules.plat.dao.OfflineCoinVolumeDao;
import com.thinkgem.jeesite.modules.plat.dao.OfflineOrderDao;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatOfflineTransferLog;
import com.thinkgem.jeesite.modules.plat.entity.OfflineCoinVolume;
import com.thinkgem.jeesite.modules.plat.entity.OfflineOrder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * c2c广告Service
 * @author dazi
 * @version 2018-04-27
 */
@Service
@Transactional(readOnly = true)
public class OfflineOrderService extends CrudService<OfflineOrderDao, OfflineOrder> {

	@Autowired
	private OfflineOrderService offlineOrderService;

	@Autowired
	private OfflineCoinVolumeDao offlineCoinVolumeDao;

	@Autowired
	private JsPlatOfflineTransferLogDao transferLogDao;

	private List<String> CANCEL_STATUS = Arrays.asList("0", "2");

	public OfflineOrder get(String id) {
		return super.get(id);
	}
	
	public List<OfflineOrder> findList(OfflineOrder offlineOrder) {
		return super.findList(offlineOrder);
	}
	
	public Page<OfflineOrder> findPage(Page<OfflineOrder> page, OfflineOrder offlineOrder) {
		return super.findPage(page, offlineOrder);
	}
	
	@Transactional(readOnly = false)
	public void save(OfflineOrder offlineOrder) {
		super.save(offlineOrder);
	}
	
	@Transactional(readOnly = false)
	public void delete(OfflineOrder offlineOrder) {
		super.delete(offlineOrder);
	}

	@Transactional(readOnly = false)
	public String doBatchCancel(String coinSymbol, String exType) {
		List<OfflineOrder> offlineOrders = dao.findCanCancelByCoinSymbol(coinSymbol, exType);
		if (CollectionUtils.isEmpty(offlineOrders)) {
			return "成功撤销广告数量0个.";
		}
		int count = 0;
		for (OfflineOrder offlineOrder : offlineOrders) {
			try {
				offlineOrderService.doCancelOrder(offlineOrder);
				count ++ ;
			} catch (ServiceException e) {
				logger.error(e.getMessage(), e);
				e.printStackTrace();
			} catch (Exception e) {
				logger.error("广告撤销失败", e);
				e.printStackTrace();
			}
		}
		return "成功撤销广告数量" + count + "个";
	}

	public void doCancel(String orderId) {
		OfflineOrder offlineOrder = dao.findByOrderId(orderId);
		offlineOrderService.doCancelOrder(offlineOrder);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void doCancelOrder(OfflineOrder offlineOrder) {
		if (Objects.isNull(offlineOrder)) {
			logger.error("撤销的订单不存在");
			throw new ServiceException("撤销的订单不存在!");
		}
		if (!CANCEL_STATUS.contains(offlineOrder.getStatus())) {
			logger.error("该广告状态不能撤销" + offlineOrder.getId());
			throw new ServiceException("该广告状态不能撤销!广告ID：" + offlineOrder.getId());
		}
		if ("0".equals(offlineOrder.getExType())) {
			doCancelBuy(offlineOrder);
		} else if ("1".equals(offlineOrder.getExType())) {
            doCancelSell(offlineOrder);
		} else {
			logger.error("该广告买卖方向有误!" + offlineOrder.getId());
			throw new ServiceException("该广告买卖方向有误! 广告ID：" + offlineOrder.getId());
		}
	}

	private void doCancelSell(OfflineOrder offlineOrder) {

		BigDecimal lastVolume = new BigDecimal(offlineOrder.getVolume()).subtract(new BigDecimal(offlineOrder.getLockVolume())).subtract(new BigDecimal(offlineOrder.getSuccessVolume()));
		if (lastVolume.compareTo(BigDecimal.ZERO) < 1) {
			logger.error("该广告没有可撤销的量! 广告ID：" + offlineOrder.getId());
			throw new ServiceException("该广告没有可撤销的量! 广告ID：" + offlineOrder.getId());
		}
        offlineOrder.setCancelDate(new Date());
		offlineOrder.setStatus("9");
		offlineOrder.setRemarks("boss撤销买广告回退数量" + lastVolume.add(new BigDecimal(offlineOrder.getFeeVolume())));
		// 更新广告状态
		long count = dao.updateOrderCancelStatusById(offlineOrder);
		if (count != 1) {
			logger.error("该卖广告撤销状态更新失败! 广告ID：" + offlineOrder.getId());
			throw new ServiceException("该卖广告撤销状态更新失败! 广告ID：" + offlineOrder.getId());
		}

		// 回退资产
		OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(offlineOrder.getUserId(), offlineOrder.getCoinId());
		BigDecimal advertVolume = new BigDecimal(offlineCoinVolume.getAdvertVolume()).subtract(lastVolume);
		if (advertVolume.compareTo(BigDecimal.ZERO) == -1) {
			throw new ServiceException("该广告的C2C广告冻结数量有误，无法撤销" + offlineOrder.getId());
		}
		BigDecimal volume = new BigDecimal(offlineCoinVolume.getVolume()).add(lastVolume).add(new BigDecimal(offlineOrder.getFeeVolume())); // 回退剩余广告冻结和手续费
		offlineCoinVolume.setAdvertVolume(advertVolume.toString());
		offlineCoinVolume.setVolume(volume.toString());
		count = offlineCoinVolumeDao.updateVolumeAndAdvert(offlineCoinVolume);
		if (count != 1) {
			logger.error("该卖广告撤销失败，C2C资产更新失败! 广告ID：" + offlineOrder.getId());
			throw new ServiceException("该卖广告撤销失败，C2C资产更新失败! 广告ID：" + offlineOrder.getId());
		}

        JsPlatOfflineTransferLog transferLog = new JsPlatOfflineTransferLog();
		transferLog.setId(SnowFlake.createSnowFlake().nextIdString());
        transferLog.setUserId(offlineCoinVolume.getUserId());
        transferLog.setCoinId(offlineCoinVolume.getCoinId());
        transferLog.setCoinSymbol(offlineCoinVolume.getCoinSymbol());
        transferLog.setVolume(offlineOrder.getFeeVolume());
        transferLog.setType("9");
        transferLog.setCreateDate(new Date());
        transferLog.setUpdateDate(new Date());
        transferLog.setMark("撤销广告回退手续费");
        transferLogDao.insertByLog(transferLog);

	}

	private void doCancelBuy(OfflineOrder offlineOrder) {
		offlineOrder.setStatus("9");
		offlineOrder.setRemarks("boss撤销买广告");
        offlineOrder.setCancelDate(new Date());
		long count = dao.updateOrderCancelStatusById(offlineOrder);
		if (count != 1) {
			logger.error("该买广告撤销状态更新失败! 广告ID：" + offlineOrder.getId());
			throw new ServiceException("该买广告撤销状态更新失败! 广告ID：" + offlineOrder.getId());
		}
	}

}