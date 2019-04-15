/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.plat.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.plat.entity.PlatUser;
import com.thinkgem.jeesite.modules.plat.dao.PlatUserDao;

/**
 * 前台用户Service
 *
 * @author dazi
 * @version 2018-04-26
 */
@Service
@Transactional(readOnly = true)
public class PlatUserService extends CrudService<PlatUserDao, PlatUser> {

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

}