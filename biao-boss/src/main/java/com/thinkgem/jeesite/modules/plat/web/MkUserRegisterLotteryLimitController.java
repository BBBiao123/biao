/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.thinkgem.jeesite.modules.plat.entity.MkUserRegisterLotteryLimit;
import com.thinkgem.jeesite.modules.plat.service.MkUserRegisterLotteryLimitService;

import java.util.List;

/**
 * 注册送奖活动限制Controller
 *
 * @author xiaoyu
 * @version 2018-11-05
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/mkUserRegisterLotteryLimit")
public class MkUserRegisterLotteryLimitController extends BaseController {

    @Autowired
    private MkUserRegisterLotteryLimitService mkUserRegisterLotteryLimitService;

    @Autowired
    private MkUserRegisterLotteryService mkUserRegisterLotteryService;

    @ModelAttribute
    public MkUserRegisterLotteryLimit get(@RequestParam(required = false) String id) {
        MkUserRegisterLotteryLimit entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = mkUserRegisterLotteryLimitService.get(id);
        }
        if (entity == null) {
            entity = new MkUserRegisterLotteryLimit();
        }
        return entity;
    }

    @RequiresPermissions("plat:mkUserRegisterLotteryLimit:view")
    @RequestMapping(value = {"list", ""})
    public String list(MkUserRegisterLotteryLimit mkUserRegisterLotteryLimit, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<MkUserRegisterLotteryLimit> page = mkUserRegisterLotteryLimitService.findPage(new Page<MkUserRegisterLotteryLimit>(request, response), mkUserRegisterLotteryLimit);
        model.addAttribute("page", page);
        return "modules/plat/mkUserRegisterLotteryLimitList";
    }

    @RequiresPermissions("plat:mkUserRegisterLotteryLimit:view")
    @RequestMapping(value = "form")
    public String form(MkUserRegisterLotteryLimit mkUserRegisterLotteryLimit, Model model) {
        model.addAttribute("mkUserRegisterLotteryLimit", mkUserRegisterLotteryLimit);
        MkUserRegisterLottery lottery = new MkUserRegisterLottery();
        lottery.setStatus(1);
        final List<MkUserRegisterLottery> lotteryList = mkUserRegisterLotteryService.findList(lottery);
        model.addAttribute("lotteryList", lotteryList);
        return "modules/plat/mkUserRegisterLotteryLimitForm";
    }

    @RequiresPermissions("plat:mkUserRegisterLotteryLimit:edit")
    @RequestMapping(value = "save")
    public String save(MkUserRegisterLotteryLimit mkUserRegisterLotteryLimit, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, mkUserRegisterLotteryLimit)) {
            return form(mkUserRegisterLotteryLimit, model);
        }
        mkUserRegisterLotteryLimitService.save(mkUserRegisterLotteryLimit);
        addMessage(redirectAttributes, "保存注册送奖活动限制成功");
        return "redirect:" + Global.getAdminPath() + "/plat/mkUserRegisterLotteryLimit/?repage";
    }

    @RequiresPermissions("plat:mkUserRegisterLotteryLimit:edit")
    @RequestMapping(value = "delete")
    public String delete(MkUserRegisterLotteryLimit mkUserRegisterLotteryLimit, RedirectAttributes redirectAttributes) {
        mkUserRegisterLotteryLimitService.delete(mkUserRegisterLotteryLimit);
        addMessage(redirectAttributes, "删除注册送奖活动限制成功");
        return "redirect:" + Global.getAdminPath() + "/plat/mkUserRegisterLotteryLimit/?repage";
    }

}