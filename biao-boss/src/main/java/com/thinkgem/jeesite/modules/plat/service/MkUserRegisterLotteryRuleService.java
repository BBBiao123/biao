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
import com.thinkgem.jeesite.modules.plat.entity.MkUserRegisterLotteryRule;
import com.thinkgem.jeesite.modules.plat.dao.MkUserRegisterLotteryRuleDao;

/**
 * 注册活动规则Service
 *
 * @author xiaoyu
 * @version 2018-10-25
 */
@Service
@Transactional(readOnly = true)
public class MkUserRegisterLotteryRuleService extends CrudService<MkUserRegisterLotteryRuleDao, MkUserRegisterLotteryRule> {
    private static final String REGISTER_LOTTERY_RULE = "register:lottery:rule";

    public MkUserRegisterLotteryRule get(String id) {
        return super.get(id);
    }

    public List<MkUserRegisterLotteryRule> findList(MkUserRegisterLotteryRule mkUserRegisterLotteryRule) {
        return super.findList(mkUserRegisterLotteryRule);
    }

    public Page<MkUserRegisterLotteryRule> findPage(Page<MkUserRegisterLotteryRule> page, MkUserRegisterLotteryRule mkUserRegisterLotteryRule) {
        return super.findPage(page, mkUserRegisterLotteryRule);
    }

    @Transactional(readOnly = false)
    public void save(MkUserRegisterLotteryRule mkUserRegisterLotteryRule) {
        super.save(mkUserRegisterLotteryRule);
        JedisUtils.del(REGISTER_LOTTERY_RULE);
    }

    @Transactional(readOnly = false)
    public void delete(MkUserRegisterLotteryRule mkUserRegisterLotteryRule) {
        super.delete(mkUserRegisterLotteryRule);
        JedisUtils.del(REGISTER_LOTTERY_RULE);
    }

}