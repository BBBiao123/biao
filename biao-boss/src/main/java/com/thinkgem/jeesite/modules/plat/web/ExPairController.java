/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
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
import com.thinkgem.jeesite.modules.plat.entity.ExPair;
import com.thinkgem.jeesite.modules.plat.service.ExPairService;

import java.util.List;

/**
 * 币币交易对Controller
 *
 * @author dazi
 * @version 2018-04-26
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/exPair")
public class ExPairController extends BaseController {

    @Autowired
    private ExPairService exPairService;
    @Autowired
    private CoinService coinService;

    @ModelAttribute
    public ExPair get(@RequestParam(required = false) String id) {
        ExPair entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = exPairService.get(id);
        }
        if (entity == null) {
            entity = new ExPair();
        }
        return entity;
    }

    @RequiresPermissions("plat:exPair:view")
    @RequestMapping(value = {"list", ""})
    public String list(ExPair exPair, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ExPair> page = exPairService.findPage(new Page<ExPair>(request, response), exPair);
        model.addAttribute("page", page);
        //设置查询参数
        model.addAttribute("exPair", exPair);
        request.getSession().setAttribute("searchExpair", exPair);
        return "modules/plat/exPairList";
    }

    @RequiresPermissions("plat:exPair:view")
    @RequestMapping(value = "form")
    public String form(ExPair exPair, Model model) {
        model.addAttribute("exPair", exPair);
        List<Coin> coinList = coinService.findList(new Coin());
        model.addAttribute(coinList);
        return "modules/plat/exPairForm";
    }

    @RequiresPermissions("plat:exPair:edit")
    @RequestMapping(value = "save")
    public String save(ExPair exPair, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (!beanValidator(model, exPair)) {
            return form(exPair, model);
        }
        if (exPair.getVolumePrecision() + exPair.getPricePrecision() > 8) {
            return form(exPair, model);
        }
        Coin mainCoin = coinService.get(exPair.getCoinId());
        Coin otherCoin = coinService.get(exPair.getOtherCoinId());
        exPair.setPairOther(otherCoin.getName());
        exPair.setPairOne(mainCoin.getName());
        exPairService.save(exPair);
        addMessage(redirectAttributes, "保存币币交易对成功");
        redirectAttributes.addFlashAttribute("exPair", request.getSession().getAttribute("searchExpair"));
        return "redirect:" + Global.getAdminPath() + "/plat/exPair/?repage";
    }

    @RequiresPermissions("plat:exPair:edit")
    @RequestMapping(value = "delete")
    public String delete(ExPair exPair, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        exPairService.delete(exPair);
        addMessage(redirectAttributes, "删除币币交易对成功");
        redirectAttributes.addFlashAttribute("exPair", request.getSession().getAttribute("searchExpair"));
        return "redirect:" + Global.getAdminPath() + "/plat/exPair/?repage";
    }

    @RequiresPermissions("plat:exPair:edit")
    @RequestMapping(value = "release")
    public String release(ExPair exPair, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        Coin mainCoin = coinService.get(exPair.getCoinId());
        Coin otherCoin = coinService.get(exPair.getOtherCoinId());
        exPair.setPairOther(otherCoin.getName());
        exPair.setPairOne(mainCoin.getName());
        exPair.setStatus("1");
        exPairService.save(exPair);
        addMessage(redirectAttributes, "发布币币交易对成功");
        redirectAttributes.addFlashAttribute("exPair", request.getSession().getAttribute("searchExpair"));
        return "redirect:" + Global.getAdminPath() + "/plat/exPair/?repage";
    }

    @RequiresPermissions("plat:exPair:edit")
    @RequestMapping(value = "lock")
    public String lock(ExPair exPair, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        Coin mainCoin = coinService.get(exPair.getCoinId());
        Coin otherCoin = coinService.get(exPair.getOtherCoinId());
        exPair.setPairOther(otherCoin.getName());
        exPair.setPairOne(mainCoin.getName());
        exPair.setStatus("0");
        exPairService.save(exPair);
        addMessage(redirectAttributes, "锁定币币交易对成功");
        redirectAttributes.addFlashAttribute("exPair", request.getSession().getAttribute("searchExpair"));
        return "redirect:" + Global.getAdminPath() + "/plat/exPair/?repage";
    }

}