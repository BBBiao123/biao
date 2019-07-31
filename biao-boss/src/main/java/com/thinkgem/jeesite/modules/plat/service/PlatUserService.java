/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.math.BigDecimal;
import java.util.List;

import com.thinkgem.jeesite.modules.plat.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.dao.PlatUserDao;
import org.springframework.util.CollectionUtils;

/**
 * 前台用户Service
 *
 * @author dazi
 * @version 2018-04-26
 */
@Service
@Transactional(readOnly = true)
public class PlatUserService extends CrudService<PlatUserDao, PlatUser> {

    @Autowired
    private UserCoinVolumeService userCoinVolumeService;

    @Autowired
    private Mk2PopularizeRegisterCoinService registerCoinService;

    @Autowired
    private Mk2PopularizeRegisterConfService registerConfService;
    @Autowired
    private CoinService coinService;

    public PlatUser get(String id) {
        return super.get(id);
    }

    public List<PlatUser> findList(PlatUser platUser) {
        return super.findList(platUser);
    }
    
    public List<PlatUser> findOne(PlatUser platUser) {
        return this.dao.findOne(platUser);
    }

    public Page<PlatUser> findPage(Page<PlatUser> page, PlatUser platUser) {
        return super.findPage(page, platUser);
    }

    @Transactional(readOnly = false)
    public void save(PlatUser platUser) {
        super.save(platUser);
    }

    @Transactional(readOnly = false)
    public void updateGoogleNull(String uid) {
        PlatUser platUser = this.get(uid);
        if (platUser != null) {
            platUser.setGoogleAuth("");
            dao.update(platUser);
        }
    }

    @Transactional(readOnly = false)
    public void delete(PlatUser platUser) {
        super.delete(platUser);
    }


    public List<PlatUser> findBySource(String source) {
        return dao.findBySource(source);
    }

    public PlatUser findByMobile(String mobile){
        return dao.findByMobile(mobile);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean giveCoin(PlatUser platUser){
        Mk2PopularizeRegisterConf conf = null;
        //第一步
        //查询注册送币规则
        List<Mk2PopularizeRegisterConf> confList = registerConfService.findEffective();
        if (!CollectionUtils.isEmpty(confList)) {
            conf = confList.get(0);
        }else{
            return false;
        }
        Coin coin = coinService.findByName(conf.getCoinSymbol());
        if(coin!=null){
            conf.setCoinId(coin.getId());
        }else{
            return false;
        }
        //第二步  给实名认证通过用户送币
        UserCoinVolume coinVolume=userCoinVolumeService.getByUserIdAndCoinId(platUser.getId(),coin.getId());
        if(coinVolume==null){
            UserCoinVolume coinVolumeNew=new UserCoinVolume();
            coinVolumeNew.setVolume(new BigDecimal("0"));
            coinVolumeNew.setCoinSymbol(coin.getName());
            coinVolumeNew.setUserId(platUser.getId());
            coinVolumeNew.setCoinId(coin.getId());
            coinVolumeNew.setLockVolume(new BigDecimal("0"));
            coinVolumeNew.setOutLockVolume(new BigDecimal("0"));
            userCoinVolumeService.save(coinVolumeNew);
            //插入交易记录历史 日志
 //           userCoinVolumeService.insertBillHistory(coinVolumeNew);
            //插入交易记录表，系统会根据交易表数据自动给用户资产表中转账
            userCoinVolumeService.insertBill(platUser,conf);
        }else{
          /*  coinVolume.setVolume(coinVolume.getVolume().add(vol));
            userCoinVolumeService.save(coinVolume);*/
            //插入交易记录历史 日志
       //     userCoinVolumeService.insertBillHistory(coinVolume);
            //插入交易记录表，系统会根据交易表数据自动给用户资产表中转账
            userCoinVolumeService.insertBill(platUser,conf);

        }
        //第三步
        //给送币记录表中插入数据
        Mk2PopularizeRegisterCoin registerCoin = new Mk2PopularizeRegisterCoin();
        registerCoin.setMail(platUser.getMail());
        registerCoin.setMobile(platUser.getMobile());
        registerCoin.setRegisterConfId(conf.getId());
        registerCoin.setConfName(conf.getName());
        registerCoin.setUserId(platUser.getId());
        registerCoin.setUserName(platUser.getRealName());
        registerCoin.setVolume(conf.getRegisterVolume());
        registerCoin.setCoinId(conf.getCoinId());
        registerCoin.setCoinSymbol(conf.getCoinSymbol());
        registerCoin.setStatus("2");
        registerCoinService.save(registerCoin);
        //第四步
        //更新规则表，已送出币数量
        conf.setGiveVolume(conf.getGiveVolume()+conf.getRegisterVolume());
        registerConfService.save(conf);
        return true;
    }
    public List<PlatUser> findAllList(PlatUser platUser) {
        return dao.findAllList(platUser);
    }
}