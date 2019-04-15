package com.biao.previous.main;

import com.biao.enums.TradeEnum;
import com.biao.pojo.TradeDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 保证撮合数据一个缓存实现;
 *
 */
public enum TradeCache {
    /**
     * 获取一个可用的实例队列;
     */
    INST;
    /**
     * 卖入的换存队列;
     */
    private Map<String, CacheNode> CACHE = new ConcurrentHashMap<>();


    /**
     * 移除在换存中的数据;
     *
     * @param key key
     */
    public void remove(String key) {
//      CACHE.remove(key);
    }

    /**
     * 修改内上辈子中的数据;
     *
     * @param key key;
     * @param dto dto;
     */
    public void update(String key, TradeDto dto) {
//        add(key,dto);
    }

    /**
     * 获取当前的数据可用的数据;
     *
     * @param key      key;
     * @param supplier 表达式;
     * @return TradeDto
     */
    public String get(String key, Supplier<String> supplier) {
        return supplier.get();
     /*  CacheNode cacheNode = CACHE.get(key);
        if(cacheNode == null){
            TradeDto tradeDto = supplier.get();
            add(key,tradeDto);
            return tradeDto;
        }
        return cacheNode.tradeDto;*/
    }

    /**
     * 增加一条数据;
     *
     * @param key      key;
     * @param supplier 表达式;
     */
    public void add(String key, Supplier<TradeDto> supplier) {
        add(key, supplier.get());
    }

    /**
     * 新增数据;
     *
     * @param key key;
     * @param dto dto;
     */
    public void add(String key, TradeDto dto) {
        if (dto == null) return;
        CacheNode cacheNode = CACHE.get(key);
        if (cacheNode == null) {
            CacheNode node = trf(dto);
            CACHE.put(key, node);
        } else {
            boolean compare = compare(cacheNode, dto);
            if (compare) {
                CacheNode trf = trf(dto);
                CACHE.put(key, trf);
            }
        }
    }

    /**
     * 得到一个cacheNode的值;
     *
     * @param dto 值对象;
     * @return CacheNode
     */
    private CacheNode trf(TradeDto dto) {
        CacheNode node = new CacheNode();
        node.tradeDto = dto;
        node.price = dto.getPrice();
        node.orderNo = dto.getOrderNo();
        return node;
    }

    /**
     * 数据比较;
     *
     * @param before 前置数据;
     * @param after  后置数据;
     * @return true or false;
     */
    private boolean compare(CacheNode before, TradeDto after) {
        if (after.getType() == TradeEnum.BUY) {
            return buyCompare(before.getPrice(), after.getPrice());
        } else {
            return sellCompare(before.getPrice(), after.getPrice());
        }
    }

    /**
     * 关于买的比较;
     *
     * @param before 前置值;
     * @param after  后值;
     * @return true or false;
     */
    private boolean buyCompare(BigDecimal before, BigDecimal after) {
        if (before.compareTo(after) > 0) {
            return false;
        } else return before.compareTo(after) != 0;
    }

    /**
     * 关于卖的比较;
     *
     * @param before 前置值;
     * @param after  后置值;
     * @return true or false;
     */
    private boolean sellCompare(BigDecimal before, BigDecimal after) {
        if (before.compareTo(after) < 0) {
            return false;
        } else return before.compareTo(after) != 0;
    }

    /**
     * 一个临时数据的处理;
     */
    @Data
    public class CacheNode {
        /**
         * 价格;
         */
        private BigDecimal price;
        /**
         * 时间;
         */
        private Long time;
        /**
         * 订单号
         */
        private String orderNo;
        /**
         * Dto;
         */
        private TradeDto tradeDto;
    }
}
