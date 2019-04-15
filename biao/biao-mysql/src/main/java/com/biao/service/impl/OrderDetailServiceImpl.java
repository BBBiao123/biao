package com.biao.service.impl;

import com.biao.entity.OrderDetail;
import com.biao.mapper.OrderDetailDao;
import com.biao.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Override
    public void updateById(OrderDetail order) {
    }

    @Override
    public OrderDetail findById(String id) {
        return orderDetailDao.findById(id);
    }

    @Override
    public String save(OrderDetail order) {
        return null;
    }


}
