/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.entity.OfflineCoin;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
import com.thinkgem.jeesite.modules.plat.service.OfflineCoinService;

/**
 * c2c_coinController
 *
 * @author dazi
 * @version 2018-04-29
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/offlineCoin")
public class OfflineCoinController extends BaseController {

    @Autowired
    private OfflineCoinService offlineCoinService;

    @Autowired
    private CoinService coinService;

    @ModelAttribute
    public OfflineCoin get(@RequestParam(required = false) String id) {
        OfflineCoin entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = offlineCoinService.get(id);
        }
        if (entity == null) {
            entity = new OfflineCoin();
        }
        return entity;
    }

    @RequiresPermissions("plat:offlineCoin:view")
    @RequestMapping(value = {"list", ""})
    public String list(OfflineCoin offlineCoin, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<OfflineCoin> page = offlineCoinService.findPage(new Page<OfflineCoin>(request, response), offlineCoin);
        model.addAttribute("page", page);
        return "modules/plat/offlineCoinList";
    }

    @RequiresPermissions("plat:offlineCoin:view")
    @RequestMapping(value = "form")
    public String form(OfflineCoin offlineCoin, Model model) {
        model.addAttribute("offlineCoin", offlineCoin);
        List<Coin> coinList = coinService.findList(new Coin());
        model.addAttribute(coinList);
        return "modules/plat/offlineCoinForm";
    }

    @RequiresPermissions("plat:offlineCoin:edit")
    @RequestMapping(value = "save")
    public String save(OfflineCoin offlineCoin, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, offlineCoin)) {
            return form(offlineCoin, model);
        }
        final BigDecimal maxPrice = offlineCoin.getMaxPrice();
        final BigDecimal minPrice = offlineCoin.getMinPrice();
        if (maxPrice.compareTo(minPrice) < 0) {
            return form(offlineCoin, model);
        }

        final BigDecimal maxVolume = offlineCoin.getMaxVolume();
        final BigDecimal minVolume = offlineCoin.getMinVolume();
        if (maxVolume.compareTo(minVolume) < 0) {
            return form(offlineCoin, model);
        }
        Coin mainCoin = coinService.get(offlineCoin.getCoinId());
        offlineCoin.setSymbol(mainCoin.getName());
        offlineCoinService.save(offlineCoin);
        addMessage(redirectAttributes, "保存c2c_coin成功");
        return "redirect:" + Global.getAdminPath() + "/plat/offlineCoin/?repage";
    }


    @RequiresPermissions("plat:offlineCoin:disable")
    @RequestMapping(value = "disable/{value}")
    public String disable(OfflineCoin offlineCoin, @PathVariable("value") Integer value, RedirectAttributes redirectAttributes) {
        offlineCoinService.disable(offlineCoin.getId(), value);
        addMessage(redirectAttributes, "操作成功");
        return "redirect:" + Global.getAdminPath() + "/plat/offlineCoin/?repage";
    }

    @RequiresPermissions("plat:offlineCoin:disable")
    @RequestMapping(value = "isVolume/{value}")
    public String isVolume(OfflineCoin offlineCoin, @PathVariable("value") Integer value, RedirectAttributes redirectAttributes) {
        offlineCoinService.isVolume(offlineCoin.getId(), value);
        addMessage(redirectAttributes, "操作成功");
        return "redirect:" + Global.getAdminPath() + "/plat/offlineCoin/?repage";
    }


    @RequiresPermissions("plat:offlineCoin:edit")
    @RequestMapping(value = "delete")
    public String delete(OfflineCoin offlineCoin, RedirectAttributes redirectAttributes) {
        offlineCoinService.delete(offlineCoin);
        addMessage(redirectAttributes, "删除c2c_coin成功");
        return "redirect:" + Global.getAdminPath() + "/plat/offlineCoin/?repage";
    }

}