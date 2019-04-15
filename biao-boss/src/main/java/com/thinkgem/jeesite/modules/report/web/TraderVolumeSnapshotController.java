/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.report.web;

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
import com.thinkgem.jeesite.modules.report.entity.TraderVolumeSnapshot;
import com.thinkgem.jeesite.modules.report.service.TraderVolumeSnapshotService;

import java.util.Objects;

/**
 * 操盘手资产快照Controller
 * @author zzj
 * @version 2018-12-21
 */
@Controller
@RequestMapping(value = "${adminPath}/report/traderVolumeSnapshot")
public class TraderVolumeSnapshotController extends BaseController {

	@Autowired
	private TraderVolumeSnapshotService traderVolumeSnapshotService;
	
	@ModelAttribute
	public TraderVolumeSnapshot get(@RequestParam(required=false) String id) {
		TraderVolumeSnapshot entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = traderVolumeSnapshotService.get(id);
		}
		if (entity == null){
			entity = new TraderVolumeSnapshot();
		}
		return entity;
	}
	
	@RequiresPermissions("report:traderVolumeSnapshot:view")
	@RequestMapping(value = {"list", ""})
	public String list(TraderVolumeSnapshot traderVolumeSnapshot, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TraderVolumeSnapshot> page = new Page<>();
		if(Objects.nonNull(traderVolumeSnapshot.getSnapDate()) || StringUtils.isNotEmpty(traderVolumeSnapshot.getUserTag())){
			page = traderVolumeSnapshotService.findPage(new Page<TraderVolumeSnapshot>(request, response), traderVolumeSnapshot);
		}
		model.addAttribute("page", page);
		return "modules/report/traderVolumeSnapshotList";
	}

	@RequiresPermissions("report:traderVolumeSnapshot:view")
	@RequestMapping(value = "form")
	public String form(TraderVolumeSnapshot traderVolumeSnapshot, Model model) {
		model.addAttribute("traderVolumeSnapshot", traderVolumeSnapshot);
		return "modules/report/traderVolumeSnapshotForm";
	}

	@RequiresPermissions("report:traderVolumeSnapshot:edit")
	@RequestMapping(value = "save")
	public String save(TraderVolumeSnapshot traderVolumeSnapshot, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, traderVolumeSnapshot)){
			return form(traderVolumeSnapshot, model);
		}
		traderVolumeSnapshotService.save(traderVolumeSnapshot);
		addMessage(redirectAttributes, "保存操盘手资产快照成功");
		return "redirect:"+Global.getAdminPath()+"/report/traderVolumeSnapshot/?repage";
	}
	
	@RequiresPermissions("report:traderVolumeSnapshot:edit")
	@RequestMapping(value = "delete")
	public String delete(TraderVolumeSnapshot traderVolumeSnapshot, RedirectAttributes redirectAttributes) {
		traderVolumeSnapshotService.delete(traderVolumeSnapshot);
		addMessage(redirectAttributes, "删除操盘手资产快照成功");
		return "redirect:"+Global.getAdminPath()+"/report/traderVolumeSnapshot/?repage";
	}

}