/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.entity.MkUserRegisterLottery;
import com.thinkgem.jeesite.modules.plat.service.MkUserRegisterLotteryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.plat.entity.MkUserRegisterLotteryRule;
import com.thinkgem.jeesite.modules.plat.service.MkUserRegisterLotteryRuleService;

import java.util.List;

/**
 * 注册活动规则Controller
 *
 * @author xiaoyu
 * @version 2018-10-25
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/mkUserRegisterLotteryRule")
public class MkUserRegisterLotteryRuleController extends BaseController {

    @Autowired
    private MkUserRegisterLotteryRuleService mkUserRegisterLotteryRuleService;

    @Autowired
    private MkUserRegisterLotteryService mkUserRegisterLotteryService;

    @ModelAttribute
    public MkUserRegisterLotteryRule get(@RequestParam(required = false) String id) {
        MkUserRegisterLotteryRule entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = mkUserRegisterLotteryRuleService.get(id);
        }
        if (entity == null) {
            entity = new MkUserRegisterLotteryRule();
        }
        return entity;
    }

    @RequiresPermissions("plat:mkUserRegisterLotteryRule:view")
    @RequestMapping(value = {"list", ""})
    public String list(MkUserRegisterLotteryRule mkUserRegisterLotteryRule, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<MkUserRegisterLotteryRule> page = mkUserRegisterLotteryRuleService.findPage(new Page<MkUserRegisterLotteryRule>(request, response), mkUserRegisterLotteryRule);
        model.addAttribute("page", page);
        return "modules/plat/mkUserRegisterLotteryRuleList";
    }

    @RequiresPermissions("plat:mkUserRegisterLotteryRule:view")
    @RequestMapping(value = "form")
    public String form(MkUserRegisterLotteryRule mkUserRegisterLotteryRule, Model model) {
        model.addAttribute("mkUserRegisterLotteryRule", mkUserRegisterLotteryRule);
        MkUserRegisterLottery lottery = new MkUserRegisterLottery();
        lottery.setStatus(1);
        final List<MkUserRegisterLottery> lotteryList = mkUserRegisterLotteryService.findList(lottery);
        model.addAttribute("lotteryList", lotteryList);
        return "modules/plat/mkUserRegisterLotteryRuleForm";
    }

    @RequiresPermissions("plat:mkUserRegisterLotteryRule:edit")
    @RequestMapping(value = "save")
    public String save(MkUserRegisterLotteryRule mkUserRegisterLotteryRule, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, mkUserRegisterLotteryRule)) {
            return form(mkUserRegisterLotteryRule, model);
        }
        final Integer minCount = mkUserRegisterLotteryRule.getMinCount();
        final Integer maxCount = mkUserRegisterLotteryRule.getMaxCount();

        if (minCount >= maxCount) {
            return form(mkUserRegisterLotteryRule, model);
        }
        mkUserRegisterLotteryRuleService.save(mkUserRegisterLotteryRule);
        addMessage(redirectAttributes, "保存注册活动规则成功");
        return "redirect:" + Global.getAdminPath() + "/plat/mkUserRegisterLotteryRule/?repage";
    }

    @RequiresPermissions("plat:mkUserRegisterLotteryRule:edit")
    @RequestMapping(value = "delete")
    public String delete(MkUserRegisterLotteryRule mkUserRegisterLotteryRule, RedirectAttributes redirectAttributes) {
        mkUserRegisterLotteryRuleService.delete(mkUserRegisterLotteryRule);
        addMessage(redirectAttributes, "删除注册活动规则成功");
        return "redirect:" + Global.getAdminPath() + "/plat/mkUserRegisterLotteryRule/?repage";
    }

}