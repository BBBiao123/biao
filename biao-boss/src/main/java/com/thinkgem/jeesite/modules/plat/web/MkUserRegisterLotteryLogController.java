/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.thinkgem.jeesite.modules.plat.entity.MkUserRegisterLotteryLog;
import com.thinkgem.jeesite.modules.plat.service.MkUserRegisterLotteryLogService;

/**
 * 注册活动抽奖记录Controller
 *
 * @author xiaoyu
 * @version 2018-10-25
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/mkUserRegisterLotteryLog")
public class MkUserRegisterLotteryLogController extends BaseController {

    @Autowired
    private MkUserRegisterLotteryLogService mkUserRegisterLotteryLogService;

    @ModelAttribute
    public MkUserRegisterLotteryLog get(@RequestParam(required = false) String id) {
        MkUserRegisterLotteryLog entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = mkUserRegisterLotteryLogService.get(id);
        }
        if (entity == null) {
            entity = new MkUserRegisterLotteryLog();
        }
        return entity;
    }

    @RequiresPermissions("plat:mkUserRegisterLotteryLog:view")
    @RequestMapping(value = {"list", ""})
    public String list(MkUserRegisterLotteryLog mkUserRegisterLotteryLog, HttpServletRequest request, HttpServletResponse response, Model model) {

        final Page<MkUserRegisterLotteryLog> pages = new Page<>(request, response);
        pages.setOrderBy(" create_date desc ");

        Page<MkUserRegisterLotteryLog> page = mkUserRegisterLotteryLogService.findPage(pages, mkUserRegisterLotteryLog);
        model.addAttribute("page", page);
        return "modules/plat/mkUserRegisterLotteryLogList";
    }

    @RequiresPermissions("plat:mkUserRegisterLotteryLog:view")
    @RequestMapping(value = "form")
    public String form(MkUserRegisterLotteryLog mkUserRegisterLotteryLog, Model model) {
        model.addAttribute("mkUserRegisterLotteryLog", mkUserRegisterLotteryLog);
        return "modules/plat/mkUserRegisterLotteryLogForm";
    }

    @RequiresPermissions("plat:mkUserRegisterLotteryLog:edit")
    @RequestMapping(value = "save")
    public String save(MkUserRegisterLotteryLog mkUserRegisterLotteryLog, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, mkUserRegisterLotteryLog)) {
            return form(mkUserRegisterLotteryLog, model);
        }
        mkUserRegisterLotteryLogService.save(mkUserRegisterLotteryLog);
        addMessage(redirectAttributes, "保存注册活动抽奖记录成功");
        return "redirect:" + Global.getAdminPath() + "/plat/mkUserRegisterLotteryLog/?repage";
    }

    @RequiresPermissions("plat:mkUserRegisterLotteryLog:edit")
    @RequestMapping(value = "delete")
    public String delete(MkUserRegisterLotteryLog mkUserRegisterLotteryLog, RedirectAttributes redirectAttributes) {
        mkUserRegisterLotteryLogService.delete(mkUserRegisterLotteryLog);
        addMessage(redirectAttributes, "删除注册活动抽奖记录成功");
        return "redirect:" + Global.getAdminPath() + "/plat/mkUserRegisterLotteryLog/?repage";
    }

}