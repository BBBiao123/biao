/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.dao.JsPlatUserCoinVolumeBillHistoryDao;
import com.thinkgem.jeesite.modules.plat.dao.PlatUserDao;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatUserCoinVolumeBillHistory;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 币币交易历史记录service
 *
 * @author dazi
 * @version 2018-04-26
 */
@Service
@Transactional(readOnly = true)
public class JsPlatUserCoinVolumeBillHistoryService extends CrudService<JsPlatUserCoinVolumeBillHistoryDao, JsPlatUserCoinVolumeBillHistory> {

    public JsPlatUserCoinVolumeBillHistory get(String id) {
        return super.get(id);
    }

    public List<JsPlatUserCoinVolumeBillHistory> findList(JsPlatUserCoinVolumeBillHistory jsPlatUserCoinVolumeBillHistory) {
        return super.findList(jsPlatUserCoinVolumeBillHistory);
    }
    

    public Page<JsPlatUserCoinVolumeBillHistory> findPage(Page<JsPlatUserCoinVolumeBillHistory> page, JsPlatUserCoinVolumeBillHistory jsPlatUserCoinVolumeBillHistory) {
        return super.findPage(page, jsPlatUserCoinVolumeBillHistory);
    }

    @Transactional(readOnly = false)
    public void save(JsPlatUserCoinVolumeBillHistory jsPlatUserCoinVolumeBillHistory) {
        super.save(jsPlatUserCoinVolumeBillHistory);
    }



    @Transactional(readOnly = false)
    public void delete(JsPlatUserCoinVolumeBillHistory jsPlatUserCoinVolumeBillHistory) {
        super.delete(jsPlatUserCoinVolumeBillHistory);
    }




}