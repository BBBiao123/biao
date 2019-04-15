package com.biao.service;

public interface SuperBookService {

    /**
     * 初始化用户超级账本
     * @param userId
     * @param symbol
     */
    void initSuperBook(String userId, String symbol);

    /**
     * 初始化用户超级账本，默认UES
     * @param userId
     */
    void initSuperBook(String userId);

}
