/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.OfflineCoin;
import com.thinkgem.jeesite.modules.plat.dao.OfflineCoinDao;

/**
 * c2c_coinService
 *
 * @author dazi
 * @version 2018-04-29
 */
@Service
@Transactional(readOnly = true)
public class OfflineCoinService extends CrudService<OfflineCoinDao, OfflineCoin> {


    @Autowired
    private OfflineCoinDao offlineCoinDao;

    public OfflineCoin get(String id) {
        return super.get(id);
    }

    public List<OfflineCoin> findList(OfflineCoin offlineCoin) {
        return super.findList(offlineCoin);
    }

    public Page<OfflineCoin> findPage(Page<OfflineCoin> page, OfflineCoin offlineCoin) {
        return super.findPage(page, offlineCoin);
    }

    @Transactional(readOnly = false)
    public void disable(String id, Integer disable) {
        offlineCoinDao.disable(id, disable);
    }

    @Transactional(readOnly = false)
    public void isVolume(String id, Integer isVolume) {
        offlineCoinDao.isVolume(id, isVolume);
    }


    @Transactional(readOnly = false)
    public void save(OfflineCoin offlineCoin) {
        super.save(offlineCoin);
    }

    @Transactional(readOnly = false)
    public void delete(OfflineCoin offlineCoin) {
        super.delete(offlineCoin);
    }

    public OfflineCoin getByCoinId(String coinId){
        return dao.getByCoinId(coinId);
    }

}