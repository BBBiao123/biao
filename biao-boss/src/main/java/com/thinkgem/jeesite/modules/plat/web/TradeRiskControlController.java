/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.modules.plat.entity.ExPair;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;
import com.thinkgem.jeesite.modules.plat.service.ExPairService;
import com.thinkgem.jeesite.modules.plat.service.PlatUserService;
import org.apache.commons.collections.CollectionUtils;
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
import com.thinkgem.jeesite.modules.plat.entity.TradeRiskControl;
import com.thinkgem.jeesite.modules.plat.service.TradeRiskControlService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 交易风险控制Controller
 *
 * @author xiaoyu
 * @version 2018-09-10
 */
@Controller
@RequestMapping(value = "${adminPath}/plat/tradeRiskControl")
public class TradeRiskControlController extends BaseController {

    @Autowired
    private TradeRiskControlService tradeRiskControlService;

    @Autowired
    private ExPairService exPairService;

    @Autowired
    private PlatUserService platUserService;

    @ModelAttribute
    public TradeRiskControl get(@RequestParam(required = false) String id) {
        TradeRiskControl entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tradeRiskControlService.get(id);
        }
        if (entity == null) {
            entity = new TradeRiskControl();
        }
        return entity;
    }

    @RequiresPermissions("plat:tradeRiskControl:view")
    @RequestMapping(value = {"list", ""})
    public String list(TradeRiskControl tradeRiskControl, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TradeRiskControl> page = tradeRiskControlService.findPage(new Page<TradeRiskControl>(request, response), tradeRiskControl);
        model.addAttribute("page", page);
        return "modules/plat/tradeRiskControlList";
    }

    @RequiresPermissions("plat:tradeRiskControl:view")
    @RequestMapping(value = "form")
    public String form(TradeRiskControl tradeRiskControl, Model model) {
        model.addAttribute("tradeRiskControl", tradeRiskControl);
        ExPair exPair = new ExPair();
        exPair.setStatus("1");
        List<ExPair> exPairList = exPairService.findList(exPair);
        model.addAttribute(exPairList);
        return "modules/plat/tradeRiskControlForm";
    }

    @RequiresPermissions("plat:tradeRiskControl:edit")
    @RequestMapping(value = "save")
    public String save(TradeRiskControl tradeRiskControl, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, tradeRiskControl)) {
            return form(tradeRiskControl, model);
        }
        final List<PlatUser> sourceUsers = platUserService.findBySource(tradeRiskControl.getSourceUser());
        if (CollectionUtils.isEmpty(sourceUsers)) {
            return form(tradeRiskControl, model);
        }
        final String userIds = sourceUsers.stream()
                .map(BaseEntity::getId).collect(Collectors.joining(","));
        tradeRiskControl.setUserIds(userIds);
        tradeRiskControlService.save(tradeRiskControl);
        addMessage(redirectAttributes, "保存交易风险控制成功");
        return "redirect:" + Global.getAdminPath() + "/plat/tradeRiskControl/?repage";
    }

    @RequiresPermissions("plat:tradeRiskControl:edit")
    @RequestMapping(value = "delete")
    public String delete(TradeRiskControl tradeRiskControl, RedirectAttributes redirectAttributes) {
        tradeRiskControlService.delete(tradeRiskControl);
        addMessage(redirectAttributes, "删除交易风险控制成功");
        return "redirect:" + Global.getAdminPath() + "/plat/tradeRiskControl/?repage";
    }

}