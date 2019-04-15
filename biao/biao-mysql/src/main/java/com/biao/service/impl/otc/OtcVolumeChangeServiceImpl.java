package com.biao.service.impl.otc;

import com.biao.constant.Constants;
import com.biao.entity.Coin;
import com.biao.entity.OfflineCoinVolume;
import com.biao.entity.PlatUser;
import com.biao.entity.otc.OtcAccountSecret;
import com.biao.entity.otc.OtcVolumeChangeRequest;
import com.biao.entity.otc.OtcVolumeChangeRequestLog;
import com.biao.enums.OtcVolumeChangeTypeEnum;
import com.biao.enums.TradeEnum;
import com.biao.enums.UserTagEnum;
import com.biao.exception.PlatException;
import com.biao.mapper.OfflineCoinVolumeDao;
import com.biao.mapper.PlatUserDao;
import com.biao.mapper.otc.OtcAccountSecretDao;
import com.biao.mapper.otc.OtcVolumeChangeRequestDao;
import com.biao.mapper.otc.OtcVolumeChangeRequestLogDao;
import com.biao.service.CoinService;
import com.biao.service.OfflineCoinVolumeLogService;
import com.biao.service.OfflineCoinVolumeService;
import com.biao.service.otc.OtcAccountSecretService;
import com.biao.service.otc.OtcVolumeChangeService;
import com.biao.util.SnowFlake;
import com.biao.util.otc.MD5Util;
import com.biao.vo.otc.OtcVolumeChangeQueryVO;
import com.biao.vo.otc.OtcVolumeChangeRequestResultVO;
import com.biao.vo.otc.OtcVolumeChangeRequestVO;
import com.biao.vo.otc.OtcVolumeChangeResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class OtcVolumeChangeServiceImpl implements OtcVolumeChangeService {

    private Logger logger = LoggerFactory.getLogger(OtcVolumeChangeServiceImpl.class);

    private final static String OTC_USER_REQUEST_URI = "/otc/volume/change";

    private final static String OTC_ADMIN_REQUEST_URI = "/otc/volume/admin/change";

    @Autowired
    private OtcVolumeChangeRequestDao otcVolumeChangeRequestDao;

    @Autowired
    private OtcVolumeChangeRequestLogDao otcVolumeChangeRequestLogDao;

    @Autowired
    private OfflineCoinVolumeDao offlineCoinVolumeDao;

    @Autowired
    private OfflineCoinVolumeLogService offlineCoinVolumeLogService;

    @Autowired
    private OfflineCoinVolumeService offlineCoinVolumeService;

    @Autowired
    private OtcVolumeChangeServiceImpl otcVolumeChangeService;

    @Autowired
    private OtcAccountSecretDao otcAccountSecretDao;

    @Autowired
    private OtcAccountSecretService otcAccountSecretService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private PlatUserDao platUserDao;

    @Override
    public OtcVolumeChangeRequestResultVO change(OtcVolumeChangeRequestVO otcVolumeChangeRequestVO) {

        OtcVolumeChangeRequestLog otcVolumeChangeRequestLog = null;
        OtcVolumeChangeRequestResultVO otcVolumeChangeRequestResultVO = null;
        try{
            //create request log
            otcVolumeChangeRequestLog = this.createRequestLog(otcVolumeChangeRequestVO);
            otcVolumeChangeRequestVO.setRequestLogId(otcVolumeChangeRequestLog.getId());
            //check Encrypted
            this.checkSecretKey(otcVolumeChangeRequestVO.getKey(), this.createParamMap(otcVolumeChangeRequestVO));
            //check
            this.checkRequest(otcVolumeChangeRequestVO);
            //检查登录用户
            this.checkLoginUser(otcVolumeChangeRequestVO);
            //资产变更核心
            otcVolumeChangeRequestResultVO = otcVolumeChangeService.changeCore(otcVolumeChangeRequestLog);
        }catch (PlatException pe){
            logger.error("资产变更失败{}", otcVolumeChangeRequestVO.getBatchNo());
            logger.error("资产变更失败", pe);
            String msg = Optional.ofNullable(pe.getMsg()).orElse("");
            msg = msg.length() > 450 ? msg.substring(0, 450) : msg;
            this.createFailRequest(otcVolumeChangeRequestVO, msg);

            otcVolumeChangeRequestResultVO = new OtcVolumeChangeRequestResultVO();
            otcVolumeChangeRequestResultVO.setBatchNo(otcVolumeChangeRequestLog.getBatchNo());
            otcVolumeChangeRequestResultVO.setPublishSource(otcVolumeChangeRequestLog.getPublishSource());
            otcVolumeChangeRequestResultVO.setStatus("0");
            otcVolumeChangeRequestResultVO.setResult(msg);
            otcVolumeChangeRequestResultVO.setCreateDate(LocalDateTime.now());
//            throw pe;
        }catch (Exception e){
            logger.error("资产变更失败{}", otcVolumeChangeRequestVO.getBatchNo());
            logger.error("资产变更失败", e);
            String msg = Optional.ofNullable(e.toString()).orElse("");
            msg = msg.length() > 450 ? msg.substring(0, 450) : msg;
            this.createFailRequest(otcVolumeChangeRequestVO, msg);

            otcVolumeChangeRequestResultVO = new OtcVolumeChangeRequestResultVO();
            otcVolumeChangeRequestResultVO.setBatchNo(otcVolumeChangeRequestLog.getBatchNo());
            otcVolumeChangeRequestResultVO.setPublishSource(otcVolumeChangeRequestLog.getPublishSource());
            otcVolumeChangeRequestResultVO.setStatus("0");
            otcVolumeChangeRequestResultVO.setResult(msg);
            otcVolumeChangeRequestResultVO.setCreateDate(LocalDateTime.now());
//            throw e;
        }

        //check Encrypted
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("batchNo",otcVolumeChangeRequestResultVO.getBatchNo());
        paramMap.put("publishSource",otcVolumeChangeRequestResultVO.getPublishSource());
        paramMap.put("status", otcVolumeChangeRequestResultVO.getStatus());
        paramMap.put("result", otcVolumeChangeRequestResultVO.getResult());
//        paramMap.put("createDate", String.valueOf(Timestamp.valueOf(otcVolumeChangeRequestResultVO.getCreateDate()).getTime()));

        otcVolumeChangeRequestResultVO.setKey(this.getSecretKey(otcVolumeChangeRequestResultVO.getPublishSource(), paramMap));
        return otcVolumeChangeRequestResultVO;
    }

    private OtcVolumeChangeRequestLog createRequestLog(OtcVolumeChangeRequestVO otcVolumeChangeRequestVO){
        OtcVolumeChangeRequestLog otcVolumeChangeRequestLog = new OtcVolumeChangeRequestLog();
        BeanUtils.copyProperties(otcVolumeChangeRequestVO, otcVolumeChangeRequestLog);
        otcVolumeChangeRequestLog.setId(SnowFlake.createSnowFlake().nextIdString());
        otcVolumeChangeRequestLog.setCreateDate(LocalDateTime.now());
        otcVolumeChangeRequestLog.setUpdateDate(LocalDateTime.now());
        otcVolumeChangeRequestLogDao.insert(otcVolumeChangeRequestLog);
        return otcVolumeChangeRequestLog;
    }

    private void checkRequest(OtcVolumeChangeRequestVO otcVolumeChangeRequestVO){

        if(Objects.isNull(otcVolumeChangeRequestVO)){
            logger.error("数据异常");
            throw new PlatException(Constants.PARAM_ERROR, "数据异常");
        }

        if(StringUtils.isEmpty(otcVolumeChangeRequestVO.getAdType())){
            logger.error("广告类型不能为空");
            throw new PlatException(Constants.PARAM_ERROR, "广告类型不能为空");
        }

        if(!String.valueOf(TradeEnum.BUY.ordinal()).equals(otcVolumeChangeRequestVO.getAdType()) && !String.valueOf(TradeEnum.SELL.ordinal()).equals(otcVolumeChangeRequestVO.getAdType())){
            logger.error("广告类型数据异常");
            throw new PlatException(Constants.PARAM_ERROR, "广告类型数据异常");
        }

        if(StringUtils.isEmpty(otcVolumeChangeRequestVO.getCoinId()) || StringUtils.isEmpty(otcVolumeChangeRequestVO.getSymbol())){
            logger.error("币种不能为空");
            throw new PlatException(Constants.PARAM_ERROR, "币种不能为空");
        }

        Coin coin = coinService.findById(otcVolumeChangeRequestVO.getCoinId());
        if (Objects.isNull(coin) || !coin.getName().equals(otcVolumeChangeRequestVO.getSymbol())) {
            logger.error("币种ID错误或与币种符号不匹配");
            throw new PlatException(Constants.PARAM_ERROR, "币种ID错误或与币种符号不匹配");
        }

        if(StringUtils.isEmpty(otcVolumeChangeRequestVO.getSellUserId())){
            logger.error("卖家不能为空");
            throw new PlatException(Constants.PARAM_ERROR, "卖家不能为空");
        }

        if(StringUtils.isEmpty(otcVolumeChangeRequestVO.getType())){
            logger.error("操作类型不能为空");
            throw new PlatException(Constants.PARAM_ERROR, "操作类型不能为空");
        }

        if(StringUtils.isEmpty(otcVolumeChangeRequestVO.getOrderId())){
            logger.error("广告不能为空");
            throw new PlatException(Constants.PARAM_ERROR, "广告不能为空");
        }

        if(Objects.isNull(otcVolumeChangeRequestVO.getVolume()) || otcVolumeChangeRequestVO.getVolume().compareTo(BigDecimal.ZERO) <= 0){
            logger.error("资产数量不能为空");
            throw new PlatException(Constants.PARAM_ERROR, "资产数量不能为空");
        }

    }

    private void checkLoginUser(OtcVolumeChangeRequestVO otcVolumeChangeRequestVO){

        logger.info(String.format("批次号[%s]的请求路径[%s]", otcVolumeChangeRequestVO.getBatchNo(), otcVolumeChangeRequestVO.getRequestUri()));

        if(OTC_ADMIN_REQUEST_URI.equals(otcVolumeChangeRequestVO.getRequestUri())){
             if(!OtcVolumeChangeTypeEnum.APPEAL_TO_BUYER.getCode().equals(otcVolumeChangeRequestVO.getType()) &&
                     !OtcVolumeChangeTypeEnum.APPEAL_TO_SELLER.getCode().equals(otcVolumeChangeRequestVO.getType()) &&
                            !OtcVolumeChangeTypeEnum.APPEAL_TO_SELLER_.getCode().equals(otcVolumeChangeRequestVO.getType()) &&
                                !OtcVolumeChangeTypeEnum.CANCEL_SELL_ORDER.getCode().equals(otcVolumeChangeRequestVO.getType())){
                 logger.error("免登录只能用于申诉业务或卖出广告撤销");
                 throw new PlatException(Constants.PARAM_ERROR, "免登录只能用于申诉业务或卖出广告撤销");
             }
//            otcAccountSecretService.checkIsMasterAccount(otcVolumeChangeRequestVO.getLoginUserId()); // 校验是否OTC总账户登录
        }else if(OTC_USER_REQUEST_URI.equals(otcVolumeChangeRequestVO.getRequestUri())){
            if(OtcVolumeChangeTypeEnum.PUBLISH_SELL_ORDER.getCode().equals(otcVolumeChangeRequestVO.getType())){
                if(!otcVolumeChangeRequestVO.getLoginUserId().equals(otcVolumeChangeRequestVO.getSellUserId())){
                    logger.error("发布卖出广告,卖家与登录用户不匹配");
                    throw new PlatException(Constants.PARAM_ERROR, "发布广告,卖家与登录用户不匹配");
                }
            }else if(OtcVolumeChangeTypeEnum.CANCEL_SELL_ORDER.getCode().equals(otcVolumeChangeRequestVO.getType())){
                if(!otcVolumeChangeRequestVO.getLoginUserId().equals(otcVolumeChangeRequestVO.getSellUserId())){
                    logger.error("撤销卖出广告,卖家与登录用户不匹配");
                    throw new PlatException(Constants.PARAM_ERROR, "发布广告,卖家与登录用户不匹配");
                }
            }else if (OtcVolumeChangeTypeEnum.CREATE_SUB_ORDER.getCode().equals(otcVolumeChangeRequestVO.getType())){
                if(String.valueOf(TradeEnum.BUY.ordinal()).equals(otcVolumeChangeRequestVO.getAdType())){
                    if(!otcVolumeChangeRequestVO.getLoginUserId().equals(otcVolumeChangeRequestVO.getSellUserId())){
                        logger.error("卖家创建订单,卖家与登录用户不匹配");
                        throw new PlatException(Constants.PARAM_ERROR, "发布广告,卖家与登录用户不匹配");
                    }
                }else if(String.valueOf(TradeEnum.SELL.ordinal()).equals(otcVolumeChangeRequestVO.getAdType())){
                    if(StringUtils.isEmpty(otcVolumeChangeRequestVO.getBuyUserId())|| !otcVolumeChangeRequestVO.getLoginUserId().equals(otcVolumeChangeRequestVO.getBuyUserId())){
                        logger.error("买家创建订单,买家与登录用户不匹配");
                        throw new PlatException(Constants.PARAM_ERROR, "买家创建订单,买家与登录用户不匹配");
                    }
                }else{
                    throw new PlatException(Constants.PARAM_ERROR, "广告类型参数非法");
                }
            }else if(OtcVolumeChangeTypeEnum.CANCEL_SUB_ORDER.getCode().equals(otcVolumeChangeRequestVO.getType())){
                if(StringUtils.isEmpty(otcVolumeChangeRequestVO.getBuyUserId())|| !otcVolumeChangeRequestVO.getLoginUserId().equals(otcVolumeChangeRequestVO.getBuyUserId())){
                    logger.error("取消订单,买家与登录用户不匹配");
                    throw new PlatException(Constants.PARAM_ERROR, "取消订单,买家与登录用户不匹配");
                }
            }else if(OtcVolumeChangeTypeEnum.CANCEL_SUB_ORDER_.getCode().equals(otcVolumeChangeRequestVO.getType())){
                if(StringUtils.isEmpty(otcVolumeChangeRequestVO.getBuyUserId())|| !otcVolumeChangeRequestVO.getLoginUserId().equals(otcVolumeChangeRequestVO.getBuyUserId())){
                    logger.error("取消订单,买家与登录用户不匹配");
                    throw new PlatException(Constants.PARAM_ERROR, "取消订单,买家与登录用户不匹配");
                }
            }else if(OtcVolumeChangeTypeEnum.CONFIRM_IN.getCode().equals(otcVolumeChangeRequestVO.getType())){
                if(!otcVolumeChangeRequestVO.getLoginUserId().equals(otcVolumeChangeRequestVO.getSellUserId())){
                    logger.error("确认收款,卖家与登录用户不匹配");
                    throw new PlatException(Constants.PARAM_ERROR, "确认收款,卖家与登录用户不匹配");
                }
            }else{
                logger.error("操作类型非法");
                throw new PlatException(Constants.PARAM_ERROR, "操作类型非法");
            }
        }


    }

    private Map<String,String> createParamMap(OtcVolumeChangeRequestVO otcVolumeChangeRequestVO){

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("type",otcVolumeChangeRequestVO.getType());
        paramMap.put("batchNo",otcVolumeChangeRequestVO.getBatchNo());
        paramMap.put("coinId",otcVolumeChangeRequestVO.getCoinId());
        paramMap.put("symbol",otcVolumeChangeRequestVO.getSymbol());
        paramMap.put("sellUserId",otcVolumeChangeRequestVO.getSellUserId());
        paramMap.put("buyUserId",otcVolumeChangeRequestVO.getBuyUserId());
        paramMap.put("orderId",otcVolumeChangeRequestVO.getOrderId());
        paramMap.put("subOrderId",otcVolumeChangeRequestVO.getSubOrderId());
        paramMap.put("adType",otcVolumeChangeRequestVO.getAdType());
        paramMap.put("volume",String.valueOf(otcVolumeChangeRequestVO.getVolume()));
        paramMap.put("feeVolume",Objects.isNull(otcVolumeChangeRequestVO.getFeeVolume()) ? "" : String.valueOf(otcVolumeChangeRequestVO.getFeeVolume()));
        paramMap.put("publishSource",otcVolumeChangeRequestVO.getPublishSource());

        return paramMap;
    }

    private void checkSecretKey(String key, Map<String, String> paramMap) {
        OtcAccountSecret accountSecret = otcAccountSecretDao.findByPublishSource("otc");
        if (Objects.isNull(accountSecret)) {
            throw new PlatException(Constants.PARAM_ERROR, "OTC信息未设置");
        }

        String mykey = MD5Util.getSign(paramMap, accountSecret.getSecret());
        if (org.apache.commons.lang3.StringUtils.isBlank(key) || org.apache.commons.lang3.StringUtils.isBlank(mykey) || !key.equals(mykey)) {
            throw new PlatException(Constants.PARAM_ERROR, "秘钥加密错误");
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OtcVolumeChangeRequestResultVO changeCore(OtcVolumeChangeRequestLog otcVolumeChangeRequestLog){

        OtcVolumeChangeRequestResultVO otcVolumeChangeRequestResultVO = new OtcVolumeChangeRequestResultVO();
        otcVolumeChangeRequestResultVO.setBatchNo(otcVolumeChangeRequestLog.getBatchNo());
        otcVolumeChangeRequestResultVO.setPublishSource(otcVolumeChangeRequestLog.getPublishSource());

        String result = "";
        if(OtcVolumeChangeTypeEnum.PUBLISH_SELL_ORDER.getCode().equals(otcVolumeChangeRequestLog.getType())){
            result = this.publishOrder(otcVolumeChangeRequestLog);
        }else if(OtcVolumeChangeTypeEnum.CANCEL_SELL_ORDER.getCode().equals(otcVolumeChangeRequestLog.getType())){
            result = this.cancelOrder(otcVolumeChangeRequestLog);
        }else if (OtcVolumeChangeTypeEnum.CREATE_SUB_ORDER.getCode().equals(otcVolumeChangeRequestLog.getType())){
            result = this.createSubOrder(otcVolumeChangeRequestLog);
        }else if(OtcVolumeChangeTypeEnum.CANCEL_SUB_ORDER.getCode().equals(otcVolumeChangeRequestLog.getType())){
            result = this.cancelSubOrder(otcVolumeChangeRequestLog);
        }else if(OtcVolumeChangeTypeEnum.CANCEL_SUB_ORDER_.getCode().equals(otcVolumeChangeRequestLog.getType())){
            result = this.cancelSubOrder(otcVolumeChangeRequestLog);
        }else if(OtcVolumeChangeTypeEnum.CONFIRM_IN.getCode().equals(otcVolumeChangeRequestLog.getType())){
            result = this.confirmIn(otcVolumeChangeRequestLog);
        }else if(OtcVolumeChangeTypeEnum.APPEAL_TO_BUYER.getCode().equals(otcVolumeChangeRequestLog.getType())){
            result = this.appealToBuyer(otcVolumeChangeRequestLog);
        }else if(OtcVolumeChangeTypeEnum.APPEAL_TO_SELLER.getCode().equals(otcVolumeChangeRequestLog.getType())){
            result = this.appealToSeller(otcVolumeChangeRequestLog);
        }else if(OtcVolumeChangeTypeEnum.APPEAL_TO_SELLER_.getCode().equals(otcVolumeChangeRequestLog.getType())){
            result = this.appealToSeller(otcVolumeChangeRequestLog);
        }else{
            result = "操作类型非法";
            this.createFailCoreRequest(otcVolumeChangeRequestLog, result);
            otcVolumeChangeRequestResultVO.setStatus("0");
            otcVolumeChangeRequestResultVO.setResult(result);
            otcVolumeChangeRequestResultVO.setCreateDate(LocalDateTime.now());
            return otcVolumeChangeRequestResultVO;
        }

        this.createSuccessRequest(otcVolumeChangeRequestLog, result);
        otcVolumeChangeRequestResultVO.setStatus("1");
        otcVolumeChangeRequestResultVO.setResult(result);
        otcVolumeChangeRequestResultVO.setCreateDate(LocalDateTime.now());
        return otcVolumeChangeRequestResultVO;

    }

    private String publishOrder(OtcVolumeChangeRequestLog otcVolumeChangeRequestLog){

        if(!String.valueOf(TradeEnum.SELL.ordinal()).equals(otcVolumeChangeRequestLog.getAdType())){
            throw new PlatException(Constants.PARAM_ERROR, "广告类型必须为卖出");
        }

        //检查与获取C2C资产
        OfflineCoinVolume offlineCoinVolume = this.checkAndGetOfflineVolume(otcVolumeChangeRequestLog);

        // C2C资产卖币方计算开始
        BigDecimal volume = null;// c2c可用资产
        BigDecimal advertVolume = null; // c2c广告冻结资产
        BigDecimal bailVolume = offlineCoinVolume.getBailVolume(); // c2c保证金
        Timestamp updateDate = Timestamp.valueOf(offlineCoinVolume.getUpdateDate());

        volume = offlineCoinVolume.getVolume().subtract(otcVolumeChangeRequestLog.getVolume());// c2c可用资产 = c2c可用资产 - 广告数量
        advertVolume = offlineCoinVolume.getOtcAdvertVolume().add(otcVolumeChangeRequestLog.getVolume()); // otc广告冻结资产 = otc广告冻结资产 + 广告数量

        if (Objects.isNull(volume)) {
            throw new PlatException(Constants.PARAM_ERROR, "广告状态错误");
        }

        if (volume.compareTo(BigDecimal.ZERO) == -1 || advertVolume.compareTo(BigDecimal.ZERO) == -1) { // 如果volume为空，表示广告参数等有误
            throw new PlatException(Constants.PARAM_ERROR, "资金不足");
        }

        if (Objects.isNull(bailVolume) || bailVolume.compareTo(BigDecimal.ZERO) < 1 ) { // 保证金不足
            throw new PlatException(Constants.PARAM_ERROR, "手续费预备金不足");
        }

        long count = offlineCoinVolumeDao.updateVolumeAndOtcAdvertVolume(otcVolumeChangeRequestLog.getSellUserId(), otcVolumeChangeRequestLog.getCoinId(), volume, advertVolume, updateDate, offlineCoinVolume.getVersion());
        if (count != 1) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }

        offlineCoinVolumeLogService.saveLog(otcVolumeChangeRequestLog.getSellUserId(), otcVolumeChangeRequestLog.getCoinId(), otcVolumeChangeRequestLog.getBatchNo());// C2C资产流水
        return String.format("发布广告资产变更");
    }


    private String cancelOrder(OtcVolumeChangeRequestLog otcVolumeChangeRequestLog){

        if(!String.valueOf(TradeEnum.SELL.ordinal()).equals(otcVolumeChangeRequestLog.getAdType())){
            throw new PlatException(Constants.PARAM_ERROR, "广告类型必须为卖出");
        }

        //检查与获取卖方C2C资产
        OfflineCoinVolume offlineCoinVolume = this.checkAndGetOfflineVolume(otcVolumeChangeRequestLog);

        // C2C资产卖币方计算开始
        BigDecimal volume = null;// c2c可用资产
        BigDecimal advertVolume = null; // c2c广告冻结资产
        Timestamp updateDate = Timestamp.valueOf(offlineCoinVolume.getUpdateDate());

        BigDecimal exVolume = otcVolumeChangeRequestLog.getVolume();// 广告剩余数量
        if (exVolume.compareTo(BigDecimal.ZERO) == -1) { //广告剩余数量 不为负数
            throw new PlatException(Constants.PARAM_ERROR);
        }
        volume = offlineCoinVolume.getVolume().add(exVolume);// c2c可用资产 = c2c可用资产 + 广告数量剩余
        advertVolume = offlineCoinVolume.getOtcAdvertVolume().subtract(exVolume); // c2c广告冻结资产 = c2c广告冻结资产 - 广告数量剩余

        if (Objects.isNull(volume)) {
            throw new PlatException(Constants.PARAM_ERROR, "广告状态错误");
        }

        if (volume.compareTo(BigDecimal.ZERO) == -1 || advertVolume.compareTo(BigDecimal.ZERO) == -1) { // 如果volume为空，表示广告参数等有误
            throw new PlatException(Constants.PARAM_ERROR, "资金不足");
        }

        long count = offlineCoinVolumeDao.updateVolumeAndOtcAdvertVolume(otcVolumeChangeRequestLog.getSellUserId(), otcVolumeChangeRequestLog.getCoinId(), volume, advertVolume, updateDate, offlineCoinVolume.getVersion());
        if (count != 1) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }

        offlineCoinVolumeLogService.saveLog(otcVolumeChangeRequestLog.getSellUserId(), otcVolumeChangeRequestLog.getCoinId(), otcVolumeChangeRequestLog.getBatchNo());// C2C资产流水
        return String.format("撤销广告资产变更");
    }

    private OfflineCoinVolume checkAndGetOfflineVolume(OtcVolumeChangeRequestLog otcVolumeChangeRequestLog){
        // check该C2C币资产是否存在
        OfflineCoinVolume offlineCoinVolume = offlineCoinVolumeDao.findByUserIdAndCoinId(otcVolumeChangeRequestLog.getSellUserId(), otcVolumeChangeRequestLog.getCoinId());
        if (Objects.isNull(offlineCoinVolume)) {
            throw new PlatException(Constants.OFFLINE_VOLUME_ERROR, "当前用户不存在该币种资产");
        }

        //C2C资产变更：可用资产要减，冻结资产要加
        offlineCoinVolumeLogService.saveLog(offlineCoinVolume.getUserId(), offlineCoinVolume.getCoinId(), otcVolumeChangeRequestLog.getBatchNo());// C2C资产流水

        return offlineCoinVolume;
    }

    //创建买入订单
    private String createSubOrder(OtcVolumeChangeRequestLog otcVolumeChangeRequestLog){

        //检查订单与买方
        this.checkSubOrderAndBuyer(otcVolumeChangeRequestLog);
        //检查卖方与获取卖方C2C资产
        OfflineCoinVolume offlineCoinVolume = this.checkAndGetOfflineVolume(otcVolumeChangeRequestLog);

        BigDecimal volume = null;
        BigDecimal lockVolume = null; // 交易冻结
        BigDecimal advertVolume = null; // 广告冻结

        if(String.valueOf(TradeEnum.BUY.ordinal()).equals(otcVolumeChangeRequestLog.getAdType())){
            volume = offlineCoinVolume.getVolume().subtract(otcVolumeChangeRequestLog.getVolume());
            advertVolume = offlineCoinVolume.getOtcAdvertVolume();
            lockVolume = offlineCoinVolume.getOtcLockVolume().add(otcVolumeChangeRequestLog.getVolume());

            if(isSellerMchForFeeDeduct(otcVolumeChangeRequestLog)){
                volume = volume.subtract(Optional.ofNullable(otcVolumeChangeRequestLog.getFeeVolume()).orElse(BigDecimal.ZERO));
                lockVolume = lockVolume.add(Optional.ofNullable(otcVolumeChangeRequestLog.getFeeVolume()).orElse(BigDecimal.ZERO));
            }

        }else if(String.valueOf(TradeEnum.SELL.ordinal()).equals(otcVolumeChangeRequestLog.getAdType())){
            volume = offlineCoinVolume.getVolume();
            advertVolume = offlineCoinVolume.getOtcAdvertVolume().subtract(otcVolumeChangeRequestLog.getVolume());
            lockVolume = offlineCoinVolume.getOtcLockVolume().add(otcVolumeChangeRequestLog.getVolume());

            if(isSellerMchForFeeDeduct(otcVolumeChangeRequestLog)){
                advertVolume = advertVolume.subtract(Optional.ofNullable(otcVolumeChangeRequestLog.getFeeVolume()).orElse(BigDecimal.ZERO));
                lockVolume = lockVolume.add(Optional.ofNullable(otcVolumeChangeRequestLog.getFeeVolume()).orElse(BigDecimal.ZERO));
            }

        }else{
            throw new PlatException(Constants.PARAM_ERROR, "广告类型参数非法");
        }

        offlineCoinVolume.setVolume(volume);
        offlineCoinVolume.setOtcAdvertVolume(advertVolume);
        offlineCoinVolume.setOtcLockVolume(lockVolume);

        this.saveCoinVolumeInfo(offlineCoinVolume, otcVolumeChangeRequestLog.getBatchNo());
        return String.format("创建订单资产变更");
    }

    //撤销买入订单(广告类型为卖出)
    private String cancelSubOrder(OtcVolumeChangeRequestLog otcVolumeChangeRequestLog){

        //检查订单与买方
        this.checkSubOrderAndBuyer(otcVolumeChangeRequestLog);
        //检查卖方与获取卖方C2C资产
        OfflineCoinVolume offlineCoinVolume = this.checkAndGetOfflineVolume(otcVolumeChangeRequestLog);

        BigDecimal volume = null;
        BigDecimal lockVolume = null; // 交易冻结
        BigDecimal advertVolume = null; // 广告冻结

        if(String.valueOf(TradeEnum.BUY.ordinal()).equals(otcVolumeChangeRequestLog.getAdType())){
            volume = offlineCoinVolume.getVolume().add(otcVolumeChangeRequestLog.getVolume());
            advertVolume = offlineCoinVolume.getOtcAdvertVolume();
            lockVolume = offlineCoinVolume.getOtcLockVolume().subtract(otcVolumeChangeRequestLog.getVolume());

            if(this.isSellerMchForFeeDeduct(otcVolumeChangeRequestLog)){
                volume = volume.add(Optional.ofNullable(otcVolumeChangeRequestLog.getFeeVolume()).orElse(BigDecimal.ZERO));
                lockVolume = lockVolume.subtract(Optional.ofNullable(otcVolumeChangeRequestLog.getFeeVolume()).orElse(BigDecimal.ZERO));
            }

        }else if(String.valueOf(TradeEnum.SELL.ordinal()).equals(otcVolumeChangeRequestLog.getAdType())){
            if (OtcVolumeChangeTypeEnum.CANCEL_SUB_ORDER.getCode().equals(otcVolumeChangeRequestLog.getType()) || OtcVolumeChangeTypeEnum.APPEAL_TO_SELLER.getCode().equals(otcVolumeChangeRequestLog.getType())) {// 订单取消、广告发布
                volume = offlineCoinVolume.getVolume();
                advertVolume = offlineCoinVolume.getOtcAdvertVolume().add(otcVolumeChangeRequestLog.getVolume());
                lockVolume = offlineCoinVolume.getOtcLockVolume().subtract(otcVolumeChangeRequestLog.getVolume());

                if(this.isSellerMchForFeeDeduct(otcVolumeChangeRequestLog)){
                    advertVolume = advertVolume.add(Optional.ofNullable(otcVolumeChangeRequestLog.getFeeVolume()).orElse(BigDecimal.ZERO));
                    lockVolume = lockVolume.subtract(Optional.ofNullable(otcVolumeChangeRequestLog.getFeeVolume()).orElse(BigDecimal.ZERO));
                }

            } else if (OtcVolumeChangeTypeEnum.CANCEL_SUB_ORDER_.getCode().equals(otcVolumeChangeRequestLog.getType()) || OtcVolumeChangeTypeEnum.APPEAL_TO_SELLER_.getCode().equals(otcVolumeChangeRequestLog.getType())) { // 订单取消、广告取消
                volume = offlineCoinVolume.getVolume().add(otcVolumeChangeRequestLog.getVolume());
                advertVolume = offlineCoinVolume.getOtcAdvertVolume();
                lockVolume = offlineCoinVolume.getOtcLockVolume().subtract(otcVolumeChangeRequestLog.getVolume());

                if(this.isSellerMchForFeeDeduct(otcVolumeChangeRequestLog)){
                    volume = volume.add(Optional.ofNullable(otcVolumeChangeRequestLog.getFeeVolume()).orElse(BigDecimal.ZERO));
                    lockVolume = lockVolume.subtract(Optional.ofNullable(otcVolumeChangeRequestLog.getFeeVolume()).orElse(BigDecimal.ZERO));
                }

            }else{
                throw new PlatException(Constants.PARAM_ERROR, "操作类型非法");
            }
        }else{
            throw new PlatException(Constants.PARAM_ERROR, "广告类型参数非法");
        }

        offlineCoinVolume.setVolume(volume);
        offlineCoinVolume.setOtcAdvertVolume(advertVolume);
        offlineCoinVolume.setOtcLockVolume(lockVolume);

        this.saveCoinVolumeInfo(offlineCoinVolume, otcVolumeChangeRequestLog.getBatchNo());
        return String.format("撤销订单资产变更");
    }

    //卖出订单确认收到款(广告类型为买入)
    private String confirmIn(OtcVolumeChangeRequestLog otcVolumeChangeRequestLog){

        //检查订单与买方
        this.checkSubOrderAndBuyer(otcVolumeChangeRequestLog);
        //检查卖方与获取卖方C2C资产
        OfflineCoinVolume offlineCoinVolume = this.checkAndGetOfflineVolume(otcVolumeChangeRequestLog);

        BigDecimal volume = null;
        BigDecimal lockVolume = null; // 交易冻结
        BigDecimal advertVolume = null; // 广告冻结
        BigDecimal bailVolume = Optional.ofNullable(offlineCoinVolume.getBailVolume()).orElse(BigDecimal.ZERO); // 保证金

        volume = offlineCoinVolume.getVolume();
        advertVolume = offlineCoinVolume.getOtcAdvertVolume();
        lockVolume = offlineCoinVolume.getOtcLockVolume().subtract(otcVolumeChangeRequestLog.getVolume()); // 卖币方资产减volume
        BigDecimal buyerSubtractFee = BigDecimal.ZERO;  // 买方扣手续费
        BigDecimal buyerVolume = otcVolumeChangeRequestLog.getVolume(); //买家得到的资产

        if(StringUtils.isEmpty(otcVolumeChangeRequestLog.getFeeDeductType()) || otcVolumeChangeRequestLog.getFeeDeductType().equals("9")){
            if(String.valueOf(TradeEnum.BUY.ordinal()).equals(otcVolumeChangeRequestLog.getAdType())){
                buyerSubtractFee = Optional.ofNullable(otcVolumeChangeRequestLog.getFeeVolume()).orElse(BigDecimal.ZERO);
            }else if(String.valueOf(TradeEnum.SELL.ordinal()).equals(otcVolumeChangeRequestLog.getAdType())){
                bailVolume = bailVolume.subtract(Optional.ofNullable(otcVolumeChangeRequestLog.getFeeVolume()).orElse(BigDecimal.ZERO));
            }else{
                throw new PlatException(Constants.PARAM_ERROR, "广告类型参数非法");
            }
        }else{
            if(otcVolumeChangeRequestLog.getFeeDeductType().equals("0")){
                if(this.isMerchant(otcVolumeChangeRequestLog.getBuyUserId())){
                    buyerVolume = buyerVolume.subtract(Optional.ofNullable(otcVolumeChangeRequestLog.getFeeVolume()).orElse(BigDecimal.ZERO));
                }else{
                    buyerSubtractFee = Optional.ofNullable(otcVolumeChangeRequestLog.getFeeVolume()).orElse(BigDecimal.ZERO);
                }
            }else if(otcVolumeChangeRequestLog.getFeeDeductType().equals("1")){
                if(this.isMerchant(otcVolumeChangeRequestLog.getSellUserId())){
                    lockVolume = lockVolume.subtract(Optional.ofNullable(otcVolumeChangeRequestLog.getFeeVolume()).orElse(BigDecimal.ZERO));
                }else{
                    bailVolume = bailVolume.subtract(Optional.ofNullable(otcVolumeChangeRequestLog.getFeeVolume()).orElse(BigDecimal.ZERO));
                }
            }else{
                throw new PlatException(Constants.PARAM_ERROR, "手续费扣除方参数非法");
            }
        }

        //更新买方资产
        offlineCoinVolumeService.coinVolumeAddBailSub(otcVolumeChangeRequestLog.getBuyUserId(), otcVolumeChangeRequestLog.getCoinId(), otcVolumeChangeRequestLog.getSymbol(), buyerVolume, buyerSubtractFee, otcVolumeChangeRequestLog.getBatchNo());// 买币方资产加volume

        //更新卖方资产
        offlineCoinVolume.setVolume(volume);
        offlineCoinVolume.setOtcAdvertVolume(advertVolume);
        offlineCoinVolume.setOtcLockVolume(lockVolume);
        offlineCoinVolume.setBailVolume(bailVolume);

        this.saveCoinVolumeInfo(offlineCoinVolume, otcVolumeChangeRequestLog.getBatchNo());
        return String.format("确认收款资产变更");
    }

    private String appealToBuyer(OtcVolumeChangeRequestLog otcVolumeChangeRequestLog){
        this.confirmIn(otcVolumeChangeRequestLog);
        return String.format("申诉判给买方资产变更");
    }

    private String appealToSeller(OtcVolumeChangeRequestLog otcVolumeChangeRequestLog){
        this.cancelSubOrder(otcVolumeChangeRequestLog);
        return String.format("申诉判给卖方资产变更");
    }

    private void checkSubOrderAndBuyer(OtcVolumeChangeRequestLog otcVolumeChangeRequestLog){
        if(StringUtils.isEmpty(otcVolumeChangeRequestLog.getBuyUserId())){
            logger.error("买家不能为空");
            throw new PlatException(Constants.PARAM_ERROR, "买家不能为空");
        }

        if(StringUtils.isEmpty(otcVolumeChangeRequestLog.getSubOrderId())){
            logger.error("订单号不能为空");
            throw new PlatException(Constants.PARAM_ERROR, "订单号不能为空");
        }
    }

    private void createSuccessRequest(OtcVolumeChangeRequestLog otcVolumeChangeRequestLog, String result){
        OtcVolumeChangeRequest otcVolumeChangeRequest = new OtcVolumeChangeRequest();
        BeanUtils.copyProperties(otcVolumeChangeRequestLog, otcVolumeChangeRequest);
        otcVolumeChangeRequest.setId(SnowFlake.createSnowFlake().nextIdString());
        otcVolumeChangeRequest.setRequestLogId(otcVolumeChangeRequestLog.getId());
        otcVolumeChangeRequest.setStatus("1");
        otcVolumeChangeRequest.setResult(result);
        otcVolumeChangeRequest.setCreateDate(LocalDateTime.now());
        otcVolumeChangeRequest.setUpdateDate(LocalDateTime.now());
        otcVolumeChangeRequestDao.insert(otcVolumeChangeRequest);
    }

    private void createFailCoreRequest(OtcVolumeChangeRequestLog otcVolumeChangeRequestLog, String result){
        OtcVolumeChangeRequest otcVolumeChangeRequest = new OtcVolumeChangeRequest();
        BeanUtils.copyProperties(otcVolumeChangeRequestLog, otcVolumeChangeRequest);
        otcVolumeChangeRequest.setId(SnowFlake.createSnowFlake().nextIdString());
        otcVolumeChangeRequest.setRequestLogId(Objects.nonNull(otcVolumeChangeRequestLog) ? otcVolumeChangeRequestLog.getId() : "");
        otcVolumeChangeRequest.setStatus("0");
        otcVolumeChangeRequest.setResult(result);
        otcVolumeChangeRequestDao.insert(otcVolumeChangeRequest);
    }

    private void createFailRequest(OtcVolumeChangeRequestVO otcVolumeChangeRequestVO, String result){
        OtcVolumeChangeRequest otcVolumeChangeRequest = new OtcVolumeChangeRequest();
        BeanUtils.copyProperties(otcVolumeChangeRequestVO, otcVolumeChangeRequest);
        otcVolumeChangeRequest.setId(SnowFlake.createSnowFlake().nextIdString());
        otcVolumeChangeRequest.setRequestLogId("");
        otcVolumeChangeRequest.setStatus("0");
        otcVolumeChangeRequest.setResult(result);
        otcVolumeChangeRequestDao.insert(otcVolumeChangeRequest);
    }

    private void saveCoinVolumeInfo(OfflineCoinVolume offlineCoinVolume, String batchNo) {
        checkCoinVolumeInfo(offlineCoinVolume);
        long count = offlineCoinVolumeDao.updateOtcCoinVolumeInfo(offlineCoinVolume);
        if (count != 1) {
            throw new PlatException(Constants.UPDATE_ERROR, "更新失败");
        }
        offlineCoinVolumeLogService.saveLog(offlineCoinVolume.getUserId(), offlineCoinVolume.getCoinId(), batchNo);// C2C资产流水
    }

    private void checkCoinVolumeInfo(OfflineCoinVolume offlineCoinVolume) {
        greaterZero(offlineCoinVolume.getVolume(), "用户资产不足");
        greaterZero(offlineCoinVolume.getAdvertVolume(), "用户广告锁定错误");
        greaterZero(offlineCoinVolume.getLockVolume(), "用户交易锁定错误");
        greaterZero(offlineCoinVolume.getOtcAdvertVolume(), "用户OTC广告锁定错误");
        greaterZero(offlineCoinVolume.getOtcLockVolume(), "用户OTC交易锁定错误");
    }

    private void greaterZero(BigDecimal target, String errorMsg) {
        if (Objects.isNull(target) || target.compareTo(BigDecimal.ZERO) == -1) {
            logger.error("参数错误{}", target);
            throw new PlatException(Constants.PARAM_ERROR, Optional.ofNullable(errorMsg).orElse("参数错误"));
        }
    }

    @Override
    public OtcVolumeChangeResultVO findByBatchNo(OtcVolumeChangeQueryVO otcVolumeChangeQueryVO) {

        //check Encrypted
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("batchNo",otcVolumeChangeQueryVO.getBatchNo());
        paramMap.put("publishSource",otcVolumeChangeQueryVO.getPublishSource());
        this.checkSecretKey(otcVolumeChangeQueryVO.getKey(), paramMap);

        OtcVolumeChangeRequest otcVolumeChangeRequest = otcVolumeChangeRequestDao.findByBatchNo(otcVolumeChangeQueryVO.getBatchNo());
        if(Objects.isNull(otcVolumeChangeRequest)){
            logger.error("订单批次号不存在");
            throw new PlatException(Constants.PARAM_ERROR, "订单批次号不存在");
        }

        OtcVolumeChangeResultVO otcVolumeChangeResultVO = new OtcVolumeChangeResultVO();
        BeanUtils.copyProperties(otcVolumeChangeRequest, otcVolumeChangeResultVO);
        otcVolumeChangeResultVO.setKey(this.getSecretKey(otcVolumeChangeRequest.getPublishSource(), this.createResultParamMap(otcVolumeChangeRequest)));
        return otcVolumeChangeResultVO;
    }

    private Map<String,String> createResultParamMap(OtcVolumeChangeRequest otcVolumeChangeRequest){

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("type",otcVolumeChangeRequest.getType());
        paramMap.put("batchNo",otcVolumeChangeRequest.getBatchNo());
        paramMap.put("coinId",otcVolumeChangeRequest.getCoinId());
        paramMap.put("symbol",otcVolumeChangeRequest.getSymbol());
//        paramMap.put("sellUserId",otcVolumeChangeRequest.getSellUserId());
//        paramMap.put("buyUserId",otcVolumeChangeRequest.getBuyUserId());
        paramMap.put("orderId",otcVolumeChangeRequest.getOrderId());
        paramMap.put("subOrderId",otcVolumeChangeRequest.getSubOrderId());
        paramMap.put("adType",otcVolumeChangeRequest.getAdType());
        paramMap.put("volume",String.valueOf(otcVolumeChangeRequest.getVolume()));
        paramMap.put("feeVolume",Objects.isNull(otcVolumeChangeRequest.getFeeVolume()) ? "" : String.valueOf(otcVolumeChangeRequest.getFeeVolume()));
        paramMap.put("publishSource",otcVolumeChangeRequest.getPublishSource());
        paramMap.put("status",otcVolumeChangeRequest.getStatus());
        paramMap.put("result",otcVolumeChangeRequest.getResult());
//        paramMap.put("createDate", String.valueOf(Timestamp.valueOf(otcVolumeChangeRequest.getCreateDate()).getTime()));
//        paramMap.put("updateDate", String.valueOf(Timestamp.valueOf(otcVolumeChangeRequest.getUpdateDate()).getTime()));

        return paramMap;
    }

    private String getSecretKey(String publishSource, Map<String, String> paramMap) {
        OtcAccountSecret accountSecret = otcAccountSecretDao.findByPublishSource(publishSource);
        if (Objects.isNull(accountSecret)) {
            throw new PlatException(Constants.PARAM_ERROR, "OTC信息未设置");
        }

        String mykey = MD5Util.getSign(paramMap, accountSecret.getSecret());
        if (org.apache.commons.lang3.StringUtils.isBlank(mykey)) {
            throw new PlatException(Constants.PARAM_ERROR, "秘钥加密错误");
        }
        return mykey;
    }

    private boolean isSellerMchForFeeDeduct(OtcVolumeChangeRequestLog otcVolumeChangeRequestLog){
        boolean isResult = false;
        if(!StringUtils.isEmpty(otcVolumeChangeRequestLog.getFeeDeductType())){
            if(otcVolumeChangeRequestLog.getFeeDeductType().equals("1")){
                if(this.isMerchant(otcVolumeChangeRequestLog.getSellUserId())){
                    isResult = true;
                }
            }
        }
        return isResult;
    }

    private boolean isMerchant(String userId){
        boolean isResult = false;
        PlatUser sellUser = platUserDao.findById(userId);
        if(Objects.isNull(sellUser)){
            throw new PlatException(Constants.PARAM_ERROR, "用户不存在");
        }
        if(sellUser.getTag().startsWith(UserTagEnum.OTC_BUSINESS.getCode())){
            isResult = true;
        }
        return isResult;
    }
}
