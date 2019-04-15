/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import com.thinkgem.jeesite.common.utils.JedisUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.MkUserRegisterLottery;
import com.thinkgem.jeesite.modules.plat.dao.MkUserRegisterLotteryDao;

/**
 * 注册抽奖活动Service
 *
 * @author xiaoyu
 * @version 2018-10-25
 */
@Service
@Transactional(readOnly = true)
public class MkUserRegisterLotteryService extends CrudService<MkUserRegisterLotteryDao, MkUserRegisterLottery> {

    private static final String REGISTER_LOTTERY = "register:lottery";

    public MkUserRegisterLottery get(String id) {
        return super.get(id);
    }

    public List<MkUserRegisterLottery> findList(MkUserRegisterLottery mkUserRegisterLottery) {
        return super.findList(mkUserRegisterLottery);
    }

    public Page<MkUserRegisterLottery> findPage(Page<MkUserRegisterLottery> page, MkUserRegisterLottery mkUserRegisterLottery) {
        return super.findPage(page, mkUserRegisterLottery);
    }

    @Transactional(readOnly = false)
    public void save(MkUserRegisterLottery mkUserRegisterLottery) {
        super.save(mkUserRegisterLottery);
        JedisUtils.del(REGISTER_LOTTERY);
    }

    @Transactional(readOnly = false)
    public void delete(MkUserRegisterLottery mkUserRegisterLottery) {
        super.delete(mkUserRegisterLottery);
        JedisUtils.del(REGISTER_LOTTERY);
    }

}