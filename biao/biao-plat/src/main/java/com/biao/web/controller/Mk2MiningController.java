package com.biao.web.controller;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.constant.Constants;
import com.biao.entity.SuperBook;
import com.biao.entity.mk2.Mk2PopularizeMiningGiveCoinLog;
import com.biao.entity.mk2.Mk2PopularizeMiningTeamConf;
import com.biao.entity.mk2.Mk2PopularizeMiningTeamSort;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.pojo.RequestQuery;
import com.biao.pojo.ResponsePage;
import com.biao.service.Mk2MiningService;
import com.biao.vo.SuperBookVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * MK2获取用户统计收入信息
 *
 *  ""f
 */
@RestController
@RequestMapping("/biao")
public class Mk2MiningController {

    @Autowired
    private Mk2MiningService mk2MiningService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String MINING_SORT_KEY = "mining:teamsort:%s:%s";// 争霸排名KEY

    private static final String MINING_CONF_KEY = "mining:teamconf";// 争霸排名配置

    private static final int TIME_OUT = 300; // 缓存5分钟

    private static final String SHOW = "1";

    private static final String SUPER_BOOK_KEY = "super:book:%s:%s"; // 超级账本KEY super:book:address:1

    /**
     * 获取百日争霸排名配置
     *
     * @return
     */
    @GetMapping("/mk2/teammining/conf")
    public Mono<GlobalMessageResponseVo> getTeamMiningList() {
        Mk2PopularizeMiningTeamConf conf = (Mk2PopularizeMiningTeamConf) redisTemplate.opsForValue().get(MINING_CONF_KEY);
        if (Objects.isNull(conf)) {
            conf = mk2MiningService.findConf();
            redisTemplate.opsForValue().set(MINING_CONF_KEY, conf, TIME_OUT, TimeUnit.SECONDS);
        }
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(conf));
    }

    /**
     * 获取百日争霸排名
     *
     * @param requestQuery
     * @return
     */
    @GetMapping("/mk2/teammining/list")
    public Mono<GlobalMessageResponseVo> getTeamMiningList(RequestQuery requestQuery) {
        Mk2PopularizeMiningTeamConf conf = mk2MiningService.findConf();
        if (SHOW.equals(conf.getShow())) {
            String cacheKey = String.format(MINING_SORT_KEY, requestQuery.getCurrentPage(), requestQuery.getShowCount());
            ResponsePage<Mk2PopularizeMiningTeamSort> page = (ResponsePage<Mk2PopularizeMiningTeamSort>) redisTemplate.opsForValue().get(cacheKey);
            if (Objects.isNull(page)) {
                page = mk2MiningService.findAll(requestQuery);
                redisTemplate.opsForValue().set(cacheKey, page, TIME_OUT, TimeUnit.SECONDS);
            }
            return Mono.just(GlobalMessageResponseVo.newSuccessInstance(page));
        } else {
            return Mono.just(GlobalMessageResponseVo.newInstance(Constants.SUCCESS_CODE, null, null));
        }

    }

    /**
     * 查询自己的争霸排名
     *
     * @return
     */
    @GetMapping("/mk2/teammining/self")
    public Mono<GlobalMessageResponseVo> getTeamHoldCoinTotal() {
        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();
        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    Mk2PopularizeMiningTeamConf conf = mk2MiningService.findConf();
                    if (SHOW.equals(conf.getShow())) {
                        return GlobalMessageResponseVo.newSuccessInstance(mk2MiningService.findByUserId(e.getId()));
                    } else {
                        return GlobalMessageResponseVo.newInstance(Constants.SUCCESS_CODE, null, null);
                    }
                });
    }

    /**
     * 超级账本查询
     *
     * @param superBookVO
     * @return
     */
    @GetMapping("/super/book/list")
    public Mono<GlobalMessageResponseVo> findSuperBookList(SuperBookVO superBookVO) {
        superBookVO.setShowCount(10); // 默认每页只查10条
        if (StringUtils.isBlank(superBookVO.getAddress())) {
            superBookVO.setAddress("");
        }
        String addressForKey = StringUtils.isNotBlank(superBookVO.getAddress()) ? superBookVO.getAddress() : "total"; //
        String cacheKey = String.format(SUPER_BOOK_KEY, addressForKey, superBookVO.getCurrentPage()); // 超级账本KEY super:book:address:1
        ResponsePage<Mk2PopularizeMiningGiveCoinLog> page = (ResponsePage<Mk2PopularizeMiningGiveCoinLog>) redisTemplate.opsForValue().get(cacheKey);
        if (Objects.isNull(page)) {
            page = mk2MiningService.findSuperBookList(superBookVO);
            redisTemplate.opsForValue().set(cacheKey, page, TIME_OUT, TimeUnit.SECONDS);
        }
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(page));
    }

    /**
     * 获取用户超级账本地址
     *
     * @return
     */
    @GetMapping("/super/book/getAddress/{coinSymbol}")
    public Mono<GlobalMessageResponseVo> getSuperAddress(@PathVariable("coinSymbol") String coinSymbol) {
        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();
        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    SuperBook superBook = mk2MiningService.getSuperBook(e.getId(), coinSymbol);
                    return GlobalMessageResponseVo.newInstance(Constants.SUCCESS_CODE, null, Objects.nonNull(superBook) ? superBook.getAddress() : "");
                });
    }
}
