package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.UserBank;
import com.biao.exception.PlatException;
import com.biao.mapper.UserBankDao;
import com.biao.service.UserBankService;
import com.biao.util.SnowFlake;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserBankServiceImpl implements UserBankService {

    @Autowired
    private UserBankDao userBankDao;

    @Override
    public String save(UserBank userBank) {
        if (StringUtils.isNotEmpty(userBank.getId())) {
            UserBank exsitBank = userBankDao.findById(userBank.getId());

            exsitBank.setBankName(userBank.getBankName());
            exsitBank.setBranchBankName(userBank.getBranchBankName());
            exsitBank.setCardNo(userBank.getCardNo());
            exsitBank.setMobile(userBank.getMobile());
            exsitBank.setRealName(userBank.getRealName());
            long count = userBankDao.updateById(exsitBank);
            if (count == 0) {
                throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
            }
        } else {
            Long existUserBank = userBankDao.findExistByCardNo(userBank.getCardNo());
            if (null != existUserBank) {
                throw new PlatException(Constants.USER_CARD_EXSIT_ERROR, "该卡号已经存在");
            }
            Long existMobile = userBankDao.findExistByMobile(userBank.getMobile());
            if (null != existMobile) {
                throw new PlatException(Constants.USER_CARD_EXSIT_ERROR, "该手机号已经存在");
            }
            String id = SnowFlake.createSnowFlake().nextIdString();
            userBank.setStatus(0);
            userBank.setCreateDate(LocalDateTime.now());
            userBank.setUpdateDate(LocalDateTime.now());
            userBank.setId(id);
            userBankDao.insert(userBank);
            return id;
        }
        return null;
    }

    @Override
    public UserBank findById(String id) {
        return userBankDao.findById(id);
    }


    @Override
    public void updateById(UserBank userBank) {
        userBank.setUpdateDate(LocalDateTime.now());
        long count = userBankDao.updateById(userBank);
    }

    @Override
    public List<UserBank> findAll(String userId) {
        return userBankDao.findAll(userId);
    }

    @Override
    public Optional<UserBank> findByUserId(String userId) {
        return Optional.ofNullable(userBankDao.findByUserId(userId));
    }

    @Override
    public void deleteByUserId(String userId) {
        long count = userBankDao.deleteByUserId(userId);
        if (count == 0) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
    }
}
