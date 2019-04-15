package com.biao.web.controller;

import com.biao.reactive.data.mongo.domain.MatchStream;
import com.biao.reactive.data.mongo.domain.UserTest;
import com.biao.reactive.data.mongo.repository.MatchStreamRepository;
import com.biao.reactive.data.mongo.repository.UserRepository;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *  ""1
 */
@RestController
@RequestMapping("/biao")
public class MongoController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatchStreamRepository matchStreamRepository;

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;


    /**
     * 新增一个 Person
     */
    @PostMapping("/mongo/matchStream")
    public Mono<Void> matchStream() {
        MatchStream matchStream = new MatchStream();
        matchStream.setCoinMain("BTC");
        matchStream.setCoinOther("ETH");
        matchStream.setTradeTime(LocalDateTime.now());
        matchStream.setVolume(BigDecimal.valueOf(7.2));
        matchStream.setPrice(new BigDecimal(10000.01));

        final Mono<Void> then =
                matchStreamRepository.insert(Mono.just(matchStream)).then();

        //then.subscribe();
        matchStream.setVolume(BigDecimal.valueOf(8));

        reactiveMongoTemplate.save(matchStream);

        matchStream.setVolume(BigDecimal.valueOf(9));

        return matchStreamRepository.insert(Mono.just(matchStream)).then();

    }

    /**
     * 新增一个 Person
     */
    @PostMapping("/user")
    public Mono<Void> add(@RequestBody Publisher<UserTest> userTest) {
        return userRepository.insert(userTest).then();
    }


    /**
     * 查询所有 user
     */
    @GetMapping("/user/list")
    public Flux<UserTest> list() {
        return userRepository.findAll();
    }


}
