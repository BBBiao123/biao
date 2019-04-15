package com.biao.service;

import com.biao.entity.UserInvited;

public interface UserInvitedService {


    UserInvited findById(String id);

    String save(UserInvited userInvited);

    void updateById(UserInvited userInvited);


}
