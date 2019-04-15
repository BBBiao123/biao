package com.biao.service.impl;

import com.biao.entity.SuperBook;
import com.biao.mapper.SuperBookDao;
import com.biao.service.SuperBookService;
import com.biao.util.SnowFlake;
import com.biao.util.SuperBookAddrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class SuperBookServiceImpl implements SuperBookService {

    @Autowired
    private SuperBookDao superBookDao;

    private static final String DEFAULT_SYMBOL = "UES";

    @Override
    @Transactional
    public void initSuperBook(String userId, String symbol) {
        SuperBook superBook = superBookDao.findByUserIdAndSymbol(userId, symbol);
        if (Objects.nonNull(superBook)) {
            return;
        } else {
            superBook = new SuperBook();
            superBook.setId(SnowFlake.createSnowFlake().nextIdString());
            superBook.setAddress(SuperBookAddrUtil.createAddr());
            superBook.setSymbol(symbol);
            superBook.setUserId(userId);
            superBook.setCreateDate(LocalDateTime.now());
            superBookDao.insert(superBook);
        }
    }

    @Override
    @Transactional
    public void initSuperBook(String userId) {
        initSuperBook(userId, DEFAULT_SYMBOL);
    }
}
