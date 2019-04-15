package com.biao.service.impl;

import com.biao.entity.PlatUser;
import com.biao.entity.PlatUserSyna;
import com.biao.enums.MessageTemplateCode;
import com.biao.mapper.PlatUserSynaDao;
import com.biao.service.PlatUserService;
import com.biao.service.PlatUserSynaService;
import com.biao.service.SmsMessageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service("platUserSynaService")
public class PlatUserSynaServiceImpl implements PlatUserSynaService {

    private static Logger logger = LoggerFactory.getLogger(PlatUserSynaServiceImpl.class);

    @Autowired
    private PlatUserSynaDao platUserSynaDao;
    @Autowired
    private PlatUserService platUserService;

    @Autowired
    private SmsMessageService smsMessageService;

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    private static String[] telFirst="134,135,136,137,138,139,147,148,150,151,152,157,158,159,172,178,182,183,184,187,188,198,130,131,132,145,146,155,156,166,171,175,176,185,186,133,149,153,173,174,177,180,181,189,199".split(",");


    @Override
    public void batchHandler(List<PlatUserSyna> platUserSynas) {
        List<PlatUserSyna> batchPlatUsers = new ArrayList<>();
        for (PlatUserSyna platUserSyna : platUserSynas) {
            //判断是否重复
            PlatUserSyna syna = platUserSynaDao.findByIdMobile(platUserSyna.getMobile());
            if (syna != null) {
                logger.error("手机号mobile:{},已经存在", platUserSyna.getMobile());
                continue;
            }
            PlatUserSyna userSyna = null;
            if (StringUtils.isNotBlank(platUserSyna.getSource())) {
                userSyna = platUserSynaDao.findByIdAndSource(platUserSyna.getSourceId(), platUserSyna.getSource());
            } else {
                userSyna = platUserSynaDao.findById(platUserSyna.getSourceId());
            }
            if (userSyna != null) {
                logger.error("同步用户id:{},source:{},已经存在", platUserSyna.getSourceId(), platUserSyna.getSource());
                continue;
            }
            batchPlatUsers.add(platUserSyna);
        }
        if (batchPlatUsers.size() > 0) {
            platUserSynaDao.batchInsert(batchPlatUsers);
        }
    }

    @Override
    public void synUserToPlatUser() {
        //查询顶级的用户
        List<PlatUserSyna> topUsers = platUserSynaDao.findListByStatusAndParent(0);
        List<String> errorListMobiles = new ArrayList<>();
        if (!CollectionUtils.isEmpty(topUsers)) {
            //同步顶级用户
            for (PlatUserSyna topUser : topUsers) {
                PlatUser platUser = platUserService.findByLoginName(topUser.getMobile());
                if (platUser != null) {
                    logger.error("手机号mobile:{},已经存在", topUser.getMobile());
                    this.updateUnSuccess(topUser, 4, String.format("手机号mobile:%s,已经存在", topUser.getMobile()));
                    continue;
                }
                try {
                    //更新用户的状态
                    PlatUserSyna platUserSyna = new PlatUserSyna();

                    platUser = new PlatUser();
                    platUser.setMobile(topUser.getMobile());
                    platUser.setSource(topUser.getSource());
                    platUser.setRealName(topUser.getUsername());
                    platUserService.synUser(platUser, platUserSyna);

                    platUserSyna.setStatus(1);
                    platUserSyna.setUpdateDate(LocalDateTime.now());
                    platUserSyna.setSynDate(LocalDateTime.now());
                    platUserSyna.setId(topUser.getId());
                    platUserSynaDao.updateById(platUserSyna);
                } catch (Exception e) {
                    logger.error("同步用户mobile:{},同步异常e:{}", topUser.getMobile(), e.getMessage());
                    errorListMobiles.add(topUser.getMobile());
                    this.updateUnSuccess(topUser, 4, String.format("同步用户mobile:%s,同步异常e:%s", topUser.getMobile(), e.getMessage().substring(0, e.getMessage().length() % 400)));
                }
            }
        }
        //同步一般用户
        topUsers = platUserSynaDao.findListByStatus(0);
        List<PlatUserSyna> recursionSynUser = recursionSynUser(topUsers, errorListMobiles);
        //递归同步用户关系
        int cycleSize = 0;
        while (!CollectionUtils.isEmpty(recursionSynUser) && cycleSize < 50) {
            recursionSynUser = recursionSynUser(recursionSynUser, errorListMobiles);
            cycleSize++;
        }
        if (!CollectionUtils.isEmpty(recursionSynUser)) {
            recursionSynUser.stream().distinct().forEach(synUser -> {
                logger.error("同步用户mobile:{},id:{},数据错误不能同步", synUser.getMobile(), synUser.getSourceId());
                this.updateUnSuccess(synUser, 4, String.format("同步用户mobile:%s,id:%s,数据错误不能同步", synUser.getMobile(), synUser.getSourceId()));
            });
        }
    }

    private List<PlatUserSyna> recursionSynUser(List<PlatUserSyna> topUsers, List<String> errorListMobiles) {
        List<PlatUserSyna> platUserSynas = new ArrayList<>();
        if (!CollectionUtils.isEmpty(topUsers)) {
            for (PlatUserSyna topUser : topUsers) {
                if (errorListMobiles.contains(topUser.getMobile())) {
                    continue;
                }
                if (StringUtils.isBlank(topUser.getSourceParentId())) {
                    errorListMobiles.add(topUser.getMobile());
                    continue;
                }
                PlatUser platUser = platUserService.findByLoginName(topUser.getMobile());
                if (platUser != null) {
                    logger.error("手机号mobile:{},已经存在", topUser.getMobile());
                    errorListMobiles.add(topUser.getMobile());
                    this.updateUnSuccess(topUser, 4, String.format("手机号mobile:%s,已经存在", topUser.getMobile()));
                    continue;
                }
                //查询上级
                PlatUserSyna parent = platUserSynaDao.findByIdAndSource(topUser.getSourceParentId(), topUser.getSource());
                if (parent == null) {
                    logger.error("手机号mobile:{},其parentId:{},source:{}的记录不存在,数据错误", topUser.getMobile(), topUser.getSourceParentId(), topUser.getSource());
                    this.updateUnSuccess(topUser, 4, String.format("手机号mobile:%s,其parentId:%s,source:%s的记录不存在,数据错误", topUser.getMobile(), topUser.getSourceParentId(), topUser.getSource()));
                    continue;
                }
                //上级已经存在,开始同步用户
                PlatUser platUserParent = platUserService.findByLoginName(parent.getMobile());
                if (platUserParent != null) {
                    try {
                        //更新用户的状态
                        PlatUserSyna platUserSyna = new PlatUserSyna();

                        platUser = new PlatUser();
                        platUser.setMobile(topUser.getMobile());
                        platUser.setInviteCode(platUserParent.getInviteCode());
                        platUser.setSource(topUser.getSource());
                        platUser.setRealName(topUser.getUsername());
                        platUserService.synUser(platUser, platUserSyna);

                        platUserSyna.setStatus(1);
                        platUserSyna.setUpdateDate(LocalDateTime.now());
                        platUserSyna.setSynDate(LocalDateTime.now());
                        platUserSyna.setId(topUser.getId());
                        platUserSynaDao.updateById(platUserSyna);
                    } catch (Exception e) {
                        logger.error("同步用户mobile:{},同步异常e:{}", topUser.getMobile(), e.getMessage());
                        errorListMobiles.add(topUser.getMobile());
                        this.updateUnSuccess(topUser, 4, String.format("同步用户mobile:%s,同步异常e:%s", topUser.getMobile(), e.getMessage().substring(0, e.getMessage().length() % 400)));
                    }
                } else {
                    platUserSynas.add(topUser);
                }
            }
        }
        return platUserSynas;
    }

    private void updateUnSuccess(PlatUserSyna platUserSyna, Integer status, String remark) {
        platUserSyna.setStatus(status);
        platUserSyna.setRemark(remark);
        platUserSyna.setUpdateDate(LocalDateTime.now());
        platUserSynaDao.updateById(platUserSyna);
    }

    @Override
    public void sendMessageToSynUser() {

        List<PlatUserSyna> platUserSynaList = platUserSynaDao.findListBySendMsgStatus("0");
        if (CollectionUtils.isEmpty(platUserSynaList)) {
            logger.info("没有可发送短信的同步用户！");
            return;
        }

        int pageCount = 30000;
        int page = platUserSynaList.size() % pageCount != 0 ? (platUserSynaList.size() / pageCount + 1) : (platUserSynaList.size() / pageCount);
        List<PlatUserSyna> pageTmp = new ArrayList<>();
        for (int index = 1; index <= page; index++) {

            logger.info(String.format("发送短信开始第%s页", String.valueOf(index)));
            if (index == page) {
                pageTmp = platUserSynaList.subList((index - 1) * pageCount, platUserSynaList.size());
            } else {
                pageTmp = platUserSynaList.subList((index - 1) * pageCount, index * pageCount);
            }

            List<PlatUserSyna> curList = pageTmp;
            executorService.execute(() -> {
                try {
                    this.sendMessageToSynUserOnePage(curList);
                } catch (Exception e) {
                    logger.error("多线程发送短信，e:{}", e);
                }
            });
        }

        List<PlatUserSyna> curUserList = platUserSynaDao.findListBySendMsgStatus("0");
        while (!CollectionUtils.isEmpty(curUserList)) {
            try {
                Thread.sleep(2000);
                curUserList = platUserSynaDao.findListBySendMsgStatus("0");
            } catch (Exception e) {
                logger.info("发送短信异常，e:{}", e);
            }
        }

    }

    public void sendMessageToSynUserOnePage(List<PlatUserSyna> platUserSynaList) {

        if (CollectionUtils.isEmpty(platUserSynaList)) {
            logger.info("没有可发送短信的同步用户！");
            return;
        }

        platUserSynaList.forEach(platUserSyna -> {
            try {
                logger.info(String.format("同步用户mobile:%s,发送密码:%s初始化短信！", platUserSyna.getMobile(), platUserSyna.getPass()));
                if (StringUtils.isNotEmpty(platUserSyna.getPass())) {
                    smsMessageService.sendSms(platUserSyna.getMobile(), MessageTemplateCode.MOBILE_SYN_TEMPLATE.getCode(), platUserSyna.getPass());
                    platUserSyna.setIsSendMsg("1");
                    platUserSynaDao.updateById(platUserSyna);
                }
            } catch (Exception e) {
                if (StringUtils.isNotEmpty(e.getMessage())) {
                    logger.error(String.format("同步用户mobile:%s,发送短信异常:%s", platUserSyna.getMobile(), e.getMessage().substring(0, e.getMessage().length() % 400)), e);
                    platUserSyna.setIsSendMsg("4");
                    platUserSyna.setRemark(String.format("同步用户mobile:%s,发送短信异常:%s", platUserSyna.getMobile(), e.getMessage().substring(0, e.getMessage().length() % 400)));
                    platUserSynaDao.updateById(platUserSyna);
                } else {
                    logger.error(String.format("同步用户mobile:%s,发送短信异常", platUserSyna.getMobile()), e);
                    platUserSyna.setIsSendMsg("4");
                    platUserSyna.setRemark(String.format("同步用户mobile:%s,发送短信异常", platUserSyna.getMobile()));
                    platUserSynaDao.updateById(platUserSyna);
                }
            }
        });
    }


    @Override
    public void synEgoToPlatUser() {
        //查询所有
        List<PlatUserSyna> topUsers = platUserSynaDao.findListByStatus(0);

        int pageCount = 30000;
        int page = topUsers.size() % pageCount != 0 ? (topUsers.size() / pageCount + 1) : (topUsers.size() / pageCount);
        List<PlatUserSyna> pageTmp = new ArrayList<>();
        for (int index = 1; index <= page; index++) {

            logger.info(String.format("开始第%s页", String.valueOf(index)));
            if (index == page) {
                pageTmp = topUsers.subList((index - 1) * pageCount, topUsers.size());
            } else {
                pageTmp = topUsers.subList((index - 1) * pageCount, index * pageCount);
            }

            List<PlatUserSyna> platUserSynaList = pageTmp;
            executorService.execute(() -> {
                try {
                    this.synUserOnePage(platUserSynaList);
                } catch (Exception e) {
                    logger.error("同步用户，e:{}", e);
                }
            });
        }

        List<PlatUserSyna> curUserList = platUserSynaDao.findListByStatus(0);
        while (!CollectionUtils.isEmpty(curUserList)) {
            try {
                Thread.sleep(2000);
                curUserList = platUserSynaDao.findListByStatus(0);
            } catch (Exception e) {
                logger.info("同步数据异常，e:{}", e);
            }
        }

    }

    private void synUserOnePage(List<PlatUserSyna> topUsers) {
        //同步错误集合
        List<String> errorListMobiles = new ArrayList<>();

        if (!CollectionUtils.isEmpty(topUsers)) {
            for (PlatUserSyna topUser : topUsers) {
                PlatUser platUser = platUserService.findByLoginName(topUser.getMobile());
                if (platUser != null) {
                    logger.error("手机号mobile:{},已经存在", topUser.getMobile());
                    errorListMobiles.add(topUser.getMobile());
                    this.updateUnSuccess(topUser, 4, String.format("手机号mobile:%s,已经存在", topUser.getMobile()));
                    continue;
                }
                try {
                    //更新用户的状态
                    platUser = platUserService.findById(topUser.getSourceParentId());
                    if (platUser == null) {
                        logger.error(String.format("手机号mobile:%s的pid:%s在平台不存在！", topUser.getMobile(), topUser.getSourceParentId()));
                        errorListMobiles.add(topUser.getMobile());
                        this.updateUnSuccess(topUser, 4, String.format("手机号mobile:%s的pid:%s在平台不存在！", topUser.getMobile(), topUser.getSourceParentId()));
                        continue;
                    }

                    PlatUser curPlatUser = new PlatUser();
                    curPlatUser.setMobile(topUser.getMobile());
                    curPlatUser.setUsername(topUser.getMobile());
                    curPlatUser.setSource(topUser.getSource());
                    curPlatUser.setRealName(topUser.getUsername());
                    curPlatUser.setInviteCode(platUser.getInviteCode());
                    PlatUserSyna platUserSyna = new PlatUserSyna();
                    platUserService.synUser(curPlatUser, platUserSyna);

                    //更新同步信息
                    topUser.setStatus(1);
                    topUser.setUpdateDate(LocalDateTime.now());
                    topUser.setSynDate(LocalDateTime.now());
                    topUser.setPass(platUserSyna.getPass());
                    platUserSynaDao.updateById(topUser);

                    logger.info(String.format("成功同步用户，手机:%s", topUser.getMobile()));
                } catch (Exception e) {
                    logger.error("同步用户mobile:{},同步异常e:{}", topUser.getMobile(), e.getMessage());
                    errorListMobiles.add(topUser.getMobile());
                    if (StringUtils.isNotEmpty(e.getMessage())) {
                        this.updateUnSuccess(topUser, 4, String.format("同步用户mobile:%s,同步异常e:%s", topUser.getMobile(), e.getMessage().substring(0, e.getMessage().length() % 400)));
                    } else {
                        this.updateUnSuccess(topUser, 4, String.format("同步用户mobile:%s,同步异常e:%s", topUser.getMobile(), e));
                    }
                }
            }
        }
    }

    @Override
    public void batchCreateSynUser() {
        for(int i = 0; i < 1000; i ++){
            PlatUserSyna platUserSyna = new PlatUserSyna();
            platUserSyna.setId(UUID.randomUUID().toString().replace("-",""));
            platUserSyna.setStatus(0);
            platUserSyna.setIsSendMsg("1");
            platUserSyna.setSource("AUTO");
            platUserSyna.setMobile(this.getMobile());
            platUserSyna.setSourceId(UUID.randomUUID().toString().replace("-",""));
            platUserSyna.setSourceParentId("235477240555638784");
            platUserSyna.setCreateDate(LocalDateTime.now());
            platUserSynaDao.insert(platUserSyna);
        }
    }

    private String getMobile(){
        int index = getNum(0,telFirst.length-1);
        String first = telFirst[index];
//        String second = String.valueOf(getNum(1,888)+10000).substring(1);
        String second = "4444";
        String third = String.valueOf(getNum(1,9100)+10000).substring(1);
        return first + second + third;
    }

    public  int getNum(int start,int end) {
        return (int)(Math.random()*(end-start+1)+start);
    }
}
