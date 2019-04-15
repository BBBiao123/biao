/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.entity.ExPair;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.plat.entity.MkUserRegisterLottery;
import com.thinkgem.jeesite.modules.plat.service.MkUserRegisterLotteryService;

import java.util.List;
import java.util.Objects;

/**
 * 注册抽奖活动Controller
 *
 * @author xiaoyu
 * @version 2018-10-25
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/mkUserRegisterLottery")
public class MkUserRegisterLotteryController extends BaseController {

    @Autowired
    private MkUserRegisterLotteryService mkUserRegisterLotteryService;

    @Autowired
    private CoinService coinService;

    @ModelAttribute
    public MkUserRegisterLottery get(@RequestParam(required = false) String id) {
        MkUserRegisterLottery entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = mkUserRegisterLotteryService.get(id);
        }
        if (entity == null) {
            entity = new MkUserRegisterLottery();
        }
        return entity;
    }

    @RequiresPermissions("plat:mkUserRegisterLottery:view")
    @RequestMapping(value = {"list", ""})
    public String list(MkUserRegisterLottery mkUserRegisterLottery, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<MkUserRegisterLottery> page = mkUserRegisterLotteryService.findPage(new Page<MkUserRegisterLottery>(request, response), mkUserRegisterLottery);
        model.addAttribute("page", page);
        return "modules/plat/mkUserRegisterLotteryList";
    }

    @RequiresPermissions("plat:mkUserRegisterLottery:view")
    @RequestMapping(value = "form")
    public String form(MkUserRegisterLottery mkUserRegisterLottery, Model model) {
        model.addAttribute("mkUserRegisterLottery", mkUserRegisterLottery);
        List<Coin> coinList = coinService.findList(new Coin());
        model.addAttribute(coinList);
        return "modules/plat/mkUserRegisterLotteryForm";
    }

    @RequiresPermissions("plat:mkUserRegisterLottery:edit")
    @RequestMapping(value = "save")
    public String save(MkUserRegisterLottery mkUserRegisterLottery, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, mkUserRegisterLottery)) {
            return form(mkUserRegisterLottery, model);
        }
        if (Objects.isNull(mkUserRegisterLottery.getStatus())) {
            mkUserRegisterLottery.setStatus(0);
        }
        mkUserRegisterLotteryService.save(mkUserRegisterLottery);
        addMessage(redirectAttributes, "保存注册抽奖活动成功");
        return "redirect:" + Global.getAdminPath() + "/plat/mkUserRegisterLottery/?repage";
    }

    @RequiresPermissions("plat:mkUserRegisterLottery:edit")
    @RequestMapping(value = "delete")
    public String delete(MkUserRegisterLottery mkUserRegisterLottery, RedirectAttributes redirectAttributes) {
        mkUserRegisterLotteryService.delete(mkUserRegisterLottery);
        addMessage(redirectAttributes, "删除注册抽奖活动成功");
        return "redirect:" + Global.getAdminPath() + "/plat/mkUserRegisterLottery/?repage";
    }

    @RequiresPermissions("plat:mkUserRegisterLottery:enable")
    @RequestMapping(value = "enable/{value}")
    public String enable(MkUserRegisterLottery mkUserRegisterLottery, @PathVariable("value") Integer value, RedirectAttributes redirectAttributes) {
        MkUserRegisterLottery lottery = mkUserRegisterLotteryService.get(mkUserRegisterLottery.getId());
        lottery.setStatus(value);
        mkUserRegisterLotteryService.save(lottery);
        addMessage(redirectAttributes, "活动操作成功");
        return "redirect:" + Global.getAdminPath() + "/plat/mkUserRegisterLottery/?repage";
    }

}