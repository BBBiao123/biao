package com.biao.business;

import com.biao.entity.DepositLog;
import com.biao.entity.PlatUser;
import com.biao.entity.mk2.Mk2PopularizeCommonMember;
import com.biao.enums.CycleEnum;
import com.biao.enums.MemberLockEnum;
import com.biao.enums.UserCoinVolumeEventEnum;
import com.biao.init.Environment;
import com.biao.mapper.*;
import com.biao.mapper.mk2.Mk2PopularizeCommonMemberDao;
import com.biao.pojo.UserCoinVolumeBillDTO;
import com.biao.service.UserCoinVolumeBillService;
import com.biao.util.DateUtils;
import com.biao.util.SnowFlake;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BlockNumberService {

    private static Logger logger = LoggerFactory.getLogger(BlockNumberService.class);

    @Autowired
    CoinDao coinDao;

    @Autowired
    CoinAddressDao coinAddressDao;

    @Autowired
    UserCoinVolumeDao userCoinVolumeDao;
    @Autowired
    WithdrawLogDao withdrawLogDao;
    @Autowired
    DepositLogDao depositLogDao;
    @Autowired
    PlatUserDao platUserDao;
    @Autowired
    Mk2PopularizeCommonMemberDao mk2PopularizeCommonMemberDao;

    Web3j web3j;

    @Autowired
    private UserCoinVolumeBillService userCoinVolumeBillService;

    /**
     * 获取到区块高度 （当前区块高度-数据库的区块高度）
     *
     * @throws Exception
     */
    @Transactional
    public void confirmEthBlockNumber() throws Exception {
        web3j = Web3j.build(new HttpService(Environment.RPC_URL));
        List<DepositLog> list = depositLogDao.findStatusAndCoinType(0, "1");
        if (CollectionUtils.isEmpty(list)) return;
        logger.info("充值待确认记录{}",list.size());
        list.forEach(log -> {
            try {
                EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt(log.getTxId()).send();
                String result = receipt.getResult().getStatus();

                if ("0x1".equals(result)) {
                    log.setStatus(1);
                    logger.info("================{},txid{}", result,log.getTxId());
                } else {
                    return;
                }

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            depositLogDao.updateById(log);
            logger.info("充值记录确认成功{}",log.getTxId());
            //给用户对应资产加上去
            UserCoinVolumeBillDTO dto = new UserCoinVolumeBillDTO();
            dto.setCoinSymbol(log.getCoinSymbol());
            dto.setUserId(log.getUserId());
            dto.setForceLock(false);
            dto.setMark("充值"+log.getCoinSymbol());
            dto.setOpLockVolume(BigDecimal.ZERO);
            dto.setOpVolume(log.getVolume());
            UserCoinVolumeEventEnum[] emst = {
                    UserCoinVolumeEventEnum.ADD_VOLUME
            };
            dto.setOpSign(emst);
            dto.setSource(log.getCoinSymbol()+"wallet");
            dto.setRefKey(log.getTxId());
            long count = userCoinVolumeBillService.insert(dto);
            if(count <= 0) {
                logger.error("用户充值上账{},coinSymbol{},volume{}", log.getUserId(), log.getCoinSymbol(), log.getVolume());
            }
            logger.info("充值记录上账成功{}",log.getTxId());
            logger.info("用户充值上账{},coinSymbol{},volume{}", log.getUserId(), log.getCoinSymbol(), log.getVolume());

        });
    }


}