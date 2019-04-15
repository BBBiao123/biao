/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.market.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.modules.plat.entity.Coin;
import com.thinkgem.jeesite.modules.plat.service.CoinService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.market.entity.Mk2PopularizeCommonMember;
import com.thinkgem.jeesite.modules.market.service.Mk2PopularizeCommonMemberService;

import java.math.BigDecimal;
import java.util.List;

/**
 * 普通用户Controller
 * @author dongfeng
 * @version 2018-08-17
 */
@Controller
@RequestMapping(value = "${adminPath}/market/mk2PopularizeCommonMember")
public class Mk2PopularizeCommonMemberController extends BaseController {

	@Autowired
	private CoinService coinService;

	@Autowired
	private Mk2PopularizeCommonMemberService mk2PopularizeCommonMemberService;
	
	@ModelAttribute
	public Mk2PopularizeCommonMember get(@RequestParam(required=false) String id) {
		Mk2PopularizeCommonMember entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mk2PopularizeCommonMemberService.get(id);
		}
		if (entity == null){
			entity = new Mk2PopularizeCommonMember();
		}
		return entity;
	}
	
	@RequiresPermissions("market:mk2PopularizeCommonMember:view")
	@RequestMapping(value = {"list", ""})
	public String list(Mk2PopularizeCommonMember mk2PopularizeCommonMember, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Mk2PopularizeCommonMember> page = mk2PopularizeCommonMemberService.findPage(new Page<Mk2PopularizeCommonMember>(request, response), mk2PopularizeCommonMember); 
		model.addAttribute("page", page);
		return "modules/market/mk2PopularizeCommonMemberList";
	}

	@RequiresPermissions("market:mk2PopularizeCommonMember:view")
	@RequestMapping(value = "form")
	public String form(Mk2PopularizeCommonMember mk2PopularizeCommonMember, Model model) {
		model.addAttribute(coinService.findList(new Coin()));
		model.addAttribute("mk2PopularizeCommonMember", mk2PopularizeCommonMember);
		return "modules/market/mk2PopularizeCommonMemberForm";
	}

	@RequiresPermissions("market:mk2PopularizeCommonMember:edit")
	@RequestMapping(value = "save")
	public String save(Mk2PopularizeCommonMember mk2PopularizeCommonMember, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mk2PopularizeCommonMember)){
			return form(mk2PopularizeCommonMember, model);
		}
		try {
			mk2PopularizeCommonMemberService.save(mk2PopularizeCommonMember);
			addMessage(model, "保存普通用户成功");
		} catch (ServiceException e) {
			logger.error("保存普通用户失败", e);
			addMessage(model, e.getMessage());
		} catch (Exception e) {
			logger.error("保存普通用户失败", e);
			addMessage(model, "参数错误，保存普通用户失败！");
		}
		return form(mk2PopularizeCommonMember, model);
	}

	@RequiresPermissions("market:mk2PopularizeCommonMember:edit")
	@RequestMapping(value = "release")
	public String release(BigDecimal manualReleaseVolume, String commonMemberId, Model model, RedirectAttributes redirectAttributes) {

		Mk2PopularizeCommonMember mk2PopularizeCommonMember = null;
		try {
			mk2PopularizeCommonMemberService.release(manualReleaseVolume, commonMemberId);
			addMessage(model, "手动释放数量" + manualReleaseVolume +"成功");
		} catch (ServiceException e) {
			logger.error("手动释放数量失败", e);
			addMessage(model, "释放失败：" + e.getMessage());
		} catch (Exception e) {
			logger.error("手动释放数量失败", e);
			addMessage(model, "手动释放数量失败！");
		}
		mk2PopularizeCommonMember = mk2PopularizeCommonMemberService.get(commonMemberId);
		model.addAttribute("mk2PopularizeCommonMember", mk2PopularizeCommonMember);
		return form(mk2PopularizeCommonMember, model);
	}
	
	@RequiresPermissions("market:mk2PopularizeCommonMember:edit")
	@RequestMapping(value = "delete")
	public String delete(Mk2PopularizeCommonMember mk2PopularizeCommonMember, RedirectAttributes redirectAttributes) {
		mk2PopularizeCommonMemberService.delete(mk2PopularizeCommonMember);
		addMessage(redirectAttributes, "删除普通用户成功");
		return "redirect:"+Global.getAdminPath()+"/market/mk2PopularizeCommonMember/?repage";
	}

	/**
	 * 导入
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("market:mk2PopularizeCommonMember:edit")
	@RequestMapping(value = "import", method=RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/market/mk2PopularizeCommonMember/list?repage";
		}
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Mk2PopularizeCommonMember> list = ei.getDataList(Mk2PopularizeCommonMember.class);
			for (Mk2PopularizeCommonMember member : list){
				try{
					mk2PopularizeCommonMemberService.saveImport(member);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureMsg.append("<br/>会员 "+ member.getRealName() +" 导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append(message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("<br/>会员 "+ member.getRealName() + " 导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/market/mk2PopularizeCommonMember/list?repage";
	}

	/**
	 * 下载导入用户数据模板
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("market:mk2PopularizeCommonMember:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "会员币冻结模板.xlsx";
			List<Mk2PopularizeCommonMember> list = Lists.newArrayList();
			Mk2PopularizeCommonMember mk2PopularizeCommonMember = new Mk2PopularizeCommonMember();
			mk2PopularizeCommonMember.setType("3");
			mk2PopularizeCommonMember.setUserId("252200802112901121");
			mk2PopularizeCommonMember.setMail("otc001@qq.com");
			mk2PopularizeCommonMember.setMobile("13659885621");
			mk2PopularizeCommonMember.setIdCard("556998965532653021");
			mk2PopularizeCommonMember.setRealName("蜀");
			mk2PopularizeCommonMember.setCoinId("e7ca5a25c7df4bcead954ade71aec7fe");
			mk2PopularizeCommonMember.setCoinSymbol("UES");
			mk2PopularizeCommonMember.setLockStatus("1");
			mk2PopularizeCommonMember.setLockVolumeDouble(Double.valueOf("2000"));
			mk2PopularizeCommonMember.setReleaseVolumeDouble(Double.valueOf("0"));
			mk2PopularizeCommonMember.setReleaseBeginDateString("2019-03-05");
			mk2PopularizeCommonMember.setReleaseCycle("1");
			mk2PopularizeCommonMember.setReleaseCycleRatioDouble(Double.valueOf("0.1"));
			mk2PopularizeCommonMember.setRemark("活动奖励");
			list.add(mk2PopularizeCommonMember);
			new ExportExcel("会员币冻结导入", Mk2PopularizeCommonMember.class, 2).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}
}