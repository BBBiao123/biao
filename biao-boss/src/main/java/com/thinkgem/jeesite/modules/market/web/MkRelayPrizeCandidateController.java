/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.market.entity.MkRelayPrizeCandidate;
import com.thinkgem.jeesite.modules.market.service.MkRelayPrizeCandidateService;

import java.util.List;

/**
 * 接力撞奖名单Controller
 * @author zzj
 * @version 2018-09-05
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mkRelayPrizeCandidate")
public class MkRelayPrizeCandidateController extends BaseController {

	@Autowired
	private MkRelayPrizeCandidateService mkRelayPrizeCandidateService;
	
	@ModelAttribute
	public MkRelayPrizeCandidate get(@RequestParam(required=false) String id) {
		MkRelayPrizeCandidate entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mkRelayPrizeCandidateService.get(id);
		}
		if (entity == null){
			entity = new MkRelayPrizeCandidate();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mkRelayPrizeCandidate:view")
	@RequestMapping(value = {"list", ""})
	public String list(MkRelayPrizeCandidate mkRelayPrizeCandidate, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MkRelayPrizeCandidate> page = mkRelayPrizeCandidateService.findPage(new Page<MkRelayPrizeCandidate>(request, response), mkRelayPrizeCandidate); 
		model.addAttribute("page", page);
		return "modules/market/mkRelayPrizeCandidateList";
	}

	@RequiresPermissions("market:mkRelayPrizeCandidate:view")
	@RequestMapping(value = "form")
	public String form(MkRelayPrizeCandidate mkRelayPrizeCandidate, Model model) {
		model.addAttribute("mkRelayPrizeCandidate", mkRelayPrizeCandidate);
		return "modules/market/mkRelayPrizeCandidateForm";
	}

	@RequiresPermissions("market:mkRelayPrizeCandidate:edit")
	@RequestMapping(value = "save")
	public String save(MkRelayPrizeCandidate mkRelayPrizeCandidate, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mkRelayPrizeCandidate)){
			return form(mkRelayPrizeCandidate, model);
		}
		mkRelayPrizeCandidateService.save(mkRelayPrizeCandidate);
		addMessage(redirectAttributes, "保存接力撞奖名单成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkRelayPrizeCandidate/?repage";
	}
	
	@RequiresPermissions("market:mkRelayPrizeCandidate:edit")
	@RequestMapping(value = "delete")
	public String delete(MkRelayPrizeCandidate mkRelayPrizeCandidate, RedirectAttributes redirectAttributes) {
		mkRelayPrizeCandidateService.delete(mkRelayPrizeCandidate);
		addMessage(redirectAttributes, "删除接力撞奖名单成功");
		return "redirect:"+Global.getAdminPath()+"/market/mkRelayPrizeCandidate/?repage";
	}

	@RequiresPermissions("market:mkRelayPrizeCandidate:view")
	@RequestMapping(value = "opt")
	public String opt(MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/market/mkRelayPrizeCandidate/?repage";
		}
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<MkRelayPrizeCandidate> list = ei.getDataList(MkRelayPrizeCandidate.class);
			for (MkRelayPrizeCandidate candidate : list){
				try{
					mkRelayPrizeCandidateService.opt(candidate);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureMsg.append("<br/>原手机号 "+candidate.getMobile()+" 导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append(message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("<br/>原手机号 "+candidate.getMobile()+" 导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/market/mkRelayPrizeCandidate/?repage";
	}

	/**
	 * 下载接力自动撞奖名单优化模板
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("market:mkRelayPrizeCandidate:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "接力自动撞奖名单优化.xlsx";
			List<MkRelayPrizeCandidate> list = Lists.newArrayList();
			MkRelayPrizeCandidate candidate = new MkRelayPrizeCandidate();
			candidate.setMobile("13111111111");
			candidate.setTargetMobile("13222222222");
			list.add(candidate);
			new ExportExcel("用户数据（文本格式，前面加撇号，如：'13222222222）", MkRelayPrizeCandidate.class, 2).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/market/mkRelayPrizeCandidate/?repage";
	}


}