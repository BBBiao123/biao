/**
 *
 */
package com.biao.reactive.data.mongo.service;

import com.biao.reactive.data.mongo.domain.Card;

import java.util.List;
import java.util.Optional;


public interface CardService {
    /**
     * 保存文件
     *
     * @param Card
     * @return
     */
    Card saveCard(Card card);

    /**
     * 删除文件
     *
     * @param Card
     * @return
     */
    void removeCard(String id);

    /**
     * 根据id获取文件
     *
     * @param Card
     * @return
     */
    Optional<Card> getCardById(String id);

    /**
     * 分页查询，按上传时间降序
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<Card> listCardByPage(int pageIndex, int pageSize);
}
