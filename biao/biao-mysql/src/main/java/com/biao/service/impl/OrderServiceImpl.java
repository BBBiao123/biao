package com.biao.service.impl;

import com.biao.constant.Constants;
import com.biao.entity.Order;
import com.biao.enums.OrderEnum;
import com.biao.enums.TradeEnum;
import com.biao.mapper.OrderDao;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.pojo.ResponsePage;
import com.biao.pojo.TradeDto;
import com.biao.query.UserTradeQuery;
import com.biao.service.OrderService;
import com.biao.util.TradeCompute;
import com.biao.vo.OrderVo;
import com.biao.vo.UserTradeVO;
import com.beust.jcommander.internal.Lists;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired(required = false)
    private OrderDao orderDao;

    @Override
    public void updateById(Order order) {
    }

    @Override
    public Order findById(String id) {
        return orderDao.findById(id);
    }

    @Override
    public long save(Order order) {
        return orderDao.insert(order);
    }

    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    /**
     * 用于Redis锁的处理;
     */
    @Autowired
    private RedissonClient rsclient;


    /**
     * 返回订单号信息；
     *
     * @param e e;
     * @return 修改的订单集合；
     */
    @Override
    @Transactional
    public GlobalMessageResponseVo updateResultOrders(Order e) {
        String lockPrix = "order_save";
        RLock lock = rsclient.getLock(lockPrix + e.getId());
        try {
            lock.lock();
            Order order = findById(e.getId());
            BigDecimal newScVolume = TradeCompute.add(e.getSuccessVolume(), order.getSuccessVolume());
            BigDecimal newExfree = TradeCompute.add(e.getExFee(), order.getExFee());
            BigDecimal toCoinVolume = TradeCompute.add(e.getToCoinVolume(), order.getToCoinVolume());
            BigDecimal spentVolume = TradeCompute.add(e.getSpentVolume(), order.getSpentVolume());
            order.setSuccessVolume(newScVolume);
            order.setExFee(newExfree);
            order.setToCoinVolume(toCoinVolume);
            order.setSpentVolume(spentVolume);
            boolean fl = Objects.equals(order.getStatus(), OrderEnum.OrderStatus.ALL_SUCCESS.getCode());
            int status = fl ? order.getStatus() : e.getStatus();
            order.setStatus(status);
            order.setCancelLock(e.getCancelLock());
            long count = orderDao.updateResultOrder(order);
            if (count <= 0) {
                logger.error("订单处理失败:{}", order);
                return GlobalMessageResponseVo.newInstance(-1, e.getId() + "更新失败！", order);
            }
            logger.info("订单处理成功:{}", order);
            return GlobalMessageResponseVo.newInstance(0, e.getId() + "更新成功！", order);
        } catch (Exception ex) {
            logger.error("修改订单失败:{}", e, ex);
            return GlobalMessageResponseVo.newInstance(-1, e.getId() + "更新失败（数据无变化）！" + ex.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long updateStatus(String orderNo, Integer status) {
        try {
            orderDao.updateOrderStatus(orderNo, status);
        } catch (Exception ex) {
            logger.error("更新订单状态失败:{}", ex);
            return 0;
        }
        return 1;
    }

    /**
     * 获取用户当前委托的挂单
     *
     * @param userTradeQuery 前台查询条件
     * @return ResponsePage<UserTradeVO>
     */
    @Override
    public ResponsePage<UserTradeVO> findByPage(UserTradeQuery userTradeQuery) {

        ResponsePage<UserTradeVO> responsePage = new ResponsePage<>();
        Page<UserTradeVO> page = PageHelper.startPage(userTradeQuery.getCurrentPage(), userTradeQuery.getShowCount());
        final List<Order> orderList = orderDao.findListByQuery(userTradeQuery);
        if (CollectionUtils.isEmpty(orderList)) {
            return responsePage;
        }
        responsePage.setCount(page.getTotal());
        responsePage.setList(build(orderList));
        return responsePage;
    }

    /**
     * super;
     *
     * @param userTradeQuery 用户数据；
     * @return
     */
    @Override
    public <T> Mono<T> findOrderProposeList(UserTradeQuery userTradeQuery) {
        if (userTradeQuery == null) {
            return Mono.just((T) GlobalMessageResponseVo.newInstance(Constants.COMMON_ERROR_CODE, "查询错误！"));
        }
        ResponsePage<OrderVo> responsePage = new ResponsePage<>();
        userTradeQuery.setStatus(userTradeQuery.getStatus());
        Page<Order> page = PageHelper.startPage(userTradeQuery.getCurrentPage(), userTradeQuery.getShowCount());
        List<Order> orders = orderDao.findListByQuery(userTradeQuery);
        if (CollectionUtils.isNotEmpty(orders)) {
            List<OrderVo> orderVos = orders.stream().map(order -> {
                OrderVo vo = new OrderVo();
                vo.setExType(order.getExType());
                vo.setPrice(order.getPrice());
                vo.setStatus(order.getStatus());
                vo.setSuccessVolume(order.getSuccessVolume());
                vo.setTime(order.getCreateDate());
                vo.setVolume(order.getAskVolume());
                vo.setOrderNo(order.getId());
                return vo;
            }).collect(Collectors.toList());
            responsePage.setCount(page.getTotal());
            responsePage.setList(orderVos);
            return Mono.just((T) GlobalMessageResponseVo.newSuccessInstance(responsePage));
        }
        return Mono.just((T) GlobalMessageResponseVo.newSuccessInstance(responsePage));
    }

    @Override
    public List<TradeDto> findTop7(Integer type) {
        List<Order> orderList = orderDao.findTop7(type);
        if (CollectionUtils.isNotEmpty(orderList)) {
            return orderList.stream().map(order -> {
                TradeDto tradeDto = new TradeDto();
                tradeDto.setVolume(order.getAskVolume());
                tradeDto.setPrice(order.getPrice());
                tradeDto.setCoinMain(order.getCoinMain());
                tradeDto.setCoinOther(order.getCoinOther());
                tradeDto.setType(TradeEnum.valueOf(order.getExType()));
                return tradeDto;
            }).collect(Collectors.toList());
        }

        return Lists.newArrayList();
    }

    private List<UserTradeVO> build(List<Order> orderList) {
        return orderList.stream().map(this::buildVO).collect(Collectors.toList());
    }


    private UserTradeVO buildVO(Order order) {
        UserTradeVO userTradeVO = new UserTradeVO();
        userTradeVO.setId(order.getId());
        userTradeVO.setAskVolume(order.getAskVolume());
        userTradeVO.setCoinMain(order.getCoinMain());
        userTradeVO.setExFee(order.getExFee());
        userTradeVO.setExType(order.getExType());
        userTradeVO.setPrice(order.getPrice());
        userTradeVO.setSuccessVolume(order.getSuccessVolume());
        userTradeVO.setCoinOther(order.getCoinOther());
        userTradeVO.setToCoinVolume(order.getToCoinVolume());
        userTradeVO.setStatus(order.getStatus());
        userTradeVO.setCreateDate(order.getCreateDate());
        return userTradeVO;
    }
}
