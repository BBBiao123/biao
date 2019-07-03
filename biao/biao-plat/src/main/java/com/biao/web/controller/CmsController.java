package com.biao.web.controller;

import com.biao.entity.CmsArticle;
import com.biao.entity.CmsArticleData;
import com.biao.entity.CmsSite;
import com.biao.enums.CategoryEnum;
import com.biao.pojo.GlobalMessageResponseVo;
import com.biao.pojo.ResponsePage;
import com.biao.service.CmsArticleDataService;
import com.biao.service.CmsArticleService;
import com.biao.service.CmsSiteService;
import com.biao.vo.BannerVO;
import com.biao.vo.CmsArticleVO;
import com.biao.vo.NoticeDetailVO;
import com.biao.vo.RedisCmsArticle;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 资讯 广告 banner
 */
@RestController
@RequestMapping("/biao")
public class CmsController {
    @Autowired
    private CmsArticleService cmsArticleService;
    @Autowired
    private CmsArticleDataService cmsArticleDataService;
    @Autowired
    private CmsSiteService cmsSiteService;

    private static final Object BANNER_LOCK = new Object();
    private static final Object NOTICE_LOCK = new Object();
    private static final Object INTRODUCE_LOCK = new Object();

    private static final int timeOut = 180;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @SuppressWarnings("unchecked")
    @RequestMapping("/cms/banner/list")
    public Mono<GlobalMessageResponseVo> bannerList(String language) {
        CmsArticleVO cmsArticleVO = new CmsArticleVO();
        cmsArticleVO.setCurrentPage(1);
        cmsArticleVO.setShowCount(10);
        cmsArticleVO.setKeyword(CategoryEnum.HEADER_BANNER.getKeyword());
        cmsArticleVO.setModule(CategoryEnum.HEADER_BANNER.getModuel());
        cmsArticleVO.setLanguage(language);
        String bannerRedisKey = getCacheKey(cmsArticleVO);
        List<BannerVO> bannerVos = new ArrayList<>();
        List<RedisCmsArticle> redisDataVOs = (List<RedisCmsArticle>) redisTemplate.opsForValue().get(bannerRedisKey);
        if (redisDataVOs == null) {
            synchronized (BANNER_LOCK) {
                redisDataVOs = (List<RedisCmsArticle>) redisTemplate.opsForValue().get(bannerRedisKey);
                if (redisDataVOs == null) {
                    List<RedisCmsArticle> datas = new ArrayList<>();
                    redisTemplate.opsForValue().set(bannerRedisKey, datas, timeOut, TimeUnit.SECONDS);
                }
            }
            if (redisDataVOs == null) {
                List<RedisCmsArticle> datas = new ArrayList<>();
                ResponsePage<CmsArticle> pageList = cmsArticleService.findPageByModuleAndKeyword(cmsArticleVO);
                pageList.getList().forEach(article -> {
                    RedisCmsArticle bannerVO = new RedisCmsArticle();
                    BeanUtils.copyProperties(article, bannerVO);
                   if(bannerVO.getImage() != null && bannerVO.getImage().length()>0){
                       bannerVO.setImage(bannerVO.getImage().replace("/_thumbs",""));
                   }
                    datas.add(bannerVO);
                });
                redisDataVOs = datas;
                redisTemplate.opsForValue().set(bannerRedisKey, datas, timeOut, TimeUnit.SECONDS);
            }
        }
        redisDataVOs.forEach(article -> {
            BannerVO bannerVO = new BannerVO();
            BeanUtils.copyProperties(article, bannerVO);
            bannerVos.add(bannerVO);
        });
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(bannerVos));
    }

    private String getCacheKey(CmsArticleVO cmsArticleVO) {
        StringBuilder builder = new StringBuilder();
        builder.append(cmsArticleVO.getCurrentPage() + "_" + cmsArticleVO.getShowCount());
        builder.append(":");
        builder.append(cmsArticleVO.getKeyword() + "_" + cmsArticleVO.getModule());
        builder.append(":");
        builder.append(cmsArticleVO.getLanguage());
        return builder.toString();
    }

    @SuppressWarnings("unchecked")
    @RequestMapping("/cms/notice/list")
    public Mono<GlobalMessageResponseVo> noticeList(CmsArticleVO cmsArticleVO) {
        cmsArticleVO.setKeyword(CategoryEnum.HEADER_NOTICE.getKeyword());
        cmsArticleVO.setModule(CategoryEnum.HEADER_NOTICE.getModuel());
        ResponsePage<BannerVO> pageResps = new ResponsePage<>();
        String noticeRedisKey = getCacheKey(cmsArticleVO);
        ResponsePage<RedisCmsArticle> redisPageList = (ResponsePage<RedisCmsArticle>) redisTemplate.opsForValue().get(noticeRedisKey);
        if (redisPageList == null || CollectionUtils.isEmpty(redisPageList.getList())) {
            synchronized (NOTICE_LOCK) {
                redisPageList = (ResponsePage<RedisCmsArticle>) redisTemplate.opsForValue().get(noticeRedisKey);
                if (redisPageList == null) {
                    redisPageList = new ResponsePage<>();
                    ResponsePage<CmsArticle> pageList = cmsArticleService.findPageByModuleAndKeyword(cmsArticleVO);
                    List<RedisCmsArticle> redisDataVOs = new ArrayList<>(pageList.getList().size());
                    pageList.getList().forEach(article -> {
                        RedisCmsArticle bannerVO = new RedisCmsArticle();
                        BeanUtils.copyProperties(article, bannerVO);
                        redisDataVOs.add(bannerVO);
                    });
                    redisPageList.setCount(pageList.getCount());
                    redisPageList.setList(redisDataVOs);
                    redisTemplate.opsForValue().set(noticeRedisKey, redisPageList, timeOut, TimeUnit.SECONDS);
                }
            }
        }
        List<BannerVO> datas = new ArrayList<>(redisPageList.getList().size());
        redisPageList.getList().forEach(article -> {
            BannerVO bannerVO = new BannerVO();
            BeanUtils.copyProperties(article, bannerVO);
            datas.add(bannerVO);
        });
        pageResps.setCount(redisPageList.getCount());
        pageResps.setList(datas);
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(pageResps));
    }

    @SuppressWarnings("unchecked")
    @RequestMapping("/cms/introduce/list")
    public Mono<GlobalMessageResponseVo> introduceList(CmsArticleVO cmsArticleVO) {
        cmsArticleVO.setKeyword(CategoryEnum.HEADER_INTRODUCE.getKeyword());
        cmsArticleVO.setModule(CategoryEnum.HEADER_INTRODUCE.getModuel());
        //设置最多数量50
        cmsArticleVO.setShowCount(50);
        ResponsePage<BannerVO> pageResps = new ResponsePage<>();
        String introduceRedisKey = getCacheKey(cmsArticleVO);

        ResponsePage<RedisCmsArticle> redisPageList = (ResponsePage<RedisCmsArticle>) redisTemplate.opsForValue().get(introduceRedisKey);
        if (redisPageList == null || CollectionUtils.isEmpty(redisPageList.getList())) {
            synchronized (INTRODUCE_LOCK) {
                redisPageList = (ResponsePage<RedisCmsArticle>) redisTemplate.opsForValue().get(introduceRedisKey);
                if (redisPageList == null || CollectionUtils.isEmpty(redisPageList.getList())) {
                    ResponsePage<CmsArticle> pageList = cmsArticleService.findPageByModuleAndKeyword(cmsArticleVO);
                    redisPageList = new ResponsePage<>();
                    List<RedisCmsArticle> redisDataVOs = new ArrayList<>(pageList.getList().size());
                    pageList.getList().forEach(article -> {
                        RedisCmsArticle bannerVO = new RedisCmsArticle();
                        BeanUtils.copyProperties(article, bannerVO);
                        redisDataVOs.add(bannerVO);
                    });
                    redisPageList.setCount(pageList.getCount());
                    redisPageList.setList(redisDataVOs);
                    redisTemplate.opsForValue().set(introduceRedisKey, redisPageList, timeOut, TimeUnit.SECONDS);
                }
            }
        }
        List<BannerVO> listVo = new ArrayList<>(redisPageList.getList().size());
        redisPageList.getList().forEach(article -> {
            BannerVO bannerVO = new BannerVO();
            BeanUtils.copyProperties(article, bannerVO);
            listVo.add(bannerVO);
        });
        pageResps.setCount(redisPageList.getCount());
        pageResps.setList(listVo);
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(pageResps));
    }

    @RequestMapping("/cms/helpCenter/list")
    public Mono<GlobalMessageResponseVo> helpCenterList(CmsArticleVO cmsArticleVO) {
        cmsArticleVO.setKeyword(CategoryEnum.HEADER_HELPCENTER.getKeyword());
        cmsArticleVO.setModule(CategoryEnum.HEADER_HELPCENTER.getModuel());
        ResponsePage<BannerVO> pageResps = new ResponsePage<>();
        ResponsePage<CmsArticle> pageList = cmsArticleService.findPageByModuleAndKeyword(cmsArticleVO);
        List<BannerVO> redisDataVOs = new ArrayList<>(pageList.getList().size());
        pageList.getList().forEach(article -> {
            BannerVO bannerVO = new BannerVO();
            BeanUtils.copyProperties(article, bannerVO);
            redisDataVOs.add(bannerVO);
        });
        pageResps.setCount(pageList.getCount());
        pageResps.setList(redisDataVOs);
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(pageResps));
    }

    @RequestMapping("/cms/appHelpCenter/list")
    public Mono<GlobalMessageResponseVo> appHelpCenterList(CmsArticleVO cmsArticleVO) {
        cmsArticleVO.setKeyword(CategoryEnum.HEADER_APP_HELPCENTER.getKeyword());
        cmsArticleVO.setModule(CategoryEnum.HEADER_APP_HELPCENTER.getModuel());
        ResponsePage<BannerVO> pageResps = new ResponsePage<>();
        ResponsePage<CmsArticle> pageList = cmsArticleService.findPageByModuleAndKeyword(cmsArticleVO);
        List<BannerVO> redisDataVOs = new ArrayList<>(pageList.getList().size());
        pageList.getList().forEach(article -> {
            BannerVO bannerVO = new BannerVO();
            BeanUtils.copyProperties(article, bannerVO);
            redisDataVOs.add(bannerVO);
        });
        pageResps.setCount(pageList.getCount());
        pageResps.setList(redisDataVOs);
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(pageResps));
    }

    @RequestMapping("/cms/column/list")
    public Mono<GlobalMessageResponseVo> columnQuery(CmsArticleVO cmsArticleVO) {
        if (StringUtils.isBlank(cmsArticleVO.getKeyword()) || StringUtils.isBlank(cmsArticleVO.getModule())) {
            return Mono.just(GlobalMessageResponseVo.newErrorInstance("参数错误"));
        }
        ResponsePage<BannerVO> pageResps = new ResponsePage<>();
        ResponsePage<CmsArticle> pageList = cmsArticleService.findPageByModuleAndKeyword(cmsArticleVO);
        List<BannerVO> redisDataVOs = new ArrayList<>(pageList.getList().size());
        pageList.getList().forEach(article -> {
            BannerVO bannerVO = new BannerVO();
            BeanUtils.copyProperties(article, bannerVO);
            redisDataVOs.add(bannerVO);
        });
        pageResps.setCount(pageList.getCount());
        pageResps.setList(redisDataVOs);
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(pageResps));
    }

    @GetMapping("/cms/view/{id}")
    public Mono<GlobalMessageResponseVo> noticeList(@PathVariable("id") String id) {
        CmsArticle cmsArticle = cmsArticleService.findById(id);
        CmsArticleData cmsArticleData = cmsArticleDataService.findById(id);
        NoticeDetailVO noticeDetailVO = new NoticeDetailVO();
        BeanUtils.copyProperties(cmsArticle, noticeDetailVO);
        BeanUtils.copyProperties(cmsArticleData, noticeDetailVO);
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance(noticeDetailVO));
    }

    @GetMapping("/cms/logo")
    public Mono<GlobalMessageResponseVo> webSiteLogo() {
        String cmsSiteRedisKey = "index:cms:site:logo";
        CmsSite cmsSite = (CmsSite) redisTemplate.opsForValue().get(cmsSiteRedisKey);
        if (cmsSite == null) {
            cmsSite = cmsSiteService.findCmsSite();
            redisTemplate.opsForValue().set(cmsSiteRedisKey, cmsSite, 600, TimeUnit.SECONDS);
        }
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance((Object) cmsSite.getLogo()));
    }

    @GetMapping("/cms/link")
    public Mono<GlobalMessageResponseVo> webSiteLink() {
        String cmsSiteRedisKey = "index:cms:site:logo";
        CmsSite cmsSite = (CmsSite) redisTemplate.opsForValue().get(cmsSiteRedisKey);
        if (cmsSite == null) {
            cmsSite = cmsSiteService.findCmsSite();
            redisTemplate.opsForValue().set(cmsSiteRedisKey, cmsSite, 600, TimeUnit.SECONDS);
        }
        return Mono.just(GlobalMessageResponseVo.newSuccessInstance((Object) cmsSite.getDomain()));
    }

    @GetMapping("/cms/service")
    public Mono<GlobalMessageResponseVo> webSiteService(String language) {
        CmsArticleVO cmsArticleVO = new CmsArticleVO();
        cmsArticleVO.setKeyword(CategoryEnum.HEADER_INTRODUCE.getKeyword());
        cmsArticleVO.setModule(CategoryEnum.HEADER_INTRODUCE.getModuel());
        cmsArticleVO.setLanguage(language);
        cmsArticleVO.setDescription("service");
        ResponsePage<CmsArticle> pageList = cmsArticleService.findPageByModuleAndKeyword(cmsArticleVO);
        if (pageList.getList() != null && pageList.getList().size() > 0) {
            CmsArticle cmsArticle = pageList.getList().get(0);
            CmsArticleData cmsArticleData = cmsArticleDataService.findById(cmsArticle.getId());
            return Mono.just(GlobalMessageResponseVo.newSuccessInstance((Object) cmsArticleData.getContent()));
        }
        return Mono.just(GlobalMessageResponseVo.newErrorInstance("请后台配置服务协议"));
    }
}
