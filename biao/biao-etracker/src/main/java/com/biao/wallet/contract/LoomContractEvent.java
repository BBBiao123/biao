package com.biao.wallet.contract;

import com.biao.business.DepositService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.web3j.tx.Contract.staticExtractEventParameters;

/**
 * Event log相关loom
 * 监听合约event
 */
@Component
public class LoomContractEvent {
    private static Logger logger = LoggerFactory.getLogger(LoomContractEvent.class);

    @Autowired
    private DepositService depositService;

    public void ContractFilter(Web3j web3j, String symbol, String contractAddress, Integer decimals) throws IOException {
        EthFilter filter = new EthFilter(
                DefaultBlockParameterName.LATEST,
                DefaultBlockParameterName.LATEST,
                contractAddress);
        Event event = new Event(
                "Transfer",
                Arrays.asList(
                        new TypeReference<Address>(true) {
                        },
                        new TypeReference<Address>(true) {
                        },
                        new TypeReference<Uint256>() {
                        }));
        String topicData = EventEncoder.encode(event);
        filter.addSingleTopic(topicData);

        logger.info("监听 " + symbol +" 币种" );
        web3j.ethLogObservable(filter).subscribe(log -> {
            EventValues eventValues = staticExtractEventParameters(event, log);
            MainErcContractObject.TransferEventResponse typedResponse = new MainErcContractObject.TransferEventResponse();
            typedResponse.log = log;
            typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            List<String> topics = log.getTopics();
            for (String topic : topics) {
                System.out.println(topic);
            }
            logger.info(symbol + "充值交易监听hash" + typedResponse.log.getTransactionHash());
            logger.info("symbol{},from{},to{},value{}", symbol, typedResponse._from, typedResponse._to, typedResponse._value);

            BigDecimal volume = BigDecimal.ZERO;
            logger.info("volume: {}", typedResponse._value);
            if (decimals == 0) {
                volume = new BigDecimal(typedResponse._value);
            } else if (decimals == 2) {
                volume =new BigDecimal(typedResponse._value).divide(new BigDecimal(100));
            } else if (decimals == 6) {
                volume = Convert.fromWei(new BigDecimal(typedResponse._value), Convert.Unit.MWEI);
            } else if (decimals == 8) {
                volume = Convert.fromWei(new BigDecimal(typedResponse._value), Convert.Unit.MWEI).divide(BigDecimal.valueOf(100l));
            } else if (decimals == 12) {
                volume = Convert.fromWei(new BigDecimal(typedResponse._value), Convert.Unit.SZABO);
            } else if (decimals == 18) {
                volume = Convert.fromWei(new BigDecimal(typedResponse._value), Convert.Unit.ETHER);
            }
            if (volume.compareTo(BigDecimal.ZERO) == -1) return;
            logger.info(symbol + "充值交易监听value:" + volume);
            try {
                logger.info("symbol{},from{},to{},value{}", symbol, typedResponse._from, typedResponse._to, volume);
                depositService.executeDepositERC20Token(typedResponse._from, typedResponse._to, volume, typedResponse.log.getTransactionHash(), symbol);
            } catch (Exception e) {
                logger.error(symbol + "充值交易监听hash" + typedResponse.log.getTransactionHash());
                logger.error(symbol + "充值交易监听blocknumber" + typedResponse.log.getBlockNumber().intValue());
                logger.error(symbol + "充值交易监听from:" + typedResponse._from);
                logger.error(symbol + "充值交易监听to:" + typedResponse._to);
                logger.error(symbol + "充值交易监听value:" + typedResponse._value);
            }
        });
    }
}
