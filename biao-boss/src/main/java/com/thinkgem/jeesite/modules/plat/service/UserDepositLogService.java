/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.dao.UserDepositLogDao;
import com.thinkgem.jeesite.modules.plat.entity.CoinCollection;
import com.thinkgem.jeesite.modules.plat.entity.UserDepositLog;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户充值管理Service
 *
 * @author dazi
 * @version 2018-05-04
 */
@Service
@Transactional(readOnly = true)
public class UserDepositLogService extends CrudService<UserDepositLogDao, UserDepositLog> {

    @Autowired
    private CoinCollectionService coinCollectionService;

    public UserDepositLog get(String id) {
        return super.get(id);
    }

    public List<UserDepositLog> findList(UserDepositLog userDepositLog) {
        return super.findList(userDepositLog);
    }

    public List<UserDepositLog> findListCount(UserDepositLog userDepositLog) {
        return this.dao.findListCount(userDepositLog);
    }

    public List<UserDepositLog> findListByUserIdAndSymbol(UserDepositLog userDepositLog) {
        return this.dao.findListByUserIdAndSymbol(userDepositLog);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean collection(UserDepositLog userDepositLog) {
        final List<UserDepositLog> logs = findListByUserIdAndSymbol(userDepositLog);
        if (CollectionUtils.isNotEmpty(logs)) {
            BigDecimal totalVolume = new BigDecimal(0.00);
            for (UserDepositLog log : logs) {
                totalVolume = totalVolume.add(new BigDecimal(log.getVolume()));
                log.setRaiseStatus(1);
                log.setUpdateDate(new Date());
                this.updateRaiseStatus(log);
            }
            CoinCollection coinCollection = new CoinCollection();
            String symbol = logs.get(0).getCoinSymbol();
            coinCollection.setStatus("0");
            coinCollection.setSymbol(symbol);
            coinCollection.setAddress(logs.get(0).getAddress());
            coinCollection.setUserId(userDepositLog.getUserId());
            coinCollection.setVolume(totalVolume);
            coinCollectionService.save(coinCollection);
        }
        return true;

    }

    public Page<UserDepositLog> findPage(Page<UserDepositLog> page, UserDepositLog userDepositLog) {
        return super.findPage(page, userDepositLog);
    }

    @Transactional(readOnly = false)
    public void save(UserDepositLog userDepositLog) {
        super.save(userDepositLog);
    }

    @Transactional(readOnly = false)
    public void updateRaiseStatus(UserDepositLog userDepositLog) {
        this.dao.updateRaiseStatus(userDepositLog);
    }

    @Transactional(readOnly = false)
    public void delete(UserDepositLog userDepositLog) {
        super.delete(userDepositLog);
    }

}