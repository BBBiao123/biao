package com.biao.service.impl;

import com.biao.constant.SercurityConstant;
import com.biao.entity.CoinVolumeRiskMgt;
import com.biao.entity.EmailSendLog;
import com.biao.mapper.CoinVolumeRiskMgtDao;
import com.biao.pojo.CoinVolumeReconciliationVO;
import com.biao.service.CoinVolumeRiskMgtTaskService;
import com.biao.service.MessageSendService;
import com.biao.util.NumberUtils;
import com.biao.util.SnowFlake;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class CoinVolumeRiskMgtTaskServiceImpl implements CoinVolumeRiskMgtTaskService {

    private static Logger logger = LoggerFactory.getLogger(CoinVolumeRiskMgtTaskServiceImpl.class);

    @Value("${riskMgt.mailList}")
    private String mailList;

    @Autowired
    private CoinVolumeRiskMgtDao coinVolumeRiskMgtDao;

    @Autowired
    private MessageSendService messageSendService;

    @Override
    public void triggerCoinVolumeRiskMgtEntry() {
        logger.info("资产对账风控---start");
        List<CoinVolumeReconciliationVO> coinVolumeReconciliationList = coinVolumeRiskMgtDao.getReconciliation();
        coinVolumeReconciliationList.forEach(reconciliationVO ->{
            CoinVolumeRiskMgt coinVolumeRiskMgt = coinVolumeRiskMgtDao.findByCoinSymbol(reconciliationVO.getCoinSymbol());
            if(Objects.nonNull(coinVolumeRiskMgt)){
                if(reconciliationVO.getBalance().compareTo(coinVolumeRiskMgt.getVolume()) < 0){
                    logger.info("资产风控报警，" + String.format("币种：%s, 实际总量：%s, 帐面总量：%s, 差额：%s, 阀值：%s", reconciliationVO.getCoinSymbol(), String.valueOf(reconciliationVO.getRealVolume()),
                            String.valueOf(reconciliationVO.getAccountVolume()),
                            String.valueOf(reconciliationVO.getBalance()), String.valueOf(coinVolumeRiskMgt.getVolume())));
                    this.sendMail(reconciliationVO);
                }
            }else{
                logger.info("币种{}无风控管理配置", reconciliationVO.getCoinSymbol());
            }
        });
        logger.info("资产对账风控---end");
    }

    private void sendMail(CoinVolumeReconciliationVO reconciliationVO) {

        if(StringUtils.isEmpty(mailList)){
            logger.info("资产风控接受邮箱列表为空");
            return ;
        }

        logger.info("资产风控接受邮箱列表：{}", mailList);

        String[] mails = mailList.split(",");
        for (int i = 0; i < mails.length; i++){
            Map<String, Object> params = new HashMap<>();
            params.put(MessageSendService.PARAM_CODE, NumberUtils.getRandomNumber(6));
            EmailSendLog emailSendLog = new EmailSendLog();
            String id = SnowFlake.createSnowFlake().nextIdString();
            emailSendLog.setId(id);
            emailSendLog.setDelFlag("0");
            emailSendLog.setStatus("0");
            emailSendLog.setBusinessType("risk_mgt");
            emailSendLog.setTemplateId("");
            emailSendLog.setSubject("资产风控报警");
            emailSendLog.setContent(String.format("币种：%s, 实际总量：%s, 帐面总量：%s, 差额：%s", reconciliationVO.getCoinSymbol(), String.valueOf(reconciliationVO.getRealVolume()),
                    String.valueOf(reconciliationVO.getAccountVolume()), String.valueOf(reconciliationVO.getBalance())));
            emailSendLog.setEmail(mails[i]);
            emailSendLog.setCreateDate(LocalDateTime.now());
            long expireTime = SercurityConstant.REDIS_EXPIRE_TIME_HALF_HOUR;
            emailSendLog.setExpireTime(LocalDateTime.now().plusSeconds(expireTime));
            messageSendService.sendMail(emailSendLog);
        }

    }
}
