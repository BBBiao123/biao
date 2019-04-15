package com.biao.mongo;

import com.biao.BaseTest;
import com.biao.reactive.data.mongo.domain.MatchStream;
import com.biao.reactive.data.mongo.domain.UserTest;
import com.biao.reactive.data.mongo.service.MatchStreamService;
import com.biao.util.DateUtils;
import com.biao.util.JsonUtils;
import com.biao.vo.TradePairVO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * <p>Description: .</p>
 *
 *  ""(Myth)
 * @version 1.0
 * @date 2018/4/8 10:58
 * @since JDK 1.8
 */
public class MongoTemplateTest extends BaseTest {

    @Autowired
    private MatchStreamService matchStreamService;

    @Autowired
    private ReactiveMongoTemplate template;


    @Before
    public void setUp() throws ParseException {
        /*	StepVerifier.create(template.dropCollection(MatchStream.class)).verifyComplete();
         *//*
        Flux<UserTest> insertAll = template
                .insertAll(Flux.just(new UserTest("Walter"), //
                        new UserTest("Skyler"), //
                        new UserTest("Saul"), //
                        new UserTest("Jesse")).collectList());

        insertAll.subscribe();*//*
        //StepVerifier.create(insertAll).expectNextCount(4).verifyComplete();
        
        *//*StepVerifier.create(template.dropCollection(MatchStream.class)).verifyComplete();*//*
        
        
        Flux<MatchStream> insertMatchStream = template
                .insertAll(Flux.just(
                		createMatchStream("USDT","BTC",new BigDecimal(100),DateUtils.parseLocalDateTime("2018-04-22 13:35:34"),0,new BigDecimal(11.23)),
                		createMatchStream("USDT","BTC",new BigDecimal(112),DateUtils.parseLocalDateTime("2018-04-22 13:35:34"),1,new BigDecimal(13.28)),
                		createMatchStream("USDT","BTC",new BigDecimal(162),DateUtils.parseLocalDateTime("2018-04-22 14:35:34"),1,new BigDecimal(57.28)),
                		createMatchStream("USDT","BTC",new BigDecimal(102),DateUtils.parseLocalDateTime("2018-04-22 15:35:34"),0,new BigDecimal(23.28)),
                		createMatchStream("USDT","ETH",new BigDecimal(152),DateUtils.parseLocalDateTime("2018-04-22 16:35:34"),0,new BigDecimal(11.28)),
                		createMatchStream("USDT","ETH",new BigDecimal(122),DateUtils.parseLocalDateTime("2018-04-22 11:35:34"),0,new BigDecimal(10.28))
                		).collectList());
                		
        insertMatchStream.subscribe();*/


    }

    private MatchStream createMatchStream(String coinMain, String coinOther, BigDecimal count, LocalDateTime tradeTime, Integer type, BigDecimal price) {
        MatchStream matchStream = new MatchStream();
        matchStream.setCoinMain(coinMain);
        matchStream.setCoinOther(coinOther);
        matchStream.setVolume(count);
        matchStream.setTradeTime(tradeTime);
        LocalDateTime minutTime = LocalDateTime.of(tradeTime.toLocalDate(), LocalTime.of(tradeTime.getHour(), tradeTime.getMinute(), 0));
        matchStream.setMinuteTime(minutTime);
        matchStream.setPrice(price);
        matchStream.setTotalVolume(count.multiply(price).setScale(8, RoundingMode.HALF_UP));
        matchStream.setType(type);
        System.out.println("json:" + JsonUtils.toJson(matchStream));
        return matchStream;
    }


    @Test
    public void sumTotalVolumeByOrderNo() {
        String orderNo = "196249406503260160";
        final BigDecimal bigDecimal = matchStreamService.sumTotalVolumeByOrderNo(orderNo);
        System.out.println(bigDecimal);
    }


    @Test
    public void listByOrderNo() {
        String orderNo = "196248978885578752";
        final List<MatchStream> list = matchStreamService.listByOrderNo(orderNo);
        list.forEach(System.out::print);


    }


    @Test
    public void testQuery() {
        Query query = new Query();
        query.addCriteria(new Criteria("name").is("Walter"));
        final Flux<UserTest> userTestFlux = template.find(query, UserTest.class);
        final List<UserTest> block = userTestFlux.collectList().block();
        final List<MatchStream> list = matchStreamService
                .findTopByCoinMainAndCoinOther("BTC", "ETH", 100);

        Optional.ofNullable(list).ifPresent(m -> System.out.println("获取成功！"));


    }

    public void test1() throws ParseException {
        matchStreamService.acquireToDayStatTradeExpair("BTC", "LOOM", DateUtils.parseLocalDateTime("2018-06-06 01:16:00"));
    }

    @Test
    public void testTaskQuery() {
        List<TradePairVO> pairVOs = matchStreamService.findStatisticsTradeByCoinMain("USDT");
        pairVOs.forEach(System.out::println);
    }

    @Test
    public void testQueryStream() {
        List<MatchStream> topByCoinMainAndCoinOther = matchStreamService.findTopByCoinMainAndCoinOther("USDT", "BCC", 15);
        System.out.println(topByCoinMainAndCoinOther);
    }

}
