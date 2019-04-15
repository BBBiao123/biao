package com.biao.web.controller;

import com.biao.config.sercurity.RedisSessionUser;
import com.biao.constant.Constants;
import com.biao.entity.mk2.Mk2PopularizeReleaseLog;
import com.biao.entity.mk2.Mk2UserAllLockCoin;
import com.biao.entity.mk2.Mk2UserStatisticsInfo;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.pojo.ResponsePage;
import com.biao.service.Mk2UserStatisticsInfoService;
import com.biao.util.DateUtils;
import com.biao.vo.UserStatisticsInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * MK2获取用户统计收入信息
 *
 *  ""f
 */
@RestController
@RequestMapping("/biao")
public class Mk2UserStatisticsController {

    private static final Logger logger = LoggerFactory.getLogger(Mk2UserStatisticsController.class);

    @Autowired
    private Mk2UserStatisticsInfoService mk2UserStatisticsInfoService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final int TOTAL_TIME_OUT = 1; // 缓存1小时

    private static final int USER_TIME_OUT = 300; // 缓存5分钟

    private static final String MINING_TOTAL_KEY = "mining:total";// 挖矿总量

    private static final String TEAM_HOLD_TOTAL_KEY = "mining:team:hold:%s"; // 团队持币总量userid

    private static final String MINING_USER_TOTAL_KEY = "mining:total:%s"; // 我的挖矿总量userid

    private static final String MINING_USER_HOLD_KEY = "mining:hold:%s:%s:%s"; // 个人持币挖矿流水userid:showcount:page

    private static final String MINING_TEAM_HOLD_KEY = "mining:team:%s:%s:%s"; // 团队挖矿流水userid:showcount:page

    private static final String MINING_NODAL_BONUS_KEY = "mining:bonus:nodal:%s:%s:%s"; // 接点人分红流水userid:showcount:page

    private static final String MINING_AREA_BONUS_KEY = "mining:bonus:area:%s:%s:%s"; // 合伙人分红流水userid:showcount:page

    private static final String MINING_COMMON_BONUS_KEY = "mining:bonus:common:%s:%s:%s"; // 普通用户分红流水userid:showcount:page

    private static final String MINING_USER_LOCK_KEY = "mining:lock:%s"; // 个人锁币userid

    private static final String RELEASE_HISTORY = "release:%s:%s:%s:%s"; // 用户冻结币释放流水userId:relationId:showcount:page

    private static final BigDecimal ONE_HUNDRED_MILLION = new BigDecimal(100000000);

    /**
     * 挖矿总览
     *
     * @return
     */
    @GetMapping("/mk2/total/info")
    public Mono<GlobalMessageResponseVo> getTotalInfo() {
        Mk2UserStatisticsInfo totalInfo = (Mk2UserStatisticsInfo) redisTemplate.opsForValue().get(MINING_TOTAL_KEY);
        if (Objects.isNull(totalInfo)) {
            totalInfo = mk2UserStatisticsInfoService.getTotalInfo();
            redisTemplate.opsForValue().set(MINING_TOTAL_KEY, totalInfo, TOTAL_TIME_OUT, TimeUnit.HOURS);
        }
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(totalInfo));
    }

    /**
     * 团队当前当前持币量，不包括自己持币
     *
     * @return
     */
    @GetMapping("/mk2/total/curteamcoin")
    public Mono<GlobalMessageResponseVo> getTeamHoldCoinTotal() {
        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();
        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    String cacheKey = String.format(TEAM_HOLD_TOTAL_KEY, e.getId());
                    String teamHoldStr = (String) redisTemplate.opsForValue().get(cacheKey);
                    String countDate = "(日期:" + DateUtils.formaterDate(LocalDateTime.now().minusDays(1).toLocalDate()) + ")";
                    if (Objects.isNull(teamHoldStr)) {
                        BigDecimal teamHold = mk2UserStatisticsInfoService.getTeamHoldCoinTotal(e.getId());
                        if (Objects.nonNull(teamHold)) {
                            if (teamHold.compareTo(ONE_HUNDRED_MILLION) == 1) {
                                teamHoldStr = "大于1亿" + countDate;
                            } else {
                                teamHoldStr = teamHold.setScale(2, BigDecimal.ROUND_DOWN) + countDate;
                            }
                        } else {
                            teamHoldStr = "小于5万" + countDate;
                        }
                        redisTemplate.opsForValue().set(cacheKey, teamHoldStr, USER_TIME_OUT, TimeUnit.SECONDS);
                    }
                    return GlobalMessageResponseVo.newInstance(Constants.SUCCESS_CODE, null, teamHoldStr);
                });
    }

    /**
     * 我的挖矿总量
     *
     * @return
     */
    @GetMapping("/mk2/mytotal/info")
    public Mono<GlobalMessageResponseVo> getMyTotalInfo() {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    String userId = e.getId();
                    String cacheKey = String.format(MINING_USER_TOTAL_KEY, e.getId());
                    Mk2UserStatisticsInfo myTotalInfo = (Mk2UserStatisticsInfo) redisTemplate.opsForValue().get(cacheKey);
                    if (Objects.isNull(myTotalInfo)) {
                        myTotalInfo = mk2UserStatisticsInfoService.getMyTotalInfo(userId);
                        redisTemplate.opsForValue().set(cacheKey, myTotalInfo, USER_TIME_OUT, TimeUnit.SECONDS);
                    }
                    return GlobalMessageResponseVo.newSuccessInstance(myTotalInfo);

                });
    }

    /**
     * 持币挖矿流水
     *
     * @param infoVO
     * @return
     */
    @GetMapping("/mk2/history/holdmining")
    public Mono<GlobalMessageResponseVo> getHoldMiningHistory(UserStatisticsInfoVO infoVO) {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    infoVO.setUserId(e.getId());
                    String cacheKey = String.format(MINING_USER_HOLD_KEY, infoVO.getUserId(), infoVO.getShowCount(), infoVO.getCurrentPage());
                    ResponsePage<Mk2UserStatisticsInfo> result = (ResponsePage<Mk2UserStatisticsInfo>) redisTemplate.opsForValue().get(cacheKey);
                    if (Objects.isNull(result)) {
                        result = mk2UserStatisticsInfoService.getHoldMiningHistory(infoVO);
                        redisTemplate.opsForValue().set(cacheKey, result, USER_TIME_OUT, TimeUnit.SECONDS);
                    }
                    return GlobalMessageResponseVo.newSuccessInstance(result);

                });
    }

    /**
     * 团队挖矿流水
     *
     * @param infoVO
     * @return
     */
    @GetMapping("/mk2/history/teammining")
    public Mono<GlobalMessageResponseVo> getTeamMiningHistory(UserStatisticsInfoVO infoVO) {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    infoVO.setUserId(e.getId());

                    String cacheKey = String.format(MINING_TEAM_HOLD_KEY, infoVO.getUserId(), infoVO.getShowCount(), infoVO.getCurrentPage());
                    ResponsePage<Mk2UserStatisticsInfo> result = (ResponsePage<Mk2UserStatisticsInfo>) redisTemplate.opsForValue().get(cacheKey);
                    if (Objects.isNull(result)) {
                        result = mk2UserStatisticsInfoService.getTeamMiningHistory(infoVO);
                        redisTemplate.opsForValue().set(cacheKey, result, USER_TIME_OUT, TimeUnit.SECONDS);
                    }

                    return GlobalMessageResponseVo.newSuccessInstance(result);

                });
    }

    /**
     * 合伙人分红流水
     *
     * @param infoVO
     * @return
     */
    @GetMapping("/mk2/history/areabonus")
    public Mono<GlobalMessageResponseVo> getAreaMemberBonus(UserStatisticsInfoVO infoVO) {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    infoVO.setUserId(e.getId());

                    String cacheKey = String.format(MINING_AREA_BONUS_KEY, infoVO.getUserId(), infoVO.getShowCount(), infoVO.getCurrentPage());
                    ResponsePage<Mk2UserStatisticsInfo> result = (ResponsePage<Mk2UserStatisticsInfo>) redisTemplate.opsForValue().get(cacheKey);
                    if (Objects.isNull(result)) {
                        result = mk2UserStatisticsInfoService.getAreaMemberBonus(infoVO);
                        redisTemplate.opsForValue().set(cacheKey, result, USER_TIME_OUT, TimeUnit.SECONDS);
                    }

                    return GlobalMessageResponseVo.newSuccessInstance(result);
                });
    }

    /**
     * 节点人分红流水
     *
     * @param infoVO
     * @return
     */
    @GetMapping("/mk2/history/nodalbonus")
    public Mono<GlobalMessageResponseVo> getNodalMemberBonus(UserStatisticsInfoVO infoVO) {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    infoVO.setUserId(e.getId());

                    String cacheKey = String.format(MINING_NODAL_BONUS_KEY, infoVO.getUserId(), infoVO.getShowCount(), infoVO.getCurrentPage());
                    ResponsePage<Mk2UserStatisticsInfo> result = (ResponsePage<Mk2UserStatisticsInfo>) redisTemplate.opsForValue().get(cacheKey);
                    if (Objects.isNull(result)) {
                        result = mk2UserStatisticsInfoService.getNodalMemberBonus(infoVO);
                        redisTemplate.opsForValue().set(cacheKey, result, USER_TIME_OUT, TimeUnit.SECONDS);
                    }

                    return GlobalMessageResponseVo.newSuccessInstance(result);
                });
    }

    /**
     * 普通用户分红流水
     *
     * @param infoVO
     * @return
     */
    @GetMapping("/mk2/history/commonbonus")
    public Mono<GlobalMessageResponseVo> getCommonMemberBonus(UserStatisticsInfoVO infoVO) {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    infoVO.setUserId(e.getId());

                    String cacheKey = String.format(MINING_COMMON_BONUS_KEY, infoVO.getUserId(), infoVO.getShowCount(), infoVO.getCurrentPage());
                    ResponsePage<Mk2UserStatisticsInfo> result = (ResponsePage<Mk2UserStatisticsInfo>) redisTemplate.opsForValue().get(cacheKey);
                    if (Objects.isNull(result)) {
                        result = mk2UserStatisticsInfoService.getCommonMemberBonus(infoVO);
                        redisTemplate.opsForValue().set(cacheKey, result, USER_TIME_OUT, TimeUnit.SECONDS);
                    }

                    return GlobalMessageResponseVo.newSuccessInstance(result);
                });
    }

    /**
     * 用户分红流水
     *
     * @param infoVO
     * @return
     */
    @GetMapping("/mk2/history/release")
    public Mono<GlobalMessageResponseVo> getMemberRelease(UserStatisticsInfoVO infoVO) {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    infoVO.setUserId(e.getId());

                    String cacheKey = String.format(RELEASE_HISTORY, infoVO.getUserId(), infoVO.getRelationId(), infoVO.getShowCount(), infoVO.getCurrentPage());
                    ResponsePage<Mk2PopularizeReleaseLog> result = (ResponsePage<Mk2PopularizeReleaseLog>) redisTemplate.opsForValue().get(cacheKey);
                    if (Objects.isNull(result)) {
                        result = mk2UserStatisticsInfoService.getMemberRelease(infoVO);
                        redisTemplate.opsForValue().set(cacheKey, result, USER_TIME_OUT, TimeUnit.SECONDS);
                    }

                    return GlobalMessageResponseVo.newSuccessInstance(result);
                });
    }
    /**
     * 我的冻结
     *
     * @return
     */
    @GetMapping("/mk2/user/alllock")
    public Mono<GlobalMessageResponseVo> getUserAllLockVolume() {

        Mono<SecurityContext> context
                = ReactiveSecurityContextHolder.getContext();

        return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                .map(s -> s.getAuthentication().getPrincipal())
                .cast(RedisSessionUser.class)
                .map(e -> {
                    String cacheKey = String.format(MINING_USER_LOCK_KEY, e.getId());
                    List<Mk2UserAllLockCoin> myLocks = (List<Mk2UserAllLockCoin>) redisTemplate.opsForValue().get(cacheKey);
                    if (Objects.isNull(myLocks)) {
                        myLocks = mk2UserStatisticsInfoService.getUserAllLockVolume(e.getId());
                        redisTemplate.opsForValue().set(cacheKey, myLocks, USER_TIME_OUT, TimeUnit.SECONDS);
                    }
                    return GlobalMessageResponseVo.newSuccessInstance(myLocks);
                });
    }

}
