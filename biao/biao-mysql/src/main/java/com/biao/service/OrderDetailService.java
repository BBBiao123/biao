package com.biao.service;

import com.biao.entity.OrderDetail;

public interface OrderDetailService {


    OrderDetail findById(String id);

    String save(OrderDetail orderDetail);

    void updateById(OrderDetail orderDetail);

}
