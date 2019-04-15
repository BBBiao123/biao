package com.biao.previous;

import com.biao.enums.TradeEnum;
import com.biao.pojo.TradeDto;
import com.biao.pojo.TradeVo;
import com.biao.previous.main.BuyInFilter;
import com.biao.previous.main.DataChain;
import com.biao.previous.main.EndFilter;
import com.biao.previous.main.PrepositionFilter;
import org.junit.Before;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.math.BigDecimal;

/**
 * Created by p on 2018/4/21.
 */
public class BuyTest {

    TradeDto buyData;

    TradeDto sellData;

    @Before
    public void setUp() {
        TradeVo buyVo = new TradeVo();
        buyVo.setCoinMain("USDT");
        buyVo.setCoinManId("123");
        buyVo.setCoinOther("HT");
        buyVo.setCoinManId("234");
        buyVo.setPrice(new BigDecimal(1.3));
        buyVo.setOrderNo("no.0001");
        buyVo.setUserId("444");
        buyVo.setVolume(new BigDecimal(100));
        BigDecimal blockVolume = buyVo.getPrice().multiply(buyVo.getVolume());
        buyData = TradeDto.transform(buyVo, TradeEnum.BUY).setBlockVolume(blockVolume).setTradeVolume(blockVolume);

        //520
        TradeVo sello = new TradeVo();
        sello.setCoinMain("USDT");
        sello.setCoinManId("123");
        sello.setCoinOther("HT");
        sello.setCoinManId("234");
        sello.setPrice(new BigDecimal(1.1));
        sello.setOrderNo("no.0002");
        sello.setUserId("3333");
        sello.setVolume(new BigDecimal(800));
        blockVolume = sello.getPrice().multiply(sello.getVolume());
        sellData = TradeDto.transform(sello, TradeEnum.SELL).setBlockVolume(blockVolume).setTradeVolume(blockVolume);

    }

    @Test
    public void test002() {


        Config config = new Config();
        String host = "redis://" + "47.244.153.103" + ":" + 6800;
        config.useSingleServer().setAddress(host);
//                .setPassword("foobaredbiaoONE123");
        RedissonClient client = Redisson.create(config);

        DataChain buyChain = new DataChain()
                .addFilter(new PrepositionFilter())
                .addFilter(new BuyInFilter(null, null))
                .addFilter(new EndFilter())
//                .setRedis(new ReactiveRedisTemplate(null,null))
                .setRedisson(client);
        System.out.println(buyChain);
    }


    @Test
    public void test001() {
        //买入者还有多少金额；
        //余usdt
        BigDecimal buyCoin = buyData.getTradeVolume().subtract(sellData.getTradeVolume());
        buyCoin = buyCoin.doubleValue() < 0 ? new BigDecimal(0) : buyCoin;
        System.out.println("buyCoin->" + buyCoin.setScale(8, BigDecimal.ROUND_HALF_DOWN));
        //花多少
        BigDecimal buyFreeCoin = buyData.getTradeVolume().subtract(buyCoin);
        System.out.println("buyFreeCoin->" + buyFreeCoin.setScale(8, BigDecimal.ROUND_HALF_DOWN));
        //得ht；
        BigDecimal buyVolume = buyFreeCoin
                .divide(sellData.getPrice(), 8, BigDecimal.ROUND_HALF_DOWN);
        System.out.println("buyVolume->" + buyVolume);


        System.out.println("#######################################################");
        //卖入者还有多少金额；
        BigDecimal sellCoin = sellData.getVolume().subtract(buyVolume).setScale(8, BigDecimal.ROUND_HALF_DOWN);
        sellCoin = sellCoin.doubleValue() < 0 ? new BigDecimal(0) : sellCoin;
        System.out.println("sellCoin->" + sellCoin);
        BigDecimal sellFreeCoin = buyVolume;
        System.out.println("sellFreeCoin->" + sellFreeCoin.setScale(8, BigDecimal.ROUND_HALF_DOWN));
        BigDecimal sellVolume = buyFreeCoin;
        System.out.println("sellVolume->" + sellVolume.setScale(8, BigDecimal.ROUND_HALF_DOWN));

    }

    @Test
    public void test004() {
        BigDecimal b = new BigDecimal(0.00004);
        BigDecimal f = new BigDecimal(2);

    }
}
