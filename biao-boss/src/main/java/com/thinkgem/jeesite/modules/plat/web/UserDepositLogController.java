/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.entity.UserDepositLog;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
import com.thinkgem.jeesite.modules.plat.service.UserDepositLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户充值管理Controller
 *
 * @author dazi
 * @version 2018-05-04
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/userDepositLog")
public class UserDepositLogController extends BaseController {

    @Autowired
    private UserDepositLogService userDepositLogService;

    @Autowired
    private CoinService coinService;

    @ModelAttribute
    public UserDepositLog get(@RequestParam(required = false) String id) {
        UserDepositLog entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = userDepositLogService.get(id);
        }
        if (entity == null) {
            entity = new UserDepositLog();
        }
        return entity;
    }

    @RequiresPermissions("plat:userDepositLog:view")
    @RequestMapping(value = {"list", ""})
    public String list(UserDepositLog userDepositLog, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<UserDepositLog> page = userDepositLogService.findPage(new Page<UserDepositLog>(request, response), userDepositLog);
        model.addAttribute("page", page);
        return "modules/plat/userDepositLogList";
    }

    @RequiresPermissions("plat:userDepositLog:count")
    @RequestMapping("count")
    public String listCount(UserDepositLog userDepositLog, HttpServletRequest request, HttpServletResponse response, Model model) {
        String firstTime = request.getParameter("firstTime");
        List<UserDepositLog> pageList = new ArrayList<>();
        if (StringUtils.isBlank(firstTime)) {
            pageList = userDepositLogService.findListCount(userDepositLog);
        }
        model.addAttribute("pageList", pageList);
        List<Coin> coinList = coinService.findList(new Coin());
        model.addAttribute(coinList);
        return "modules/plat/userDepositLogListCount";
    }

    @RequiresPermissions("plat:userDepositLog:view")
    @RequestMapping(value = "form")
    public String form(UserDepositLog userDepositLog, Model model) {
        model.addAttribute("userDepositLog", userDepositLog);
        return "modules/plat/userDepositLogForm";
    }

    @RequiresPermissions("plat:userDepositLog:edit")
    @RequestMapping(value = "save")
    public String save(UserDepositLog userDepositLog, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, userDepositLog)) {
            return form(userDepositLog, model);
        }
        userDepositLogService.save(userDepositLog);
        addMessage(redirectAttributes, "保存用户充值管理成功");
        return "redirect:" + Global.getAdminPath() + "/plat/userDepositLog/?repage";
    }


    @RequestMapping(value = "updateStatus")
    public String changeStatus(UserDepositLog userDepositLog, Model model, RedirectAttributes redirectAttributes) {
        final List<UserDepositLog> list = userDepositLogService.findListByUserIdAndSymbol(userDepositLog);
        for (UserDepositLog log : list) {
            if (log.getRaiseStatus() == 1) {
                addMessage(redirectAttributes, "该用户下的币种正在归集，等归集完成后再次设置");
                return "redirect:" + Global.getAdminPath() + "/plat/userDepositLog/?repage";
            }
        }
        userDepositLogService.collection(userDepositLog) ;
        addMessage(redirectAttributes, "修改归集中成功");
        return "redirect:" + Global.getAdminPath() + "/plat/userDepositLog/?repage";
    }

    @RequiresPermissions("plat:userDepositLog:edit")
    @RequestMapping(value = "delete")
    public String delete(UserDepositLog userDepositLog, RedirectAttributes redirectAttributes) {
        userDepositLogService.delete(userDepositLog);
        addMessage(redirectAttributes, "删除用户充值管理成功");
        return "redirect:" + Global.getAdminPath() + "/plat/userDepositLog/?repage";
    }


    @RequiresPermissions("plat:userDepositLog:manual")
    @RequestMapping(value = "manual")
    public String manual(UserDepositLog userDepositLog, RedirectAttributes redirectAttributes) {
        userDepositLogService.delete(userDepositLog);
        addMessage(redirectAttributes, "删除用户充值管理成功");
        return "redirect:" + Global.getAdminPath() + "/plat/userDepositLog/?repage";
    }

    //https://etherscan.io/tx/0x520f891069495542ca8bbf2c90b18bbcfdccf86a4ee13d885aa02feda5d742e2
}