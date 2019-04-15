package com.biao.service.impl;

import com.biao.entity.UserInvited;
import com.biao.mapper.UserInvitedDao;
import com.biao.service.UserInvitedService;
import com.biao.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserInvitedServiceImpl implements UserInvitedService {

    @Autowired
    private UserInvitedDao userInvitedDao;

    @Override
    public String save(UserInvited userInvited) {
        String id = SnowFlake.createSnowFlake().nextIdString();
        userInvited.setCreateDate(LocalDateTime.now());
        userInvited.setId(id);
        userInvitedDao.insert(userInvited);
        return id;
    }

    @Override
    public void updateById(UserInvited userInvited) {

    }

    @Override
    public UserInvited findById(String id) {
        return userInvitedDao.findById(id);
    }


}
