package com.biao.web.controller;

import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.pojo.RequestQuery;
import com.biao.service.RelayPrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 接力撞奖
 */

@RestController
@RequestMapping("/biao")
public class RelayPrizeController {

    @Autowired
    private RelayPrizeService relayPrizeService;

    @PostMapping("/relay/list")
    public Mono<GlobalMessageResponseVo> candidateWinnersList(RequestQuery requestQuery) {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(relayPrizeService.findPage(requestQuery)));
    }

    @PostMapping("/relay/prize/list")
    public Mono<GlobalMessageResponseVo> winnersList(RequestQuery requestQuery) {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(relayPrizeService.findWinnersPage(requestQuery)));
    }

    @PostMapping("/relay/candidate/list")
    public Mono<GlobalMessageResponseVo> candidateList() {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(relayPrizeService.findCandidateList()));
    }

    @GetMapping("/relay/info")
    public Mono<GlobalMessageResponseVo> info() {
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(relayPrizeService.findRelayPrize()));
    }


}
