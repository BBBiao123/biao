package com.thinkgem.jeesite.modules.disruptor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.lmax.disruptor.WorkHandler;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.modules.plat.entity.JsPlatUserAirdrop;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;
import com.thinkgem.jeesite.modules.plat.entity.UserCoinVolume;
import com.thinkgem.jeesite.modules.plat.service.JsPlatUserAirdropService;
import com.thinkgem.jeesite.modules.plat.service.PlatUserService;
import com.thinkgem.jeesite.modules.plat.service.UserCoinVolumeService;

public class DisruptorDataEventHandler implements WorkHandler<DisruptorData>{

	private static Logger logger = LoggerFactory.getLogger(DisruptorDataEventHandler.class);
	
	private UserCoinVolumeService userCoinVolumeService;
	
	private PlatUserService platUserService ;
	
	private JsPlatUserAirdropService jsPlatUserAirdropService;
	
	private static volatile ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()<<1) ;
	
	public DisruptorDataEventHandler(){
		userCoinVolumeService = SpringContextHolder.getBean(UserCoinVolumeService.class);
		platUserService = SpringContextHolder.getBean(PlatUserService.class);
		jsPlatUserAirdropService = SpringContextHolder.getBean(JsPlatUserAirdropService.class);
	}
	
	@Override
	public void onEvent(DisruptorData disruptorData) throws Exception {
		//获取
		Object data = disruptorData.getData();
		if(data instanceof JsPlatUserAirdrop) {
			JsPlatUserAirdrop airdrop = (JsPlatUserAirdrop)data ;
			handlerAirdrop(airdrop);
		}
	}

	private void handlerAirdrop(JsPlatUserAirdrop airdrop) {
		//处理用户   增加资产    更新记录状态
		Page<PlatUser> page = new Page<>();
		page.setPageNo(1);
		page.setPageSize(2000);
		PlatUser platUser = new PlatUser();
		if(airdrop.getUserType().equals("1")) {
			//实名用户
			platUser.setCardStatus("1");
		}
		if(airdrop.getEndTime()!=null) {
			platUser.setEndTime(airdrop.getEndTime());
		}
		if(airdrop.getStartTime()!=null) {
			platUser.setStartTime(airdrop.getStartTime());
		}
		page.setOrderBy("id");
		Page<PlatUser> users = platUserService.findPage(page, platUser);
		if(users.getCount()>page.getPageSize()) {
			//开启分页
			airdrop.setStatus("1");
			jsPlatUserAirdropService.update(airdrop);
			for(PlatUser user : users.getList()) {
				handlerUserCoin(user, airdrop);
			}
			long pageTotal = (users.getCount()/page.getPageSize())+(users.getCount()%page.getPageSize()==0?0:1) ;
			for(int i=2;i<=pageTotal;i++) {
				final int pageNo = i ;
				executorService.execute(()->{
					 Page<PlatUser> pageQuery = new Page<>();
					 pageQuery.setPageSize(page.getPageSize());
					 pageQuery.setPageNo(pageNo);
					 pageQuery.setOrderBy("id");
					 PlatUser platUserQuery = new PlatUser();
					 if(airdrop.getUserType().equals("1")) {
						//实名用户
						 platUserQuery.setCardStatus("1");
					 }
					 if(airdrop.getEndTime()!=null) {
						 platUserQuery.setEndTime(airdrop.getEndTime());
					 }
					 Page<PlatUser> pageQueryUsers = platUserService.findPage(pageQuery, platUserQuery);
					 if(pageQueryUsers!=null&&pageQueryUsers.getList()!=null) {
						 for(PlatUser user : pageQueryUsers.getList()) {
							 handlerUserCoin(user, airdrop);
						 }
					 }
				});
				if(i==pageTotal) {
					airdrop.setStatus("2");
					jsPlatUserAirdropService.update(airdrop);
				}
			}
		}else {
			if(!CollectionUtils.isEmpty(users.getList())) {
				for(PlatUser user : users.getList()) {
					handlerUserCoin(user, airdrop);
				}
			}
			airdrop.setStatus("2");
			jsPlatUserAirdropService.update(airdrop);
		}
	}
	
	private void handlerUserCoin(PlatUser platUser,JsPlatUserAirdrop airdrop) {
		UserCoinVolume userCoinVolume = new UserCoinVolume();
		userCoinVolume.setCoinId(airdrop.getCoinId());
		userCoinVolume.setCoinSymbol(airdrop.getCoinSymbol());
		userCoinVolume.setUserId(platUser.getId());
		userCoinVolume.setVolume(new BigDecimal(airdrop.getNumber()));
		userCoinVolume.setCreateBy(airdrop.getCreateBy());
		userCoinVolume.setCreateDate(new Date());
		userCoinVolume.setDelFlag("0");
		userCoinVolume.setMail(platUser.getMail());
		userCoinVolume.setMobile(platUser.getMobile());
		userCoinVolume.setAirdropId(platUser.getId()+"_"+airdrop.getId());
		userCoinVolumeService.insert(userCoinVolume);
		if(logger.isDebugEnabled()) {
			logger.debug("空头用户userId:{},币种coin:{},数量volume:{}",platUser.getId(),airdrop.getCoinSymbol(),airdrop.getNumber());
		}
	}

}
