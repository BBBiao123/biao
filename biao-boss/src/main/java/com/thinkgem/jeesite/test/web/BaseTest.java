package com.thinkgem.jeesite.test.web;


import com.thinkgem.jeesite.common.security.shiro.cache.JedisCacheManager;
import com.thinkgem.jeesite.modules.plat.constant.RedisKeyConstant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-context*.xml")
public class BaseTest {


    @Test
    public void save() {
        JedisCacheManager cacheManager = new JedisCacheManager();
        cacheManager.setCacheKeyPrefix(RedisKeyConstant.USER_COIN_VOLUME);
        //cacheManager.getCache(userCoinVolume.getUserId()).remove(userCoinVolume.getCoinSymbol());
        cacheManager.getCache("205020213111033856").put("ABCD", new ArrayList<String>());
    }

    @Test
    public void delete() {
        JedisCacheManager cacheManager = new JedisCacheManager();
        cacheManager.setCacheKeyPrefix(RedisKeyConstant.USER_COIN_VOLUME);
        //cacheManager.getCache(userCoinVolume.getUserId()).remove(userCoinVolume.getCoinSymbol());
        cacheManager.getCache("205020213111033856").remove("ABCD");
    }
}
