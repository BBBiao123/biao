package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.WithdrawAddress;
import com.biao.enums.WithdrawAddressStatusEnum;
import com.biao.exception.PlatException;
import com.biao.mapper.WithdrawAddressDao;
import com.biao.service.WithdrawAddressService;
import com.biao.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WithdrawAddressServiceImpl implements WithdrawAddressService {

    @Autowired
    private WithdrawAddressDao withdrawAddressDao;

    @Override
    public String save(WithdrawAddress withdrawAddress) {
        String id = SnowFlake.createSnowFlake().nextIdString();
        withdrawAddress.setStatus(WithdrawAddressStatusEnum.INIT.getCode());
        withdrawAddress.setCreateDate(LocalDateTime.now());
        withdrawAddress.setUpdateDate(LocalDateTime.now());
        withdrawAddress.setId(id);
        withdrawAddressDao.insert(withdrawAddress);
        return id;
    }

    @Override
    public void updateById(WithdrawAddress withdrawAddress) {

    }

    @Override
    public void updateStatusByUserIdAndId(String status, String userId, String id) {
        long count = withdrawAddressDao.updateStatusByUserIdAndId(status, userId, id);
        if (count == 0) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
    }

    @Override
    public List<WithdrawAddress> findAll(String userId) {
        return withdrawAddressDao.findAll(userId, WithdrawAddressStatusEnum.INIT.getCode());
    }

    @Override
    public List<WithdrawAddress> findByUserIdAndCoinId(String userId, String id) {
        return withdrawAddressDao.findByUserIdAndCoinId(userId, WithdrawAddressStatusEnum.INIT.getCode(), id);
    }

    @Override
    public List<WithdrawAddress> findByUserIdAndType(String userId, String type) {
        return withdrawAddressDao.findByUserIdAndType(userId, WithdrawAddressStatusEnum.INIT.getCode(), type);
    }

    @Override
    public WithdrawAddress findById(String id) {
        return withdrawAddressDao.findById(id);
    }


}
