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
import com.thinkgem.jeesite.modules.plat.entity.MkUserRegisterLotteryLimit;
import com.thinkgem.jeesite.modules.plat.dao.MkUserRegisterLotteryLimitDao;

/**
 * 注册送奖活动限制Service
 *
 * @author xiaoyu
 * @version 2018-11-05
 */
@Service
@Transactional(readOnly = true)
public class MkUserRegisterLotteryLimitService extends CrudService<MkUserRegisterLotteryLimitDao, MkUserRegisterLotteryLimit> {


    private static final String REGISTER_LOTTERY_LIMIT = "register:lottery:limit";

    public MkUserRegisterLotteryLimit get(String id) {
        return super.get(id);
    }

    public List<MkUserRegisterLotteryLimit> findList(MkUserRegisterLotteryLimit mkUserRegisterLotteryLimit) {
        return super.findList(mkUserRegisterLotteryLimit);
    }

    public Page<MkUserRegisterLotteryLimit> findPage(Page<MkUserRegisterLotteryLimit> page, MkUserRegisterLotteryLimit mkUserRegisterLotteryLimit) {
        return super.findPage(page, mkUserRegisterLotteryLimit);
    }

    @Transactional(readOnly = false)
    public void save(MkUserRegisterLotteryLimit mkUserRegisterLotteryLimit) {
        super.save(mkUserRegisterLotteryLimit);
        JedisUtils.del(REGISTER_LOTTERY_LIMIT);
    }

    @Transactional(readOnly = false)
    public void delete(MkUserRegisterLotteryLimit mkUserRegisterLotteryLimit) {
        super.delete(mkUserRegisterLotteryLimit);
        JedisUtils.del(REGISTER_LOTTERY_LIMIT);
    }

}