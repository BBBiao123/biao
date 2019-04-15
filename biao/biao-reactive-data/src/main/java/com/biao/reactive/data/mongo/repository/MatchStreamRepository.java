package com.biao.reactive.data.mongo.repository;


import com.biao.reactive.data.mongo.domain.MatchStream;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *  ""1
 */
public interface MatchStreamRepository extends ReactiveMongoRepository<MatchStream, String> {


    /**
     * 根据主区 和交易区币种 获取
     *
     * @param coinMain  币种主区 就是使用什么币买
     * @param coinOther 被买入的币种
     * @return Flux<MatchStream>
     */
    @Query("{ 'coinMain': ?0, 'coinOther': ?1 }")
    Flux<MatchStream> findByCoinMainAndCoinOther(Mono<String> coinMain, Mono<String> coinOther);

}
