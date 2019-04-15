package com.biao.service.impl.otc;

import com.biao.constant.Constants;
import com.biao.entity.Coin;
import com.biao.entity.otc.OtcAccountSecret;
import com.biao.entity.otc.OtcConvertCoin;
import com.biao.entity.otc.OtcConvertCoinConf;
import com.biao.entity.otc.OtcConvertCoinLog;
import com.biao.exception.PlatException;
import com.biao.mapper.CoinDao;
import com.biao.mapper.otc.OtcConvertCoinConfDao;
import com.biao.mapper.otc.OtcConvertCoinDao;
import com.biao.mapper.otc.OtcConvertCoinLogDao;
import com.biao.service.OfflineCoinVolumeService;
import com.biao.service.otc.OtcAccountSecretService;
import com.biao.service.otc.OtcConvertCoinService;
import com.biao.util.SnowFlake;
import com.biao.vo.otc.OtcConvertCoinVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class OtcConvertCoinServiceImpl implements OtcConvertCoinService {

    private Logger logger = LoggerFactory.getLogger(OtcConvertCoinServiceImpl.class);

    @Autowired
    private OtcConvertCoinDao otcConvertCoinDao;

    @Autowired
    private CoinDao coinDao;

    @Autowired
    private OtcAccountSecretService otcAccountSecretService;

    @Autowired
    private OtcConvertCoinLogDao otcConvertCoinLogDao;

    @Autowired
    private OtcConvertCoinConfDao otcConvertCoinConfDao;

    @Autowired
    private OtcConvertCoinService otcConvertCoinService;

    @Autowired
    private OfflineCoinVolumeService offlineCoinVolumeService;

    @Override
    public OtcConvertCoin findByBatchNo(OtcConvertCoinVO otcConvertCoinVO) {
        return otcConvertCoinDao.findByBatchNo(otcConvertCoinVO.getBatchNo());
    }

    @Override
    public OtcConvertCoin executeConvert(OtcConvertCoin otcConvertCoin, OtcConvertCoinVO otcConvertCoinVO) {
        OtcConvertCoinLog log = new OtcConvertCoinLog();
        BeanUtils.copyProperties(otcConvertCoinVO, log);
        log.setSecretkey(otcConvertCoinVO.getKey());
        log.setId(SnowFlake.createSnowFlake().nextIdString());
        log.setCreateDate(LocalDateTime.now());
        otcConvertCoinLogDao.insert(log); // 记录请求日志
        otcConvertCoin.setId(SnowFlake.createSnowFlake().nextIdString());
        otcConvertCoin.setRequestLogId(log.getId());
        otcConvertCoin.setCreateDate(LocalDateTime.now());
        try {
            // 校验批次号是否存在
            checkBatchNo(otcConvertCoin);
            // 校验来源
            checkSecret(otcConvertCoinVO);
            // 校验参数为空，大于0等
            checkParam(otcConvertCoin);
            // 校验输入币种
            checkConvertCoin(otcConvertCoin);
            // 币种兑换
            otcConvertCoinService.doConvertCore(otcConvertCoin);
        } catch (PlatException pe) {
            logger.error("币种兑换失败{}", otcConvertCoin.getBatchNo());
            logger.error("币种兑换失败", pe);
            String msg = Optional.ofNullable(pe.getMsg()).orElse("");
            msg = msg.length() > 450 ? msg.substring(0, 450) : msg;
            otcConvertCoin.setStatus("0");// 兑换失败
            otcConvertCoin.setResult(msg);
            otcConvertCoinDao.insert(otcConvertCoin);
        } catch (Exception e) {
            logger.error("币种兑换失败{}", otcConvertCoin.getBatchNo());
            logger.error("币种兑换失败", e);
            String msg = Optional.ofNullable(e.toString()).orElse("");
            msg = msg.length() > 450 ? msg.substring(0, 450) : msg;
            otcConvertCoin.setStatus("0");// 兑换失败
            otcConvertCoin.setResult(msg);
            otcConvertCoinDao.insert(otcConvertCoin);
        }
        return otcConvertCoin;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doConvertCore(OtcConvertCoin otcConvertCoin) {

        OtcAccountSecret secret = otcAccountSecretService.findByPublishSource("otc");
        if (Objects.isNull(secret)) {
            throw new PlatException(Constants.PARAM_ERROR, "otc总账户未配置");
        }
        otcConvertCoin.setStatus("1");
        otcConvertCoin.setResult("币兑换成功");
        otcConvertCoinDao.insert(otcConvertCoin);

        // FROM 减EUC, 主账户加EUC
        offlineCoinVolumeService.coinVolumeSubtract(otcConvertCoin.getUserId(), otcConvertCoin.getFromCoinId(), otcConvertCoin.getFromVolume(), otcConvertCoin.getBatchNo());
        offlineCoinVolumeService.coinVolumeAdd(secret.getUserId(), otcConvertCoin.getFromCoinId(), otcConvertCoin.getFromCoinSymbol(), otcConvertCoin.getFromVolume(), otcConvertCoin.getBatchNo());// 主账户
        // FROM 加USDT, 主账户减USDT
        offlineCoinVolumeService.coinVolumeSubtract(secret.getUserId(), otcConvertCoin.getToCoinId(), otcConvertCoin.getToVolume(), otcConvertCoin.getBatchNo());// 主账户
        offlineCoinVolumeService.coinVolumeAdd(otcConvertCoin.getUserId(), otcConvertCoin.getToCoinId(), otcConvertCoin.getToCoinSymbol(), otcConvertCoin.getToVolume(), otcConvertCoin.getBatchNo());
    }

    private void checkSecret(OtcConvertCoinVO otcConvertCoinVO) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("batchNo", otcConvertCoinVO.getBatchNo());
        paramMap.put("fromCoinId", otcConvertCoinVO.getFromCoinId());
        paramMap.put("fromVolume", String.valueOf(otcConvertCoinVO.getFromVolume()));
        paramMap.put("toCoinId", otcConvertCoinVO.getToCoinId());
        paramMap.put("toVolume", String.valueOf(otcConvertCoinVO.getToVolume()));
        paramMap.put("rate", String.valueOf(otcConvertCoinVO.getRate()));
        paramMap.put("feeVolume", String.valueOf(otcConvertCoinVO.getFeeVolume()));
        paramMap.put("publishSource", otcConvertCoinVO.getPublishSource());
        otcAccountSecretService.checkAccountSecret(otcConvertCoinVO, paramMap); // 来源安全校验
    }

    private void checkParam(OtcConvertCoin otcConvertCoin) {
        if (StringUtils.isBlank(otcConvertCoin.getBatchNo())) {
            throw new PlatException(Constants.PARAM_ERROR, "批次号不能为空");
        }
        if (StringUtils.isBlank(otcConvertCoin.getFromCoinId())) {
            throw new PlatException(Constants.PARAM_ERROR, "转出币种ID不能为空");
        }
        if (StringUtils.isBlank(otcConvertCoin.getToCoinId())) {
            throw new PlatException(Constants.PARAM_ERROR, "转入币种ID不能为空");
        }
        if (Objects.isNull(otcConvertCoin.getFromVolume()) || otcConvertCoin.getFromVolume().compareTo(BigDecimal.ZERO) < 1) {
            throw new PlatException(Constants.PARAM_ERROR, "转出数量不能小于0");
        }
        if (Objects.isNull(otcConvertCoin.getToVolume()) || otcConvertCoin.getToVolume().compareTo(BigDecimal.ZERO) < 1) {
            throw new PlatException(Constants.PARAM_ERROR, "转入数量不能小于0");
        }
    }

    private void checkBatchNo(OtcConvertCoin otcConvertCoin) {
        OtcConvertCoin convertCoin = otcConvertCoinDao.findByBatchNo(otcConvertCoin.getBatchNo());
        if (Objects.nonNull(convertCoin)) {
            throw new PlatException(Constants.PARAM_ERROR, "[" + otcConvertCoin.getBatchNo() + "]该批次号已存在");
        }
    }

    private void checkConvertCoin(OtcConvertCoin otcConvertCoin) {
        Coin coinFrom = coinDao.findById(otcConvertCoin.getFromCoinId());
        if (Objects.isNull(coinFrom)) {
            throw new PlatException(Constants.PARAM_ERROR, "转出币种错误");
        }
        otcConvertCoin.setFromCoinSymbol(coinFrom.getName());
        Coin toFrom = coinDao.findById(otcConvertCoin.getToCoinId());
        if (Objects.isNull(toFrom)) {
            throw new PlatException(Constants.PARAM_ERROR, "转入币种错误");
        }
        otcConvertCoin.setToCoinSymbol(toFrom.getName());
        OtcConvertCoinConf convertCoinConf = otcConvertCoinConfDao.findBySymbol(otcConvertCoin.getFromCoinSymbol(), otcConvertCoin.getToCoinSymbol());
        if (Objects.isNull(convertCoinConf)) {
            throw new PlatException(Constants.PARAM_ERROR, "目前不支持该兑换币种对");
        }
    }
}
